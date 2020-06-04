package transceiver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import pluginHelper.SelfStartMethod;
import surveillance.Log;
import transceiver.event.FriendAddEvent;
import transceiver.event.GroupAddEvent;
import transceiver.event.GroupBanEvent;
import transceiver.event.GroupMemberChangeEvent;
import transceiver.event.MessageReceiveEvent;
import transceiver.event.MessageSendEvent;

public class EventTrigger
{
	public enum EventResult{PASS,STOP}
	private enum ThreadOperter{CONTINUE,SLEEP,STOP,ERROR}
	
	static EventTrigger eventTrigger;
	
	EventList<SelfStartMethod> friendAddEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> groupAddEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> groupBanEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> groupMemberChangeEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> messageReceiveEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> messageSendEvents=new EventList<SelfStartMethod>();
	
	Queue<FriendAddEvent> friendAddEventsQueue=new ArrayDeque<FriendAddEvent>();
	Queue<GroupAddEvent> groupAddEventsQueue=new ArrayDeque<GroupAddEvent>();
	Queue<GroupBanEvent> groupBanEventsQueue=new ArrayDeque<GroupBanEvent>();
	Queue<GroupMemberChangeEvent> groupMemberChangeEventsQueue=new ArrayDeque<GroupMemberChangeEvent>();
	Queue<MessageReceiveEvent> messageReceiveEventsQueue=new ArrayDeque<MessageReceiveEvent>();
	Queue<MessageSendEvent> messageSendEventsQueue=new ArrayDeque<MessageSendEvent>();
	
	HashMap<String, Thread> threadPool=new HashMap<String, Thread>();
	
	class EventList<T> extends ArrayList<T>
	{
		ArrayList<Integer> priority=new ArrayList<Integer>();
		public void add(T t, int priority)
		{
			if(this.priority.isEmpty())
			{
				add(t);
				this.priority.add(priority);
				return;
			}
			for (int i=0;i<this.priority.size();i++)
			{
				Integer integer = this.priority.get(i);
				if(priority<integer)
					continue;
				else
				{
					add(i, t);
					this.priority.add(i, priority);
					return;
				}
			}
			add(t);
			this.priority.add(priority);
		}
	}
	
	public EventTrigger()
	{
		if(eventTrigger!=null)
		{
			Log.e("触发器已经被启动！");
			return;
		}
		eventTrigger=this;
		
//		FIXME:虽然这样也可以，不过挺难看的，下次整合到枚举中吧
		
		setupThread(new ThreadMethod() {
			
			@Override
			public ThreadOperter run()
			{
				for(;!friendAddEventsQueue.isEmpty();)
				{
					FriendAddEvent event=friendAddEventsQueue.poll();
					code_0:for (SelfStartMethod selfStartMethod : friendAddEvents)
					{
						EventResult result=(EventResult) selfStartMethod.startMethod(event);
						switch (result)
						{
						case PASS:
							continue;
						case STOP:
							break code_0;
						default:
							return ThreadOperter.ERROR;
						}
					}
				}
				return ThreadOperter.SLEEP;
			}
		}, 500, "friendAdd");
		
		setupThread(new ThreadMethod() {
			
			@Override
			public ThreadOperter run()
			{
				for(;!groupAddEventsQueue.isEmpty();)
				{
					GroupAddEvent event=groupAddEventsQueue.poll();
					code_0:for (SelfStartMethod selfStartMethod : groupAddEvents)
					{
						EventResult result=(EventResult) selfStartMethod.startMethod(event);
						switch (result)
						{
						case PASS:
							continue;
						case STOP:
							break code_0;
						default:
							return ThreadOperter.ERROR;
						}
					}
				}
				return ThreadOperter.SLEEP;
			}
		}, 500, "groupAdd");
		
		setupThread(new ThreadMethod() {
			
			@Override
			public ThreadOperter run()
			{
				for(;!groupBanEventsQueue.isEmpty();)
				{
					GroupBanEvent event=groupBanEventsQueue.poll();
					code_0:for (SelfStartMethod selfStartMethod : groupBanEvents)
					{
						EventResult result=(EventResult) selfStartMethod.startMethod(event);
						switch (result)
						{
						case PASS:
							continue;
						case STOP:
							break code_0;
						default:
							return ThreadOperter.ERROR;
						}
					}
				}
				return ThreadOperter.SLEEP;
			}
		}, 500, "groupBan");
		
		setupThread(new ThreadMethod() {
	
			@Override
			public ThreadOperter run()
			{
				for(;!groupMemberChangeEventsQueue.isEmpty();)
				{
					GroupMemberChangeEvent event=groupMemberChangeEventsQueue.poll();
					code_0:for (SelfStartMethod selfStartMethod : groupMemberChangeEvents)
					{
						EventResult result=(EventResult) selfStartMethod.startMethod(event);
						switch (result)
						{
						case PASS:
							continue;
						case STOP:
							break code_0;
						default:
							return ThreadOperter.ERROR;
						}
					}
				}
				return ThreadOperter.SLEEP;
			}
		}, 500, "groupMemberChange");
		
		setupThread(new ThreadMethod() {
	
			@Override
			public ThreadOperter run()
			{
				for(;!messageReceiveEventsQueue.isEmpty();)
				{
					MessageReceiveEvent event=messageReceiveEventsQueue.poll();
//					先执行触发器，然后执行命令器
					code_0:for (SelfStartMethod selfStartMethod : messageReceiveEvents)
					{
						EventResult result=(EventResult) selfStartMethod.startMethod(event);
						switch (result)
						{
						case PASS:
							continue;
						case STOP:
							break code_0;
						default:
							return ThreadOperter.ERROR;
						}
					}
				}
				return ThreadOperter.SLEEP;
			}
		}, 100, "messageReceive");
		
		setupThread(new ThreadMethod() {
			
			@Override
			public ThreadOperter run()
			{
				for(;!messageSendEventsQueue.isEmpty();)
				{
					MessageSendEvent event=messageSendEventsQueue.poll();
					code_0:for (SelfStartMethod selfStartMethod : messageSendEvents)
					{
						EventResult result=(EventResult) selfStartMethod.startMethod(event);
						switch (result)
						{
						case PASS:
							continue;
						case STOP:
							break code_0;
						default:
							return ThreadOperter.ERROR;
						}
					}
				}
				return ThreadOperter.SLEEP;
			}
		}, 500, "messageSend");
		
		for (Thread thread : threadPool.values())
		{
			thread.start();
		}
		eventTrigger=this;
	}
	
	public static EventTrigger getEventTrigger()
	{
		return eventTrigger;
	}
	
	private interface ThreadMethod
	{
		ThreadOperter run();
	}
	
	private void setupThread(final ThreadMethod method,final int restTime,final String mark)
	{
		if(threadPool.containsKey(mark))
		{
			Log.e("已经建立"+mark+"线程，但是被重复建立！");
			return;
		}
		else
		{
			threadPool.put(mark, new Thread(){
				boolean flag=true;
				@Override
				public void run()
				{
					code_0:for(;flag;)
					{
						ThreadOperter operter=method.run();
						switch (operter)
						{
						case CONTINUE:
							break;
						case SLEEP:
							try
							{
								sleep(restTime);
							} catch (InterruptedException e)
							{
								Log.e("睡眠强行终止!");
							}
							break;
						case STOP:
							Log.i("线程"+mark+"被终止，来源：内部方法");
							break code_0;
						case ERROR:
							Log.e("线程"+mark+"被终止，来源：内部方法发生错误");
							break code_0;

						default:
							break;
						}
					}
				}
				public void shutdown()
				{
					if(!flag)
					{
						Log.e("线程"+mark+"已经被终止！");
						return;
					}
					flag=false;
					Log.i("线程"+mark+"被终止，来源：外部方法");
				}
			});
		}
	}
	
	public void addFriendAddMethod(SelfStartMethod method,int priority)
	{
		if(paramCheck(method, FriendAddEvent.class))
			friendAddEvents.add(method,priority);
	}
	public void addGroupAddMethod(SelfStartMethod method,int priority)
	{
		if(paramCheck(method, GroupAddEvent.class))
			groupAddEvents.add(method,priority);
	}
	public void addGroupBanMethod(SelfStartMethod method,int priority)
	{
		if(paramCheck(method, GroupBanEvent.class))
			groupBanEvents.add(method,priority);
	}
	public void addGroupMemberChangeMethod(SelfStartMethod method,int priority)
	{
		if(paramCheck(method, GroupMemberChangeEvent.class))
			groupMemberChangeEvents.add(method,priority);
	}
	public void addMessgeReceiveMethod(SelfStartMethod method,int priority)
	{
		if(paramCheck(method, MessageReceiveEvent.class))
			messageReceiveEvents.add(method,priority);
	}
	public void addMessgeSendMethod(SelfStartMethod method,int priority)
	{
		if(paramCheck(method, MessageSendEvent.class))
			messageSendEvents.add(method,priority);
	}
	private boolean paramCheck(SelfStartMethod method,Class<?>...classes)
	{
		Class<?>[] params=method.getParameterTypes();
		if(classes.length!=params.length)
			return false;
		for(int i=0;i<params.length;i++)
		{
			if(params[i]!=classes[i])
			{
				return false;
			}
		}
		return true;
	}
	public boolean friendAdd(FriendAddEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("好友添加请求处理后被拦截，拦截者："+method.getParentName());
				return false;
			}
		}
		return true;
	}
	public boolean groupAdd(GroupAddEvent addEvent)
	{
		for(int i=0;i<groupAddEvents.size();i++)
		{
			SelfStartMethod method=groupAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("群添加请求处理后被拦截，拦截者："+method.getParentName());
				return false;
			}
		}
		return true;
	}
	public boolean groupBan(GroupBanEvent addEvent)
	{
		for(int i=0;i<groupBanEvents.size();i++)
		{
			SelfStartMethod method=groupBanEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("群被禁言请求处理后被拦截，拦截者："+method.getParentName());
				return false;
			}
		}
		return true;
	}
	public boolean groupMemberChange(GroupMemberChangeEvent addEvent)
	{
		for(int i=0;i<groupMemberChangeEvents.size();i++)
		{
			SelfStartMethod method=groupMemberChangeEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("群人数增减请求处理后被拦截，拦截者："+method.getParentName());
				return false;
			}
		}
		return true;
	}
	public boolean messageReceive(MessageReceiveEvent addEvent)
	{
		for(int i=0;i<messageReceiveEvents.size();i++)
		{
			SelfStartMethod method=messageReceiveEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("消息接受请求处理后被拦截，拦截者："+method.getParentName());
				return false;
			}
		}
		return true;
	}
	@Deprecated
	public boolean messageSend(MessageSendEvent addEvent)
	{
		for(int i=0;i<messageSendEvents.size();i++)
		{
			SelfStartMethod method=messageSendEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("消息发出请求处理后被拦截，拦截者："+method.getParentName());
				return false;
			}
		}
		return true;
	}
}
