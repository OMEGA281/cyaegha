package commandMethod.register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import commandPointer.ClassLoader;
import commandPointer.Reflector;
import commandPointer.annotations.AuxiliaryClass;
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
			try
			{
				new ClassLoader(Class.forName(classpath)).reloadClass();
			} catch (ClassNotFoundException e)
			{
				Log.e("没有找到目标的类！出乎意料的错误！");
			}
		}
		
	}
}
