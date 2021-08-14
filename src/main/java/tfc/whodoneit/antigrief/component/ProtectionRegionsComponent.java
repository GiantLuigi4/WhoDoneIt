package tfc.whodoneit.antigrief.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import tfc.whodoneit.api.PreventionCategory;
import tfc.whodoneit.claim.Claim;
import tfc.whodoneit.claim.ClaimBox;

import java.util.ArrayList;
import java.util.UUID;

public class ProtectionRegionsComponent implements ComponentV3, AutoSyncedComponent {
	public final ArrayList<Claim> claims = new ArrayList<>();
	
	public ProtectionRegionsComponent() {
//		Claim claim = new Claim("test", UUID.fromString("b36fdbca-7336-4e5a-8e08-045c169ee07e"));
//		claim.addBox(new ClaimBox("testBox", new BlockPos(0,0,0), new BlockPos(64,64,64)));
//		claim.setProtectionCategory(new Identifier("whodoneit:mob"), (byte) 0);
//		claim.setChangeReason(new Identifier("whodoneit:placement"), (byte) 0);
//		claims.add(claim);
	}
	
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
	
	public boolean isProtected(PlayerEntity player, Identifier reason, PreventionCategory category, BlockPos blockPos) {
		for (Claim claim : claims) {
			if (claim.contains(blockPos)) {
				if (claim.isProtected(player, reason, category)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player.hasPermissionLevel(2);
	}
	
	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		NbtCompound tag = new NbtCompound();
		this.writeToNbt(tag);
		buf.writeNbt(tag);
	}
}
