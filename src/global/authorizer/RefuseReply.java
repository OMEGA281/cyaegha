package global.authorizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当检查命令权限发现不足以启动时，将会寻找此注解<br>
 * 若无注解则会使用默认值
 * @author GuoJiaCheng
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RefuseReply
{
	/**
	 * 代表自定义返回的标签，如果为空则不会使用自定义标签
	 * @return
	 */
	String key();
	/**
	 * 当权限拒绝的时候回复的语句
	 * @return
	 */
	String reply();
}
