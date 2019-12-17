package transceiver;

import java.util.HashMap;
import java.util.Map;

import connection.ReceiveMessageType;
import global.ConstantTable;

public class Translator 
{
	static String msgTrans(ReceiveMessageType m)
	{
//		先行预处理，将\n去除
		String pastMsg=m.getMsg();
		String afterMsg=pastMsg.replaceAll("\r\n", "\\\\n");
		
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		sb.append(ConstantTable.STRING_TIME+"=");
		sb.append(m.gettime());
		sb.append(","+ConstantTable.STRING_MSGTYPE+"=");
		sb.append(m.getMsgType());
		sb.append(","+ConstantTable.STRING_SUBTYPE+"=");
		sb.append(m.getsubType());
		sb.append(","+ConstantTable.STRING_MSGID+"=");
		sb.append(m.getMsgID());
		sb.append(",");
		if(m.getMsgType()==ConstantTable.MSGTYPE_PERSON)
		{
			sb.append(ConstantTable.STRING_FROMQQ+"=");
			sb.append(m.getfromQQ());
		}
		else if(m.getMsgType()==ConstantTable.MSGTYPE_GROUP)
			if(m.getfromQQ()!=ConstantTable.QQ_ANONYMOUS)
			{
				sb.append(ConstantTable.STRING_FROMQQ+"=");
				sb.append(m.getfromQQ());
				sb.append(","+ConstantTable.STRING_FROMGROUP+"=");
				sb.append(m.getfromGroup());
			}
			else
			{
				sb.append(","+ConstantTable.STRING_FROMANONYMOUS+"=");
				sb.append(m.getfromAnonymous());
				sb.append(","+ConstantTable.STRING_FROMGROUP+"=");
				sb.append(m.getfromGroup());
			}
		else
		{
			sb.append(ConstantTable.STRING_FROMQQ+"=");
			sb.append(m.getfromQQ());
			sb.append(","+ConstantTable.STRING_FROMDISCUSS+"=");
			sb.append(m.getfromGroup());
		}
		sb.append("]");
		sb.append(afterMsg);
		sb.append("\n");
		return sb.toString();
	}
	public static ReceiveMessageType stringTrans(String s)
	{
		String info=s.substring(1, s.indexOf("]"));
		String[] infoArr=info.split(",");
		Map<String,String> infoMap=new HashMap<String, String>();
		for(int i=0;i<infoArr.length;i++)
		{
			String[] value=infoArr[i].split("=");
			infoMap.put(value[0], value[1]);
		}
		int MsgType=infoMap.containsKey(ConstantTable.STRING_MSGTYPE)?Integer.parseInt(infoMap.get(ConstantTable.STRING_MSGTYPE)):ConstantTable.NUM_NULL;
		int SubType=infoMap.containsKey(ConstantTable.STRING_SUBTYPE)?Integer.parseInt(infoMap.get(ConstantTable.STRING_SUBTYPE)):ConstantTable.NUM_NULL;
		int MsgID=infoMap.containsKey(ConstantTable.STRING_MSGID)?Integer.parseInt(infoMap.get(ConstantTable.STRING_MSGID)):ConstantTable.NUM_NULL;
		long fromQQ=infoMap.containsKey(ConstantTable.STRING_FROMQQ)?Long.parseLong(infoMap.get(ConstantTable.STRING_FROMQQ)):ConstantTable.NUM_NULL;
		long fromGroup=infoMap.containsKey(ConstantTable.STRING_FROMGROUP)?Long.parseLong(infoMap.get(ConstantTable.STRING_FROMGROUP)):ConstantTable.NUM_NULL;
		String fromAnonymous=infoMap.containsKey(ConstantTable.STRING_FROMANONYMOUS)?infoMap.get(ConstantTable.STRING_FROMANONYMOUS):null;
		long time=infoMap.containsKey(ConstantTable.STRING_TIME)?Long.parseLong(infoMap.get(ConstantTable.STRING_TIME)):ConstantTable.NUM_NULL;
		String Msg=s.substring(s.indexOf("]")+1);
		ReceiveMessageType messageType=new ReceiveMessageType(MsgType, SubType, MsgID, fromQQ, fromGroup, fromAnonymous, Msg, time);
		return messageType;
	}
}
