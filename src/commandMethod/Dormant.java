package commandMethod;

import commandMethod.register.OnEventListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.ConstantTable;
import transceiver.Transmitter;

public class Dormant extends Father
{
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		OnMessageReceiveListener listener=new OnMessageReceiveListener() {
			
			@Override
			public int run(ReceiveMessageType messageType) 
			{
				// TODO Auto-generated method stub
				receiveMessageType=messageType;
				boolean accessible;
				if(getDataExchanger().getItem(getMark())==null)
					accessible=true;
				else
					accessible=Boolean.parseBoolean(getDataExchanger().getItem(getMark()));
				//FIXME:
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
	public void changeDormant()
	{
		boolean accessible=Boolean.parseBoolean(getDataExchanger().getItem(getMark()));
		accessible=!accessible;
		getDataExchanger().addItem(getMark(), Boolean.toString(accessible));
		sendBackMsg("设为"+(accessible?"监听":"苏醒")+"模式");
	}
	private String getMark()
	{
		String string = null;
		switch(receiveMessageType.getMsgType())
		{
		case ConstantTable.MSGTYPE_PERSON:
			string="P"+receiveMessageType.getfromQQ();
			break;
		case ConstantTable.MSGTYPE_GROUP:
			string="G"+receiveMessageType.getfromGroup();
			break;
		case ConstantTable.MSGTYPE_DISCUSS:
			string="D"+receiveMessageType.getfromGroup();
			break;
		}
		return string;
	}
}
