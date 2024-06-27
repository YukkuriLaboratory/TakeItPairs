package net.yukulab.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RespawnAnchorBlock.class)
public abstract class MixinRespawnAnchorBlock {
    @Shadow
    protected abstract ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit);

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getSpawnPointDimension()Lnet/minecraft/registry/RegistryKey;"
            )
    )
    private void syncRespawnPointEvenIfAlreadySet(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        var serverPlayer = (ServerPlayerEntity) player;
        if (serverPlayer.getSpawnPointDimension() == world.getRegistryKey() && pos.equals(serverPlayer.getSpawnPointPosition())) {
            takeitpairs$syncToOtherPlayers(state, world, pos, serverPlayer, hit);
        }
    }

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
            ),
            cancellable = true
    )
    private void syncRespawnPointForOtherPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (takeitpairs$syncToOtherPlayers(state, world, pos, (ServerPlayerEntity) player, hit)) {
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    /**
     * @return {@code true} if any player synced point
     */
    @Unique
    private boolean takeitpairs$syncToOtherPlayers(BlockState state, World world, BlockPos pos, ServerPlayerEntity serverPlayer, BlockHitResult hit) {
        var registryKey = world.getRegistryKey();
        return serverPlayer.getServerWorld()
                .getPlayers((p) -> p != serverPlayer && serverPlayer.distanceTo(p) <= 6)
                .stream()
                .filter((p) -> p.getSpawnPointDimension() != registryKey || !pos.equals(p.getSpawnPointPosition()))
                .map((p) -> onUse(state, world, pos, p, hit))
                .anyMatch(r -> r == ActionResult.SUCCESS);
    }
}
