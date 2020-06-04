package pluginHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import surveillance.Log;

public class SelfStartMethod
{
	private Method method;		
	private Object obj;
	
	public SelfStartMethod(Method method,Object object)
	{
		this.method=method;
		obj=object;
	}
	public <T extends Annotation> T getAnnotation(Class<T> type) 
	{
		try
		{
			return method.getAnnotation(type);
		} catch (NullPointerException e)
		{
			return null;
		}
	}
	
	public Object startMethod()
	{
		if(obj!=null)
		{
			Log.e("实例为空！");
			return null;
		}
		try
		{
			return method.invoke(obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			Log.e("无法启动方法");
			return null;
		}
	}
	public Object startMethod(Object...objects)
	{
		if(obj==null)
		{
			Log.e("实例为空！");
			return null;
		}
		try
		{
			return method.invoke(obj, objects);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
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
	public Class<?> getParentClass()
	{
		return obj.getClass();
	}
	public Class<?> getReturnType()
	{
		return method.getReturnType();
	}
}
