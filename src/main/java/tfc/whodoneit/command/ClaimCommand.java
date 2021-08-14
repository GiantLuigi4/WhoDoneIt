package tfc.whodoneit.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import tfc.whodoneit.antigrief.component.ProtectionRegionsComponent;
import tfc.whodoneit.claim.Claim;
import tfc.whodoneit.claim.ClaimBox;
import tfc.whodoneit.command.arguments.ClaimNameArgumentType;
import tfc.whodoneit.command.arguments.UsernameOrUUIDArgumentType;
import tfc.whodoneit.components.WhoDoneItWorldComponents;
import tfc.whodoneit.util.NameGetter;

import java.util.UUID;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class ClaimCommand {
	public static LiteralArgumentBuilder<ServerCommandSource> insert(LiteralArgumentBuilder<ServerCommandSource> command) {
		LiteralArgumentBuilder<ServerCommandSource> claim = literal("claim");
		{
			LiteralArgumentBuilder<ServerCommandSource> create = literal("create");
			ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = worldBuilder("name");
			argumentBuilder = argumentBuilder.executes((context) -> {
				if (
						context.getSource().getWorld() != null &&
								context.getSource().getEntity() instanceof PlayerEntity
				) {
					ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getSource().getWorld());
					String name = StringArgumentType.getString(context, "name");
					UUID sender = ((ServerPlayerEntity) context.getSource().getEntity()).getGameProfile().getId();
					for (Claim claim1 : protectionRegions.claims) {
						if (claim1.getName().equals(name) && claim1.getOwner().equals(sender)) {
							context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.create.fail.claim_exists", name), false);
							return 0;
						}
					}
					Claim claim1 = new Claim(name, sender);
					protectionRegions.claims.add(claim1);
					context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.create.success", name), false);
					if (context.getSource().getPlayer().hasPermissionLevel(2)) claim1.adminApproval(true);
					else claim1.adminApproval(false);
					WhoDoneItWorldComponents.PROTECTION_REGIONS.sync(context.getSource().getWorld());
					return 1;
				}
				return 0;
			});
			create.then(argumentBuilder);
			claim.then(create);
		}
		{
			LiteralArgumentBuilder<ServerCommandSource> delete = literal("delete");
			ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = worldBuilder("name");
			argumentBuilder = argumentBuilder.executes((context) -> {
				if (
						context.getSource().getWorld() != null &&
								context.getSource().getEntity() instanceof PlayerEntity
				) {
					ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getSource().getWorld());
					String name = StringArgumentType.getString(context, "name");
					UUID sender = ((ServerPlayerEntity) context.getSource().getPlayer()).getGameProfile().getId();
					int toRemove = -1;
					{
						int indx = 0;
						for (Claim claim1 : protectionRegions.claims) {
							if (claim1.getName().equals(name) && claim1.getOwner().equals(sender)) {
								toRemove = indx;
								break;
							}
							indx++;
						}
					}
					if (toRemove == -1) {
						context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.delete.fail.no_exist", name), false);
						return 0;
					}
					protectionRegions.claims.remove(toRemove);
					WhoDoneItWorldComponents.PROTECTION_REGIONS.sync(context.getSource().getWorld());
					context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.delete.success", name), false);
					return 1;
				}
				return 0;
			});
			delete.then(argumentBuilder);
			claim.then(delete);
		}
		{
			LiteralArgumentBuilder<ServerCommandSource> modify = literal("modify");
			modify = fillModify(modify);
			claim.then(modify);
		}
		
		{
			LiteralArgumentBuilder<ServerCommandSource> approve = literal("approve");
			approve.requires((object) -> {
				try {
					return object.getPlayer().hasPermissionLevel(2);
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
				}
				return false;
			});
			ArgumentBuilder<ServerCommandSource, ?> uuidArg = argument("uuid", UsernameOrUUIDArgumentType.uuid());
			ArgumentBuilder<ServerCommandSource, ?> nameArg = argument("name", ClaimNameArgumentType.names("uuid"));
			nameArg.executes(context -> {
				if (
						context.getSource().getWorld() != null &&
								context.getSource().getEntity() instanceof PlayerEntity
				) {
					ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getSource().getWorld());
					UUID owner = UsernameOrUUIDArgumentType.getUUID(context, "uuid");
					String claimName = ClaimNameArgumentType.getName(context, "name");
					for (Claim claim1 : protectionRegions.claims) {
						if (claim1.getOwner().equals(owner) && claim1.getName().equals(claimName)) {
							claim1.adminApproval(true);
							WhoDoneItWorldComponents.PROTECTION_REGIONS.sync(context.getSource().getWorld());
						}
					}
				}
				return 0;
			});
			approve.then(uuidArg.then(nameArg));
			claim.then(approve);
		}
		
		command = command.then(claim);
		
		return command;
	}
	
	protected static LiteralArgumentBuilder<ServerCommandSource> fillModify(LiteralArgumentBuilder<ServerCommandSource> modify) {
		{
			modify.then(worldBuilder("name")
					.then(regions()));
		}
		return modify;
	}
	
	public static ArgumentBuilder<ServerCommandSource, ?> regions() {
		ArgumentBuilder<ServerCommandSource, ?> regions = literalBuilder("regions");
		{
			ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = worldBuilder("region_name");
			argumentBuilder = argumentBuilder.executes((context) -> {
				if (
						context.getSource().getWorld() != null &&
								context.getSource().getEntity() instanceof PlayerEntity
				) {
					ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getSource().getWorld());
					String name = StringArgumentType.getString(context, "name");
					UUID sender = ((ServerPlayerEntity) context.getSource().getEntity()).getGameProfile().getId();
					String boxName = StringArgumentType.getString(context, "region_name");
					for (Claim claim1 : protectionRegions.claims) {
						if (claim1.getName().equals(name) && claim1.getOwner().equals(sender)) {
							BlockPos start = BlockPosArgumentType.getBlockPos(context, "startPos");
							BlockPos end = BlockPosArgumentType.getBlockPos(context, "endPos");
							ClaimBox box = new ClaimBox(boxName, start, end);
							for (Claim claim : protectionRegions.claims) {
								if (claim.intersects(box)) {
									context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.add.fail.intersects", boxName, NameGetter.getNameFor(claim.getOwner())), false);
									return 0;
								}
							}
							if (claim1.hasBox(box.boxName)) {
								context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.add.fail.box_exists", name, boxName), false);
								return 0;
							}
							claim1.addBox(box);
							context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.add.success", name, boxName), false);
							WhoDoneItWorldComponents.PROTECTION_REGIONS.sync(context.getSource().getWorld());
							return 1;
						}
					}
					context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.add.fail.no_exist", name, boxName), false);
				}
				return 0;
			});
			regions = regions
					.then(literalBuilder("add")
							.then(coordinateBuilder("startPos")
									.then(coordinateBuilder("endPos")
											.then(argumentBuilder))));
		}
		{
			ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = worldBuilder("region_name");
			argumentBuilder = argumentBuilder.executes((context) -> {
				if (
						context.getSource().getWorld() != null &&
								context.getSource().getEntity() instanceof PlayerEntity
				) {
					ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getSource().getWorld());
					String name = StringArgumentType.getString(context, "name");
					UUID sender = ((ServerPlayerEntity) context.getSource().getEntity()).getGameProfile().getId();
					String boxName = StringArgumentType.getString(context, "region_name");
					for (Claim claim1 : protectionRegions.claims) {
						if (claim1.getName().equals(name) && claim1.getOwner().equals(sender)) {
							if (claim1.removeBox(boxName)) {
								context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.delete.success", name, boxName), false);
								WhoDoneItWorldComponents.PROTECTION_REGIONS.sync(context.getSource().getWorld());
								return 1;
							} else {
								context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.delete.fail.box_no_exist", name, boxName), false);
								return 0;
							}
						}
					}
					context.getSource().sendFeedback(new TranslatableText("whodoneit.claim.modify.region.delete.fail.no_exist", name, boxName), false);
				}
				return 0;
			});
			regions = regions
					.then(literalBuilder("remove")
							.then(argumentBuilder));
		}
//		{
//			ArgumentBuilder<ServerCommandSource, ?> argumentBuilder = worldBuilder("region_name");
//			argumentBuilder = argumentBuilder.executes((context) -> {
//				if (
//						context.getSource().getWorld() != null &&
//								context.getSource().getEntity() instanceof PlayerEntity
//				) {
//					ProtectionRegionsComponent protectionRegions = WhoDoneItWorldComponents.PROTECTION_REGIONS.get(context.getSource().getWorld());
//					String name = StringArgumentType.getString(context, "name");
//					UUID sender = ((ServerPlayerEntity) context.getSource().getEntity()).getGameProfile().getId();
//					for (Claim claim : protectionRegions.claims) {
//						if (claim.getName().equals(name) && claim.getOwner().equals(sender)) {
//
//						}
//					}
//				}
//				return 0;
//			});
//			regions = regions
//					.then(literalBuilder("protection_categories")
//							.then(argumentBuilder));
//		}
		return regions;
	}
	
	protected static ArgumentBuilder<ServerCommandSource, ?> literalBuilder(String name) {
		return literal(name);
	}
	
	protected static ArgumentBuilder<ServerCommandSource, ?> worldBuilder(String name) {
		return argument(name, StringArgumentType.word());
	}
	
	protected static ArgumentBuilder<ServerCommandSource, ?> coordinateBuilder(String name) {
		return argument(name, BlockPosArgumentType.blockPos());
	}
}
