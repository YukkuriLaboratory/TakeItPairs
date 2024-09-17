package net.yukulab.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.yukulab.extension.TakeItPairs$Feeding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
abstract public class MixinItemStack {
    @Inject(
            method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void feedRidingPlayer(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getPlayer() instanceof PlayerEntity player) {
            var itemStack = context.getStack();
            var hand = context.getHand();
            var result = takeitpairs$feedRidingPlayer(player, itemStack, hand);
            if (result.isAccepted()) {
                takeitpairs$feedRidingPlayer(player, itemStack, hand);
                cir.setReturnValue(result);
            }
        }
    }


    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void feedRidingPlayer(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        var itemStack = user.getStackInHand(hand);
        if (user instanceof PlayerEntity player) {
            var result = takeitpairs$feedRidingPlayer(player, itemStack, hand);
            if (result.isAccepted()) {
                cir.setReturnValue(TypedActionResult.consume(itemStack));
            }
        }
    }

    @Unique
    private static ActionResult takeitpairs$feedRidingPlayer(PlayerEntity user, ItemStack food, Hand hand) {
        // Check user looking down
        if (user.getPitch() < 75) return ActionResult.PASS;
        var foodComponent = food.get(DataComponentTypes.FOOD);
        if (foodComponent == null) return ActionResult.PASS;
        if (user.getVehicle() instanceof ServerPlayerEntity player && player.canConsume(foodComponent.canAlwaysEat())) {
            if (player instanceof TakeItPairs$Feeding feeding) {
                feeding.takeitpairs$startFeeding(player, food, hand);
                return ActionResult.CONSUME;
            }
        } else {
            return ActionResult.CONSUME_PARTIAL;
        }
        return ActionResult.PASS;
    }
}
