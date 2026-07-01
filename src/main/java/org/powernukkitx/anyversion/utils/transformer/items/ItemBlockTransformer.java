package org.powernukkitx.anyversion.utils.transformer.items;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockState;
import lombok.RequiredArgsConstructor;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.registries.registries.BlockStateRegistry;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

@RequiredArgsConstructor
public class ItemBlockTransformer extends ItemDataTransformer {

    protected final ProtocolVersion version;

    @Override
    public ItemData transform(ItemData original) {
        BlockDefinition blockDefinition = original.getBlockDefinition();
        if(blockDefinition == null) {
            BlockState state = Block.get(original.getDefinition().getIdentifier()).getBlockState();
            if(state == null) throw new RuntimeException(original.getDefinition().getIdentifier() + " has no SimpleBlockDefinition!");
            blockDefinition = new SimpleBlockDefinition(state.getIdentifier(), state.blockStateHash(), state.getBlockStateTag());
        }
        BlockState originalBlockState = cn.nukkit.registry.Registries.BLOCKSTATE.get(blockDefinition.getRuntimeId());
        if(originalBlockState == null) originalBlockState = Block.get(original.getDefinition().getIdentifier()).getProperties().getDefaultState();
        if(originalBlockState == null) return Registries.ITEM.getOutdated(original);
        BlockState downgraded = Registries.BLOCKSTATE.downgrade(version, BlockStateRegistry.clone(originalBlockState), true, Integer.MAX_VALUE);
        if(downgraded.getIdentifier().equals(BlockID.UNKNOWN) || downgraded.getIdentifier().equals(BlockID.INFO_UPDATE)) return Registries.ITEM.getOutdated(original);
        SimpleBlockDefinition downgradedBlockDefinition = new SimpleBlockDefinition(
                downgraded.getIdentifier(),
                Registries.BLOCKPALETTE.getRuntimeId(version, downgraded),
                downgraded.getBlockStateTag()
        );
        ItemDefinition originalItemDefinition = original.getDefinition();
        int runtimeId = cn.nukkit.registry.Registries.ITEM_RUNTIMEID.getInt(downgraded.getIdentifier());
        if (runtimeId == -1) {
            runtimeId = originalItemDefinition.getRuntimeId();
        }
        ItemDefinition itemDefinition = new SimpleItemDefinition(downgraded.getIdentifier(), runtimeId, originalItemDefinition.getVersion(), originalItemDefinition.isComponentBased(), originalItemDefinition.getComponentData());
        return ItemData.builder()
                .definition(itemDefinition)
                .damage(original.getDamage())
                .count(original.getCount())
                .tag(original.getTag())
                .canPlace(original.getCanPlace())
                .canBreak(original.getCanBreak())
                .blockingTicks(original.getBlockingTicks())
                .blockDefinition(downgradedBlockDefinition)
                .usingNetId(original.isUsingNetId())
                .netId(original.getNetId())
                .build();
    }
}
