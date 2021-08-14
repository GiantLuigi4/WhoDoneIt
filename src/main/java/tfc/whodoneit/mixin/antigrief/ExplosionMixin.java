package tfc.whodoneit.mixin.antigrief;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.whodoneit.antigrief.component.ProtectionRegionsComponent;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.util.interfaces.ICauseAware;

import java.util.ArrayList;
import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Shadow @Final private List<BlockPos> affectedBlocks;
	
	@Shadow @Final private World world;
	
	@Inject(at = @At("HEAD"), method = "affectWorld")
	public void WhoDoneIt_preAffectWorld(boolean particles, CallbackInfo ci) {
		if (world.isClient) return;
		ArrayList<BlockPos> toRemove = new ArrayList<>();
		ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(world);
		for (BlockPos affectedBlock : affectedBlocks) {
			// TODO: check the prevention category against the ownership region
			ICauseAware causeAware = (ICauseAware) this;
			if (protectionRegions.isProtected(world.getPlayerByUuid(causeAware.getCause()), causeAware.getMessage(), ChangeReasons.getCategory(causeAware.getMessage()), affectedBlock)) {
				toRemove.add(affectedBlock);
			}
		}
		affectedBlocks.removeAll(toRemove);
	}
}
