package commandPointer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import connection.CQSender;
import connection.ReceiveMessageType;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;

/**
 * 简单的反射器，残缺版，只能执行响应的功能
 * @author GuoJiaCheng
 *
 */
public class Reflector 
{
	private static Map<String,Reflector> reflectorMap=new HashMap<String, Reflector>();
	Class<?> clazz;
	Object object;
	public Reflector(String className) 
	{
		// TODO Auto-generated constructor stub
//		System.out.println(className);
		try
		{
			clazz=Class.forName(className);
			object=clazz.newInstance();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void call(String methodName,Object...pramas)
	{
		Method method;
		try
		{
			if(pramas.length<1)
				method=clazz.getMethod(methodName);
			else
			{
				Class<?>[] types=new Class[pramas.length];
				for (int i=0;i<types.length;i++)
				{
					types[i]=pramas[i].getClass();
				}
				
				method=clazz.getMethod(methodName, types);
			}
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try
		{
			if(pramas.length<1)
				method.invoke(object);
			else
				method.invoke(object, pramas);
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * 获取全部的注解
	 * @param methodName 方法的名称
	 * @param hasPramas 是否具有参数
	 * @return
	 */
	public Annotation[] getAnnotation(String methodName,boolean hasPramas)
	{
		Method method;
		try
		{
			if(!hasPramas)
					method=clazz.getMethod(methodName);
			else
			{
				method=clazz.getMethod(methodName, ArrayList.class);
			}
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Annotation[] annotations=method.getDeclaredAnnotations();
		return annotations;
	}
	/**
	 * 获取注解
	 * @param <T>注解的类型
	 * @param methodName 方法名
	 * @param annotationClass 注解类型
	 * @param hasPramas 是否存在参数
	 * @return 需要的注解，如果不存在则会返回null
	 */
	public <T extends Annotation> T getAnnotation(String methodName,Class<T> annotationClass,boolean hasPramas)
	{
		Method method;
		try
		{
			if(!hasPramas)
					method=clazz.getMethod(methodName);
			else
			{
				method=clazz.getMethod(methodName, ArrayList.class);
			}
		} catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return method.getAnnotation(annotationClass);
	}
	/**
	 * 获得类的注解
	 * @param <T> 注解类型
	 * @param annotationClass 注解类型
	 * @return 查询的注解，如果不存在则返回null
	 */
	public <T extends Annotation> T getClassAnnotation(Class<T> annotationClass)
	{
		return clazz.getAnnotation(annotationClass);
	}
	
	private AuthirizerUser getMethodAuthirizer(String string,boolean hasPramas)
	{
		MinimumAuthority minimumAuthority=(MinimumAuthority) getAnnotation(string, MinimumAuthority.class, hasPramas);
		if(minimumAuthority==null)
			return AuthirizerUser.GROUP_MEMBER;
		return minimumAuthority.authirizerUser();
	}
	
	public static Reflector getReflector(String className)
	{
		if(!reflectorMap.containsKey(className))
		{
			reflectorMap.put(className, new Reflector(className));
		}
		return reflectorMap.get(className);
	}
	/**启动本反射器的对应方法
	 * @param methodName 方法名字
	 * @param receiveMessageType 来源信息包，用于回复
	 * @param params 参数们
	 * */
	public void startMethod(String methodName, ReceiveMessageType receiveMessageType, ArrayList<String> params)
	{
		call("setReceiveMessageType",receiveMessageType);
		
		AuthirizerUser user=CQSender.getAuthirizer(receiveMessageType);
		boolean hasPramas=params.isEmpty()?false:true;
		
		if(getMethodAuthirizer(methodName, hasPramas).ifAccessible(user))
		{
			if(!hasPramas)
				call(methodName);
			else
				call(methodName, params);
		}
	}
	/**启动本反射器的对应方法(无参)
	 * @param methodName 方法名字
	 * @param receiveMessageType 来源信息包，用于回复
	 * */
	public void startMethod(String methodName,ReceiveMessageType receiveMessageType)
	{
		call("setReceiveMessageType",receiveMessageType);
		call(methodName);
	}
	/**专门启动自定类的初始化方法(无参)*/
	public void startInit()
	{
		call("initialize");
	}
}
