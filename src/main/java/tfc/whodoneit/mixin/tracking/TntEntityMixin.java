package tfc.whodoneit.mixin.tracking;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.whodoneit.util.interfaces.ICauseAware;

import java.util.UUID;

@Mixin(TntEntity.class)
public class TntEntityMixin implements ICauseAware {
	UUID cause;
	Identifier message;
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V")
	public void postInit(World world, double x, double y, double z, @Nullable LivingEntity igniter, CallbackInfo ci) {
		if (igniter instanceof PlayerEntity) {
			cause = igniter.getUuid();
			message = new Identifier("whodoneit:tnt");
		} else {
			if (igniter instanceof CreeperEntity) {
				CreeperEntity entity = (CreeperEntity) igniter;
				if (entity.getTarget() instanceof PlayerEntity) {
					cause = ((CreeperEntity) igniter).getTarget().getUuid();
					message = new Identifier("whodoneit:creeper_tnt");
				}
			} else if (igniter instanceof WitherEntity) {
				WitherEntity entity = (WitherEntity) igniter;
				if (entity.getTarget() instanceof PlayerEntity) {
					cause = ((WitherEntity) igniter).getTarget().getUuid();
					message = new Identifier("whodoneit:wither_tnt");
				}
			} else if (igniter instanceof GhastEntity) {
				GhastEntity entity = (GhastEntity) igniter;
				if (entity.getTarget() instanceof PlayerEntity) {
					cause = ((GhastEntity) igniter).getTarget().getUuid();
					message = new Identifier("whodoneit:ghast_tnt");
				}
			} else if (igniter instanceof SkeletonEntity) {
				SkeletonEntity entity = (SkeletonEntity) igniter;
				if (entity.getTarget() instanceof PlayerEntity) {
					cause = ((SkeletonEntity) igniter).getTarget().getUuid();
					message = new Identifier("whodoneit:ghast_tnt");
				}
			}
		}
	}
	
	@Override
	public void setCause(UUID cause) {
		this.cause = cause;
	}
	
	@Override
	public void setMessage(Identifier message) {
		this.message = message;
	}
	
	@Override
	public UUID getCause() {
		return cause;
	}
	
	@Override
	public Identifier getMessage() {
		return message;
	}
}
