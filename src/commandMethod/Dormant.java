package commandMethod;

import commandMethod.register.OnEventListener;
import commandMethod.register.OnGroupMemberChangeListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.GroupChangeType;
import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.UniversalConstantsTable;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;
import transceiver.Transmitter;

public class Dormant extends Father
{
	private static final String groupNumHead="N";
	private static final String discussNumHead="D";
	private static final String personNumHead="P";
	
	boolean accessible;
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		OnMessageReceiveListener listener=new OnMessageReceiveListener() {
			
			@Override
			@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
			public int run(ReceiveMessageType messageType) {
				// TODO Auto-generated method stub
				receiveMessageType=messageType;
				String mark=getMark();
				if(mark==null)
					return RETURN_STOP;
				String value=getDataExchanger().getItem(mark);
				if(value==null)
					accessible=false;
				else
					accessible=Boolean.parseBoolean(value);
				if((!accessible)&messageType.getMsg().equals(".dormant"))
				{
					accessible=true;
					getDataExchanger().addItem(mark, Boolean.toString(accessible));
					sendBackMsg("退出休眠状态");
					return RETURN_STOP;
				}
				if(accessible)
				{
					return RETURN_PASS;
				}
				else
				{
					return RETURN_STOP;
				}
			}
		};
		listener.priority=OnEventListener.PRIORITY_HIGH;
		addMessageReceiveListener(listener);
		OnGroupMemberChangeListener groupMemberChangeListener=new OnGroupMemberChangeListener() {
			
			@Override
			public int run(GroupChangeType groupChangeType)
			{
				String mark=getMark();
				if(mark==null)
					return RETURN_STOP;
				String value=getDataExchanger().getItem(mark);
				if(value==null)
					accessible=false;
				else
					accessible=Boolean.parseBoolean(value);
				if(accessible)
					return RETURN_PASS;
				else
					return RETURN_STOP;
			}
		};
		groupMemberChangeListener.priority=OnEventListener.PRIORITY_MAX;
		addGroupMemberChangeListener(groupMemberChangeListener);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
	public void changeDormant()
	{
		String mark=getMark();
		if(mark==null)
			return;
		String value=getDataExchanger().getItem(mark);
		if(value==null)
			accessible=false;
		else
			accessible=Boolean.parseBoolean(value);
		if(accessible)
		{
			accessible=false;
			getDataExchanger().addItem(mark, Boolean.toString(accessible));
			sendBackMsg("进入休眠状态");
		}
	}
	private String getMark()
	{
		switch(receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_GROUP:
			return groupNumHead+receiveMessageType.getfromGroup();
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			return discussNumHead+receiveMessageType.getfromGroup();
		case UniversalConstantsTable.MSGTYPE_PERSON:
			return personNumHead+receiveMessageType.getfromQQ();
		}
		return null;
	}
}
