package tools;

import java.util.HashMap;

public class DoubleMap<K,V1,V2>
{
	HashMap<K, V1> value1=new HashMap<>();
	HashMap<K, V2> value2=new HashMap<>();
	/**
	 * 向地图中添加，如果{@code key}存在则修改原数值
	 * 本方法添加的数值是第一个，第二个会以{@code null}填充
	 * @param key 键
	 * @param value 第一个数值
	 * @return 是否已经存在键
	 */
	public boolean putFirst(K key,V1 value)
	{
		value1.put(key, value);
		if(value2.containsKey(key))
		{
			value2.put(key, null);
			return true;
		}
		return false;
	}
	/**
	 * 向地图中添加，如果{@code key}存在则修改原数值
	 * 本方法添加的数值是第二个，第一个会以{@code null}填充
	 * @param key 键
	 * @param value 第一个数值
	 * @return 是否已经存在键
	 */
	public boolean putLast(K key,V2 value)
	{
		value2.put(key, value);
		if(value1.containsKey(key))
		{
			value1.put(key, null);
			return true;
		}
		return false;
	}
	/**
	 * 向地图中添加，如果{@code key}存在则修改原数值
	 * @param key 键
	 * @param value1 第一个数值
	 * @param value2 第二个数值
	 * @return 是否已经存在键
	 */
	public boolean putFirst(K key,V1 value1,V2 value2)
	{
		boolean result=this.value1.containsKey(key);
		this.value1.put(key, value1);
		this.value2.put(key, value2);
		return result;
	}
	/**
	 * 获得第一个值
	 * @param key 键
	 * @return 不存在则返回null
	 */
	public V1 getFirstValue(K key)
	{
		return value1.get(key);
	}
	/**
	 * 获得第二个值
	 * @param key 键
	 * @return 不存在则返回null
	 */
	public V2 getLastValue(K key)
	{
		return value2.get(key);
	}
	/**
	 * 删除第一个值
	 * @param key
	 * @return 删除的值
	 */
	public V1 removeFirstValue(K key)
	{
		if(!value1.containsKey(key))
			return null;
		V1 v1=getFirstValue(key);
		putFirst(key, null);
		if(getFirstValue(key)==null&&getLastValue(key)==null)
		{
			value1.remove(key);
			value2.remove(key);
		}
		return v1;
	}
	/**
	 * 删除第二个值
	 * @param key
	 * @return 删除的值
	 */
	public V2 removeLastValue(K key)
	{
		if(!value2.containsKey(key))
			return null;
		V2 v2=getLastValue(key);
		putLast(key, null);
		if(getFirstValue(key)==null&&getLastValue(key)==null)
		{
			value1.remove(key);
			value2.remove(key);
		}
		return v2;
	}
	public int size()
	{
		return value1.size();
	}
}
