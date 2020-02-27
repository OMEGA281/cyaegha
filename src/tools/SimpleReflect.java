package tools;

import java.lang.reflect.Method;

public class SimpleReflect
{
	private String className;
	Class<?> clazz;
	Object object;
	
	class UnableGetClassExpression extends Exception
	{
		public UnableGetClassExpression(String string)
		{
			// TODO Auto-generated constructor stub
			super(string);
		}
	}
	
	public SimpleReflect(String className) throws UnableGetClassExpression
	{
		this.className=className;
		try
		{
			clazz=Class.forName(className);
			object=clazz.newInstance();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			throw new UnableGetClassExpression("没有该名称的类");
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			throw new UnableGetClassExpression("该类没有无参构造函数");
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			throw new UnableGetClassExpression("权限不足，无法实例化");
		}
	}
	public void startMethod(String name,Object...objects)
	{
		Method method;
		try
		{
			if(objects.length<1)
				method=clazz.getMethod(name);
			else
			{
				Class<?>[] types=new Class[objects.length];
				for (int i=0;i<types.length;i++)
				{
					types[i]=objects[i].getClass();
				}
				
				method=clazz.getMethod(name, types);
			}
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
