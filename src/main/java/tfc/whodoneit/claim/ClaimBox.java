package tfc.whodoneit.claim;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ClaimBox {
	public final String boxName;
	public final BlockPos startPos;
	public final BlockPos endPos;
	
	public ClaimBox(String boxName, BlockPos startPos, BlockPos endPos) {
		this.boxName = boxName;
		int minX = Math.min(startPos.getX(), endPos.getX());
		int minY = Math.min(startPos.getY(), endPos.getY());
		int minZ = Math.min(startPos.getZ(), endPos.getZ());
		this.startPos = new BlockPos(minX, minY, minZ);
		int maxX = Math.max(startPos.getX(), endPos.getX());
		int maxY = Math.max(startPos.getY(), endPos.getY());
		int maxZ = Math.max(startPos.getZ(), endPos.getZ());
		this.endPos = new BlockPos(maxX, maxY, maxZ);
	}
	
	public void write(NbtCompound nbt) {
		nbt.putIntArray(boxName, new int[]{
				startPos.getX(), startPos.getY(), startPos.getZ(),
				endPos.getX(), endPos.getY(), endPos.getZ()
		});
	}
	
	public static ClaimBox from(NbtCompound nbt, String boxName) {
		int[] array = nbt.getIntArray(boxName);
		BlockPos start = new BlockPos(array[0], array[1], array[2]);
		BlockPos end = new BlockPos(array[3], array[4], array[5]);
		return new ClaimBox(boxName, start, end);
	}
	
	public boolean contains(BlockPos pos) {
		return startPos.getX() <= pos.getX() &&
				startPos.getY() <= pos.getY() &&
				startPos.getZ() <= pos.getZ() &&
				endPos.getX() >= pos.getX() &&
				endPos.getY() >= pos.getY() &&
				endPos.getZ() >= pos.getZ();
	}
}
