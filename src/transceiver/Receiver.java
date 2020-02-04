package transceiver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Queue;

import commandMethod.register.OnEventListener;
import commandMethod.register.OnMessageReceiveListener;
import commandMethod.register.Register;
import commandPointer.Matcher;
import connection.CQSender;
import connection.ReceiveMessageType;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;
import surveillance.Log;
import tools.FileSimpleIO;

public class Receiver 
{
	static Receiver stringTrans;
	Queue<ReceiveMessageType> MsgQueue;
	MsgThread MsgThread;
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
					ReceiveMessageType receiveMessageType=MsgQueue.poll();
//					System.out.println("抽取队列，当前队列剩余量："+MsgQueue.size());
					int response=OnEventListener.RETURN_PASS;
					if(!receiveMessageType.shouldRespone())
						continue;
					
					try
					{
						code_0:for (OnMessageReceiveListener messageReceiveListener : Register.getRegister().messageReceiveListeners) 
						{
							AuthirizerUser userAuthirizer=CQSender.getAuthirizer(receiveMessageType);
							boolean hasPermision;
							
							Class<?> nowClass=messageReceiveListener.getClass();
							Method method = null;
							method = nowClass.getDeclaredMethod("run", ReceiveMessageType.class);
							if(method!=null)
							{
								MinimumAuthority authority=method.getAnnotation(MinimumAuthority.class);
								if(authority==null)
								{
//								FIXME:将来会在这里加入默认影响
									hasPermision=AuthirizerUser.GROUP_MEMBER.ifAccessible(userAuthirizer);
								}
								else
								{
									hasPermision=authority.authirizerUser().ifAccessible(userAuthirizer);
								}
							}
							else
//							如果这步的话就是错误的方法了
								continue;
							
							if(!hasPermision)
								continue;
							
							response=messageReceiveListener.run(receiveMessageType);
							switch(response)
							{
							case OnEventListener.RETURN_PASS:
								continue;
							case OnEventListener.RETURN_STOP:
								break code_0;
							default:
								Log.e("出现未知的返回类型");
								continue;
							}
						}
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						Log.e("监听器出现问题");
					}
					
					try
					{
						if(response==OnEventListener.RETURN_PASS&Matcher.ifCommand(receiveMessageType.getMsg()))
						{
							Matcher.getMatcher().CommandProcesser(receiveMessageType);
						}
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						Log.e("命令反射器出现错误");
					}
					
				}
				try 
				{
//					System.out.println("进入睡眠");
					Thread.sleep(100);
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
		MsgThread=new MsgThread();
		new Thread(MsgThread).start();
		System.out.println("消息接受线程启动");
	}
	public void endThread()
	{
		if(MsgThread!=null)
			MsgThread.setflag(false);
		System.out.println("消息线程中断");
	}
	public static Receiver getReceiver() 
	{
		if(stringTrans==null)
		{
			stringTrans=new Receiver();
			stringTrans.MsgQueue=new ArrayDeque<ReceiveMessageType>();
			Log.d("初始化信息接受");
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
