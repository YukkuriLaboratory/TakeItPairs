package net.yukulab.mixin.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;
import net.yukulab.config.ConfigIO;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements TakeItPairs$ClientConfigHolder {
    @Shadow @Nullable public Entity cameraEntity;
    @Shadow @Nullable public ClientPlayerEntity player;
    @Unique
    private final ClientConfig takeitpairs$clientConfig = ConfigIO.readConfigOrDefault(ClientConfig.class, ClientConfig.getDefaultConfig());

    @Override
    public ClientConfig takeitpairs$getClientConfig() {
        return takeitpairs$clientConfig;
    }

    @Override
    public void takeitpairs$updateClientConfig() {
        ConfigIO.writeConfig(takeitpairs$clientConfig);
    }

    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void onTick(CallbackInfo ci) {
        if(player == null) return;
        if(player.getVehicle() instanceof OtherClientPlayerEntity clientPlayer) {
            if(cameraEntity == null) return;
//            cameraEntity.setYaw(clientPlayer.getYaw());
//            cameraEntity.setBodyYaw(clientPlayer.getBodyYaw());
            cameraEntity.setHeadYaw(clientPlayer.getHeadYaw());
//            player.setYaw(clientPlayer.getYaw());
//            player.setBodyYaw(clientPlayer.getBodyYaw());
            player.setHeadYaw(clientPlayer.getHeadYaw());
//            LogUtils.getLogger().info("Camera Entity head yaw was synced!");
        }
//        if(player.getFirstPassenger() instanceof OtherClientPlayerEntity clientPlayer) {
//            if(cameraEntity == null) return;
//        }
    }
}
