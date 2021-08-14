package tfc.whodoneit.claim;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import tfc.whodoneit.antigrief.permissions.PermissionList;
import tfc.whodoneit.api.PreventionCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// TODO: optimization of claim shapes

public class Claim extends PermissionList {
	private final String name;
	private final UUID owner;
	private final HashMap<Identifier, PermissionList> groupPerms = new HashMap<>();
	private final HashMap<UUID, Identifier> playerGroups = new HashMap<>();
	private final ArrayList<ClaimBox> boxes = new ArrayList<>();
	private boolean adminApproved = false;
	
	public Claim(String name, UUID owner) {
		this.name = name;
		this.owner = owner;
	}
	
	public void addBox(String name, BlockPos start, BlockPos end) {
		boxes.add(new ClaimBox(name, start, end));
	}
	
	public void addBox(ClaimBox box) {
		boxes.add(box);
	}
	
	public NbtCompound serialize() {
		NbtCompound compound = new NbtCompound();
		
		compound.putString("name", name);
		compound.putUuid("owner", owner);
		compound.putBoolean("isFreeTrial", !adminApproved);
		
		NbtCompound boxesCompound = new NbtCompound();
		for (ClaimBox box : boxes) box.write(boxesCompound);
		compound.put("boxes", boxesCompound);
		
		NbtCompound groupsCompound = new NbtCompound();
		for (Identifier identifier : groupPerms.keySet())
			groupsCompound.put(identifier.toString(), groupPerms.get(identifier).serialize(new NbtCompound()));
		compound.put("groups", groupsCompound);
		
		NbtCompound playerGroupsCompound = new NbtCompound();
		for (UUID key : playerGroups.keySet())
			groupsCompound.putString(key.toString(), playerGroups.get(key).toString());
		compound.put("playerGroups", playerGroupsCompound);
		
		return super.serialize(compound);
	}
	
	public static Claim deserialize(NbtCompound compound) {
		Claim claim = new Claim(compound.getString("name"), compound.getUuid("owner"));
		
		claim.adminApproved = !compound.getBoolean("isFreeTrial");
		
		NbtCompound boxes = compound.getCompound("boxes");
		for (String key : boxes.getKeys()) claim.boxes.add(ClaimBox.from(boxes, key));
		
		NbtCompound groups = compound.getCompound("groups");
		for (String key : groups.getKeys()) {
			PermissionList permissionList = new PermissionList();
			permissionList.read(groups.getCompound(key));
			claim.groupPerms.put(new Identifier(key), permissionList);
		}
		
		NbtCompound playerGroups = compound.getCompound("playerGroups");
		for (String key : playerGroups.getKeys())
			claim.playerGroups.put(UUID.fromString(key), new Identifier(playerGroups.getString(key)));
		
		claim.read(compound);
		
		return claim;
	}
	
	public boolean contains(BlockPos blockPos) {
		for (ClaimBox box : boxes) if (box.contains(blockPos)) return true;
		return false;
	}
	
	public boolean isProtected(PlayerEntity player, Identifier reason, PreventionCategory category) {
		return getProtectionLevel(player, reason, category) == 0;
	}
	
	public byte getProtectionLevel(PlayerEntity player, Identifier reason, PreventionCategory category) {
		byte v = 1;
		if (player != null) {
			if (playerGroups.containsKey(player.getUuid())) {
				PermissionList perms = groupPerms.get(playerGroups.get(player.getUuid()));
				byte v2 = perms.getChangeReason(reason);
				if (v2 == 1) v2 = perms.getProtectionCategory(category);
				if (category.parentCategories != null) {
					for (PreventionCategory parentCategory : category.parentCategories) {
						if (v2 == 1) {
							byte v1 = getProtectionLevel(player, reason, parentCategory);
							if (v1 == 0) v2 = v1;
						}
					}
				}
				if (v2 == 0 || v2 == 2) return v2;
			}
			v = getChangeReason(reason);
			if (v == 1) v = getProtectionCategory(category);
			if (category.parentCategories != null) {
				for (PreventionCategory parentCategory : category.parentCategories) {
					if (v == 1) {
						byte v1 = getProtectionLevel(player, reason, parentCategory);
						if (v1 == 0) v = v1;
					}
				}
			}
		}
//		if (v == 0) {
//			TODO: fallback to global defaults
//		}
		return v;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean intersects(ClaimBox box) {
		for (ClaimBox claimBox : boxes) {
			if (claimBox.intersects(box)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean removeBox(String boxName) {
		int indxToRemove = -1;
		int indx = 0;
		for (ClaimBox box : boxes) {
			if (box.boxName.equals(boxName)) {
				indxToRemove = indx;
			}
			indx++;
		}
		if (indxToRemove == -1) return false;
		boxes.remove(indxToRemove);
		return true;
	}
	
	public void adminApproval(boolean b) {
		adminApproved = b;
	}
	
	public boolean isAdminApproved() {
		return adminApproved;
	}
	
	public boolean hasBox(String boxName) {
		for (ClaimBox box : boxes) if (box.boxName.equals(boxName)) return true;
		return false;
	}
}
