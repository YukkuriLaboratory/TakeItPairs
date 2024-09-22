package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.yukulab.extension.TakeItPairs$StateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
abstract public class MixinKeyboardInput {
    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void ignoreIfPlayerIsRider(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player instanceof TakeItPairs$StateHolder holder && holder.takeitpairs$isMovementDisabled()) {
            var input = (KeyboardInput) (Object) this;
            input.pressingForward = false;
            input.pressingBack = false;
            input.pressingLeft = false;
            input.pressingRight = false;
            input.movementForward = 0.0F;
            input.movementSideways = 0.0F;
            input.jumping = false;
        }
    }
}
