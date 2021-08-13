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
import java.util.UUID;

public class ModificationTrackerComponent implements ComponentV3 {
	private final HashMap<BlockPos, ArrayList<BlockModification>> map = new HashMap<>();
	
	public HashMap<BlockPos, ArrayList<BlockModification>> getModifications() {
		return map;
	}
	
	@Override
	public void readFromNbt(NbtCompound compoundTag) {
		for (String key : compoundTag.getKeys()) {
			BlockPos pos = BlockPos.fromLong(Long.parseLong(key));
			NbtList tag = compoundTag.getList(key, NbtType.LIST);
			ArrayList<BlockModification> modifications;
			if (!map.containsKey(pos)) map.put(pos, new ArrayList<>());
			modifications = map.get(pos);
			for (NbtElement tag1 : tag) {
				NbtCompound compoundTag1 = (NbtCompound) tag1;
				modifications.add(BlockModification.deserialize(compoundTag1));
			}
			modifications.sort(BlockModification::compare);
		}
	}
	
	@Override
	public void writeToNbt(NbtCompound compoundTag) {
		for (Map.Entry<BlockPos, ArrayList<BlockModification>> blockPosArrayListPair : map.entrySet()) {
			NbtList list = new NbtList();
			for (BlockModification blockModification : blockPosArrayListPair.getValue()) {
				list.add(blockModification.serialize());
				System.out.println(blockModification.toString());
			}
			compoundTag.put(blockPosArrayListPair.getKey().asLong() + "", list);
		}
	}
	
	public BlockModification addModification(PlayerEntity player, BlockPos pos, String blockState, String state, long epochTimeMs, String method) {
		return addModification(player.getGameProfile().getId(), pos, blockState, state, epochTimeMs, method);
	}
	
	public BlockModification addModification(UUID uuid, BlockPos pos, String blockState, String state, long epochTimeMs, String method) {
		ArrayList<BlockModification> modifications;
		if (!map.containsKey(pos)) map.put(pos, new ArrayList<>());
		modifications = map.get(pos);
		BlockModification modification = new BlockModification(
				uuid, blockState,
				state, method,
				epochTimeMs
		);
		modifications.add(modification);
		return modification;
	}
	
	public BlockModification getLatest(BlockPos pos) {
		ArrayList<BlockModification> modifications = map.getOrDefault(pos, null);
		if (modifications == null) return null;
		return modifications.get(modifications.size() - 1);
	}
}
