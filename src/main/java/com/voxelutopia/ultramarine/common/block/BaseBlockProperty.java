package com.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.Codec;
import com.voxelutopia.ultramarine.init.data.ModBlockTags;
import com.voxelutopia.ultramarine.init.registry.ModSounds;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public record BaseBlockProperty(BlockBehaviour.Properties properties, BlockMaterial material)  {

    public static final BaseBlockProperty STONE = stone();
    public static final BaseBlockProperty MARBLE = marble();
    public static final BaseBlockProperty TERRACOTTA = terracotta();
    public static final BaseBlockProperty IRON = iron();
    public static final BaseBlockProperty COPPER = copper();
    public static final BaseBlockProperty BRONZE = bronze();
    public static final BaseBlockProperty TILE = tile();
    public static final BaseBlockProperty PORCELAIN = porcelain();
    public static final BaseBlockProperty WOOD = wood();
    public static final BaseBlockProperty BAMBOO_WOOD = bamboo_wood();
    public static final BaseBlockProperty BAMBOO = bamboo();
    public static final BaseBlockProperty GLAZED = glazed();
    public static final BaseBlockProperty JADE = jade();
    public static final BaseBlockProperty FLAX = flax();
    public static final BaseBlockProperty CROP = crop();
    public static final BaseBlockProperty PLANT = plant();
    public static final BaseBlockProperty LILY = lily();
    public static final BaseBlockProperty SILK = silk();
    public static final BaseBlockProperty WOOL = wool();
    public static final BaseBlockProperty PAPER = paper();
    public static final BaseBlockProperty DYE = dye();
    public static final BaseBlockProperty ICE = ice();

    public static BaseBlockProperty stone() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.BASEDRUM),
                BlockMaterial.STONE);
    }

    public static BaseBlockProperty marble() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(1.8F, 7.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.BASEDRUM),
                BlockMaterial.STONE);
    }

    public static BaseBlockProperty terracotta() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(1.2F, 4.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.BASEDRUM),
                BlockMaterial.STONE);
    }

    public static BaseBlockProperty iron() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(5.0F, 6.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE),
                BlockMaterial.METAL);
    }

    public static BaseBlockProperty copper() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(5.0F, 6.0F)
                .requiresCorrectToolForDrops(),
                BlockMaterial.METAL);
    }

    public static BaseBlockProperty bronze() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(5.5F, 6.5F)
                .requiresCorrectToolForDrops(),
                BlockMaterial.METAL);
    }

    public static BaseBlockProperty tile() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.BONE_BLOCK)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.BASEDRUM),
                BlockMaterial.STONE);
    }

    public static BaseBlockProperty porcelain() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(ModSounds.PORCELAIN)
                .strength(1.0F, 1.0F)
                .instrument(NoteBlockInstrument.HAT),
                BlockMaterial.PORCELAIN);
    }

    public static BaseBlockProperty wood() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.WOOD)
                .strength(2.0F, 3.0F)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.BASS),
                BlockMaterial.WOOD);
    }

    public static BaseBlockProperty bamboo_wood() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(ModSounds.BAMBOO_WOOD)
                .strength(2.0F, 3.0F)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.BASS),
                BlockMaterial.WOOD);
    }

    public static BaseBlockProperty bamboo() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.BAMBOO)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.XYLOPHONE)
                .strength(1.5F, 2.5F),
                BlockMaterial.BAMBOO);
    }

    public static BaseBlockProperty glazed() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.GLASS)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.HAT),
                BlockMaterial.STONE);
    }

    public static BaseBlockProperty jade() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.GLASS)
                .strength(1.2F, 5.0F)
                .requiresCorrectToolForDrops()
                .instrument(NoteBlockInstrument.BELL),
                BlockMaterial.STONE);
    }

    public static BaseBlockProperty flax() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(ModSounds.FLAX)
                .strength(1F, 2F)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.BANJO),
                BlockMaterial.FLAX);
    }

    public static BaseBlockProperty crop() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.CROP)
                .strength(1F, 1.5F)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY),
                BlockMaterial.PLANT);
    }

    public static BaseBlockProperty plant() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.CROP)
                .strength(1F, 1.5F)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY),
                BlockMaterial.PLANT);
    }

    public static BaseBlockProperty lily() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.LILY_PAD)
                .strength(1F, 1.5F)
                .instabreak(),
                BlockMaterial.PLANT);
    }

    public static BaseBlockProperty silk() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.WOOL)
                .strength(1F, 2F)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.GUITAR),
                BlockMaterial.FABRIC);
    }

    public static BaseBlockProperty wool() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.WOOL)
                .strength(1F, 2F)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.GUITAR),
                BlockMaterial.WOOL);
    }

    public static BaseBlockProperty paper() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.WOOL)
                .strength(1F, 2F)
                .ignitedByLava(),
                BlockMaterial.PAPER);
    }

    public static BaseBlockProperty dye() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.WOOL)
                .strength(1F, 1F),
                BlockMaterial.PAPER);
    }

    public static BaseBlockProperty ice() {
        return new BaseBlockProperty(BlockBehaviour.Properties.of()
                .sound(SoundType.CROP)
                .strength(0.5F, 1.0F),
                BlockMaterial.ICE);
    }

    //todo map colors

    public static final Codec<BaseBlockProperty> CODEC = Codec.STRING.xmap(
            name -> {
                try {
                    Field field = BaseBlockProperty.class.getField(name);
                    if (field.getType() == BaseBlockProperty.class) {
                        return (BaseBlockProperty) field.get(null);
                    }
                } catch (Exception ignored) {
                }
                return STONE;
            },
            prop -> {
                for (Field field : BaseBlockProperty.class.getFields()) {
                    if (field.getType() == BaseBlockProperty.class) {
                        try {
                            if (field.get(null) == prop) {
                                return field.getName();
                            }
                        } catch (IllegalAccessException ignored) {
                        }
                    }
                }
                return "STONE";
            }
    );

    public BlockMaterial getMaterial() {
        return material;
    }

    public enum BlockMaterial {
        STONE(BlockTags.MINEABLE_WITH_PICKAXE),
        METAL(BlockTags.MINEABLE_WITH_PICKAXE),
        ICE(BlockTags.MINEABLE_WITH_PICKAXE),
        WOOD(BlockTags.MINEABLE_WITH_AXE),
        PORCELAIN(BlockTags.MINEABLE_WITH_PICKAXE),
        BAMBOO(BlockTags.MINEABLE_WITH_AXE),
        FABRIC(ModBlockTags.MINEABLE_WITH_SHEARS),
        WOOL(ModBlockTags.MINEABLE_WITH_SHEARS),
        PAPER(ModBlockTags.MINEABLE_WITH_SHEARS),
        PLANT(BlockTags.MINEABLE_WITH_HOE),
        FLAX(BlockTags.MINEABLE_WITH_HOE);

        final TagKey<Block> tool;

        public TagKey<Block> getTool() {
            return tool;
        }

        BlockMaterial(TagKey<Block> tool) {
            this.tool = tool;
        }
    }

}
