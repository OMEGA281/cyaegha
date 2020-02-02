package connection;

import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.Member;
import org.meowy.cqp.jcq.entity.QQInfo;
import org.meowy.cqp.jcq.entity.enumerate.Authority;

import global.UniversalConstantsTable;
import global.authorizer.AuthirizerUser;
import surveillance.Log;

public class CQSender 
{
	static CQSender cqSender;
	static CoolQ CQ;
	
	public static QQInfo getQQInfo(long QQ)
	{
		return CQ.getStrangerInfo(QQ);
	}
	
	public static Member getQQInfoInGroup(long QQ,long GroupNum)
	{
		return CQ.getGroupMemberInfo(GroupNum, QQ);
	}
	/**
	 * 获取这条信息发出者所在环境的权限
	 * @param messageType 信息包
	 * @return 权限情况
	 */
	public static AuthirizerUser getAuthirizer(ReceiveMessageType messageType)
	{
		int msgType=messageType.MsgType;
//		FIXME:这里是之后加入处理匿名者的代码
		if(messageType.getfromAnonymous()!=null)
			if(messageType.getfromAnonymous()!="")
				return AuthirizerUser.BANNED_USER;
		
		switch(msgType)
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			return AuthirizerUser.PERSON_CLIENT;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			return AuthirizerUser.DISCUSS_MEMBER;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			long QQ=messageType.getfromQQ();
			long GroupNum=messageType.getfromGroup();
			Authority i=getQQInfoInGroup(QQ, GroupNum).getAuthority();
			switch(i)
			{
			case ADMIN:
				return AuthirizerUser.GROUP_MANAGER;
			case MEMBER:
				return AuthirizerUser.GROUP_MEMBER;
			case OWNER:
				return AuthirizerUser.GROUP_OWNER;
			}
		}
		return AuthirizerUser.BANNED_USER;
	}
	
	public static String getMyName()
	{
		return CQ.getLoginNick();
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
