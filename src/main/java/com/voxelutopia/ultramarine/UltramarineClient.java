package com.voxelutopia.ultramarine;

import com.voxelutopia.ultramarine.client.handler.ModClientEventHandler;
import com.voxelutopia.ultramarine.init.registry.ModPackets;
import net.fabricmc.api.ClientModInitializer;

/**
 * Name: Ultramarine / UltramarineClient
 * Author: cnlimiter
 * CreateTime: 2023/10/3 11:00
 * Description:
 */

public class UltramarineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPackets.registerClient();
        ModClientEventHandler.onClientSetup();
    }
}
