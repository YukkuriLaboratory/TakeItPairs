package net.yukulab.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LookAtEntityGoal.class)
abstract public class MixinLookAtEntityGoal {
    @Shadow
    @Nullable
    protected Entity target;

    @Inject(
            method = "canStart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;DDD)Lnet/minecraft/entity/player/PlayerEntity;",
                    shift = At.Shift.AFTER
            )
    )
    private void randomiseRidingPlayerTarget(CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof PlayerEntity player && player.getVehicle() instanceof PlayerEntity vehicle) {
            if (player.getRandom().nextInt(10) > 2) {
                target = vehicle;
            }
        }
    }
}
