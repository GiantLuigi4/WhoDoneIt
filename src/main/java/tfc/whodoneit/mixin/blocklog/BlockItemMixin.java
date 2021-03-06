package tfc.whodoneit.mixin.blocklog;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.whodoneit.components.WhoDoneItChunkComponents;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.registry.ChangeReasons;
import tfc.whodoneit.registry.PreventionCategories;

@Mixin(value = BlockItem.class, priority = -10000)
public class BlockItemMixin {
	
	@Inject(at = @At("HEAD"), method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z")
	public void WhoDoneIt_prePlacement(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (context.getWorld().isClient) return;
//		context.getPlayer().sendMessage(
		WhoDoneItChunkComponents.BLOCKLOG.get(context.getWorld().getChunk(context.getBlockPos())).addModification(
				context.getPlayer(),
				context.getBlockPos(),
				context.getWorld().getBlockState(context.getBlockPos()).getBlock().getTranslationKey(),
				state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
				ChangeReasons.PLACEMENT.toString()
		)
//						.getMessage(context.getBlockPos()), false)
		;
	}
}
