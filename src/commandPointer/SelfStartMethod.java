package commandPointer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import surveillance.Log;

public class SelfStartMethod
{
	private Method method;
	private Annotation[] annotations;		
	private Object obj;
	
	public SelfStartMethod(Method method,Object object)
	{
		obj=object;
		annotations=method.getAnnotations();
	}
	public <T> T getAnnotation(Class<T> type) 
	{
		for (Annotation annotation : annotations)
		{
			if(annotation.getClass()==type)
			{
				return (T) annotation;
			}
		}
		return null;
	}
	
	public Object startMethod(Object...objects)
	{
		if(obj!=null)
		{
			Log.e("实例为空！");
			return null;
		}
		try
		{
			return method.invoke(obj, objects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			Log.e("无法启动方法");
			return null;
		}
	}
	public String getName()
	{
		return method.getName();
	}
	public Class<?>[] getParameterTypes()
	{
		return method.getParameterTypes();
	}
	public String getParentName()
	{
		return obj.getClass().getName();
	}
}
