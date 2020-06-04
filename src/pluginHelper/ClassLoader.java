package pluginHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;

import pluginHelper.annotations.AuxiliaryClass;
import pluginHelper.annotations.RegistCommand;
import pluginHelper.annotations.UseAuthirizerList;
import pluginHelper.annotations.RegistListener.FriendAddListener;
import pluginHelper.annotations.RegistListener.GroupAddListener;
import pluginHelper.annotations.RegistListener.GroupBanListener;
import pluginHelper.annotations.RegistListener.GroupMemberChangeListener;
import pluginHelper.annotations.RegistListener.MessageReceiveListener;
import pluginHelper.annotations.RegistListener.MessageSendListener;
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
		UseAuthirizerList authirizerList=clazz.getAnnotation(UseAuthirizerList.class);
		String defaultAuthirizerList;
		if(authirizerList==null)
			defaultAuthirizerList=clazz.getName();
		else
			defaultAuthirizerList=authirizerList.AuthirizerListName();
		AuthirizerListBook.getAuthirizerListBook().addNewAuthirizerList(clazz.getName(), defaultAuthirizerList);
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
