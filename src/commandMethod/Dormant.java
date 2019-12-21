package commandMethod;

import commandMethod.register.EventListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;

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
				if((!accessible)&messageType.getMsg().equals(".dormant"))
				{
					accessible=true;
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
		}
	}
}
