package commandPointer.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 限制执行此方法的权限<br>
 * 和权限表相关联
 * @author GuoJiaCheng
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface AuthirizerListNeed
{
	/**权限表的名称，若缺省则为该类的默认表*/
	String AuthirizerList() default "";
	/**白名单用户是否可以访问*/
	boolean WhiteList_Accessible() default true;
	/**无记录用户是否可以访问*/
	boolean Normal_Accessible();
	/**黑名单用户是否可以访问*/
	boolean BlackList_Accessible() default false;
}
