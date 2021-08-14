package tfc.whodoneit.mixin.tracking;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.util.interfaces.ICauseAware;

import java.util.UUID;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin implements ICauseAware {
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
	
	@Override
	public UUID getCause() {
		return cause;
	}
	
	@Override
	public Identifier getMessage() {
		return message;
	}
	
	@Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;ignite()V"))
	public void WhoDoneIt_preIgnite(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		cause = player.getUuid();
		message = ChangeReasons.LIT_CREEPER;
	}
}
