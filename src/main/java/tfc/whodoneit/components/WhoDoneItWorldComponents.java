package tfc.whodoneit.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.util.Identifier;
import tfc.whodoneit.antigrief.component.ProtectionRegionsComponent;

public class WhoDoneItWorldComponents implements WorldComponentInitializer {
	public static final ComponentKey<ProtectionRegionsComponent> PROTECTION_REGIONS =
			ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("whodoneit:protection_regions"), ProtectionRegionsComponent.class);
	
	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(PROTECTION_REGIONS, chunk -> new ProtectionRegionsComponent());
	}
}
