package transceiver;

import java.util.ArrayDeque;
import java.util.Queue;

import connection.Output;
import connection.SendMessageType;
import surveillance.Log;

public class Transmitter 
{
	static Transmitter transmitter;
	Queue<SendMessageType> SendMsgQueue;
	SendThread sendThread;
	public Transmitter() 
	{
		// TODO Auto-generated constructor stub
		SendMsgQueue=new ArrayDeque<>();
		sendThread=new SendThread();
	}
	class SendThread extends Thread
	{
		private boolean flag=true;
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			for(;flag;)
			{
				for(;!SendMsgQueue.isEmpty();)
				{
					Output.getOutput().sendMsg(SendMsgQueue.poll());
				}
				try 
				{
					Thread.sleep(100);
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
			flag=false;
		}
	}
	public void startThread()
	{
		new Thread(sendThread).start();
		System.out.println("消息发送线程启动");
	}
	public void endThread()
	{
		if(sendThread!=null)
			sendThread.setflag(false);
		System.out.println("消息线程中断");
	}
	public void addMsg(SendMessageType sendMessageType)
	{
		SendMsgQueue.add(sendMessageType);
	}
	public static Transmitter getTransmitter()
	{
		if(transmitter==null)
		{
			transmitter=new Transmitter();
			Log.d("初始化消息发送");
		}
		return transmitter;
	}
}
