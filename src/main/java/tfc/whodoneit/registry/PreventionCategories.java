package tfc.whodoneit.registry;

import net.minecraft.util.Identifier;
import tfc.whodoneit.api.PreventionCategory;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PreventionCategories {
	private static final ArrayList<PreventionCategory> CATEGORIES = new ArrayList<>();
	
	public static final PreventionCategory PLAYER = register(new Identifier("whodoneit:player"));
	public static final PreventionCategory MOB = register(new Identifier("whodoneit:mob"));
	public static final PreventionCategory BOSS = register(new Identifier("whodoneit:boss"));
	public static final PreventionCategory CONTRAPTION = register(new Identifier("whodoneit:contraption"));
	public static final PreventionCategory TNT = register(new Identifier("whodoneit:tnt"));
	public static final PreventionCategory TNT_PLAYER = register(new PreventionCategory(new Identifier("whodoneit:tnt_player"), true, true, new PreventionCategory[] {PLAYER, TNT}));
	public static final PreventionCategory UNKNOWN = register(new Identifier("whodoneit:unknown"));
	
	public static void forEach(Consumer<PreventionCategory> idConsumer) {
		CATEGORIES.forEach(idConsumer);
	}
	
	public static PreventionCategory register(PreventionCategory category) {
		CATEGORIES.add(category);
		return category;
	}
	
	public static PreventionCategory register(Identifier category) {
		PreventionCategory category1 = new PreventionCategory(category, false, false, null);
		CATEGORIES.add(category1);
		return category1;
	}
}
