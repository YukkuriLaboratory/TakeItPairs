package net.yukulab.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.yukulab.TakeItPairs;
import net.yukulab.network.packet.play.SyncStateC2SPacket;

public class Networking {
    public static final Identifier SYNC_DISABLE_STATE = Identifier.of(TakeItPairs.MOD_ID, "syncstate");

    public static void registerServerHandlers() {
        PayloadTypeRegistry.playC2S().register(SyncStateC2SPacket.ID, SyncStateC2SPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SyncStateC2SPacket.ID, SyncStateC2SPacket::onReceive);
    }
}
