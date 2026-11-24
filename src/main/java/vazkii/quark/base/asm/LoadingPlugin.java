/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [26/03/2016, 21:34:16 (GMT)]
 */
package vazkii.quark.base.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import vazkii.quark.base.asm.mixin.QuarkMixins;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Quark Plugin")
@IFMLLoadingPlugin.TransformerExclusions("vazkii.quark.base.asm")
@IFMLLoadingPlugin.SortingIndex(1001) // After runtime deobfuscation
public class LoadingPlugin implements IFMLLoadingPlugin {

	public static boolean runtimeDeobfEnabled = false;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ "vazkii.quark.base.asm.ClassTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		runtimeDeobfEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");

		// called after all coremods are constructed, so any mixin provider is on the classpath by now
		if (QuarkMixins.enabled())
			QuarkMixins.Loader.load();
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
