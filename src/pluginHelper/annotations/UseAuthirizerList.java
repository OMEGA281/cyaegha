package pluginHelper.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 本注释代表方法使用哪一张权限表
 * 如果不使用该注释，或者默认则使用主权限表
 * @author GuoJiaCheng
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface UseAuthirizerList
{
	String AuthirizerListName() default "Main";
}
