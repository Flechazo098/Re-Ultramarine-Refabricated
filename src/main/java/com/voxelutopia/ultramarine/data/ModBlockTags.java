package com.voxelutopia.ultramarine.data;

import com.voxelutopia.ultramarine.Ultramarine;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> MINEABLE_WITH_SHEARS = create();

    private static TagKey<Block> create() {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(Ultramarine.MOD_ID, "mineable_with_shears"));
    }
}

