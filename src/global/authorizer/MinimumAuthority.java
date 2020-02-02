package global.authorizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
/**
 * 设置使用本方法的最低权限人，权限参照{@link MinimumAuthority}<br>
 * 默认为{@link MinimumAuthority.GROUP_MEMBER};
 * @author GuoJiaCheng
 *
 */
public @interface MinimumAuthority
{
	AuthirizerUser authirizerUser() default AuthirizerUser.GROUP_MEMBER;
}