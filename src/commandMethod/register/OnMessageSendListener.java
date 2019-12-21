package commandMethod.register;

import connection.SendMessageType;

/**当消息发出的时候会启动该方法*/
public class OnMessageSendListener extends OnEventListener
{
	public int run(SendMessageType messageType)
	{
		return RESPONSE_PASS;
	}
}
