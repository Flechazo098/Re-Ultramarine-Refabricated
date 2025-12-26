package com.voxelutopia.ultramarine.init.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BlockEvents {

    public static final Event<Place> PLACE = EventFactory.createArrayBacked(Place.class, callbacks -> (level, pos, state, placer) -> {
        if (state == null) return null;
        BlockState modified = null;
        for (Place c : callbacks) {
            BlockState current = modified != null ? modified : state;
            BlockState res = c.placeBlock(level, pos, current, placer);
            if (res != null) modified = res;
        }
        return modified != null ? modified : state;
    });

    public static final Event<NeighborNotify> NEIGHBOR_NOTIFY =
            EventFactory.createArrayBacked(NeighborNotify.class, callbacks -> (level, pos, state, sides, forceRedstoneUpdate) -> {
                for (NeighborNotify cb : callbacks) {
                    if (!cb.onNeighborNotify(level, pos, state, sides, forceRedstoneUpdate))
                        return false;
                }
                return true;
            });

    @FunctionalInterface
    public interface Place {
        @Nullable BlockState placeBlock(Level level, BlockPos pos, BlockState state, @Nullable Entity placer);
    }

    @FunctionalInterface
    public interface NeighborNotify {
        boolean onNeighborNotify(Level level,
                                 BlockPos pos,
                                 BlockState state,
                                 EnumSet<Direction> notifiedSides,
                                 boolean forceRedstoneUpdate);
    }
}
