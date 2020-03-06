package commandPointer.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将该方法注册成命令反射器
 * @author GuoJiaCheng
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistCommand
{
	/**
	 * 命令条目，当输入信息“.[本字符串]”的时候，将会启动本方法
	 * @return
	 */
	String CommandString();
	/**
	 * 帮助内容，用于显示帮助,可省略
	 * @return
	 */
	String Help() default "";
	/**
	 * 类型，可省略，若有类型，则会折叠到类型条目中，只具有一定的标记功能
	 * @return
	 */
	String Type() default "";
	/**
	 * 是否可见，默认可见
	 * @return
	 */
	boolean visiable() default true;
}
