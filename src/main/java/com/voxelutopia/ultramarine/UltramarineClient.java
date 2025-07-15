package com.voxelutopia.ultramarine;

import com.voxelutopia.ultramarine.client.event.ModClientRenderEventHandler;
import net.fabricmc.api.ClientModInitializer;

public class UltramarineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModClientRenderEventHandler.onClientSetup();
    }
}
