package tfc.whodoneit.util.interfaces;

import net.minecraft.util.Identifier;

import java.util.UUID;

// TODO: write to nbt
public interface ICauseAware {
	UUID getCause();
	void setCause(UUID cause);
	Identifier getMessage();
	void setMessage(Identifier message);
}
