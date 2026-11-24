package vazkii.quark.base.asm.mixin;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class QuarkMixinPlugin implements IMixinConfigPlugin {

	private static final Logger LOGGER = LogManager.getLogger("Quark Mixin");
	private static final String TOGGLE_DESC = Type.getDescriptor(QuarkModuleToggle.class);
	private static final String MODULES_CATEGORY = "_modules";
	private static final String GLOBAL_CATEGORY = "_global";

	private Configuration config;
	private boolean hasOptifine;

	@Override
	public void onLoad(String mixinPackage) {
		// read only, never saved. quark writes the full file itself during preInit
		config = new Configuration(new File(Launch.minecraftHome, "config/quark.cfg"));
		hasOptifine = Launch.classLoader.getResource("optifine/OptiFineTweaker.class") != null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		AnnotationNode toggle = getAnnotation(getAnnotations(mixinClassName), TOGGLE_DESC);
		if (toggle == null)
			return true;

		if (hasOptifine && !getValue(toggle, "compatibleWithOptifine", true)) {
            LOGGER.debug("Skipping {}, OptiFine is present", mixinClassName);
			return false;
		}

		String module = getValue(toggle, "module", null);
		String feature = getValue(toggle, "feature", null);
		boolean enabledByDefault = getValue(toggle, "enabledByDefault", true);

		String category = module;
		if (module.equals(QuarkModuleToggle.GLOBAL))
			category = GLOBAL_CATEGORY;
		else if (!getBoolean(MODULES_CATEGORY, module, !module.equals("oddities"))) {
            LOGGER.debug("Skipping {}, module {} is disabled", mixinClassName, module);
			return false;
		}

		if (!getBoolean(category, feature, enabledByDefault)) {
            LOGGER.debug("Skipping {}, {}/{} is disabled", mixinClassName, category, feature);
			return false;
		}

		String subfeature = getValue(toggle, "subfeature", null);
		if (subfeature != null && !subfeature.isEmpty() && !getBoolean(category + "." + feature, subfeature, true)) {
            LOGGER.debug("Skipping {}, {}.{}/{} is disabled", mixinClassName, category, feature, subfeature);
			return false;
		}

		return true;
	}

	private boolean getBoolean(String category, String key, boolean def) {
		if (!config.hasCategory(category))
			return def;
		ConfigCategory cat = config.getCategory(category);
		Property prop = cat.get(key);
		return prop == null ? def : prop.getBoolean(def);
	}

	private static List<AnnotationNode> getAnnotations(String mixinClassName) {
		try {
			ClassNode node = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
			if (node.invisibleAnnotations != null)
				return node.invisibleAnnotations;
		} catch (Exception e) {
			LOGGER.error("Failed to read " + mixinClassName, e);
		}
		return Collections.emptyList();
	}

	private static AnnotationNode getAnnotation(List<AnnotationNode> annotations, String desc) {
		for (AnnotationNode annotation : annotations)
			if (annotation.desc.equals(desc))
				return annotation;
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T> T getValue(AnnotationNode annotation, String name, T def) {
		if (annotation.values != null)
			for (int i = 0; i < annotation.values.size(); i += 2)
				if (annotation.values.get(i).equals(name))
					return (T) annotation.values.get(i + 1);
		return def;
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
