package tfc.whodoneit.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfc.whodoneit.blocklog.component.ChunkComponentInitializer;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(at = @At("HEAD"), method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z")
	public void WhoDoneIt_prePlacement(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (context.getWorld().isClient) return;
//		context.getPlayer().sendMessage(
		ChunkComponentInitializer.BLOCKLOG.get(context.getWorld().getChunk(context.getBlockPos())).addModification(
				context.getPlayer(),
				context.getBlockPos(),
				context.getWorld().getBlockState(context.getBlockPos()).getBlock().getTranslationKey(),
				state.getBlock().getTranslationKey(), Util.getEpochTimeMs(),
				"whodoneit.blocklog.placement"
		)
//						.getMessage(context.getBlockPos()), false)
		;
	}
}
