package connection;

import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.Member;
import org.meowy.cqp.jcq.entity.QQInfo;
import org.meowy.cqp.jcq.entity.enumerate.Authority;

import global.UniversalConstantsTable;
import global.authorizer.AuthirizerUser;
import global.authorizer.AuthorizerListGetter;
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
		
		long client=messageType.getfromQQ();
		if(AuthorizerListGetter.getCoreAuthirizerList().isSOP(client))
			return AuthirizerUser.SUPER_OP;
		if(AuthorizerListGetter.getCoreAuthirizerList().isOP(client))
			return AuthirizerUser.OP;
		
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
	
	/**
	 * 获得某群内某人的权限<br>
	 * @param groupNum 群号
	 * @param num QQ号
	 * @return
	 */
	public static AuthirizerUser getAuthirizer(long groupNum,long num)
	{
		
		long client=num;
		if(AuthorizerListGetter.getCoreAuthirizerList().isSOP(client))
			return AuthirizerUser.SUPER_OP;
		if(AuthorizerListGetter.getCoreAuthirizerList().isOP(client))
			return AuthirizerUser.OP;
		
		Member member=getQQInfoInGroup(num, groupNum);
		if(member==null)
			return AuthirizerUser.BANNED_USER;
		switch(member.getAuthority())
		{
		case ADMIN:
			return AuthirizerUser.GROUP_MANAGER;
		case MEMBER:
			return AuthirizerUser.GROUP_MEMBER;
		case OWNER:
			return AuthirizerUser.GROUP_OWNER;
		}
		return AuthirizerUser.BANNED_USER;
	}
	
	public static String getMyName()
	{
		return CQ.getLoginNick();
	}
	
	public static long getMyQQ()
	{
		return CQ.getLoginQQ();
	}
	
	public static void dismissGroup(long l)
	{
		AuthirizerUser authirizerUser=getAuthirizer(l, getMyQQ());
		if(authirizerUser!=AuthirizerUser.GROUP_OWNER&&
				authirizerUser!=AuthirizerUser.GROUP_MEMBER&&authirizerUser!=AuthirizerUser.GROUP_MANAGER)
		{
			Log.e("异常的退群操作！中止本次操作！群号："+l);
			return;
		}
		CQ.setGroupLeave(l, authirizerUser==AuthirizerUser.GROUP_OWNER?true:false);
	}
	
	public static void dismissDiscuss(long l)
	{
		CQ.setDiscussLeave(l);
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
