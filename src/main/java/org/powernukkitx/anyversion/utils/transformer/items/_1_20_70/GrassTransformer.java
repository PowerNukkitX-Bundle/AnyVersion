package org.powernukkitx.anyversion.utils.transformer.items._1_20_70;

import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.powernukkitx.anyversion.utils.transformer.items.ItemDataTransformer;

import static org.powernukkitx.tags.BlockTags.GRASS;

public class GrassTransformer extends ItemDataTransformer {

    @Override
    public ItemData transform(ItemData original) {
        ItemDefinition originalItemDefinition = original.getDefinition();
        SimpleItemDefinition itemDefinition = new SimpleItemDefinition(GRASS, originalItemDefinition.getRuntimeId(), originalItemDefinition.getVersion(), originalItemDefinition.isComponentBased(), originalItemDefinition.getComponentData());
        return ItemData.builder()
                .definition(itemDefinition)
                .damage(original.getDamage())
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
