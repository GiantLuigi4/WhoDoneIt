package tfc.whodoneit.blocklog.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import tfc.whodoneit.blocklog.BlockModification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChunkComponent implements ComponentV3 {
	private final HashMap<BlockPos, ArrayList<BlockModification>> map = new HashMap<>();
	
	public HashMap<BlockPos, ArrayList<BlockModification>> getModifications() {
		return map;
	}
	
	@Override
	public void readFromNbt(NbtCompound compoundTag) {
		for (String key : compoundTag.getKeys()) {
			BlockPos pos = BlockPos.fromLong(Long.parseLong(key));
			NbtList tag = compoundTag.getList(key, NbtType.LIST);
			for (NbtElement tag1 : tag) {
				NbtCompound compoundTag1 = (NbtCompound) tag1;
				ArrayList<BlockModification> modifications;
				if (!map.containsKey(pos)) map.put(pos, new ArrayList<>());
				modifications = map.get(pos);
				modifications.add(BlockModification.deserialize(compoundTag1));
			}
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound compoundTag) {
		for (Map.Entry<BlockPos, ArrayList<BlockModification>> blockPosArrayListPair : map.entrySet()) {
			NbtList list = new NbtList();
			for (BlockModification blockModification : blockPosArrayListPair.getValue()) {
				list.add(blockModification.serialize());
			}
			compoundTag.put(blockPosArrayListPair.getKey().asLong() + "", list);
		}
	}
	
	public BlockModification addModification(PlayerEntity player, BlockPos pos, String blockState, String state, long epochTimeMs, String method) {
		ArrayList<BlockModification> modifications;
		if (!map.containsKey(pos)) map.put(pos, new ArrayList<>());
		modifications = map.get(pos);
		BlockModification modification = new BlockModification(
				player.getGameProfile().getId(), blockState,
				state, method,
				epochTimeMs
		);
		modifications.add(modification);
		return modification;
	}
}