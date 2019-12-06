package record;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.Iterator;

import connection.MessageType;
import global.ConstantTable;
import global.FileCode;

public class StringTrans 
{
	static StringTrans stringTrans;
	Queue<MessageType> MsgQueue;
	MsgThread t;
	class MsgThread implements Runnable
	{
		private boolean flag=true;
		@Override
		public void run() 
		{
			for(;flag;)
			{
				for(;!MsgQueue.isEmpty();)
				{
					MessageType m=MsgQueue.poll();
//					System.out.println("抽取队列，当前队列剩余量："+MsgQueue.size());
					FileCode.getFileCode().writeLine(msgTrans(m));
				}
				try 
				{
//					System.out.println("进入睡眠");
					Thread.sleep(3000);
//					System.out.println("离开睡眠");
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public void setflag(boolean b) 
		{
			// TODO Auto-generated method stub
			this.flag=b;
		}
	}
	public void startThread()
	{
		t=new MsgThread();
		new Thread(t).start();
		System.out.println("消息处理线程启动");
	}
	public void endThread()
	{
		if(t!=null)
			t.setflag(false);
		System.out.println("消息线程中断");
	}
	public static StringTrans getStringTrans() 
	{
		if(stringTrans==null)
		{
			stringTrans=new StringTrans();
			stringTrans.MsgQueue=new ArrayDeque<MessageType>();
			System.out.println("文字转换初始化");
		}
		return stringTrans;
	}
	public synchronized void addMsg(MessageType m)
	{
		System.out.println("收到消息:"+m.getMsg());
		MsgQueue.add(m);
//		System.out.println("加入队列");
	}
	private String msgTrans(MessageType m)
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
	public MessageType stringTrans(String s)
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
		MessageType messageType=new MessageType(MsgType, SubType, MsgID, fromQQ, fromGroup, fromAnonymous, Msg, time);
		return messageType;
	}
}
