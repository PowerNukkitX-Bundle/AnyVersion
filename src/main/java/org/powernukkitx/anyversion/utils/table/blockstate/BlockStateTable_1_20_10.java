package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_10.ConcreteTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_10.ObserverTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_10.ShulkerBoxTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_20_10 extends BlockStateTable {

    public BlockStateTable_1_20_10() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_10,
                of(OBSERVER, new ObserverTransformer()),
                of(BLACK_CONCRETE, new ConcreteTransformer()),
                of(BLUE_CONCRETE, new ConcreteTransformer()),
                of(BROWN_CONCRETE, new ConcreteTransformer()),
                of(CYAN_CONCRETE, new ConcreteTransformer()),
                of(GRAY_CONCRETE, new ConcreteTransformer()),
                of(GREEN_CONCRETE, new ConcreteTransformer()),
                of(LIGHT_BLUE_CONCRETE, new ConcreteTransformer()),
                of(LIME_CONCRETE, new ConcreteTransformer()),
                of(MAGENTA_CONCRETE, new ConcreteTransformer()),
                of(ORANGE_CONCRETE, new ConcreteTransformer()),
                of(PINK_CONCRETE, new ConcreteTransformer()),
                of(PURPLE_CONCRETE, new ConcreteTransformer()),
                of(RED_CONCRETE, new ConcreteTransformer()),
                of(LIGHT_GRAY_CONCRETE, new ConcreteTransformer()),
                of(YELLOW_CONCRETE, new ConcreteTransformer()),
                of(WHITE_CONCRETE, new ConcreteTransformer()),
                of(BLACK_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(BLUE_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(BROWN_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(CYAN_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(GRAY_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(GREEN_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(LIGHT_BLUE_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(LIME_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(MAGENTA_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(ORANGE_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(PINK_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(PURPLE_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(RED_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(LIGHT_GRAY_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(YELLOW_SHULKER_BOX, new ShulkerBoxTransformer()),
                of(WHITE_SHULKER_BOX, new ShulkerBoxTransformer())
        );
    }
}
