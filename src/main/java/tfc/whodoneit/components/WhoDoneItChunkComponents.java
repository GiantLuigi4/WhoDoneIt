package tfc.whodoneit.components;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import net.minecraft.util.Identifier;
import tfc.whodoneit.blocklog.component.ModificationTrackerComponent;

public class WhoDoneItChunkComponents implements dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer {
	public static final ComponentKey<ModificationTrackerComponent> BLOCKLOG =
			ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("whodoneit:blocklog"), ModificationTrackerComponent.class);
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(BLOCKLOG, chunk -> new ModificationTrackerComponent());
	}
}
