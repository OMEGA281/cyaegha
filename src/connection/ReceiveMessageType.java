package connection;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.meowy.cqp.jcq.message.ActionMsg;
import org.meowy.cqp.jcq.message.CQCode;
import org.meowy.cqp.jcq.message.CoolQMsg;

import global.UniversalConstantsTable;

public class ReceiveMessageType 
{
	int MsgType;
	int SubType;
	int MsgID;
	long fromQQ;
	long fromGroup;
	String fromAnonymous;
	String Msg;
	long time;
	boolean shouldRespone;
	/**收到消息时的通用包形式
	 * @param MsgType 消息类型，常量池中有
	 * @param subType 子类型，常量池中有
	 * @param MsgID 消息的ID
	 * @param fromQQ 来源QQ
	 * @param fromGroup 来源群号或讨论组号
	 * @param fromAnonymous 来源匿名者
	 * @param Msg 消息内容
	 * @param time 时间戳*/
	public ReceiveMessageType(int MsgType,int subType,int MsgID,long fromQQ,long fromGroup,String fromAnonymous,String Msg,long time) 
	{
		// TODO Auto-generated constructor stub
		this.MsgType=MsgType;
		this.SubType=subType;
		this.MsgID=MsgID;
		this.fromQQ=fromQQ;
		this.fromGroup=fromGroup;
		this.fromAnonymous=fromAnonymous;
		this.Msg=Msg.trim();
		dealMsg();
		this.time=time;
	}
	public int getMsgType()
	{
		return MsgType;
	}
	public int getsubType() 
	{
		return SubType;
	}
	public int getMsgID() 
	{
		return MsgID;
	}
	public long getfromQQ() 
	{
		return fromQQ;
	}
	public long getfromGroup() 
	{
		return fromGroup;
	}
	public String getfromAnonymous() 
	{
		return fromAnonymous;
	}
	public String getMsg() 
	{
		return Msg;
	}
	public long gettime() 
	{
		return time;
	}
	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		sb.append(UniversalConstantsTable.STRING_MSGTYPE+"="+MsgType!=null?MsgType:"null"+",");
		sb.append(UniversalConstantsTable.STRING_SUBTYPE+"="+SubType!=null?SubType:"null"+",");
		sb.append(UniversalConstantsTable.STRING_MSGID+"="+MsgID!=null?MsgID:"null"+",");
		sb.append(UniversalConstantsTable.STRING_FROMQQ+"="+fromQQ!=null?fromQQ:"null"+",");
		sb.append(UniversalConstantsTable.STRING_FROMGROUP+"="+fromGroup!=null?fromGroup:"null"+",");
		sb.append(UniversalConstantsTable.STRING_FROMANONYMOUS+"="+fromAnonymous!=null?fromAnonymous:"null"+",");
		sb.append(UniversalConstantsTable.STRING_MSG+"="+Msg!=null?Msg:"null"+",");
		sb.append(UniversalConstantsTable.STRING_TIME+"="+time!=null?time:"null");
		return sb.toString();
	}
	public boolean shouldRespone()
	{
		return shouldRespone;
	}
	private void dealMsg()
	{
		Pattern space=Pattern.compile("[ ]+");
		CQCode code=new CQCode();
		CoolQMsg coolQMsg=new CoolQMsg(Msg);
		ArrayList<Long> arrayList=new ArrayList<Long>();
		int index=0;
		for(;index<coolQMsg.size();index++)
		{
			ActionMsg actionMsg=coolQMsg.get(index);
			if(space.matcher(actionMsg.getMsg()).matches())
				continue;
			long l=code.getAt(actionMsg.getMsg());
			if(l==-1000)
				break;
			arrayList.add(l);
		}
//		处理是否应该响应
		if(arrayList.size()>0)
		{
			if(arrayList.contains(-1l))
				shouldRespone=true;
			else if(arrayList.contains(CQSender.getMyQQ()))
				shouldRespone=true;
			else
				shouldRespone=false;
		}
		else
			shouldRespone=true;
		
		if(index>=coolQMsg.size())
		{
			shouldRespone=false;
			Msg="";
		}
		StringBuilder builder=new StringBuilder();
		for(int i=index;i<coolQMsg.size();i++)
		{
			builder.append(coolQMsg.get(i).getMsg());
		}
		Msg=builder.toString().trim();
	}
	public String getNick()
	{
		switch(MsgType)
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			return CQSender.getQQInfo(fromQQ).getNick();
		case UniversalConstantsTable.MSGTYPE_GROUP:
			String card=CQSender.getQQInfoInGroup(fromQQ, fromGroup).getCard();
			return card.isEmpty()?CQSender.getQQInfo(fromQQ).getNick():card;
		}
		return null;
	}
}
