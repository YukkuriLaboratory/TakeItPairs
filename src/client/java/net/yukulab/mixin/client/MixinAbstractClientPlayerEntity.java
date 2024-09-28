package net.yukulab.mixin.client;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractClientPlayerEntity.class)
abstract public class MixinAbstractClientPlayerEntity extends PlayerEntity {

    public MixinAbstractClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Override
    public void onPassengerLookAround(Entity passenger) {
        clampPassengerYaw(passenger);
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (!passenger.getType().isIn(EntityTypeTags.CAN_TURN_IN_BOATS)) {
            clampPassengerYaw(passenger);
        }
    }

    /**
     * Adjust the player's camera when they are riding a player
     * See {@link net.minecraft.entity.vehicle.BoatEntity#clampPassengerYaw  }
     */
    private void clampPassengerYaw(Entity passenger) {
        if (passenger instanceof PlayerEntity) {
            var vehicle = passenger.getVehicle();
            if (vehicle == null) return;
            passenger.setBodyYaw(vehicle.getYaw());
            float f = MathHelper.wrapDegrees(passenger.getYaw() - vehicle.getYaw());
            float g = MathHelper.clamp(f, -105.0F, 105.0F);
            passenger.prevYaw += g - f;
            passenger.setYaw(passenger.getYaw() + g - f);
            passenger.setHeadYaw(passenger.getYaw());
        }
    }
}
