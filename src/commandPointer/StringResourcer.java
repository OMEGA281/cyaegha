package commandPointer;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;

import surveillance.Log;
import tools.DoubleMap;
import tools.GetJarResources;
import tools.XMLDocument;

class StringResourcer
{
	/**
	 * 这个是储存着资源字符串的表，<键，包内名称，包外名称>
	 */
	private static DoubleMap<String, String, String> doubleMap=new DoubleMap<>();
	private Document Jar,File;
	/**{@code true}为文件指向*/
	private boolean point;
	/**
	 * 向表中添加条目<br>
	 * 若表中已经有了之前的条目则会修改（若参数填写{@code null}则不会更改）
	 * @param key 类的名称
	 * @param JarResource 包内的名称
	 * @param FileResource 包外的名称
	 */
	protected static void addNewResource(String key,String JarResource,String FileResource)
	{
		if(doubleMap.containKey(key))
		{
			if(JarResource!=null)
				doubleMap.putFirst(key, JarResource);
			if(FileResource!=null)
				doubleMap.putLast(key, FileResource);
		}
		else
			doubleMap.put(key, JarResource, FileResource);
	}
	private StringResourcer(String className) throws Exception
	{
		try
		{
			Jar=XMLDocument.getDocument(new GetJarResources(className).getJarResources());
			File=XMLDocument.getDocument(className, false);
		} catch (JDOMException | IOException e)
		{
			// TODO Auto-generated catch block
			Log.e("无法获得"+className+"的字符串文件");
		}
		if(File==null)
		{
			point=false;
			if(Jar==null)
			{
				Log.e(className+"没有任何的字符串文件可以使用");
				throw new Exception("无法获得任何的字符串文件");
			}
		}
	}
	/**
	 * 调整使用的字符串来源
	 * @param b 真为外部，假为内部
	 */
	public void setStringSource(boolean b)
	{
		if(b)
		{
			if(File!=null)
				point=true;
			else
				Log.e("无法调整为外部模式");
		}
		else
		{
			if(Jar!=null)
				point=false;
			else
				Log.e("无法调整为内部模式");
		}
	}
	
	public String getString(String index)
	{
		if(point)
			return File.getRootElement().getChild(index).getText();
		else
			return File.getRootElement().getChild(index).getText();
	}
}
