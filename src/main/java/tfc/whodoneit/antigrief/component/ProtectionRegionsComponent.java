package tfc.whodoneit.antigrief.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import tfc.whodoneit.claim.Claim;

import java.util.ArrayList;

public class ProtectionRegionsComponent implements ComponentV3 {
	public final ArrayList<Claim> claims = new ArrayList<>();
	
	@Override
	public void readFromNbt(NbtCompound nbtCompound) {
		NbtList list = nbtCompound.getList("claims", NbtType.COMPOUND);
		for (NbtElement nbtElement : list) claims.add(Claim.deserialize((NbtCompound) nbtElement));
	}
	
	@Override
	public void writeToNbt(NbtCompound nbtCompound) {
		NbtList list = new NbtList();
		for (Claim claim : claims) list.add(claim.serialize());
		nbtCompound.put("claims", list);
	}
}
