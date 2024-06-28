package net.yukulab.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
    @Inject(
            method = "interactItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V"),
            cancellable = true
    )
    private void checkInteractWithPotion(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(player.getMainHandStack().getUseAction() == UseAction.DRINK && MinecraftClient.getInstance().crosshairTarget instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
