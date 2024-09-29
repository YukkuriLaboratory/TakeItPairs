package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import net.yukulab.config.ClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRender<T extends LivingEntity, M extends EntityModel<T>> {
    @Redirect(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;hasVehicle()Z"
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/LivingEntity;isBaby()Z"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/entity/LivingEntity;getVehicle()Lnet/minecraft/entity/Entity;"
                    )
            )
    )
    private boolean disableYawClampIfRidingPlayer(LivingEntity instance) {
        // Sync riding player body yaw with the vehicle player head yaw
        return instance.hasVehicle() && !(instance.getVehicle() instanceof PlayerEntity);
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;animateModel(Lnet/minecraft/entity/Entity;FFF)V")
    )
    public void renderPlayer(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        ClientConfig config = ((TakeItPairs$ClientConfigHolder) MinecraftClient.getInstance()).takeitpairs$getClientConfig();

        double riderPosY = -(config.isShoulderRideMode() ? config.getShoulderModeRiderY() : config.getRiderPosY());
        double riderPosZ = -(config.isShoulderRideMode() ? config.getShoulderModeRiderZ() : config.getRiderPosZ());

        // === Change rider pos from config value ===
        if (livingEntity instanceof PlayerEntity playerEntity && playerEntity.hasVehicle() && playerEntity.getVehicle() instanceof PlayerEntity vehicleEntity) {
            var offset = takeitpairs$checkSize(vehicleEntity, 1);
            matrixStack.translate(0, riderPosY * offset, riderPosZ * offset);
        }
    }

    @Unique
    public int takeitpairs$checkSize(PlayerEntity playerEntity, int n) {
        if (playerEntity.hasVehicle() && playerEntity.getVehicle() instanceof PlayerEntity vehicleEntity) {
            return takeitpairs$checkSize(vehicleEntity, n + 1);
        }
        return n;
    }
}
