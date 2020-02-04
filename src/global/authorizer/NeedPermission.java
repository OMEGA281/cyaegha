package global.authorizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NeedPermission
{
	/**
	 * 执行本方法所需要的权限，若有父级权限也可通过
	 * @return
	 */
	String[] permissions();
}
