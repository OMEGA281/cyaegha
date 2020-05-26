package commandPointer.authorizer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 新建权限，将该注解包裹在{@link AddNewPermissions}中
 * @author GuoJiaCheng
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewPermission
{
	/**
	 * 权限的名称
	 * @return
	 */
	String permissionName();
	/**
	 * 父级权限，如果是某权限的子权限，那么如果拥有父级权限则拥有本权限<br>
	 * 留空则代表没有父级权限，默认无父级权限<br>
	 * 如果父级权限没有被注册过会产生错误<br>
	 * 能且只能注册一个父级权限<br>
	 * 如果产生环状的权限链则会报错<br>
	 * <br>
	 * 核心本身有预置权限，参照{@link AuthirizerUser}
	 * @return
	 */
	String SuperPermission() default "";
	/**
	 * 是否将该权限注册为全局的权限，默认为false<br>
	 * 如果将该权限注册为全局的权限那么其他的应用可能会对其进行修改
	 * @return
	 */
	boolean GloblePermission() default false;
	/**
	 * 是否允许该全局权限被读取，默认为true<br>
	 * 该项只在此权限为全局权限的时候才具有功能
	 * @return
	 */
	boolean Readable() default true;
	/**
	 * 是否允许该全局权限被写入，默认为false<br>
	 * 该项只在此权限为全局权限的时候才具有功能
	 * @return
	 */
	boolean Writeable() default false;
}
