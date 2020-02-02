package global.authorizer;

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
