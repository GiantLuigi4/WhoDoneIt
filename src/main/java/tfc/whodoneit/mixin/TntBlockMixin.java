package tfc.whodoneit.mixin;

import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tfc.whodoneit.blocklog.BlockModification;
import tfc.whodoneit.components.WhoDoneItChunkComponents;
import tfc.whodoneit.blocklog.mixin.TntMixin;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.util.interfaces.ICauseAware;

@Mixin(TntBlock.class)
public class TntBlockMixin {
	/**
	 * @author TFC, GiantLuigi4
	 * tracking explosions
	 */
	@Overwrite
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		if (!world.isClient) {
			TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosion.getCausingEntity());
			tntEntity.setFuse((short)(world.random.nextInt(tntEntity.getFuseTimer() / 4) + tntEntity.getFuseTimer() / 8));
			TntMixin.onDestroyedByExplosion(world, pos, explosion, tntEntity);
			world.spawnEntity(tntEntity);
		}
	}
	
	/**
	 * @author TFC, GiantLuigi4
	 * tracking explosions
	 */
	@Overwrite
	private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {
		if (!world.isClient) {
			TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter);
			if (igniter == null) {
				BlockModification modification = WhoDoneItChunkComponents.BLOCKLOG.get(world.getChunk(pos)).getLatest(pos);
				if (modification != null) {
					ICauseAware causeAware = (ICauseAware)tntEntity;
					causeAware.setCause(modification.culprit);
					causeAware.setMessage(ChangeReasons.TNT_REDSTONE);
				}
			}
			world.spawnEntity(tntEntity);
			world.playSound((PlayerEntity)null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
}
