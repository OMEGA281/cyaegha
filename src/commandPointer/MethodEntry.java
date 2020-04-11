package commandPointer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import commandPointer.annotations.RegistListener;
import surveillance.Log;
import tools.ClassUtils;

public class MethodEntry
{
	public MethodEntry()
	{
		Set<String> className=getPluginName();
		for (String string : className)
		{
			ClassLoader classLoader;
			try
			{
				classLoader=new ClassLoader(Class.forName(string));
			} catch (ClassNotFoundException e)
			{
				Log.e("出乎意料的错误");
				return;
			}
			for (SelfStartMethod method : classLoader.arrayList)
			{
				
			}
		}
	}
	public Set<String> getPluginName()
	{
		return ClassUtils.getClassName("commandMethod", false);
	}
	public void registFriendAddListener(Method method)
	{
		
	}
}
