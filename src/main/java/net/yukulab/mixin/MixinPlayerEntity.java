package net.yukulab.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Debug(export = true)
@Mixin(PlayerEntity.class)
abstract public class MixinPlayerEntity {
    @Redirect(
            method = "getBlockBreakingSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z"
            )
    )
    private boolean ignoreIfRidingPlayer(PlayerEntity instance) {
        return instance.isOnGround() || (instance.getVehicle() instanceof PlayerEntity player && player.isOnGround());
    }
}
