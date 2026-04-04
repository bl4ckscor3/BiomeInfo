package bl4ckscor3.mod.biomeinfo;

import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.fml.config.IConfigSpec;

public class CommonBiomeInfo {
	public static final String MODID = "biomeinfo";
	public static final Identifier OVERLAY_ID = Identifier.fromNamespaceAndPath(MODID, "overlay");
	public static final PlatformHelper PLATFORM = load(PlatformHelper.class);

	public static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz, CommonBiomeInfo.class.getClassLoader())
			.findFirst()
			.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
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

	@FunctionalInterface
	public interface ReloadListenerRegistration {
		void register(Identifier id, PreparableReloadListener listener);
	}
}
