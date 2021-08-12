package tfc.whodoneit.registry;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PreventionCategories {
	private static final ArrayList<Identifier> CATEGORIES = new ArrayList<>();
	
	public static final Identifier PLAYER = register(new Identifier("whodoneit:player"));
	public static final Identifier MOB = register(new Identifier("whodoneit:mob"));
	public static final Identifier BOSS = register(new Identifier("whodoneit:boss"));
	public static final Identifier CONTRAPTION = register(new Identifier("whodoneit:contraption"));
	public static final Identifier TNT = register(new Identifier("whodoneit:tnt"));
	
	public static void forEach(Consumer<Identifier> idConsumer) {
		CATEGORIES.forEach(idConsumer);
	}
	
	public static Identifier register(Identifier category) {
		CATEGORIES.add(category);
		return category;
	}
}
