package net.yukulab.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.NearestPlayersSensor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(NearestPlayersSensor.class)
abstract public class MixinNearestPlayersSensor {
    @Redirect(
            method = "sense",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            )
    )
    private Object randomiseRidingPlayerTarget(List<PlayerEntity> instance, int i) {
        var player = instance.get(i);
        if (player.getVehicle() instanceof PlayerEntity vehicle) {
            if (player.getRandom().nextBoolean()) {
                return vehicle;
            }
        }
        return player;
    }

    @Inject(
            method = "sense",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/util/Optional;)V"
            )
    )
    private void randomiseRidingPlayerTarget(ServerWorld world, LivingEntity entity, CallbackInfo ci, @Local LocalRef<Optional<PlayerEntity>> ref) {
        var randomisedPlayer = ref.get().map(player -> {
            if (player.getVehicle() instanceof PlayerEntity vehicle) {
                if (player.getRandom().nextInt(10) > 2) {
                    return vehicle;
                }
            }
            return player;
        });
        ref.set(randomisedPlayer);
    }
}
