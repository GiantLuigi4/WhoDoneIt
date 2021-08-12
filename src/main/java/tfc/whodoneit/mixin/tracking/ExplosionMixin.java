package tfc.whodoneit.mixin.tracking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.util.GeneralUtils;
import tfc.whodoneit.util.interfaces.ICauseAware;

import java.util.UUID;

@Mixin(Explosion.class)
public class ExplosionMixin implements ICauseAware {
	@Shadow @Final private @Nullable Entity entity;
	UUID cause;
	Identifier message;
	
	@Override
	public void setCause(UUID cause) {
		this.cause = cause;
	}
	
	@Override
	public void setMessage(Identifier message) {
		this.message = message;
	}
	
	// TODO: make this not a mess
	@Override
	public UUID getCause() {
		if (this.cause != null) return cause;
		if (this.entity instanceof ICauseAware) {
			if (((ICauseAware) this.entity).getCause() != null) {
				return ((ICauseAware) this.entity).getCause();
			}
		}
		if (this.entity instanceof TntEntity) {
			if (((TntEntity)entity).getCausingEntity() instanceof PlayerEntity) {
				return ((TntEntity) entity).getCausingEntity().getUuid();
			} else if (entity instanceof ArrowEntity && ((ArrowEntity) entity).getOwner() instanceof SkeletonEntity) {
				return GeneralUtils.getRootCause(entity);
			} else if (entity instanceof WitherEntity) {
				return GeneralUtils.getRootCause(entity);
			} else if (entity instanceof CreeperEntity) {
				return GeneralUtils.getRootCause(entity);
			} else if (entity instanceof FireballEntity) {
				return GeneralUtils.getRootCause(entity);
			}
		} else if (this.entity instanceof PlayerEntity) {
			return this.entity.getUuid();
		} else if (this.entity instanceof CreeperEntity) {
			if (((CreeperEntity)entity).getTarget() instanceof PlayerEntity) {
				return ((CreeperEntity)entity).getTarget().getUuid();
			}
		} else if (this.entity instanceof GhastEntity) {
			if (((GhastEntity)entity).getTarget() instanceof PlayerEntity) {
				return ((GhastEntity)entity).getTarget().getUuid();
			}
		} else if (this.entity instanceof WitherEntity) {
			if (((WitherEntity)entity).getTarget() instanceof PlayerEntity) {
				return ((WitherEntity)entity).getTarget().getUuid();
			}
		} else if (this.entity instanceof ArrowEntity) {
			return GeneralUtils.getRootCause(entity);
		} else if (this.entity instanceof EndCrystalEntity) {
			// TODO
		}
		return null;
	}
	
	@Override
	public Identifier getMessage() {
		if (this.message != null) return message;
		if (this.entity instanceof ICauseAware) {
			if (((ICauseAware) this.entity).getMessage() != null) {
				return ((ICauseAware) this.entity).getMessage();
			}
		}
		if (this.entity instanceof TntEntity) {
			if (((TntEntity)entity).getCausingEntity() instanceof PlayerEntity) {
				return ChangeReasons.TNT;
			} else if (entity instanceof ArrowEntity && ((ArrowEntity) entity).getOwner() instanceof SkeletonEntity) {
				return ChangeReasons.SKELETON_TNT;
			} else if (entity instanceof WitherEntity) {
				return ChangeReasons.WITHER_TNT;
			} else if (entity instanceof CreeperEntity) {
				return ChangeReasons.CREEPER_TNT;
			} else if (entity instanceof FireballEntity) {
				if (((FireballEntity) entity).getOwner() instanceof GhastEntity) {
					return ChangeReasons.GHAST_TNT;
				} else {
					return ChangeReasons.EXPLOSION;
				}
			}
		} else if (this.entity instanceof PlayerEntity) {
			return ChangeReasons.EXPLOSION;
		} else if (this.entity instanceof CreeperEntity) {
			if (((CreeperEntity)entity).getTarget() instanceof PlayerEntity) {
				return ChangeReasons.CREEPER;
			}
		} else if (this.entity instanceof GhastEntity) {
			if (((GhastEntity)entity).getTarget() instanceof PlayerEntity) {
				return ChangeReasons.GHAST;
			}
		} else if (this.entity instanceof WitherEntity) {
			if (((WitherEntity)entity).getTarget() instanceof PlayerEntity) {
				return ChangeReasons.WITHER;
			}
		} else if (this.entity instanceof ArrowEntity) {
			return ChangeReasons.EXPLOSION;
		} else if (this.entity instanceof EndCrystalEntity) {
			// TODO
		}
		return null;
	}
}
