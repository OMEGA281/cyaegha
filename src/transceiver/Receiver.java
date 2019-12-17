package transceiver;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.Iterator;

import connection.ReceiveMessageType;
import global.ConstantTable;
import global.FileCode;

public class Receiver 
{
	static Receiver stringTrans;
	Queue<ReceiveMessageType> MsgQueue;
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
					ReceiveMessageType m=MsgQueue.poll();
//					System.out.println("抽取队列，当前队列剩余量："+MsgQueue.size());
					FileCode.getFileCode().writeLine(Translator.msgTrans(m));
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
	public static Receiver getStringTrans() 
	{
		if(stringTrans==null)
		{
			stringTrans=new Receiver();
			stringTrans.MsgQueue=new ArrayDeque<ReceiveMessageType>();
			System.out.println("文字转换初始化");
		}
		return stringTrans;
	}
	public synchronized void addMsg(ReceiveMessageType m)
	{
		System.out.println("收到消息:"+m.getMsg());
		MsgQueue.add(m);
//		System.out.println("加入队列");
	}
	
	
}
