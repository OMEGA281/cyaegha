package commandPointer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表明这个类有字符串资源文件
 * @author GuoJiaCheng
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StringResource
{
	/**
	 * 字符串资源文件的名称，默认为空字符串<br>
	 * 若为默认且表明本注解则为与本类同名的文件
	 * @return
	 */
	String resourceName() default "";
}
