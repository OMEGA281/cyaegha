package pluginHelper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pluginHelper.AuthirizerUser;

/**
 * 设置使用本方法的最低权限使用者，权限参照{@link AuthirizerUser}<br>
 * 默认为{@link GROUP_MEMBER};
 * 
 * @author GuoJiaCheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MinimumAuthority
{
	AuthirizerUser value() default AuthirizerUser.GROUP_MEMBER;
}