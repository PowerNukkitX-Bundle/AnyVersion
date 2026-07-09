package org.powernukkitx.anyversion.registries.registries;

import org.powernukkitx.block.BlockState;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.palette.BlockHashBlockPalette;
import org.powernukkitx.anyversion.utils.palette.BlockPalette;
import org.powernukkitx.anyversion.utils.palette.LegacyBlockPalette;

public class BlockPaletteRegistry extends Registry {

    private BlockHashBlockPalette DEFAULT = new BlockHashBlockPalette();

    private Int2ObjectArrayMap<BlockPalette> PALETTES = new Int2ObjectArrayMap<>();

    @Override
    public void init() {
        register(new LegacyBlockPalette(ProtocolVersion.MINECRAFT_PE_1_19_70));
    }

    public int getRuntimeId(ProtocolVersion version, BlockState state) {
        return PALETTES.getOrDefault(version.protocol(), DEFAULT).getRuntimeId(state);
    }

    private void register(BlockPalette palette) {
        PALETTES.put(palette.getVersion().protocol(), palette);
    }

}
