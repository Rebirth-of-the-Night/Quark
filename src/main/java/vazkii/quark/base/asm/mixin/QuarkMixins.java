package vazkii.quark.base.asm.mixin;

import net.minecraft.launchwrapper.Launch;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

/**
 * Quark doesn't ship Mixin. If another mod provides Mixin and MixinExtras (0.4+), Quark's mixins are used
 * and the ASM patches they replace are skipped. Otherwise only the ASM patches are applied.
 */
public final class QuarkMixins {

	private static Boolean enabled;

	public static boolean enabled() {
		if (enabled == null) {
			enabled = hasClass("org.spongepowered.asm.launch.MixinBootstrap")
					&& hasClass("com.llamalad7.mixinextras.MixinExtrasBootstrap")
					&& hasClass("com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod");
			LogManager.getLogger("Quark ASM").info(enabled ? "Mixin found, using mixins" : "Mixin not found, using ASM patches");
		}
		return enabled;
	}

	private static boolean hasClass(String name) {
		return Launch.classLoader.getResource(name.replace('.', '/') + ".class") != null;
	}

	/** Only loaded if {@link #enabled()}. */
	public static final class Loader {

		public static void load() {
			MixinBootstrap.init();
			MixinExtrasBootstrap.init();
			Mixins.addConfiguration("mixins.quark.json");
		}
	}
}
