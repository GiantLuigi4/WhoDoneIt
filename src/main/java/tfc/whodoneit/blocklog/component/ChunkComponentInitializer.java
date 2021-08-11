package tfc.whodoneit.blocklog.component;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import net.minecraft.util.Identifier;

public class ChunkComponentInitializer implements dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer {
	public static final ComponentKey<ChunkComponent> BLOCKLOG =
			ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("whodoneit:blocklog"), ChunkComponent.class);
	
	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(BLOCKLOG, chunk -> new ChunkComponent());
	}
}
