package commandMethod.register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import commandMethod.Info;
import commandPointer.Reflector;
import surveillance.Log;
import tools.ClassUtils;

public class Register 
{
	private static Register register;
	public List<OnMessageReceiveListener> messageReceiveListeners=new ArrayList<OnMessageReceiveListener>();
	public List<OnGroupMemberChangeListener> groupMemberChangeListeners=new ArrayList<OnGroupMemberChangeListener>();
	public List<OnMessageSendListener> messageSendListeners=new ArrayList<OnMessageSendListener>();
	public Register() 
	{
		// TODO Auto-generated constructor stub
		if(register==null)
			Log.d("初始化注册器");
			register=this;
	}
	public static Register getRegister()
	{
		return register;
	}
	/**重载所有的类，会重新按照优先度排序*/
	public void reloadAllClass()
	{
		Log.d("重新加载所有类中……");
		Set<String> className=ClassUtils.getClassName("commandMethod", false);
//		System.out.println(className.size());
		for (String classpath : className) 
		{
			if(!Info.ifIgnore(classpath))
			{
//				System.out.println(classpath);
				Reflector.getReflector(classpath).startMethod();
				Log.d("加载类：",classpath);
			}
		}
		Collections.sort(messageReceiveListeners, new Comparator<OnMessageReceiveListener>() {

			@Override
			public int compare(OnMessageReceiveListener o1, OnMessageReceiveListener o2) {
				// TODO Auto-generated method stub
				return o2.priority-o1.priority;
			}
			
		});
		Collections.sort(groupMemberChangeListeners, new Comparator<OnGroupMemberChangeListener>() {

			@Override
			public int compare(OnGroupMemberChangeListener o1, OnGroupMemberChangeListener o2) {
				// TODO Auto-generated method stub
				return o2.priority-o1.priority;
			}
			
		});
		Collections.sort(messageSendListeners, new Comparator<OnMessageSendListener>() {

			@Override
			public int compare(OnMessageSendListener o1, OnMessageSendListener o2) {
				// TODO Auto-generated method stub
				return o2.priority-o1.priority;
			}
			
		});
	}
		
	public void addMessageReceiveListener(OnMessageReceiveListener messageReceiveListener)
	{
		this.messageReceiveListeners.add(messageReceiveListener);
	}
	public void addMessageReceiveListener(List<OnMessageReceiveListener> messageReceiveListeners)
	{
		this.messageReceiveListeners.addAll(messageReceiveListeners);
	}
	public void addGroupMemberChangeListener(OnGroupMemberChangeListener groupMemberChangeListener)
	{
		this.groupMemberChangeListeners.add(groupMemberChangeListener);
	}
	public void addGroupMemberChangeListener(List<OnGroupMemberChangeListener> groupMemberChangeListeners)
	{
		this.groupMemberChangeListeners.addAll(groupMemberChangeListeners);
	}
	public void addMessageSendListener(OnMessageSendListener messageSendListener)
	{
		this.messageSendListeners.add(messageSendListener);
	}
	public void addMessageSendListener(List<OnMessageSendListener> messageSendListeners)
	{
		this.messageSendListeners.addAll(messageSendListeners);
	}
}
