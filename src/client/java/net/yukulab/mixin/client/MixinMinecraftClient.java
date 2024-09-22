package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;
import net.yukulab.config.ConfigIO;
import net.yukulab.extension.TakeItPairs$StateHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow
    @Final
    public GameOptions options;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Inject(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
            ),
            cancellable = true
    )
    private void ignoreIfPlayerIsCarrier(CallbackInfo ci) {
        if (player instanceof TakeItPairs$StateHolder holder && holder.takeitpairs$isClickDisabled()) {
            while (this.options.attackKey.wasPressed()) {
            }

            while (this.options.useKey.wasPressed()) {
            }

            while (this.options.pickItemKey.wasPressed()) {
            }
            handleBlockBreaking(false);
            ci.cancel();
        }
    }
}
