package tfc.whodoneit.mixin.antigrief;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Shadow @Final private List<BlockPos> affectedBlocks;
	
	@Inject(at = @At("HEAD"), method = "affectWorld")
	public void preAffectWorld(boolean particles, CallbackInfo ci) {
		ArrayList<BlockPos> toRemove = new ArrayList<>();
		for (BlockPos affectedBlock : affectedBlocks) {
			// TODO: check the prevention category against the ownership region
		}
		affectedBlocks.removeAll(toRemove);
	}
}
