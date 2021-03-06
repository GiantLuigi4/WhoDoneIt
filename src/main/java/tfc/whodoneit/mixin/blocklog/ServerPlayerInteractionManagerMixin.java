package tfc.whodoneit.mixin.blocklog;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.whodoneit.components.WhoDoneItChunkComponents;
import tfc.whodoneit.registry.ChangeReasons;

@Mixin(value = ServerPlayerInteractionManager.class, priority = -10000)
public class ServerPlayerInteractionManagerMixin {
	@Shadow public ServerWorld world;
	
	@Shadow public ServerPlayerEntity player;
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"), method = "tryBreakBlock")
	public void WhoDoneIt_preBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		WhoDoneItChunkComponents.BLOCKLOG.get(world.getChunk(pos))
				.addModification(
						player,
						pos,
						world.getBlockState(pos).getBlock().getTranslationKey(),
						Blocks.AIR.getDefaultState().getBlock().getTranslationKey(),
						Util.getEpochTimeMs(),
						ChangeReasons.MINING.toString()
				);
	}
}
