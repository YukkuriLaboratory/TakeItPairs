package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
abstract public class MixinGameRender {
    @Inject(
            method = "method_18144",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void ignoreRidingPlayer(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        var result = cir.getReturnValue();
        if (result && entity.getVehicle() == MinecraftClient.getInstance().player) {
            cir.setReturnValue(false);
        }
    }
}
