package commandMethod.register;

import connection.GroupChangeType;

/**当群人员变更时会启动该方法*/
public class OnGroupMemberChangeListener implements OnEventListener
{

	/**优先级，0~100*/
	public int priority;
	public int run(GroupChangeType groupChangeType) 
	{
		return RETURN_PASS;
	}
}