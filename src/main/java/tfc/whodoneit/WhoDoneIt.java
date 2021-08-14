package tfc.whodoneit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import tfc.whodoneit.command.WhoDoneItCommand;

public class WhoDoneIt implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(WhoDoneItCommand.build());
		});
	}
}
