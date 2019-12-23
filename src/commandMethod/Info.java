package commandMethod;

import java.util.HashMap;
import java.util.Map;

public class Info
{
	public static final String[] IGNORE_CLASS={"Father","Info"};
	/**数据查询失败时返回
	 * {@link Info#DataReturn NULLCLASS}
	 * {@link Info#DataReturn NULLKEY}*/
	public enum DataReturn{/**无目标类*/NULLCLASS,/**无目标条目*/NULLKEY;};
	
	public static Map<String,Map<String,String>> methodData=new HashMap<String, Map<String,String>>();
	/**获取数据表中的数据
	 * @param className 目标方法名
	 * @param key 条目
	 * @param value 值
	 * @return 数据值String，若未寻找到则返回{@link DataReturn}中的值*/
	protected static String getData(String className,String key,String value)
	{
		if(methodData.containsKey(className))
		{
			if(methodData.get(className).containsKey(key))
			{
				return methodData.get(className).get(key);
			}
			return DataReturn.NULLKEY.name();
		}
		return DataReturn.NULLCLASS.name();
	}
	/**设置数据表中的数据
	 * @param className 目标方法名
	 * @param key 条目
	 * @param value 值*/
	protected static void setData(String className,String key,String value) 
	{
		if(!methodData.containsKey(className))
		{
			Map<String, String> map=new HashMap<String,String>();
			methodData.put(className, map);
		}
		methodData.get(className).put(key, value);
	}
	/**检测是否该类被忽略加载
	 * @param className 全类名*/
	public static boolean ifIgnore(String className)
	{
		String[] s=className.split("\\.");
		String name=s[s.length-1];
		for (String string : IGNORE_CLASS) {
			if(name.equals(string))
				return true;
		}
		return false;
	}
}

