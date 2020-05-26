package commandPointer.authorizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加新的权限，添加的权限会被注册
 * @author GuoJiaCheng
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AddNewPermissions
{
	/**
	 * 新建的权限，详见{@link NewPermission}
	 * @return
	 */
	NewPermission[] permissions();
}
