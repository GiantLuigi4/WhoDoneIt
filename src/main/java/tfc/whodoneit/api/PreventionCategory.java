package tfc.whodoneit.api;

import net.minecraft.util.Identifier;

public class PreventionCategory {
	public final Identifier name;
	public final boolean ownerBypass;
	public final boolean groupBypass;
	public final PreventionCategory[] parentCategories;
	
	public PreventionCategory(Identifier name, boolean ownerBypass, boolean groupBypass, PreventionCategory[] parentCategories) {
		this.name = name;
		this.ownerBypass = ownerBypass;
		this.groupBypass = groupBypass;
		this.parentCategories = parentCategories;
	}
}
