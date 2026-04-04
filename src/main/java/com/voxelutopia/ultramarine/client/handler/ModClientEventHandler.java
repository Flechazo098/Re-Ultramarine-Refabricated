package com.voxelutopia.ultramarine.client.handler;

import com.voxelutopia.ultramarine.client.screen.BrickKilnScreen;
import com.voxelutopia.ultramarine.client.screen.ChiselTableScreen;
import com.voxelutopia.ultramarine.client.screen.ContainerDecorativeBlockScreen;
import com.voxelutopia.ultramarine.client.screen.WoodworkingWorkbenchScreen;
import com.voxelutopia.ultramarine.init.registry.ModEntityTypes;
import com.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;

public class ModClientEventHandler {
    public static void onClientSetup() {
        registerMenuScreens();
        registerEntityRenderers();
    }

    private static void registerMenuScreens() {
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X1, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X3, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X6, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X3, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X6, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.BRICK_KILN, BrickKilnScreen::new);
        MenuScreens.register(ModMenuTypes.CHISEL_TABLE, ChiselTableScreen::new);
        MenuScreens.register(ModMenuTypes.WOODWORKING_WORKBENCH, WoodworkingWorkbenchScreen::new);
    }

    private static void registerEntityRenderers() {
        EntityRenderers.register(ModEntityTypes.SEAT, NoopRenderer::new);
        EntityRenderers.register(ModEntityTypes.TRAVELLING_MERCHANT, WanderingTraderRenderer::new);
    }
}
