package net.yukulab.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract public class MixinEntity {
    @Shadow
    public abstract @Nullable Entity getVehicle();

    @Inject(
            method = "startRiding(Lnet/minecraft/entity/Entity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void rideWithRidingPlayer(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (getVehicle() instanceof PlayerEntity vehiclePlayer) {
            if (vehiclePlayer.startRiding(entity)) {
                cir.setReturnValue(true);
            }
        }
    }
}
