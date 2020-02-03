package commandMethod;

import commandMethod.register.OnEventListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;
import transceiver.Transmitter;

public class Dormant extends Father
{
	static boolean accessible=true;
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		OnMessageReceiveListener listener=new OnMessageReceiveListener() {
			
			@Override
			@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_OWNER)
			public int run(ReceiveMessageType messageType) {
				// TODO Auto-generated method stub
				receiveMessageType=messageType;
				if((!accessible)&messageType.getMsg().equals(".dormant"))
				{
					accessible=true;
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
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_OWNER)
	public void changeDormant()
	{
		if(accessible)
		{
			accessible=false;
			sendBackMsg("进入休眠状态");
		}
	}
}
