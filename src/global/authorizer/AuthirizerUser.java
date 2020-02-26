package global.authorizer;

/**
 * 预置权限枚举，内含从SOP到禁止用户<br>
 * 注意：在权限等级上，讨论组人或私聊用户和群主的等级是相同的<br>
 * @author GuoJiaCheng
 *
 */
public enum AuthirizerUser
{
	SUPER_OP(100),OP(90),GROUP_OWNER(70),DISCUSS_MEMBER(70),PERSON_CLIENT(70),GROUP_MANAGER(50)
	,GROUP_MEMBER(30),BANNED_USER(0);

	private int authorityValue;
	
	AuthirizerUser(int i)
	{
		// TODO Auto-generated constructor stub
		authorityValue=i;
	}
	/**
	 * 比较获得该用户的权限是否可以得到
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
