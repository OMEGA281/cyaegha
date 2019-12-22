package commandPointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joor.Reflect;

import connection.ReceiveMessageType;

public class Reflector 
{
	private static Map<String,Reflector> reflectorMap=new HashMap<String, Reflector>();
	private Reflect reflect;
	public Reflector(String className) 
	{
		// TODO Auto-generated constructor stub
//		System.out.println(className);
		reflect= Reflect.on(className).create();
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
		reflect.call("setReceiveMessageType",receiveMessageType);
		if(params.size()==0)
			reflect.call(methodName);
		else
			reflect.call(methodName, params);
	}
	/**启动本反射器的对应方法(无参)
	 * @param methodName 方法名字
	 * @param receiveMessageType 来源信息包，用于回复
	 * */
	public void startMethod(String methodName,ReceiveMessageType receiveMessageType)
	{
		reflect.call("setReceiveMessageType",receiveMessageType);
		reflect.call(methodName);
	}
	/**专门启动自定类的初始化方法(无参)*/
	public void startMethod()
	{
		reflect.call("initialize");
	}
}
