package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockState;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseActionType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUsePredictedResult;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseTriggerType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.data.ItemUseInventoryTransaction;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

import java.lang.reflect.Method;

public class InventoryTransactionHandler extends PacketHandler<InventoryTransactionPacket> {

    @Override
    public void handle(ProtocolPlayer player, InventoryTransactionPacket packet) {
        if (!(packet.getTransaction() instanceof ItemUseInventoryTransaction transaction)) {
            return;
        }
        Player p = player.nukkitPlayer();
        if (p == null) {
            return;
        }

        if (transaction.getTargetBlockId() == null) {
            BlockState air = BlockAir.STATE;
            transaction.setTargetBlockId(new SimpleBlockDefinition(air.getIdentifier(), air.blockStateHash(), NbtMap.EMPTY));
        }
        if (transaction.getTriggerType() == null) {
            transaction.setTriggerType(ItemUseTriggerType.PLAYER_INPUT);
        }

        Item iHand = p.getInventory().getItemInMainHand();
        ItemData.Builder builder = ItemData.builder();
        builder.definition(new SimpleItemDefinition(iHand.getId(), iHand.getRuntimeId(), false));
        if (iHand.getNbt() != null) {
            builder.tag(deepcopy(iHand.getNbt()));
        }
        if (iHand.isBlock()) {
            Block block = iHand.getBlockUnsafe();
            builder.blockDefinition(new SimpleBlockDefinition(block.getId(), block.getRuntimeId(), NbtMap.EMPTY));
        }
        ItemData serverHand = builder.build();
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
        return NbtMap.fromMap(tag.parseValue());
    }
}
