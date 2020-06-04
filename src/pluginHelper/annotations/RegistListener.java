package pluginHelper.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 添加事件监听器
 * @author GuoJiaCheng
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
@Inherited
public @interface RegistListener
{
	public static final Class<?>[] ListenerList= {MessageReceiveListener.class,GroupMemberChangeListener.class,
			MessageSendListener.class,GroupBanListener.class,FriendAddListener.class,GroupAddListener.class};
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface MessageReceiveListener
	{
		public int priority() default PRIORITY_NORMAL;
	}
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface GroupMemberChangeListener
	{
		public int priority() default PRIORITY_NORMAL;
	}
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface MessageSendListener
	{
		public int priority() default PRIORITY_NORMAL;
	}
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface GroupBanListener
	{
		public int priority() default PRIORITY_NORMAL;
	}
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface FriendAddListener
	{
		public int priority() default PRIORITY_NORMAL;
	}
	@Documented
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface GroupAddListener
	{
		public int priority() default PRIORITY_NORMAL;
	}
	int PRIORITY_MAX=100;
	int PRIORITY_HIGH=75;
	int PRIORITY_NORMAL=50;
	int PRIORITY_LOW=25;
	int PRIORITY_MIN=0;
	/**监听器帮助文件，建议填写*/
	public String help() default "";
	/**监听器优先级*/
	public int priority() default PRIORITY_NORMAL;
}
