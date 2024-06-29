package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRender<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;animateModel(Lnet/minecraft/entity/Entity;FFF)V")
    )
    public void changeRiderPos(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        double riderPosY = ((TakeItPairs$ClientConfigHolder) MinecraftClient.getInstance()).takeitpairs$getClientConfig().riderPosY();
        if(livingEntity instanceof OtherClientPlayerEntity otherClientPlayerEntity && otherClientPlayerEntity.hasVehicle()) {
            matrixStack.translate(0, riderPosY,0);
        }
        if(livingEntity instanceof ClientPlayerEntity clientPlayerEntity && clientPlayerEntity.hasVehicle() && clientPlayerEntity.getVehicle() instanceof OtherClientPlayerEntity) {
            matrixStack.translate(0, riderPosY, 0);
        }
    }
}
