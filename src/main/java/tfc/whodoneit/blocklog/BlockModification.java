package tfc.whodoneit.blocklog;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import tfc.whodoneit.util.NameGetter;

import java.util.UUID;

public class BlockModification {
	public final UUID culprit;
	public final String oldState;
	public final String newState;
	public final String method;
	public final long setTime;
	
	public BlockModification(UUID culprit, String oldState, String newState, String method, long setTime) {
		this.culprit = culprit;
		this.oldState = oldState;
		this.newState = newState;
		this.method = method;
		this.setTime = setTime;
	}
	
	public static BlockModification deserialize(NbtCompound compoundTag1) {
		return new BlockModification(
				compoundTag1.getUuid("culprit"),
				compoundTag1.getString("oldState"),
				compoundTag1.getString("newState"),
				compoundTag1.getString("method"),
				compoundTag1.getLong("setTime")
		);
	}
	
	public NbtCompound serialize() {
		NbtCompound thisTag = new NbtCompound();
		thisTag.putUuid("culprit", culprit);
		thisTag.putString("oldState", oldState);
		thisTag.putString("newState", newState);
		thisTag.putString("method", method);
		thisTag.putLong("setTime", setTime);
		return thisTag;
	}
	
	public String toString() {
		// TODO: translation text component stuff
		return NameGetter.getNameFor(culprit) // TODO: get culprit name
		+ " changed the block at [pos]" // TODO: get the pos
		+ " from " + oldState + " to " + newState + " via " + method.replace(":", ".blocklog.");
	}
	
	public MutableText getMessage(BlockPos pos) {
		return
				new LiteralText(NameGetter.getNameFor(culprit))
						.append(new LiteralText(" changed the block at "))
						.append(new LiteralText(pos.getX() + " " + pos.getY() + " " + pos.getZ()))
						.append(new LiteralText(" from "))
						.append(new TranslatableText(oldState))
						.append(new LiteralText(" to "))
						.append(new TranslatableText(newState))
						.append(new LiteralText(" via "))
						.append(new TranslatableText(method.replace(":", ".blocklog.")))
				;
	}
	
	// [username of culprit] changed the block at [pos] from [oldState] to [newState] via [method]
}
