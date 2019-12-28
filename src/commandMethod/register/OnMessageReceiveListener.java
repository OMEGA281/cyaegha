package commandMethod.register;

import connection.ReceiveMessageType;

/**当收到消息的时候会启动该方法<br>
 * 不建议使用此作为命令入口<br>
 * 命令请使用反射器静态启动*/
public class OnMessageReceiveListener implements OnEventListener
{
	/**进入感受态，暂时最大化优先度，执行本方法并截停，执行之后回复为默认*/
	public final int RESPONSE_FEELING=3;
	
	/**优先级，0~100*/
	public int priority;
	
	public int run(ReceiveMessageType messageType)
	{
		return RETURN_PASS;
	}
}
