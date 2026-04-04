package com.voxelutopia.ultramarine.dev;

import com.voxelutopia.ultramarine.init.handler.CommonEventHandler;
import com.voxelutopia.ultramarine.init.registry.ModEntityTypes;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;

public class DevCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("ultramarine")
                    .then(Commands.literal("test_natural_spawn")
                            .executes(context -> {
                                var level = context.getSource().getLevel();
                                try {
                                    CommonEventHandler.spawnTrader(level);
                                    context.getSource().sendSuccess(() -> Component.literal("已尝试触发 POI 生成逻辑，请检查周围..."), true);
                                } catch (Exception e) {
                                    context.getSource().sendFailure(Component.literal("触发失败: " + e.getMessage()));
                                }
                                return 1;
                            })
                    )

                    .then(Commands.literal("spawn_here")
                            .executes(context -> {
                                var player = context.getSource().getPlayer();
                                if (player == null) return 0;

                                var pos = player.blockPosition();
                                var world = player.level();

                                var merchant = ModEntityTypes.TRAVELLING_MERCHANT.spawn(
                                        world,
                                        pos,
                                        EntitySpawnReason.COMMAND
                                );

                                if (merchant != null) {
                                    context.getSource().sendSuccess(() -> Component.literal("行商已刷在脚下"), true);
                                    return 1;
                                }
                                return 0;
                            })
                    )
            );
        });
    }
}