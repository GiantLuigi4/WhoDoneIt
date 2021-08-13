package tfc.whodoneit.claim;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import tfc.whodoneit.antigrief.permissions.PermissionList;

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
}
