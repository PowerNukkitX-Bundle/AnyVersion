package org.powernukkitx.anyversion.utils.transformer.items._1_21_20;

import org.powernukkitx.block.BlockID;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.transformer.items.ItemDataTransformer;

import java.util.Map;

import static org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20.LightBlockTransformer.LIGHT_BLOCK;

public class LightBlockTransformer extends ItemDataTransformer {

    @Override
    public ItemData transform(ItemData original) {
        SimpleBlockDefinition definition = (SimpleBlockDefinition) original.getBlockDefinition();
        ItemDefinition originalItemDefinition = original.getDefinition();
        NbtMap map = NbtMap.fromMap((Map<String, Object>) definition.getState().get("states"));
        String identifier = map.getString("identifier");
        if(identifier.equals(BlockID.AIR) || identifier.isEmpty()) return Registries.ITEM.getOutdated(original);
        int damage = Integer.parseInt(identifier.replace(LIGHT_BLOCK + "_", ""));
        SimpleItemDefinition itemDefinition = new SimpleItemDefinition(originalItemDefinition.getIdentifier(), -215, originalItemDefinition.getVersion(), originalItemDefinition.isComponentBased(), originalItemDefinition.getComponentData());
        return ItemData.builder()
                .definition(itemDefinition)
                .damage(damage)
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
