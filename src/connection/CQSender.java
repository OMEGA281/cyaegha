package connection;

import org.meowy.cqp.jcq.entity.CQStatus;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.entity.IRequest;
import org.meowy.cqp.jcq.entity.Member;
import org.meowy.cqp.jcq.entity.QQInfo;
import org.meowy.cqp.jcq.entity.enumerate.Authority;

import surveillance.Log;
import transceiver.IdentitySymbol;
import transceiver.event.FriendAddEvent;
import transceiver.event.GroupAddEvent;
import transceiver.event.MessageSendEvent;

public class CQSender implements IRequest
{
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
			CQSender.CQ=CQ;
	}
	public static void sendMsg(MessageSendEvent event)
	{
		int re = 0;
		switch (event.type) 
		{
		case PERSON:
			re=CQ.sendPrivateMsg(event.userNum,event.Msg);
			break;
		case GROUP:
			re=CQ.sendGroupMsg(event.groupNum, event.Msg);
			break;
		case DISCUSS:
			re=CQ.sendDiscussMsg(event.groupNum, event.Msg);
			break;
		}
		if(re<0)
		{
			Log.e("发信失败！"+event.toString());
		}
		Log.d(event.toString());
	}
	public static String getNickorCard(IdentitySymbol symbol)
	{
		switch (symbol.type)
		{
		case PERSON:
		case DISCUSS:
			return CQ.getStrangerInfo(symbol.userNum).getNick();
		case GROUP:
			CQ.getGroupMemberInfo(symbol.groupNum, symbol.userNum).getCard();
		default:
				return null;
		}
	}
	public static boolean setGroupAddRequest(GroupAddEvent event,boolean accept)
	{
		int status=CQ.setGroupAddRequest(event.responseFlag, REQUEST_GROUP_INVITE, accept?REQUEST_ADOPT:REQUEST_REFUSE, null);
		return CQStatus.getStatus(status).isSuccess();
	}
	public static boolean setFriendAddRequest(FriendAddEvent event,boolean accept)
	{
		int status=CQ.setFriendAddRequest(event.responseFlag, accept?REQUEST_ADOPT:REQUEST_REFUSE, null);
		return CQStatus.getStatus(status).isSuccess();
	}
}
