package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.yukulab.PlayerRole;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;
import net.yukulab.config.ConfigIO;
import net.yukulab.extension.TakeItPairs$RoleHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements TakeItPairs$ClientConfigHolder {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    protected abstract void handleBlockBreaking(boolean breaking);

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
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            ),
            cancellable = true
    )
    private void ignoreIfPlayerIsCarrier(CallbackInfo ci) {
        if (player instanceof TakeItPairs$RoleHolder holder && holder.takeitpairs$getRole() == PlayerRole.CARRIER) {
            handleBlockBreaking(false);
            ci.cancel();
        }
    }
}
