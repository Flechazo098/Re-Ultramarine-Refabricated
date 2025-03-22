package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.menu.BrickKilnMenu;
import org.voxelutopia.ultramarine.common.menu.ChiselTableMenu;
import org.voxelutopia.ultramarine.common.menu.ContainerDecorativeBlockMenu;
import org.voxelutopia.ultramarine.common.menu.WoodworkingWorkbenchMenu;
import org.voxelutopia.ultramarine.util.IForgeMenuType;

public class ModMenuTypes {

    public static MenuType<ContainerDecorativeBlockMenu> CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X1;
    public static MenuType<ContainerDecorativeBlockMenu> CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X3;
    public static MenuType<ContainerDecorativeBlockMenu> CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X6;
    public static MenuType<ContainerDecorativeBlockMenu> CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X3;
    public static MenuType<ContainerDecorativeBlockMenu> CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X6;
    public static MenuType<WoodworkingWorkbenchMenu> WOODWORKING_WORKBENCH;
    public static MenuType<BrickKilnMenu> BRICK_KILN;
    public static MenuType<ChiselTableMenu> CHISEL_TABLE;


    public static void registerModMenus() {
        CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X1 = Registry.register(BuiltInRegistries.MENU, "container_decorative_block_menu_generic_9x1", new MenuType<>(ContainerDecorativeBlockMenu::genericOneRow, FeatureFlagSet.of()));
        CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X3 = Registry.register(BuiltInRegistries.MENU, "container_decorative_block_menu_generic_9x3", new MenuType<>(ContainerDecorativeBlockMenu::genericThreeRows, FeatureFlagSet.of()));
        CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X6 = Registry.register(BuiltInRegistries.MENU, "container_decorative_block_menu_generic_9x6", new MenuType<>(ContainerDecorativeBlockMenu::genericSixRows, FeatureFlagSet.of()));
        CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X3 = Registry.register(BuiltInRegistries.MENU, "container_decorative_block_menu_food_9x3", new MenuType<>(ContainerDecorativeBlockMenu::foodThreeRows, FeatureFlagSet.of()));
        CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X6 = Registry.register(BuiltInRegistries.MENU, "container_decorative_block_menu_food_9x6", new MenuType<>(ContainerDecorativeBlockMenu::foodSixRows, FeatureFlagSet.of()));
        WOODWORKING_WORKBENCH = Registry.register(BuiltInRegistries.MENU, "woodworking_workbench", new MenuType<>(WoodworkingWorkbenchMenu::new, FeatureFlagSet.of()));
        BRICK_KILN = Registry.register(BuiltInRegistries.MENU, "brick_kiln",
                IForgeMenuType.create((windowId, inv, data) -> {
                    // 添加空指针检查并获取方块位置
                    BlockPos pos = data != null ? data.readBlockPos() : BlockPos.ZERO;
                    return new BrickKilnMenu(windowId, pos, inv);
                }));
        Ultramarine.LOGGER.info("砖窑menu已注册");
        CHISEL_TABLE = Registry.register(BuiltInRegistries.MENU, "chisel_table", new MenuType<>(ChiselTableMenu::new, FeatureFlagSet.of()));
    }

}
