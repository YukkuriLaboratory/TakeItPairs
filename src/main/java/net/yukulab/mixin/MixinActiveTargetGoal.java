package net.yukulab.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ActiveTargetGoal.class)
abstract public class MixinActiveTargetGoal {
    @Shadow
    @Nullable
    protected LivingEntity targetEntity;

    @Redirect(
            method = "findClosestTarget",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/ai/goal/ActiveTargetGoal;targetEntity:Lnet/minecraft/entity/LivingEntity;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void randomiseRidingPlayerTarget(ActiveTargetGoal instance, LivingEntity value) {
        if (value instanceof PlayerEntity player && player.getVehicle() instanceof PlayerEntity vehicle) {
            if (player.getRandom().nextInt(10) > 2) {
                targetEntity = vehicle;
                return;
            }
        }
        targetEntity = value;
    }
}
