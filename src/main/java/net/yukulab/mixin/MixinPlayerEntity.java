package net.yukulab.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.yukulab.extension.TakeItPairs$StateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
abstract public class MixinPlayerEntity extends LivingEntity implements TakeItPairs$StateHolder {
    @Unique
    private static TrackedData<Boolean> TAKEITPAIRS_DISABLE_MOVEMENT = null;
    @Unique
    private static TrackedData<Boolean> TAKEITPAIRS_DISABLE_CLICK = null;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "<clinit>",
            at = @At("RETURN")
    )
    private static void registerPlayerRoleData(CallbackInfo ci) {
        TAKEITPAIRS_DISABLE_MOVEMENT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        TAKEITPAIRS_DISABLE_CLICK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Inject(
            method = "initDataTracker",
            at = @At("RETURN")
    )
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(TAKEITPAIRS_DISABLE_MOVEMENT, false);
        builder.add(TAKEITPAIRS_DISABLE_CLICK, false);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("RETURN")
    )
    private void readPlayerRole(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("TakeItPairsDisableMovement")) {
            this.takeitpairs$disableMovement(nbt.getBoolean("TakeItPairsDisableMovement"));
        }
        if (nbt.contains("TakeItPairsDisableClick")) {
            this.takeitpairs$disableClick(nbt.getBoolean("TakeItPairsDisableClick"));
        }
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("RETURN")
    )
    private void writePlayerRole(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("TakeItPairsDisableMovement", this.takeitpairs$isMovementDisabled());
        nbt.putBoolean("TakeItPairsDisableClick", this.takeitpairs$isClickDisabled());
    }

    @Override
    public void takeitpairs$disableMovement(boolean disable) {
        this.dataTracker.set(TAKEITPAIRS_DISABLE_MOVEMENT, disable);
    }

    @Override
    public boolean takeitpairs$isMovementDisabled() {
        return this.dataTracker.get(TAKEITPAIRS_DISABLE_MOVEMENT);
    }

    @Override
    public void takeitpairs$disableClick(boolean disable) {
        this.dataTracker.set(TAKEITPAIRS_DISABLE_CLICK, disable);
    }

    @Override
    public boolean takeitpairs$isClickDisabled() {
        return this.dataTracker.get(TAKEITPAIRS_DISABLE_CLICK);
    }

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
