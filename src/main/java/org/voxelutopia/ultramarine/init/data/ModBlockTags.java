package org.voxelutopia.ultramarine.init.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.voxelutopia.ultramarine.Ultramarine;

public class ModBlockTags {

    public static final TagKey<Block> MINEABLE_WITH_SHEARS = create("mineable_with_shears");

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, string));
    }
}
