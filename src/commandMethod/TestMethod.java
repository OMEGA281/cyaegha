package commandMethod;

import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;

public class TestMethod extends Father
{
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
	public void tm()
	{
		System.out.println("a");
	}

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
		
	}
}
