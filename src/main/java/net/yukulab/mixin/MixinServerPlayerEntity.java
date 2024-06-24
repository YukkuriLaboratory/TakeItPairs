package net.yukulab.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.yukulab.extension.TakeItPairs$Feeding;
import net.yukulab.extension.TakeIrPairs$ForceSpawnConsumptionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends LivingEntity implements TakeItPairs$Feeding {

    @Shadow protected abstract void consumeItem();

    protected MixinServerPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private ServerPlayerEntity takeitpairs$feedingPlayer = null;
    @Unique
    private int takeitpairs$feedingTimeLeft = 0;
    @Unique
    private int takeitpairs$resetFeedingLeft = 0;

    @SuppressWarnings("UnreachableCode")
    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        var entity = (ServerPlayerEntity) (Object) this;
        if (isAlive()) {
            var targetHand = Hand.MAIN_HAND;
            var handItem = player.getStackInHand(targetHand);
            if (handItem.isEmpty() && !player.hasPassenger(entity)) {
                //FIXME Sync player ride state
                player.startRiding(entity);
                return ActionResult.SUCCESS;
            }
            var foodComponent = handItem.get(DataComponentTypes.FOOD);
            if (foodComponent == null || !entity.canConsume(foodComponent.canAlwaysEat())) {
                targetHand = Hand.OFF_HAND;
                handItem = player.getStackInHand(targetHand);
                foodComponent = handItem.get(DataComponentTypes.FOOD);
            }
            if (foodComponent != null && entity.canConsume(foodComponent.canAlwaysEat())) {
                if (player instanceof TakeItPairs$Feeding takeItPairs$feeding) {
                    takeItPairs$feeding.takeitpairs$startFeeding(entity, handItem, hand);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.interact(player, hand);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void updatePassedItemState(CallbackInfo ci) {
        if (takeitpairs$feedingPlayer != null && --takeitpairs$resetFeedingLeft < 0) {
            takeitpairs$resetFeedingTarget();
        }
    }

    /**
     * {@link LivingEntity#tickItemStackUsage(ItemStack)}
     * <br/>
     * {@link LivingEntity#consumeItem()}
     */
    @Override
    public void takeitpairs$startFeeding(ServerPlayerEntity target, ItemStack food, Hand hand) {
        takeitpairs$resetFeedingLeft = 4;
        if (!target.equals(takeitpairs$feedingPlayer)) {
            takeitpairs$feedingPlayer = target;
            takeitpairs$feedingTimeLeft = food.getMaxUseTime(target);
        }
        if (takeitpairs$shouldSpawnFeedingEffects(food)) {
            if (target instanceof TakeIrPairs$ForceSpawnConsumptionEffects t) {
                t.takeitpairs$forceSpawnConsumptionEffects(food, 5);
            }
        }
        if (--takeitpairs$feedingTimeLeft == 0 && !food.isUsedOnRelease()) {
            if (target instanceof TakeIrPairs$ForceSpawnConsumptionEffects t) {
                t.takeitpairs$forceSpawnConsumptionEffects(food, 16);
            }
            var newItem = food.finishUsing(getWorld(), target);
            if (newItem != food) {
                setStackInHand(hand, newItem);
            }
            takeitpairs$resetFeedingTarget();
        }
    }

    @Unique
    private void takeitpairs$resetFeedingTarget() {
        takeitpairs$feedingPlayer = null;
    }

    /**
     * {@link LivingEntity#shouldSpawnConsumptionEffects()}
     */
    @Unique
    private boolean takeitpairs$shouldSpawnFeedingEffects(ItemStack food) {
        var maxUseTime = food.getMaxUseTime(takeitpairs$feedingPlayer);
        var timeDiff = maxUseTime - takeitpairs$feedingTimeLeft;
        int min = (int) (maxUseTime * 0.21785f);
        return timeDiff > min && takeitpairs$feedingTimeLeft % 2 == 0;
    }
}
