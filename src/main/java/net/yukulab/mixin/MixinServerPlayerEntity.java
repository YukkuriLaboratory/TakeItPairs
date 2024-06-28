package net.yukulab.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.logging.LogUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.yukulab.extension.TakeIrPairs$ForceSpawnConsumptionEffects;
import net.yukulab.extension.TakeItPairs$Feeding;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends LivingEntity implements TakeItPairs$Feeding {

    @Shadow
    protected abstract void consumeItem();

    @Shadow
    public abstract ServerWorld getServerWorld();

    protected MixinServerPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private ServerPlayerEntity takeitpairs$feedingPlayer = null;
    @Unique
    private int takeitpairs$feedingTimeLeft = 0;
    @Unique
    private int takeitpairs$resetFeedingLeft = 0;
//    @Unique
//    private static final Hand[] takeitpairs$acceptHands = {Hand.MAIN_HAND, Hand.OFF_HAND};

    @SuppressWarnings("UnreachableCode")
    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        var entity = (ServerPlayerEntity) (Object) this;
        if (isAlive()) {
            var targetHand = Hand.MAIN_HAND;
            var handItem = player.getStackInHand(targetHand);
            if (handItem.isEmpty() && !player.hasPassenger(entity)) {
                if (player.startRiding(entity)) {
                    entity.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
                }
                return ActionResult.SUCCESS;
            }
            if(handItem.getUseAction() == UseAction.DRINK) {
                if (player instanceof TakeItPairs$Feeding takeItPairs$feeding) {
                    LogUtils.getLogger().debug("[TIP] Fired potionComponent");
                    takeItPairs$feeding.takeitpairs$startFeeding(entity, handItem, targetHand);
                    return ActionResult.SUCCESS;
                }
            }
            var foodComponent = handItem.get(DataComponentTypes.FOOD);
            if (foodComponent == null || !entity.canConsume(foodComponent.canAlwaysEat())) {
                targetHand = Hand.OFF_HAND;
                handItem = player.getStackInHand(targetHand);
                foodComponent = handItem.get(DataComponentTypes.FOOD);
            }
            if (foodComponent != null && entity.canConsume(foodComponent.canAlwaysEat())) {
                if (player instanceof TakeItPairs$Feeding takeItPairs$feeding) {
                    takeItPairs$feeding.takeitpairs$startFeeding(entity, handItem, targetHand);
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

    @Inject(
            method = "stopRiding",
            at = @At("TAIL")
    )
    private void syncForVehiclePlayer(CallbackInfo ci, @Local Entity entity) {
        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            serverPlayerEntity.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
        }
    }

    @Unique
    boolean takeitpairs$isUpdatingSpawnPoint = false;

    @Inject(
            method = "setSpawnPoint",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ignoreIfUpdating(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {
        if (takeitpairs$isUpdatingSpawnPoint) {
            ci.cancel();
        }
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(
            method = "setSpawnPoint",
            at = @At("TAIL")
    )
    private void syncSpawnPointToOtherPlayer(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {
        if (pos != null) {
            var player = (ServerPlayerEntity) (Object) this;
            takeitpairs$isUpdatingSpawnPoint = true;

            var spawnPointDimension = player.getSpawnPointDimension();
            var spawnPointPosition = player.getSpawnPointPosition();
            var spawnAngle = player.getSpawnAngle();
            var spawnForced = player.isSpawnForced();

            var targets = getServerWorld().getPlayers((p) -> p != player && player.distanceTo(p) <= 6);
            for (ServerPlayerEntity target : targets) {
                target.setSpawnPoint(spawnPointDimension, spawnPointPosition, spawnAngle, spawnForced, true);
            }

            takeitpairs$isUpdatingSpawnPoint = false;
        }
    }

    /**
     * {@link LivingEntity#tickItemStackUsage(ItemStack)}
     * <br/>
     * {@link LivingEntity#consumeItem()}
     */
    @Override
    public void takeitpairs$startFeeding(ServerPlayerEntity target, ItemStack feedItem, Hand hand) {
        takeitpairs$resetFeedingLeft = 4;
        if (!target.equals(takeitpairs$feedingPlayer)) {
            takeitpairs$feedingPlayer = target;
            takeitpairs$feedingTimeLeft = feedItem.getMaxUseTime(target);
        }
        if (takeitpairs$shouldSpawnFeedingEffects(feedItem)) {
            if (target instanceof TakeIrPairs$ForceSpawnConsumptionEffects t) {
                t.takeitpairs$forceSpawnConsumptionEffects(feedItem, 5);
            }
        }
        if (--takeitpairs$feedingTimeLeft == 0 && !feedItem.isUsedOnRelease()) {
            if (target instanceof TakeIrPairs$ForceSpawnConsumptionEffects t) {
                t.takeitpairs$forceSpawnConsumptionEffects(feedItem, 16);
            }
            var newItem = feedItem.finishUsing(getWorld(), target);
            if (newItem != feedItem) {
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
