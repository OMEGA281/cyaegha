package connection;

import global.ConstantTable;

public class SendMessageType 
{
	int type;
	long toClient;
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
	public SendMessageType(int type,long toClient,String Msg) 
	{
		// TODO Auto-generated constructor stub
		this.type=type;
		this.toClient=toClient;
		this.Msg=Msg;
	}
	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		sb.append(ConstantTable.STRING_SENDTYPE+"="+type!=null?type:"null"+",");
		sb.append(ConstantTable.STRING_SENDCLIENT+"="+toClient!=null?toClient:"null"+",");
		sb.append(ConstantTable.STRING_MSG+"="+Msg!=null?Msg:"null");
		return sb.toString();
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setToClient(long toClient) {
		this.toClient = toClient;
	}
	public long getToClient() {
		return toClient;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public String getMsg() {
		return Msg;
	}
}
