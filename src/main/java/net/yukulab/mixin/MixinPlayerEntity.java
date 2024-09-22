package net.yukulab.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.yukulab.PlayerRole;
import net.yukulab.extension.TakeItPairs$RoleHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalInt;

@Mixin(PlayerEntity.class)
abstract public class MixinPlayerEntity extends LivingEntity implements TakeItPairs$RoleHolder {
    @Unique
    private static TrackedData<OptionalInt> TAKEITPAIRS_PLAYER_ROLE = null;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "<clinit>",
            at = @At("RETURN")
    )
    private static void registerPlayerRoleData(CallbackInfo ci) {
        TAKEITPAIRS_PLAYER_ROLE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_INT);
    }

    @Inject(
            method = "initDataTracker",
            at = @At("RETURN")
    )
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(TAKEITPAIRS_PLAYER_ROLE, OptionalInt.empty());
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("RETURN")
    )
    private void readPlayerRole(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("TakeItPairsPlayerRole")) {
            this.takeitpairs$setRole(PlayerRole.values()[nbt.getInt("TakeItPairsPlayerRole")]);
        }
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("RETURN")
    )
    private void writePlayerRole(NbtCompound nbt, CallbackInfo ci) {
        var role = this.takeitpairs$getRole();
        if (role != null) {
            nbt.putInt("TakeItPairsPlayerRole", role.ordinal());
        }
    }

    @Nullable
    @Override
    public PlayerRole takeitpairs$getRole() {
        return this.dataTracker.get(TAKEITPAIRS_PLAYER_ROLE).isPresent() ? PlayerRole.values()[this.dataTracker.get(TAKEITPAIRS_PLAYER_ROLE).getAsInt()] : null;
    }

    @Override
    public void takeitpairs$setRole(@Nullable PlayerRole role) {
        this.dataTracker.set(TAKEITPAIRS_PLAYER_ROLE, role == null ? OptionalInt.empty() : OptionalInt.of(role.ordinal()));
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
