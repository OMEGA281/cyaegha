package connection;

import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.Member;
import org.meowy.cqp.jcq.entity.QQInfo;

import global.ConstantTable;
import surveillance.Log;

public class CQSender 
{
	static CQSender cqSender;
	CoolQ CQ;
	
	public QQInfo getQQInfo(long QQ)
	{
		return CQ.getStrangerInfo(QQ);
	}
	
	public Member getQQInfoInGroup(long QQ,long GroupNum)
	{
		return CQ.getGroupMemberInfo(GroupNum, QQ);
	}
	
	public String getMyName()
	{
		return CQ.getLoginNick();
	}
	
	public CQSender(CoolQ CQ) 
	{
		// TODO Auto-generated constructor stub
		if(cqSender==null)
		{
			this.CQ=CQ;
			cqSender=this;
			Log.d("初始化发信器");
		}
	}
	public static CQSender getSender()
	{
		if (cqSender!=null) 
		{
			return cqSender;
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
