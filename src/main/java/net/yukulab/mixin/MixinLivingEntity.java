package net.yukulab.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.yukulab.extension.TakeIrPairs$ForceSpawnConsumptionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
abstract public class MixinLivingEntity implements TakeIrPairs$ForceSpawnConsumptionEffects {
    @Shadow
    protected abstract SoundEvent getDrinkSound(ItemStack stack);

    @Shadow
    public abstract SoundEvent getEatSound(ItemStack stack);

    /**
     * {@link LivingEntity#spawnConsumptionEffects(ItemStack, int)}
     */
    @SuppressWarnings("UnreachableCode")
    @Override
    public void takeitpairs$forceSpawnConsumptionEffects(ItemStack stack, int particleCount) {
        var livingEntity = (LivingEntity) (Object) this;
        if (stack.getUseAction() == UseAction.DRINK) {
            livingEntity.getWorld().playSound(null, livingEntity.getBlockPos(), getDrinkSound(stack), SoundCategory.PLAYERS, 0.5F, livingEntity.getWorld().random.nextFloat() * 0.1F + 0.9F);
        }

        if (stack.getUseAction() == UseAction.EAT) {
            takeitpairs$spawnItemParticlesForAll(stack, particleCount);
            livingEntity.getWorld().playSound(null, livingEntity.getBlockPos(), getEatSound(stack), SoundCategory.PLAYERS, 0.5F + 0.5F * livingEntity.getRandom().nextInt(2), (livingEntity.getRandom().nextFloat() - livingEntity.getRandom().nextFloat()) * 0.2F + 1.0F);
        }
    }

    /**
     * {@link LivingEntity#spawnItemParticles(ItemStack, int)}
     */
    @Unique
    @SuppressWarnings("UnreachableCode")
    private void takeitpairs$spawnItemParticlesForAll(ItemStack stack, int count) {
        var livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.getWorld() instanceof ServerWorld serverWorld) {
            for (int i = 0; i < count; i++) {
                Vec3d vec3d = new Vec3d((livingEntity.getRandom().nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3d = vec3d.rotateX(-livingEntity.getPitch() * (float) (Math.PI / 180.0));
                vec3d = vec3d.rotateY(-livingEntity.getYaw() * (float) (Math.PI / 180.0));
                double d = (double) (-livingEntity.getRandom().nextFloat()) * 0.6 - 0.3;
                Vec3d vec3d2 = new Vec3d((livingEntity.getRandom().nextFloat() - 0.5) * 0.3, d, 0.6);
                vec3d2 = vec3d2.rotateX(-livingEntity.getPitch() * (float) (Math.PI / 180.0));
                vec3d2 = vec3d2.rotateY(-livingEntity.getYaw() * (float) (Math.PI / 180.0));
                vec3d2 = vec3d2.add(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());

                serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, 1, vec3d.x, vec3d.y + 0.05, vec3d.z, 0.05);
            }
        }
    }
}
