package pluginHelper;

/**
 * 预置权限枚举，内含从SOP到禁止用户<br>
 * 注意：在权限等级上，讨论组人或私聊用户和群主的等级是相同的<br>
 * @author GuoJiaCheng
 *
 */
public enum AuthirizerUser
{
	/**超级OP权限*/
	SUPER_OP(100),
	/**OP权限*/
	OP(90),
	/**群主权限<br>权限值等同于{@link DISCUSS_MEMBER}和{@link PERSON_CLIENT}*/
	GROUP_OWNER(70),
	/**讨论组群员权限<br>权限值等同于{@link GROUP_OWNER}和{@link PERSON_CLIENT}*/
	DISCUSS_MEMBER(70),
	/**私聊权限<br>权限值等同于{@link GROUP_OWNER}和{@link DISCUSS_MEMBER}*/
	PERSON_CLIENT(70),
	/**群管理员权限*/
	GROUP_MANAGER(50),
	/**群成员权限*/
	GROUP_MEMBER(30),
	/**错误*/
	ERROR(0);

	private int authorityValue;
	
	AuthirizerUser(int i)
	{
		// TODO Auto-generated constructor stub
		authorityValue=i;
	}
	/**
	 * 比较获得该用户的权限是否足够
	 * @param clientAuthirizer 用户的权限
	 * @return
	 */
	public boolean ifAccessible(AuthirizerUser clientAuthirizer)
	{
		if(clientAuthirizer.authorityValue>=authorityValue)
			return true;
		return false;
	}
	public boolean ifAccessible(int value)
	{
		if(value>=authorityValue)
			return true;
		return false;
	}
}
