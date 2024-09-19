package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.yukulab.client.extension.TakeItPairs$ClientConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRender extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public MixinPlayerEntityRender(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(
            method = "setModelPose",
            at = @At("RETURN")
    )
    private void invisibleLegWhenRiding(AbstractClientPlayerEntity player, CallbackInfo ci) {
        if(MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON
                && player.getVehicle() instanceof ClientPlayerEntity
                && ((TakeItPairs$ClientConfigHolder)MinecraftClient.getInstance()).takeitpairs$getClientConfig().getInvisibleRiderOnRideMode()) {
            getModel().setVisible(false);
        }
    }
}
