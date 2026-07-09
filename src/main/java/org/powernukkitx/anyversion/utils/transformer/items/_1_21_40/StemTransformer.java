package org.powernukkitx.anyversion.utils.transformer.items._1_21_40;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.powernukkitx.anyversion.utils.transformer.items.ItemDataTransformer;

import java.util.Map;

import static org.powernukkitx.block.BlockID.*;

public class StemTransformer extends ItemDataTransformer {

    @Override
    public ItemData transform(ItemData original) {
        SimpleBlockDefinition originalDefinition = (SimpleBlockDefinition) original.getBlockDefinition();
        NbtMap map = NbtMap.fromMap((Map<String, Object>) originalDefinition.getState().get("states"));
        int bits = switch (map.getString("identifier")) {
            case MUSHROOM_STEM -> 15;
            case RED_MUSHROOM_BLOCK -> 14;
            default -> 0;
        };
        int runtimeId = map.getString("identifier").equals(RED_MUSHROOM_BLOCK) ? 100 : 99;
        int blockRuntimeId = switch (map.getString("identifier")) {
            case MUSHROOM_STEM -> 13028;
            case RED_MUSHROOM_BLOCK -> 6040;
            default -> 13043;
        };
        NbtMap states = NbtMapBuilder.from(map).putInt("huge_mushroom_bits", bits).build();
        SimpleBlockDefinition blockDefinition = new SimpleBlockDefinition(originalDefinition.getIdentifier(), blockRuntimeId, NbtMapBuilder.from(originalDefinition.getState()).putCompound("states", states).build());
        SimpleItemDefinition originalItemDefinition = (SimpleItemDefinition) original.getDefinition();

        SimpleItemDefinition simpleItemDefinition = new SimpleItemDefinition(originalItemDefinition.getIdentifier(), runtimeId, originalItemDefinition.getVersion(), originalItemDefinition.isComponentBased(), originalItemDefinition.getComponentData());
        return ItemData.builder()
                .definition(simpleItemDefinition)
                .damage(original.getDamage())
                .count(original.getCount())
                .tag(original.getTag())
                .canPlace(original.getCanPlace())
                .canBreak(original.getCanBreak())
                .blockingTicks(original.getBlockingTicks())
                .blockDefinition(blockDefinition)
                .usingNetId(original.isUsingNetId())
                .netId(original.getNetId())
                .build();
    }

}
