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
	ArrayList<SelfStartMethod> arrayList=new ArrayList<>();
	
	
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
			arrayList.add(new SelfStartMethod(method,object));
		}
	}
	
	public Object startMethod(String methodName,Object...params)
	{
		for (SelfStartMethod selfMethod : arrayList)
		{
			if(selfMethod.getName().equals(methodName))
			{
				if(params.length!=selfMethod.getParameterTypes().length)
				{
					return selfMethod.startMethod(params);
				}
			}
		}
		return null;
	}
	
	
	
}
