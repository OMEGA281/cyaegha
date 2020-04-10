package transceiver;

import java.lang.reflect.Method;
import java.util.ArrayList;

import commandPointer.SelfStartMethod;
import surveillance.Log;
import transceiver.event.Event;
import transceiver.event.FriendAddEvent;
import transceiver.event.GroupAddEvent;
import transceiver.event.GroupBanEvent;
import transceiver.event.GroupMemberChangeEvent;
import transceiver.event.MessageReceiveEvent;
import transceiver.event.MessageSendEvent;

public class EventTrigger
{
	public enum EventResult{PASS,STOP}
	
	EventList<SelfStartMethod> friendAddEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> groupAddEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> groupBanEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> groupMemberChangeEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> messageReceiveEvents=new EventList<SelfStartMethod>();
	EventList<SelfStartMethod> messageSendEvents=new EventList<SelfStartMethod>();
	
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
	private void friendAdd(FriendAddEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("好友添加请求处理后被拦截，拦截者："+method.getParentName());
				break;
			}
		}
	}
	private void groupAdd(GroupAddEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("群添加请求处理后被拦截，拦截者："+method.getParentName());
				break;
			}
		}
	}
	private void groupBan(GroupBanEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("群被禁言请求处理后被拦截，拦截者："+method.getParentName());
				break;
			}
		}
	}
	private void GroupMemberChange(GroupMemberChangeEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("群人数增减请求处理后被拦截，拦截者："+method.getParentName());
				break;
			}
		}
	}
	private void MessageReceive(MessageReceiveEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("消息接受请求处理后被拦截，拦截者："+method.getParentName());
				break;
			}
		}
	}
	@Deprecated
	private void MessageSend(MessageSendEvent addEvent)
	{
		for(int i=0;i<friendAddEvents.size();i++)
		{
			SelfStartMethod method=friendAddEvents.get(i);
			EventResult result=(EventResult)method.startMethod(addEvent);
			if(result==EventResult.STOP)
			{
				Log.d("消息发出请求处理后被拦截，拦截者："+method.getParentName());
				break;
			}
		}
	}
}
