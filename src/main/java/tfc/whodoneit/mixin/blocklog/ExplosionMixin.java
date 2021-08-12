package tfc.whodoneit.mixin.blocklog;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tfc.whodoneit.util.interfaces.ICauseAware;
import tfc.whodoneit.blocklog.component.ChunkComponentInitializer;

@Mixin(value = Explosion.class, priority = -10000)
public class ExplosionMixin {
	@Shadow @Final private @Nullable Entity entity;
	
	@Shadow @Final private World world;
	
	@ModifyArgs(method = "affectWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public void WhoDoneIt_preSetBlockState(Args args) {
		BlockPos pos = args.get(0);
		BlockState state = args.get(1);
		ICauseAware causeAware = (ICauseAware)this;
		if (causeAware.getCause() != null) {
			if (causeAware.getMessage() == null) {
				System.out.println("[Explosion Logging] WARN: cause is not null while reason is! This is not intended!");
			} else {
				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
						causeAware.getCause(),
						pos,
						world.getBlockState(pos).getBlock().getTranslationKey(),
						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
						causeAware.getMessage().toString()
				);
			}
		}
//		if (this.entity instanceof ICauseAware) {
//			if (((ICauseAware) this.entity).getCause() != null) {
//				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
//						(PlayerEntity) ((TntEntity)entity).getCausingEntity(),
//						pos,
//						world.getBlockState(pos).getBlock().getTranslationKey(),
//						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
//						((ICauseAware) this.entity).getMessage().toString()
//				);
//			}
//		}
//		if (this.entity instanceof TntEntity) {
//			if (((TntEntity)entity).getCausingEntity() instanceof PlayerEntity) {
//				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
//						(PlayerEntity) ((TntEntity)entity).getCausingEntity(),
//						pos,
//						world.getBlockState(pos).getBlock().getTranslationKey(),
//						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
//						"whodoneit:tnt"
//				);
//			}
//		} else if (this.entity instanceof PlayerEntity) {
//			ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
//					(PlayerEntity) entity,
//					pos,
//					world.getBlockState(pos).getBlock().getTranslationKey(),
//					state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
//					"whodoneit:explosion"
//			);
//		} else if (this.entity instanceof CreeperEntity) {
//			if (((CreeperEntity)entity).getTarget() instanceof PlayerEntity) {
//				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
//						(PlayerEntity) ((CreeperEntity) entity).getTarget(),
//						pos,
//						world.getBlockState(pos).getBlock().getTranslationKey(),
//						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
//						"whodoneit:creeper"
//				);
//			}
//		} else if (this.entity instanceof GhastEntity) {
//			if (((GhastEntity)entity).getTarget() instanceof PlayerEntity) {
//				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
//						(PlayerEntity) ((GhastEntity) entity).getTarget(),
//						pos,
//						world.getBlockState(pos).getBlock().getTranslationKey(),
//						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
//						"whodoneit:ghast"
//				);
//			}
//		} else if (this.entity instanceof WitherEntity) {
//			if (((WitherEntity)entity).getTarget() instanceof PlayerEntity) {
//				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
//						(PlayerEntity) ((WitherEntity) entity).getTarget(),
//						pos,
//						world.getBlockState(pos).getBlock().getTranslationKey(),
//						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
//						"whodoneit:wither"
//				);
//			}
//		} else if (this.entity instanceof EndCrystalEntity) {
//			// TODO
//		}
	}
}
