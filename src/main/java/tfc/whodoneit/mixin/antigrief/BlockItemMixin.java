package tfc.whodoneit.mixin.antigrief;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.registry.PreventionCategories;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(at = @At("HEAD"), method = "canPlace", cancellable = true)
	public void WhoDoneIt_preTestCanPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getWorld()).isProtected(context.getPlayer(), ChangeReasons.PLACEMENT, PreventionCategories.PLAYER, context.getBlockPos())) {
			cir.setReturnValue(false);
		}
	}
}
