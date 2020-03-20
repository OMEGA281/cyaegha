package commandPointer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import surveillance.Log;

public class ClassLoader
{
	Class<?> clazz;
	Object object;
	ArrayList<SelfMethod> arrayList=new ArrayList<>();
	
	
	public ClassLoader(Class<?> clazz)
	{
		this.clazz=clazz;
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
			arrayList.add(new SelfMethod(method));
		}
	}
	
	public Object startMethod(String methodName,Object...params)
	{
		for (SelfMethod selfMethod : arrayList)
		{
			if(selfMethod.method.getName().equals(methodName))
			{
				if(params.length!=selfMethod.method.getParameterTypes().length)
				{
					return selfMethod.startMethod(params);
				}
			}
		}
		return null;
	}
	
	public class SelfMethod
	{
		private Method method;
		private Annotation[] annotations;		
		
		public SelfMethod(Method method)
		{
			annotations=method.getAnnotations();
		}
		public <T extends Annotation> T getAnnotation(Class<T> type) 
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
			if(object!=null)
			{
				Log.e("实例为空！");
				return null;
			}
			try
			{
				return method.invoke(object, objects);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				Log.e("无法启动方法");
				return null;
			}
		}
	}
	
}
