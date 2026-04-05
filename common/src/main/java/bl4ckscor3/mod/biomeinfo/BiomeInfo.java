package bl4ckscor3.mod.biomeinfo;

import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.fml.config.IConfigSpec;

public class BiomeInfo {
	public static final String MODID = "biomeinfo";
	public static final Identifier OVERLAY_ID = Identifier.fromNamespaceAndPath(MODID, "overlay");
	private static Platform platform;

	public static void initialize(Platform platform) {
		if (BiomeInfo.platform != null) {
			throw new IllegalArgumentException("BiomeInfo platform has already been initialized");
		}

		BiomeInfo.platform = platform;
	}

	public static void registerReloadListener(ReloadListenerRegistration registrar) {
		registrar.register(
			Identifier.fromNamespaceAndPath(MODID, "cache_invalidation"),
			(_, backgroundExecutor, barrier, _) ->
				CompletableFuture.runAsync(BiomeInfoRenderer.NAME_CACHE::clear, backgroundExecutor)
					.thenCompose(barrier::wait)
		);
	}

	public static void onConfigChange(IConfigSpec spec) {
		if (spec == Configuration.CONFIG_SPEC)
			BiomeInfoRenderer.NAME_CACHE.clear();
	}

	public static Platform platform() {
		return platform;
	}

	@FunctionalInterface
	public interface ReloadListenerRegistration {
		void register(Identifier id, PreparableReloadListener listener);
	}
}
