package org.powernukkitx.anyversion.handler.handlers;

import lombok.SneakyThrows;
import org.cloudburstmc.protocol.bedrock.data.inventory.FullContainerName;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotInfo;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.*;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.lang.reflect.Field;
import java.util.List;

public class ItemStackRequestHandler extends PacketHandler<ItemStackRequestPacket> {
    @SneakyThrows
    @Override
    public void handle(ProtocolPlayer player, ItemStackRequestPacket packet) {
        for (int requestIndex = 0; requestIndex < packet.getRequests().size(); requestIndex++) {
            ItemStackRequest request = packet.getRequests().get(requestIndex);
            ItemStackRequestAction[] actions = request.getActions();
            for (int actionIndex = 0; actionIndex < actions.length; actionIndex++) {
                ItemStackRequestAction action = actions[actionIndex];
                if (action instanceof TransferItemStackRequestAction requestAction) {
                    ensureFullContainerName(requestAction.getSource());
                    ensureFullContainerName(requestAction.getDestination());
                } else if (action instanceof DestroyAction destroyAction) {
                    ensureFullContainerName(destroyAction.getSource());
                } else if (action instanceof DropAction dropAction) {
                    ensureFullContainerName(dropAction.getSource());
                } else if (action instanceof ConsumeAction consumeAction) {
                    ensureFullContainerName(consumeAction.getSource());
                } else if (action instanceof CraftRecipeAction craftRecipeAction) {
                    if (player.protocol() < ProtocolVersion.MINECRAFT_PE_1_21_20.protocol()) {
                        Field field = CraftRecipeAction.class.getDeclaredField("numberOfRequestedCrafts");
                        field.setAccessible(true);
                        field.set(craftRecipeAction, 1);
                        field.setAccessible(false);
                    }
                } else if (action instanceof SwapAction swapAction) {
                    ensureFullContainerName(swapAction.getSource());
                    ensureFullContainerName(swapAction.getDestination());
                } else if (action instanceof CraftResultsDeprecatedAction craftResults
                        && craftResults.getCraftResults() == null) {
                    actions[actionIndex] = new CraftResultsDeprecatedAction(
                            craftResults.getResultItemsDeprecated(), List.of(), craftResults.getNumCrafts());
                }
            }
            if (request.getStringsToFilterOrigin() == null) {
                packet.getRequests().set(requestIndex, new ItemStackRequest(
                        request.getClientRequestId(), actions, request.getStringsToFilter(),
                        TextProcessingEventOrigin.UNKNOWN));
            }
        }
    }

    private void ensureFullContainerName(ItemStackRequestSlotInfo slot) throws ReflectiveOperationException {
        if (slot.getFullContainerName() == null) {
            Field field = ItemStackRequestSlotInfo.class.getDeclaredField("fullContainerName");
            field.setAccessible(true);
            field.set(slot, new FullContainerName(slot.getContainerEnumName(), 0));
            field.setAccessible(false);
        }
    }
}
