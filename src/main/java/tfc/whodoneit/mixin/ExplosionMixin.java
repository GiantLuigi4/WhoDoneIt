package tfc.whodoneit.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tfc.whodoneit.blocklog.component.ChunkComponentInitializer;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Shadow @Final private @Nullable Entity entity;
	
	@Redirect(method = "affectWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	public boolean preSetBlockState(World world, BlockPos pos, BlockState state, int flags) {
		if (this.entity instanceof TntEntity) {
			if (((TntEntity)entity).getCausingEntity() instanceof PlayerEntity) {
				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
						(PlayerEntity) ((TntEntity)entity).getCausingEntity(),
						pos,
						world.getBlockState(pos).getBlock().getTranslationKey(),
						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
						"whodoneit:tnt"
				);
			}
		} else if (this.entity instanceof PlayerEntity) {
			ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
					(PlayerEntity) entity,
					pos,
					world.getBlockState(pos).getBlock().getTranslationKey(),
					state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
					"whodoneit:explosion"
			);
		} else if (this.entity instanceof CreeperEntity) {
			if (((CreeperEntity)entity).getTarget() instanceof PlayerEntity) {
				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
						(PlayerEntity) ((CreeperEntity) entity).getTarget(),
						pos,
						world.getBlockState(pos).getBlock().getTranslationKey(),
						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
						"whodoneit:creeper"
				);
			}
		} else if (this.entity instanceof GhastEntity) {
			if (((GhastEntity)entity).getTarget() instanceof PlayerEntity) {
				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
						(PlayerEntity) ((GhastEntity) entity).getTarget(),
						pos,
						world.getBlockState(pos).getBlock().getTranslationKey(),
						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
						"whodoneit:ghast"
				);
			}
		} else if (this.entity instanceof WitherEntity) {
			if (((WitherEntity)entity).getTarget() instanceof PlayerEntity) {
				ChunkComponentInitializer.BLOCKLOG.get(world.getChunk(pos)).addModification(
						(PlayerEntity) ((WitherEntity) entity).getTarget(),
						pos,
						world.getBlockState(pos).getBlock().getTranslationKey(),
						state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
						"whodoneit:wither"
				);
			}
		} else if (this.entity instanceof EndCrystalEntity) {
			// TODO
		}
		return world.setBlockState(pos, state, flags);
	}
}
