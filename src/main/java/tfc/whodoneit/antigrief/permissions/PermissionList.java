package tfc.whodoneit.antigrief.permissions;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class PermissionList {
	private final HashMap<Identifier, Byte> protectionCategorySettings = new HashMap<>();
	private final HashMap<Identifier, Byte> changeReasonSettings = new HashMap<>();
	
	public NbtCompound serialize(NbtCompound compound) {
		NbtCompound protectionCategoriesCompound = new NbtCompound();
		for (Identifier key : protectionCategorySettings.keySet()) {
			byte value = protectionCategorySettings.get(key);
			protectionCategoriesCompound.putByte(key.toString(), value);
		}
		compound.put("protectionCategories", protectionCategoriesCompound);
		
		NbtCompound changeReasonCompound = new NbtCompound();
		for (Identifier key : changeReasonSettings.keySet()) {
			byte value = changeReasonSettings.get(key);
			changeReasonCompound.putByte(key.toString(), value);
		}
		compound.put("changeReasons", changeReasonCompound);
		
		return compound;
	}
	
	public void read(NbtCompound compound) {
		NbtCompound protectionCategoriesCompound = compound.getCompound("protectionCategories");
		for (String key : protectionCategoriesCompound.getKeys())
			protectionCategorySettings.put(new Identifier(key), protectionCategoriesCompound.getByte(key));
		
		NbtCompound changeReasons = compound.getCompound("changeReasons");
		for (String key : changeReasons.getKeys())
			changeReasonSettings.put(new Identifier(key), changeReasons.getByte(key));
	}
	
	public void setProtectionCategory(Identifier category, byte value) {
		protectionCategorySettings.put(category, value);
	}
	
	public void setChangeReason(Identifier reason, byte value) {
		changeReasonSettings.put(reason, value);
	}
}
