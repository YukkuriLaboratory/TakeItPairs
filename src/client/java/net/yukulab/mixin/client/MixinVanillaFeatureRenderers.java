package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({
        ArmorFeatureRenderer.class,
        ElytraFeatureRenderer.class,
        HeadFeatureRenderer.class,
        HeldItemFeatureRenderer.class,
        HeadFeatureRenderer.class,
        TridentRiptideFeatureRenderer.class,
        ShoulderParrotFeatureRenderer.class,
        Deadmau5FeatureRenderer.class,
})
public abstract class MixinVanillaFeatureRenderers<T extends Entity> {
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void makePlayerInvisible(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON
                && livingEntity.getVehicle() instanceof ClientPlayerEntity
                && ((TakeItPairs$ClientConfigHolder)MinecraftClient.getInstance()).takeitpairs$getClientConfig().getInvisibleRiderOnRideMode()) {
            ci.cancel();
        }
    }
}
