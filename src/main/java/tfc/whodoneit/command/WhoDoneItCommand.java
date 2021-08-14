package tfc.whodoneit.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class WhoDoneItCommand {
	public static LiteralArgumentBuilder<ServerCommandSource> build() {
		LiteralArgumentBuilder<ServerCommandSource> builder = literal("whodoneit");
		
		builder = ClaimCommand.insert(builder);
		
		return builder;
	}
}
