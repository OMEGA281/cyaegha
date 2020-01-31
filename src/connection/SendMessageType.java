package connection;

import global.UniversalConstantsTable;

public class SendMessageType 
{
	int type;
	long toQQ;
	long toGroup;
	String Msg;
	public SendMessageType() 
	{
		// TODO Auto-generated constructor stub
	}
	/**发送用的信息包
	 * @param type 类型：0/私人	1/群		2/讨论组
	 * @param toClient 目标的号码
	 * @param Msg 信息
	 * */
	public SendMessageType(int type,long toQQ,long toGroup,String Msg) 
	{
		// TODO Auto-generated constructor stub
		this.type=type;
		this.toQQ=toQQ;
		this.toGroup=toGroup;
		this.Msg=Msg;
	}
	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		sb.append((UniversalConstantsTable.STRING_SENDTYPE+"="+type!=null?type:"null")+",");
		sb.append((UniversalConstantsTable.STRING_SENDCLIENT+"="+toQQ!=null?toQQ:"null")+",");
		sb.append((UniversalConstantsTable.STRING_SENDCLIENT+"="+toGroup!=null?toGroup:"null")+",");
		sb.append(UniversalConstantsTable.STRING_MSG+"="+Msg!=null?Msg:"null");
		return sb.toString();
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setToQQ(long toClient) {
		this.toQQ = toClient;
	}
	public long getToQQ() {
		return toQQ;
	}
	public void setToGroup(long toGroup) 
	{
		this.toGroup = toGroup;
	}
	public long getToGroup() 
	{
		return toGroup;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public String getMsg() {
		return Msg;
	}
}
