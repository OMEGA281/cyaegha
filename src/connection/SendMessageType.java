package connection;

import global.ConstantTable;

public class SendMessageType 
{
	int type;
	long toClient;
	String Msg;
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
}
