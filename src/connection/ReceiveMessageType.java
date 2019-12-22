package connection;

import global.ConstantTable;

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
		this.Msg=Msg;
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
		sb.append(ConstantTable.STRING_MSGTYPE+"="+MsgType!=null?MsgType:"null"+",");
		sb.append(ConstantTable.STRING_SUBTYPE+"="+SubType!=null?SubType:"null"+",");
		sb.append(ConstantTable.STRING_MSGID+"="+MsgID!=null?MsgID:"null"+",");
		sb.append(ConstantTable.STRING_FROMQQ+"="+fromQQ!=null?fromQQ:"null"+",");
		sb.append(ConstantTable.STRING_FROMGROUP+"="+fromGroup!=null?fromGroup:"null"+",");
		sb.append(ConstantTable.STRING_FROMANONYMOUS+"="+fromAnonymous!=null?fromAnonymous:"null"+",");
		sb.append(ConstantTable.STRING_MSG+"="+Msg!=null?Msg:"null"+",");
		sb.append(ConstantTable.STRING_TIME+"="+time!=null?time:"null");
		return sb.toString();
	}
}
