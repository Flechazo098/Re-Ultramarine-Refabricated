package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.common.network.WoodworkingRecipesSyncPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {
    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(WoodworkingRecipesSyncPacket.TYPE, WoodworkingRecipesSyncPacket.STREAM_CODEC);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(WoodworkingRecipesSyncPacket.TYPE, WoodworkingRecipesSyncPacket::handle);
    }
}
