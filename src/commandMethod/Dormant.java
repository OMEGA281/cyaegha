package commandMethod;

import commandMethod.register.EventListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import connection.SendMessageType;
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
			public int run(ReceiveMessageType messageType) {
				// TODO Auto-generated method stub
				receiveMessageType=messageType;
				if((!accessible)&messageType.getMsg().equals(".dormant"))
				{
					accessible=true;
					Transmitter.getTransmitter().addMsg(new SendMessageType(
							receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
							, receiveMessageType.getfromGroup(), "退出休眠状态"));
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
		listener.response=accessible?EventListener.RESPONSE_PASS:EventListener.RESPONSE_STOP;
		listener.priority=EventListener.PRIORITY_HIGH;
		addMessageReceiveListener(listener);
	}
	public void changeDormant()
	{
		if(accessible)
		{
			accessible=false;
			Transmitter.getTransmitter().addMsg(new SendMessageType(
					receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
					, receiveMessageType.getfromGroup(), "进入休眠状态"));
		}
	}
}
