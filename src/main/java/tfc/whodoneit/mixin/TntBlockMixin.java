package tfc.whodoneit.mixin;

import net.minecraft.block.TntBlock;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tfc.whodoneit.blocklog.mixin.TntMixin;

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
}
