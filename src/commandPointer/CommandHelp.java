package commandPointer;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记这个方法的帮助，可以被查询到<br>
 * 标记的位置可以是有参和无参的方法<br>
 * 但是当有参和无参的方法都被标记的时候以无参的为准
 * @author GuoJiaCheng
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface CommandHelp
{
	/**
	 * 帮助信息，默认为空字符串
	 * @return
	 */
	String help() default "";
}
