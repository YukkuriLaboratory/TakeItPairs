package net.yukulab.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.yukulab.TakeItPairsClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends LivingEntity {
    protected MixinClientPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void tickItemStackUsage(ItemStack stack) {
        TakeItPairsClient.LOGGER.info("Tick Item Stack Usage of {}", stack.getName().toString());
        super.tickItemStackUsage(stack);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        TakeItPairsClient.LOGGER.info("Fired on {} with {}", getName().toString(), getMainHandStack().getName().toString());
        return super.interact(player, hand);
    }
}
