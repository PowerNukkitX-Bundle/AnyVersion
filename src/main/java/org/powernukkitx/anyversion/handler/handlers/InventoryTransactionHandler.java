package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseClientCooldownState;
import org.cloudburstmc.protocol.bedrock.data.HandSlot;
import org.powernukkitx.Player;
import org.powernukkitx.block.Block;
import org.powernukkitx.block.BlockAir;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.item.Item;
import org.powernukkitx.level.Level;
import org.powernukkitx.math.BlockFace;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseActionType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUsePredictedResult;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseTriggerType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.data.ItemUseInventoryTransaction;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.NbtMapConverter;

import java.lang.reflect.Method;

public class InventoryTransactionHandler extends PacketHandler<InventoryTransactionPacket> {

    @Override
    public void handle(ProtocolPlayer player, InventoryTransactionPacket packet) {
        if (!(packet.getTransaction() instanceof ItemUseInventoryTransaction transaction)) {
            return;
        }
        if (transaction.getHand() == null) {
            // Protocol 2169 does not always supply this field, but the 2192
            // codec requires it when AnyVersion re-encodes the transaction.
            transaction.setHand(HandSlot.MAINHAND);
        }
        if (transaction.getPosition() != null && transaction.getFromPosition() != null) {
            Vector3i position = transaction.getPosition();
            int correctedY = (position.getY() << 1) ^ (position.getY() >> 31);
            double playerY = transaction.getFromPosition().getY();

            // Some clients encode the block Y as an unsigned VarInt, while the
            // protocol codec decodes it as a signed ZigZag VarInt. Only apply the
            // inverse mapping when it produces the plausible nearby coordinate.
            if (Math.abs(position.getY() - playerY) > 16 && Math.abs(correctedY - playerY) <= 16) {
                transaction.setPosition(Vector3i.from(position.getX(), correctedY, position.getZ()));
            }
        }
        Player p = player.nukkitPlayer();
        if (p == null) {
            return;
        }
        if(transaction.getClientCooldownState() == null) {
            transaction.setClientCooldownState(ItemUseClientCooldownState.ON);
        }

        if (transaction.getTargetBlockId() == null) {
            BlockState air = BlockAir.STATE;
            transaction.setTargetBlockId(new SimpleBlockDefinition(air.getIdentifier(), air.blockStateHash(), NbtMap.EMPTY));
        }
        if (transaction.getTriggerType() == null) {
            transaction.setTriggerType(ItemUseTriggerType.PLAYER_INPUT);
        }

        Item iHand = p.getInventory().getItemInMainHand();
        // Survival validates count, wear, NBT and network stack identity before
        // firing interactions. Preserve the complete authoritative inventory item.
        ItemData serverHand = authoritativeHand(iHand);
        ItemData data = transaction.getItem();
        if (data != null && data.getDefinition() != null && Registries.ITEM.downgrade(player.getVersion(), serverHand).getDefinition().getRuntimeId() == data.getDefinition().getRuntimeId()) {
            transaction.setItem(serverHand);
        }

        if (transaction.getClientInteractPrediction() == null) {
            if (transaction.getActionType() == ItemUseActionType.PLACE) {
                try {
                    Method method = Level.class.getDeclaredMethod("beforePlaceBlock", Item.class, BlockFace.class, Block.class, Block.class);
                    method.setAccessible(true);
                    BlockFace blockFace = BlockFace.fromIndex(transaction.getFace());
                    Vector3i targetLoc = transaction.getPosition();
                    if (targetLoc != null) {
                        Block target = p.getLevel().getBlock(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ());
                        Block hand = (Block) method.invoke(p.getLevel(), p.getInventory().getItemInMainHand(), blockFace, target.getSide(blockFace), target);
                        method.setAccessible(false);
                        if (hand != null) {
                            var diff = p.getNextPosition().subtract(p.getPosition());
                            var aabb = p.getBoundingBox().getOffsetBoundingBox(diff.x, diff.y, diff.z);
                            if (aabb != null && hand.getBoundingBox() != null && aabb.intersectsWith(hand.getBoundingBox()) && aabb.intersection(hand.getBoundingBox()).getVolume() > 0.005f) {
                                transaction.setClientInteractPrediction(ItemUsePredictedResult.FAILURE);
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            transaction.setClientInteractPrediction(ItemUsePredictedResult.SUCCESS);
        }
    }

    public static NbtMap deepcopy(CompoundTag tag) {
        return NbtMapConverter.toCloudburst(tag);
    }

    public static ItemData authoritativeHand(Item item) {
        return item.toNetwork();
    }
}
