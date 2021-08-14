package tfc.whodoneit.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import tfc.whodoneit.api.PreventionCategory;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ChangeReasons {
	private static final ArrayList<Pair<Identifier, PreventionCategory>> REASONS = new ArrayList<>();
	
	public static final Identifier TNT_REDSTONE = register(new Identifier("whodoneit:tnt_redstone"), PreventionCategories.TNT_PLAYER);
	public static final Identifier LIT_CREEPER = register(new Identifier("whodoneit:creeper_lit"), PreventionCategories.PLAYER);
	public static final Identifier EXPLOSION = register(new Identifier("whodoneit:explosion"), PreventionCategories.PLAYER);
	public static final Identifier PLACEMENT = register(new Identifier("whodoneit:placement"), PreventionCategories.PLAYER);
	public static final Identifier MINING = register(new Identifier("whodoneit:mining"), PreventionCategories.PLAYER);
	public static final Identifier TNT = register(new Identifier("whodoneit:tnt"), PreventionCategories.TNT_PLAYER);
	public static final Identifier BED = register(new Identifier("whodoneit:bed"), PreventionCategories.PLAYER);
	
	public static final Identifier CHAIN_TNT_REDSTONE = register(new Identifier("whodoneit:chain_tnt_redstone"), PreventionCategories.TNT_PLAYER);
	public static final Identifier CHAIN_SKELETON_TNT = register(new Identifier("whodoneit:chain_skeleton_tnt"), PreventionCategories.TNT);
	public static final Identifier CHAIN_CREEPER_TNT = register(new Identifier("whodoneit:chain_creeper_tnt"), PreventionCategories.TNT);
	public static final Identifier CHAIN_WITHER_TNT = register(new Identifier("whodoneit:chain_wither_tnt"), PreventionCategories.TNT);
	public static final Identifier CHAIN_GHAST_TNT = register(new Identifier("whodoneit:chain_ghast_tnt"), PreventionCategories.TNT);
	public static final Identifier CHAIN_TNT = register(new Identifier("whodoneit:chain_tnt"), PreventionCategories.TNT_PLAYER);
	
	public static final Identifier SKELETON_TNT = register(new Identifier("whodoneit:skeleton_tnt"), PreventionCategories.TNT);
	public static final Identifier CREEPER_TNT = register(new Identifier("whodoneit:creeper_tnt"), PreventionCategories.TNT);
	public static final Identifier WITHER_TNT = register(new Identifier("whodoneit:wither_tnt"), PreventionCategories.TNT);
	public static final Identifier GHAST_TNT = register(new Identifier("whodoneit:ghast_tnt"), PreventionCategories.TNT);
	
	public static final Identifier REDSTONE = register(new Identifier("whodoneit:redstone"), PreventionCategories.CONTRAPTION);
	
	public static final Identifier CREEPER = register(new Identifier("whodoneit:creeper"), PreventionCategories.MOB);
	public static final Identifier GHAST = register(new Identifier("whodoneit:ghast"), PreventionCategories.MOB);
	
	public static final Identifier WITHER = register(new Identifier("whodoneit:wither"), PreventionCategories.BOSS);
	public static final Identifier DRAGON = register(new Identifier("whodoneit:dragon"), PreventionCategories.BOSS);
	
	public static void forEach(Consumer<Pair<Identifier, PreventionCategory>> idConsumer) {
		REASONS.forEach(idConsumer);
	}
	
	public static Identifier register(Identifier name, PreventionCategory category) {
		REASONS.add(new Pair<>(name, category));
		return name;
	}
	
	public static PreventionCategory getCategory(Identifier reason) {
		for (Pair<Identifier, PreventionCategory> identifierIdentifierPair : REASONS) {
			if (reason.equals(identifierIdentifierPair.getLeft())) {
				return identifierIdentifierPair.getRight();
			}
		}
		return PreventionCategories.UNKNOWN;
	}
}
