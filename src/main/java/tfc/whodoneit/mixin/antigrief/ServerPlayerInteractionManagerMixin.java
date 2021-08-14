package tfc.whodoneit.mixin.antigrief;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.registry.PreventionCategories;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	@Shadow public ServerWorld world;
	
	@Shadow public ServerPlayerEntity player;
	
	@Inject(at = @At(value = "HEAD"), method = "tryBreakBlock",cancellable = true)
	public void WhoDoneIt_preBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (WhoDoneItWorldComponents.PROTECTION_REGIONS.get(world).isProtected(player, ChangeReasons.MINING, PreventionCategories.PLAYER, pos)) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
