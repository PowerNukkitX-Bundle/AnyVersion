package org.powernukkitx.anyversion.utils.palette;

import org.powernukkitx.block.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.nbt.NbtUtils;
import org.powernukkitx.anyversion.AnyVersion;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.definition.LegacyBlockDefinition;

import java.io.IOException;
import java.util.Map;

import static org.powernukkitx.block.BlockID.INFO_UPDATE;

public class LegacyBlockPalette extends BlockPalette {

    private final Object2ObjectArrayMap<String, ObjectOpenHashSet<LegacyBlockDefinition>> states = new Object2ObjectArrayMap<>();

    public LegacyBlockPalette(ProtocolVersion version)  {
        super(version);
        try (var stream = AnyVersion.getPlugin().getResource("states/block_palette_" + version.protocol() + ".nbt")) {
            NbtMap root = (NbtMap) NbtUtils.createGZIPReader(stream).readTag();
            var itemComponents = root.getList("blocks", NbtType.COMPOUND);
            int runtimeId = 0;
            for(NbtMap tag : itemComponents) {
                String name = tag.getString("name");
                if(!states.containsKey(name)) {
                    this.states.put(name, new ObjectOpenHashSet<>());
                }
                int id = tag.getInt("id");
                Map<String, Object> states = tag.getCompound("states");
                LegacyBlockDefinition definition = new LegacyBlockDefinition(name, id, runtimeId++, states);
                this.states.get(name).add(definition);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRuntimeId(BlockState state) {
        ObjectOpenHashSet<LegacyBlockDefinition> definitions = states.get(state.getIdentifier());
        if(definitions != null) {
            for(LegacyBlockDefinition definition : definitions) {
                if(definition.equals(state)) {
                    return definition.getRuntimeId();
                }
            }
        }
        return getRuntimeId(new BlockProperties(INFO_UPDATE).getDefaultState());
    }
}
