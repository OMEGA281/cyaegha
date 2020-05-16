package commandPointer;

import java.lang.reflect.Method;
import java.util.ArrayList;

import commandPointer.annotations.AuxiliaryClass;
import commandPointer.annotations.RegistCommand;
import commandPointer.annotations.RegistListener.FriendAddListener;
import commandPointer.annotations.RegistListener.GroupAddListener;
import commandPointer.annotations.RegistListener.GroupBanListener;
import commandPointer.annotations.RegistListener.GroupMemberChangeListener;
import commandPointer.annotations.RegistListener.MessageReceiveListener;
import commandPointer.annotations.RegistListener.MessageSendListener;
import surveillance.Log;
import transceiver.EventTrigger;

public class ClassLoader
{
	Class<?> clazz;
	Object object;
	ArrayList<SelfStartMethod> arrayList=new ArrayList<>();
	
	
	public ClassLoader(Class<?> clazz)
	{
		this.clazz=clazz;
		if(clazz.getAnnotation(AuxiliaryClass.class)!=null)
			return;
		try
		{
			object=clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			Log.e("无法实例化类"+clazz);
		}
		Method[] methods=clazz.getMethods();
		for (Method method : methods)
		{
			arrayList.add(new SelfStartMethod(method,object));
		}
	}
	/**
	 * 载入所有的方法，将该类中的所有方法重新注册一遍<br>
	 * 注意：本方法不会清楚之前的方法，多次注册会重复
	 */
	public void reloadClass()
	{
		for (SelfStartMethod selfStartMethod : arrayList)
		{
			{
				FriendAddListener listener=selfStartMethod.getAnnotation(FriendAddListener.class);
				if(listener!=null)
					EventTrigger.getEventTrigger().addFriendAddMethod(selfStartMethod, listener.priority());
			}
			{
				GroupAddListener listener=selfStartMethod.getAnnotation(GroupAddListener.class);
				if(listener!=null)
					EventTrigger.getEventTrigger().addGroupAddMethod(selfStartMethod, listener.priority());
			}
			{
				GroupBanListener listener=selfStartMethod.getAnnotation(GroupBanListener.class);
				if(listener!=null)
					EventTrigger.getEventTrigger().addGroupBanMethod(selfStartMethod, listener.priority());
			}
			{
				GroupMemberChangeListener listener=selfStartMethod.getAnnotation(GroupMemberChangeListener.class);
				if(listener!=null)
					EventTrigger.getEventTrigger().addGroupMemberChangeMethod(selfStartMethod, listener.priority());
			}
			{
				MessageReceiveListener listener=selfStartMethod.getAnnotation(MessageReceiveListener.class);
				if(listener!=null)
					EventTrigger.getEventTrigger().addMessgeReceiveMethod(selfStartMethod, listener.priority());
			}
			{
				MessageSendListener listener=selfStartMethod.getAnnotation(MessageSendListener.class);
				if(listener!=null)
					EventTrigger.getEventTrigger().addMessgeSendMethod(selfStartMethod, listener.priority());
			}
			{
				RegistCommand command=selfStartMethod.getAnnotation(RegistCommand.class);
				if(command!=null)
					CommandControler.getCommandControler().addCommand(selfStartMethod, command);
			}
		}
	}
}
