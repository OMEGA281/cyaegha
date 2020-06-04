package connection;

import org.meowy.cqp.jcq.entity.CQStatus;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.Member;
import org.meowy.cqp.jcq.entity.QQInfo;
import org.meowy.cqp.jcq.entity.enumerate.Authority;

import global.UniversalConstantsTable;
import pluginHelper.AuthirizerUser;
import surveillance.Log;

public class CQSender 
{
	static CQSender cqSender;
	static CoolQ CQ;
	
	public static QQInfo getQQInfo(long QQ)
	{
		return CQ.getStrangerInfo(QQ);
	}
	
	public static boolean canSendImage()
	{
		return CQ.canSendImage();
	}
	
	public static boolean canSendRecord()
	{
		return CQ.canSendRecord();
	}
	
	public static Member getQQInfoInGroup(long QQ,long GroupNum)
	{
		Member member=CQ.getGroupMemberInfo(GroupNum, QQ);
		if(member.getCard()==null)
			member=CQ.getGroupMemberInfo(GroupNum, QQ, true);
		if(member.getCard().isEmpty())
			member=CQ.getGroupMemberInfo(GroupNum, QQ, true);
		return CQ.getGroupMemberInfo(GroupNum, QQ);
	}
	
	public static String getMyName()
	{
		return CQ.getLoginNick();
	}
	
	public static long getMyQQ()
	{
		return CQ.getLoginQQ();
	}
	
	public static boolean dismissGroup(long l)
	{
		Member member=CQ.getGroupMemberInfo(l, getMyQQ());
		if(member==null)
		{
			Log.e("不存在于该群，群号："+l);
			return false;
		}
		int status=CQ.setGroupLeave(l, member.getAuthority()==Authority.OWNER?true:false);
		return CQStatus.getStatus(status).isSuccess();
	}
	
	public static boolean dismissDiscuss(long l)
	{
		int status=CQ.setDiscussLeave(l);
		return CQStatus.getStatus(status).isSuccess();
	}
	
	public CQSender(CoolQ CQ) 
	{
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
		case UniversalConstantsTable.MSGTYPE_PERSON:
			CQ.sendPrivateMsg(sendMessageType.toQQ, sendMessageType.Msg);
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			CQ.sendGroupMsg(sendMessageType.toGroup, sendMessageType.Msg);
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			CQ.sendDiscussMsg(sendMessageType.toGroup, sendMessageType.Msg);
			break;
		default:
			Log.e("未发现发送信息的类型");
			break;
		}
		Log.d("发送了",sendMessageType.toString());
	}
}
