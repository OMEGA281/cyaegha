package commandMethod.register;

import connection.SendMessageType;

/**当消息发出的时候会启动该方法*/
public class OnMessageSendListener implements OnEventListener
{
	/**优先级，0~100*/
	public int priority;
	public int run(SendMessageType messageType)
	{
		return RETURN_PASS;
	}
}
