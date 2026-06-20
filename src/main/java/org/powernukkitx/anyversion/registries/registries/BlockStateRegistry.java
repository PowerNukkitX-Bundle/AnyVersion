package org.powernukkitx.anyversion.registries.registries;

import cn.nukkit.block.BlockState;
import cn.nukkit.block.BlockStateImpl;
import cn.nukkit.block.property.type.BlockPropertyType;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.definition.BlockStateDefinition;
import org.powernukkitx.anyversion.utils.table.blockstate.*;


import java.util.Comparator;

public class BlockStateRegistry extends Registry {

    private final ObjectArrayList<BlockStateTable> TABLES = new ObjectArrayList<>();

    private final Object2ObjectArrayMap<Integer, Object2ObjectArrayMap<Long, BlockState>> blockStateCache = new Object2ObjectArrayMap<>();

    @Override
    public void init() {
        TABLES.add(new BlockStateTable_1_26_30());
        TABLES.add(new BlockStateTable_1_26_20());
        TABLES.add(new BlockStateTable_1_21_110());
        TABLES.add(new BlockStateTable_1_21_90());
        TABLES.add(new BlockStateTable_1_21_70());
        TABLES.add(new BlockStateTable_1_21_60());
        TABLES.add(new BlockStateTable_1_21_50());
        TABLES.add(new BlockStateTable_1_21_40());
        TABLES.add(new BlockStateTable_1_21_30());
        TABLES.add(new BlockStateTable_1_21_20());
        TABLES.add(new BlockStateTable_1_21_0());
        TABLES.add(new BlockStateTable_1_20_80());
        TABLES.add(new BlockStateTable_1_20_70());
        TABLES.add(new BlockStateTable_1_20_60());
        TABLES.add(new BlockStateTable_1_20_50());
        TABLES.add(new BlockStateTable_1_20_40());
        TABLES.add(new BlockStateTable_1_20_30());
        TABLES.add(new BlockStateTable_1_20_10());
        TABLES.add(new BlockStateTable_1_20_0());
        TABLES.add(new BlockStateTable_1_19_80());
    }

    public BlockState downgrade(ProtocolVersion version, BlockState state) {
        long unsignedHash = state.blockStateHash();
        if(blockStateCache.containsKey(version.protocol())) {
            Object2ObjectArrayMap<Long, BlockState> versionedCache = blockStateCache.get(version.protocol());
            if(versionedCache.containsKey(unsignedHash)) {
                return versionedCache.get(unsignedHash);
            }
        } else blockStateCache.put(version.protocol(), new Object2ObjectArrayMap<>());
        BlockState blockState = downgrade(version, clone(state), true, Integer.MAX_VALUE);
        if(blockState.blockStateHash() != unsignedHash) {
            Object2ObjectArrayMap<Long, BlockState> versionedCache = blockStateCache.get(version.protocol());
            versionedCache.put(unsignedHash, blockState);
        }
        return blockState;
    }

    public BlockState downgrade(ProtocolVersion version, BlockState state, boolean ignoreEqual, int lastProtocol) {
        final String identifier = state.getIdentifier();
        BlockStateDefinition definition = BlockStateDefinition.of(state);
        ObjectArrayList<BlockStateTable> tables = new ObjectArrayList<>();
        for(BlockStateTable table : TABLES) {
            if(table.getVersion().protocol() > version.protocol()) {
                var optresult = table.getContent().stream().filter(definition::equals).findFirst();
                if (optresult.isPresent()) {
                    tables.add(table);
                }
            }
        }

        tables.sort(Comparator.comparingInt(o -> o.getVersion().protocol()));
        for(BlockStateTable table : tables.reversed()) {
            int prot = table.getVersion().protocol();
            if((prot > version.protocol() || (!ignoreEqual && prot == version.protocol())) && prot < lastProtocol) {
                state = table.getContent().stream().filter(definition::equals).findFirst().orElse(definition).getDowngrade().transform(state);
                lastProtocol = prot;
            }
        }
        if(!identifier.equals(state.getIdentifier())) {
            state = downgrade(version, state, true, lastProtocol);
        }
        return state;
    }

    public static BlockState clone(BlockState blockState) {
        return new BlockStateImpl(blockState.getIdentifier(), blockState.blockStateHash(), blockState.getBlockPropertyValues().toArray(BlockPropertyType.BlockPropertyValue[]::new));
    }

}
