package record;

import java.util.ArrayDeque;
import java.util.Queue;

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
					FileCode.getFileCode().writeLine(trans(m));
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
	public static StringTrans geStringTrans() 
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
	private String trans(MessageType m)
	{
//		先行预处理，将\n去除
		String pastMsg=m.getMsg();
		String afterMsg=pastMsg.replaceAll("\r\n", "\\\\n");
		
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		sb.append("time=");
		sb.append(m.gettime());
		sb.append(",MsgType=");
		sb.append(m.getMsgType());
		sb.append(",SubType=");
		sb.append(m.getsubType());
		sb.append(",MsgID=");
		sb.append(m.getMsgID());
		sb.append(",");
		if(m.getMsgType()==ConstantTable.MSGTYPE_PERSON)
		{
			sb.append("fromQQ=");
			sb.append(m.getfromQQ());
		}
		else if(m.getMsgType()==ConstantTable.MSGTYPE_GROUP)
			if(m.getfromQQ()!=ConstantTable.QQ_ANONYMOUS)
			{
				sb.append("fromQQ=");
				sb.append(m.getfromQQ());
				sb.append(",fromGroup=");
				sb.append(m.getfromGroup());
			}
			else
			{
				sb.append("fromAnonymous=");
				sb.append(m.getfromAnonymous());
				sb.append(",fromGroup=");
				sb.append(m.getfromGroup());
			}
		else
		{
			sb.append("fromQQ=");
			sb.append(m.getfromQQ());
			sb.append(",fromDiscuss=");
			sb.append(m.getfromGroup());
		}
		sb.append("]");
		sb.append(afterMsg);
		sb.append("\n");
		return sb.toString();
	}
}
