package org.powernukkitx.anyversion.utils.transformer.items._1_20_70;

import org.powernukkitx.item.ItemID;
import org.powernukkitx.registry.Registries;
import lombok.RequiredArgsConstructor;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.powernukkitx.anyversion.utils.transformer.items.ItemDataTransformer;

@RequiredArgsConstructor
public class SpawnEggTransformer extends ItemDataTransformer {

    final int eid;

    @Override
    public ItemData transform(ItemData original) {
        ItemDefinition originalItemDefinition = original.getDefinition();
        SimpleItemDefinition itemDefinition = new SimpleItemDefinition(ItemID.SPAWN_EGG, Registries.ITEM_RUNTIMEID.get(ItemID.SPAWN_EGG), originalItemDefinition.getVersion(), originalItemDefinition.isComponentBased(), originalItemDefinition.getComponentData());
        return ItemData.builder()
                .definition(itemDefinition)
                .damage(eid)
                .count(original.getCount())
                .tag(original.getTag())
                .canPlace(original.getCanPlace())
                .canBreak(original.getCanBreak())
                .blockingTicks(original.getBlockingTicks())
                .blockDefinition(original.getBlockDefinition())
                .usingNetId(original.isUsingNetId())
                .netId(original.getNetId())
                .build();
    }

}
