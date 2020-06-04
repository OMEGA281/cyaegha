package pluginHelper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将该方法注册成命令反射器
 * 命令反射器的优先级要比最低的监听器还要低
 * 重量级的方法建议注册成命令，大量的监听器会加重负担以及误识别的问题
 * @author GuoJiaCheng
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistCommand
{
	/**
	 * 命令条目，当输入信息“.[本字符串]”的时候，将会启动本方法<br>
	 * 如果想做子命令的话，就是每一个子命令都是一个命令，例如：<br>
	 * <code>help的子命令：list,demo,file<br>
	 * 那么就注册三个命令分别是：<br>
	 * 		help list<br>
	 * 		help demo<br>
	 * 		help file</code><br>
	 * 后来命令会自动归类显示<br>
	 * 为了您开发和维护方便请尽量将监听用字符串和方法名称设为一致（看着方便）
	 * @return
	 */
	String CommandString();
	/**
	 * 帮助内容，说明本命令的帮助内容，不建议省略
	 * @return
	 */
	String Help() default "";
	/**
	 * 命令是否一般可见，默认可见
	 * @return
	 */
	boolean visiable() default true;
}
