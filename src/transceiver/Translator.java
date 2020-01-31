package transceiver;

import java.util.HashMap;
import java.util.Map;

import connection.ReceiveMessageType;
import global.UniversalConstantsTable;

public class Translator 
{
	static String msgTrans(ReceiveMessageType m)
	{
//		先行预处理，将\n去除
		String pastMsg=m.getMsg();
		String afterMsg=pastMsg.replaceAll("\r\n", "\\\\n");
		
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		sb.append(UniversalConstantsTable.STRING_TIME+"=");
		sb.append(m.gettime());
		sb.append(","+UniversalConstantsTable.STRING_MSGTYPE+"=");
		sb.append(m.getMsgType());
		sb.append(","+UniversalConstantsTable.STRING_SUBTYPE+"=");
		sb.append(m.getsubType());
		sb.append(","+UniversalConstantsTable.STRING_MSGID+"=");
		sb.append(m.getMsgID());
		sb.append(",");
		if(m.getMsgType()==UniversalConstantsTable.MSGTYPE_PERSON)
		{
			sb.append(UniversalConstantsTable.STRING_FROMQQ+"=");
			sb.append(m.getfromQQ());
		}
		else if(m.getMsgType()==UniversalConstantsTable.MSGTYPE_GROUP)
			if(m.getfromQQ()!=UniversalConstantsTable.QQ_ANONYMOUS)
			{
				sb.append(UniversalConstantsTable.STRING_FROMQQ+"=");
				sb.append(m.getfromQQ());
				sb.append(","+UniversalConstantsTable.STRING_FROMGROUP+"=");
				sb.append(m.getfromGroup());
			}
			else
			{
				sb.append(","+UniversalConstantsTable.STRING_FROMANONYMOUS+"=");
				sb.append(m.getfromAnonymous());
				sb.append(","+UniversalConstantsTable.STRING_FROMGROUP+"=");
				sb.append(m.getfromGroup());
			}
		else
		{
			sb.append(UniversalConstantsTable.STRING_FROMQQ+"=");
			sb.append(m.getfromQQ());
			sb.append(","+UniversalConstantsTable.STRING_FROMDISCUSS+"=");
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
		int MsgType=infoMap.containsKey(UniversalConstantsTable.STRING_MSGTYPE)?Integer.parseInt(infoMap.get(UniversalConstantsTable.STRING_MSGTYPE)):UniversalConstantsTable.NUM_NULL;
		int SubType=infoMap.containsKey(UniversalConstantsTable.STRING_SUBTYPE)?Integer.parseInt(infoMap.get(UniversalConstantsTable.STRING_SUBTYPE)):UniversalConstantsTable.NUM_NULL;
		int MsgID=infoMap.containsKey(UniversalConstantsTable.STRING_MSGID)?Integer.parseInt(infoMap.get(UniversalConstantsTable.STRING_MSGID)):UniversalConstantsTable.NUM_NULL;
		long fromQQ=infoMap.containsKey(UniversalConstantsTable.STRING_FROMQQ)?Long.parseLong(infoMap.get(UniversalConstantsTable.STRING_FROMQQ)):UniversalConstantsTable.NUM_NULL;
		long fromGroup=infoMap.containsKey(UniversalConstantsTable.STRING_FROMGROUP)?Long.parseLong(infoMap.get(UniversalConstantsTable.STRING_FROMGROUP)):UniversalConstantsTable.NUM_NULL;
		String fromAnonymous=infoMap.containsKey(UniversalConstantsTable.STRING_FROMANONYMOUS)?infoMap.get(UniversalConstantsTable.STRING_FROMANONYMOUS):null;
		long time=infoMap.containsKey(UniversalConstantsTable.STRING_TIME)?Long.parseLong(infoMap.get(UniversalConstantsTable.STRING_TIME)):UniversalConstantsTable.NUM_NULL;
		String Msg=s.substring(s.indexOf("]")+1);
		ReceiveMessageType messageType=new ReceiveMessageType(MsgType, SubType, MsgID, fromQQ, fromGroup, fromAnonymous, Msg, time);
		return messageType;
	}
}
