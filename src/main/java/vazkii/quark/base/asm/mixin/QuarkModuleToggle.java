package vazkii.quark.base.asm.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Only applies the annotated mixin if the module and the feature are enabled in quark.cfg at launch.
 * With module {@link #GLOBAL}, feature is an option of the _global category instead.
 * If set, subfeature also has to be enabled in the feature's own category.
 * If not compatibleWithOptifine, the mixin is also skipped when OptiFine is present.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface QuarkModuleToggle {

	String GLOBAL = "global";

	String module();

	String feature();

	String subfeature() default "";

	boolean enabledByDefault() default true;

	boolean compatibleWithOptifine() default true;
}
