package connection;

public class MessageType 
{
	int MsgType;
	int subType;
	int MsgID;
	long fromQQ;
	long fromGroup;
	String fromAnonymous;
	String Msg;
	long time;
	public MessageType(int MsgType,int subType,int MsgID,long fromQQ,long fromGroup,String fromAnonymous,String Msg,long time) 
	{
		// TODO Auto-generated constructor stub
		this.MsgType=MsgType;
		this.subType=subType;
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
		return subType;
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
}
