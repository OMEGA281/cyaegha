package connection;

import org.meowy.cqp.jcq.entity.CoolQ;

import global.ConstantTable;
import surveillance.Log;

public class Output 
{
	static Output output;
	CoolQ CQ;
	public Output(CoolQ CQ) 
	{
		// TODO Auto-generated constructor stub
		if(output==null)
		{
			this.CQ=CQ;
			output=this;
			Log.d("初始化发信器");
		}
	}
	public static Output getOutput()
	{
		if (output!=null) 
		{
			return output;
		}
		Log.e("发信部件尚未启动");
		return null;
	}
	public void sendMsg(SendMessageType sendMessageType)
	{
		switch (sendMessageType.getType()) 
		{
		case ConstantTable.MSGTYPE_PERSON:
			CQ.sendPrivateMsg(sendMessageType.toQQ, sendMessageType.Msg);
			break;
		case ConstantTable.MSGTYPE_GROUP:
			CQ.sendGroupMsg(sendMessageType.toGroup, sendMessageType.Msg);
			break;
		case ConstantTable.MSGTYPE_DISCUSS:
			CQ.sendDiscussMsg(sendMessageType.toGroup, sendMessageType.Msg);
			break;
		default:
			Log.e("未发现发送信息的类型");
			break;
		}
		Log.d("发送了",sendMessageType.toString());
	}
}
