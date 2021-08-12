package tfc.whodoneit.blocklog.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import tfc.whodoneit.util.interfaces.ICauseAware;
import tfc.whodoneit.mixin.ExplosionAccessor;

public class TntMixin {
	public static void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, TntEntity tntEntity) {
		if (tntEntity instanceof ICauseAware) {
			ICauseAware causeAware = (ICauseAware) tntEntity;
			{
				Entity igniter = ((ExplosionAccessor) explosion).getEntity();
				if (igniter instanceof ICauseAware && ((ICauseAware) igniter).getCause() != null) {
					causeAware.setCause(((ICauseAware) igniter).getCause());
					if (((ICauseAware) igniter).getMessage().getPath().startsWith("chain_")) {
						causeAware.setMessage(((ICauseAware) igniter).getMessage());
					} else {
						causeAware.setMessage(
								new Identifier(
										((ICauseAware) igniter).getMessage().getNamespace(),
										"chain_" + ((ICauseAware) igniter).getMessage().getPath()
								)
						);
					}
					System.out.println(causeAware.getMessage());
				} else if (igniter instanceof PlayerEntity) {
					causeAware.setCause(igniter.getUuid());
					causeAware.setMessage(new Identifier("whodoneit:tnt"));
				} else {
					if (igniter instanceof CreeperEntity) {
						CreeperEntity entity = (CreeperEntity) igniter;
						if (entity.getTarget() instanceof PlayerEntity) {
							causeAware.setCause(((CreeperEntity) igniter).getTarget().getUuid());
							causeAware.setMessage(new Identifier("whodoneit:creeper_tnt"));
						}
					} else if (igniter instanceof WitherEntity) {
						WitherEntity entity = (WitherEntity) igniter;
						if (entity.getTarget() instanceof PlayerEntity) {
							causeAware.setCause(((WitherEntity) igniter).getTarget().getUuid());
							causeAware.setMessage(new Identifier("whodoneit:wither_tnt"));
						}
					} else if (igniter instanceof GhastEntity) {
						GhastEntity entity = (GhastEntity) igniter;
						if (entity.getTarget() instanceof PlayerEntity) {
							causeAware.setCause(((GhastEntity) igniter).getTarget().getUuid());
							causeAware.setMessage(new Identifier("whodoneit:ghast_tnt"));
						}
					}
				}
			}
		}
	}
}
