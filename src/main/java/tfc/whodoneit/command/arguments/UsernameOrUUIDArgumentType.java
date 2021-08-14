package tfc.whodoneit.command.arguments;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import tfc.whodoneit.antigrief.component.ProtectionRegionsComponent;
import tfc.whodoneit.claim.Claim;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.util.NameGetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UsernameOrUUIDArgumentType implements ArgumentType<UUID> {
	private static final UsernameOrUUIDArgumentType UUIDType = new UsernameOrUUIDArgumentType();
	
	public static UsernameOrUUIDArgumentType uuid() {
		return UUIDType;
	}
	
	public static UUID getUUID(CommandContext<ServerCommandSource> context, String argumentName) {
		return context.getArgument(argumentName, UUID.class);
	}
	
	@Override
	public UUID parse(StringReader reader) throws CommandSyntaxException {
		StringBuilder uuid = new StringBuilder();
		char peek;
		while (reader.canRead() && (peek = reader.peek()) != ' ') {
			uuid.append(peek);
			reader.skip();
		}
		try {
			UUID uuid1 = NameGetter.getUUIDFor(uuid.toString());
			if (uuid1 == null) {
				throw new CommandSyntaxException(new CommandExceptionType() {
				}, new LiteralText("Invalid User " + uuid.toString()));
			}
			return uuid1;
		} catch (Throwable err) {
			throw new CommandSyntaxException(new CommandExceptionType() {
			}, new LiteralText("Malformed UUID " + uuid.toString()));
		}
	}
	
	@Override
	public CompletableFuture<Suggestions> listSuggestions(CommandContext context, SuggestionsBuilder builder) {
		ArrayList<UUID> toSuggest = new ArrayList<>();
		if (context.getSource() instanceof ClientCommandSource) {
			World world = MinecraftClient.getInstance().world;
			ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(world);
			for (Claim claim : protectionRegions.claims) {
				if (!claim.isAdminApproved()) {
					if (!toSuggest.contains(claim.getOwner())) {
						toSuggest.add(claim.getOwner());
					}
				}
			}
		}
		for (UUID uuid : toSuggest) {
			String text = NameGetter.getNameFor(uuid);
			if (text.contains(builder.getRemaining())) {
				builder.suggest(text);
			}
		}
		return builder.buildFuture();
	}
	
	private final ImmutableList<String> EXAMPLES = ImmutableList.of(
			"3e7e37bd-95de-43c1-9ee4-b3b63dbdf66f",
			"GiantLuigi4"
	);
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
