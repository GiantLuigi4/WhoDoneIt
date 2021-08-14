package tfc.whodoneit.command.arguments;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import tfc.whodoneit.antigrief.component.ProtectionRegionsComponent;
import tfc.whodoneit.claim.Claim;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.util.NameGetter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClaimNameArgumentType implements ArgumentType<String> {
	private final String argumentUUID;
	
	public static ClaimNameArgumentType names(String argumentUUID) {
		return new ClaimNameArgumentType(argumentUUID);
	}
	
	public ClaimNameArgumentType(String argumentUUID) {
		this.argumentUUID = argumentUUID;
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		UUID player = context.getArgument(argumentUUID, UUID.class);
		
		ArrayList<String> toSuggest = new ArrayList<>();
		if (context.getSource() instanceof ClientCommandSource) {
			World world = MinecraftClient.getInstance().world;
			ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(world);
			for (Claim claim : protectionRegions.claims) {
				if (!claim.isAdminApproved()) {
					if (claim.getOwner().equals(player)) {
						toSuggest.add(claim.getName());
					}
				}
			}
		}
		for (String string : toSuggest) {
			if (string.contains(builder.getRemaining())) {
				builder.suggest(string);
			}
		}
		return builder.buildFuture();
 	}
	
	public static String getName(CommandContext<ServerCommandSource> context, String argumentName) {
		return context.getArgument(argumentName, String.class);
	}
	
	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		StringBuilder text = new StringBuilder();
		char peek;
		while (reader.canRead() && (peek = reader.peek()) != ' ') {
			text.append(peek);
			reader.skip();
		}
		return text.toString();
	}
	
	private final ImmutableList<String> EXAMPLES = ImmutableList.of(
			"claim",
			"test_claim",
			"test"
	);
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
