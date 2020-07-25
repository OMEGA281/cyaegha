package plugin.dataExchanger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import surveillance.Log;
import tools.XMLDocument;

class Exchanger
{
	private static final String LISTELEMENTNAME = "r";
	private static final Attribute ALLOWREPETITION = new Attribute("repetition", "true");
	private static final Attribute FORBIDDENREPETITION = new Attribute("repetition", "false");

	private enum ElementType
	{
		ITEM("ITEM", "http://cyaegha.item"), LIST("LIST", "http://cyaegha.list"), MAP("MAP", "http://cyaegha.map");

		private String string;
		private Namespace namespace;

		ElementType(String string, String uri)
		{
			this.string = string;
			namespace = Namespace.getNamespace(string, uri);
		}

		@Override
		public String toString()
		{
			return string;
		}
	}

	private Document document;
	private Element rootElement;
	private String path;

	/**
	 * 初始化一个交换器，如果不存在文件则会新建
	 * 
	 * @param path
	 */
	protected Exchanger(String path)
	{
		// TODO Auto-generated constructor stub
		try
		{
			this.path = path;
			document = XMLDocument.getDocument(path, true);
		} catch (JDOMException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.f("读取数据XML失败");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.f("读取数据XML失败");
		}
		rootElement = document.getRootElement();
	}

	/**
	 * 设置一个向量数据<br>
	 * 在有相同名称存在的情况下会修改值
	 * 
	 * @param name 数据名称
	 * @param text 数据内容
	 */
	protected void setItem(String name, String text)
	{
		Element element = rootElement.getChild(name, ElementType.ITEM.namespace);
		if (element == null)
		{
			element = new Element(name,ElementType.ITEM.namespace);
			rootElement.addContent(element);
		}
		element.setText(text);
	}

	/**
	 * 获取向量数据
	 * 
	 * @param name 名称
	 * @return 数据，如果不存在则会返回null
	 */
	protected String getItem(String name)
	{
		Element element = rootElement.getChild(name, ElementType.ITEM.namespace);
		if (element == null)
			return null;
		return element.getText();
	}

	/**
	 * 获取全部向量数据
	 * 
	 * @return 返回一个HashMap
	 */
	protected HashMap<String, String> getAllItem()
	{
		HashMap<String, String> result = new HashMap<String, String>();
		List<Element> sub = rootElement.getChildren();
		for (Element element : sub)
			if (element.getNamespace().equals(ElementType.ITEM.namespace))
				result.put(element.getName(), element.getText());
		return result;
	}

	/**
	 * 检测是否存在该向量数据
	 * @param name 名称
	 * @return
	 */
	protected boolean hasItem(String name)
	{
		return rootElement.getChild(name, ElementType.ITEM.namespace) == null ? false : true;
	}

	/**
	 * 删除向量数据
	 * 
	 * @param name 数据名称
	 * @return 返回是否进行了删除
	 */
	protected boolean deleteItem(String name)
	{
		return rootElement.removeChild(name, ElementType.ITEM.namespace);
	}

	/**
	 * 检测是否存在列表数据
	 * @param name 名称
	 * @return
	 */
	protected boolean hasList(String name)
	{
		return rootElement.getChild(name, ElementType.LIST.namespace) == null ? false : true;
	}

	/**
	 * 创建一个列表
	 * @param name 表名
	 * @param allowRepetition 是否允许表中存在重复
	 * @return 是否创建成功
	 */
	protected boolean creatList(String name, boolean allowRepetition)
	{
		if (!hasList(name))
		{
			rootElement.addContent(new Element(name, ElementType.LIST.namespace)
					.setAttribute(allowRepetition ? ALLOWREPETITION : FORBIDDENREPETITION));
			return true;
		}
		else
			return false;
	}

	/**
	 * 在列表中储存元素<br>
	 * 若不存在表，则会新建一个表
	 * 
	 * @param listName        列表名称
	 * @param text            数据内容
	 * @param allowRepetition 如果不存在该表则创建表格，设置为是否是可重复的
	 * @return 是否添加成功
	 */
	protected boolean addList(String listName, String text, boolean allowRepetition)
	{
		if(!hasList(listName))
			creatList(listName, allowRepetition);
		Element listElement = rootElement.getChild(listName, ElementType.LIST.namespace);
		if (!allowRepetition)
			if (containInList(listName, text))
				return false;
		listElement.addContent(new Element(LISTELEMENTNAME).setText(text));
		return true;
	}
	/**
	 * 在列表储存元素<br>
	 * 若不存在表，则会新建一个表<br>
	 * 表内的元素默认允许出现重复
	 * 
	 * @param listName        列表名称
	 * @param text            数据内容
	 * @return 是否添加成功
	 */
	protected boolean addList(String listName, String text)
	{
		return addList(listName, text, true);
	}

	/**
	 * 返回列表中的数值，如果不存在则返回null
	 * 
	 * @param listName 列表名称
	 * @param Name     项目名称
	 * @return 要求的列表，不存在该列表则返回null
	 */
	protected ArrayList<String> getList(String listName)
	{
		if(!hasList(listName))
			return null;
		Element listElement = rootElement.getChild(listName, ElementType.LIST.namespace);
		List<Element> elements = listElement.getChildren();
		ArrayList<String> stringList = new ArrayList<>();
		for (Element element : elements)
			stringList.add(element.getText());
		return stringList;
	}

	/**
	 * 检测是否在表中存在该值
	 * 
	 * @param listName 表名
	 * @param text     值
	 * @return
	 */
	protected boolean containInList(String listName, String text)
	{
		if (hasList(listName))
			for (String string : getList(listName))
				if (string.equals(text))
					return true;
		return false;
	}

	/**
	 * 删除列表中的某个数据
	 * 
	 * @param listName 列表名称
	 * @param text     内容
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteList(String listName, String text)
	{
		Element listElement = rootElement.getChild(listName);
		if (listElement == null)
			return false;
		if(!containInList(listName, text))
			return false;
		ArrayList<Element> deleteList=new ArrayList<Element>();
		for (Element element : listElement.getChildren())
			if(element.getText().equals(text))
				deleteList.add(element);
		for (Element element : deleteList)
			listElement.removeContent(element);
		return deleteList.size()!=0;
	}

	/**
	 * 删除整个列表
	 * 
	 * @param listName 列表名
	 * @return 是否进行了删除
	 */
	protected boolean deleteList(String listName)
	{
		return rootElement.removeChild(listName, ElementType.LIST.namespace);
	}
	/**
	 * 是否存在地图
	 * @param name
	 * @return
	 */
	protected boolean hasMap(String name)
	{
		return rootElement.getChild(name, ElementType.MAP.namespace)!=null;
	}
	/**
	 * 创建地图
	 * @param name
	 * @return
	 */
	protected boolean creatMap(String name)
	{
		if(hasMap(name))
			return false;
		rootElement.addContent(new Element(name, ElementType.MAP.namespace));
		return true;
	}
	/**
	 * 获得整个地图
	 * @param name
	 * @return 如果不存在则会返回null
	 */
	protected Map<String,String> getMap(String name)
	{
		Element map=rootElement.getChild(name, ElementType.MAP.namespace);
		if(map==null)
			return null;
		HashMap<String, String> hashMap=new HashMap<>();
		for (Element element : map.getChildren())
			hashMap.put(element.getName(), element.getText());
		return hashMap;
	}
	/**
	 * 获得地图内的一个数据
	 * @param name 地图名称
	 * @param key 键名称
	 * @return 检索的数值，不存在则返回null
	 */
	protected String getMapData(String name,String key)
	{
		Map<String, String> map=getMap(name);
		if(map!=null)
			return map.get(key);
		else
			return null;
	}
	/**
	 * 设置地图的数据，若已经有该数据则会覆盖原数据
	 * @param name 地图名称
	 * @param key 键值
	 * @param text 值
	 */
	protected void setMapData(String name,String key,String text)
	{
		Element map=rootElement.getChild(name, ElementType.MAP.namespace);
		if(map==null)
		{
			creatMap(name);
			map=rootElement.getChild(name, ElementType.MAP.namespace);
		}
		Element sub=map.getChild(key);
		if(sub==null)
		{
			sub=new Element(key).setText(text);
			map.addContent(sub);
		}
		else
			sub.setText(text);
	}
	/**
	 * 删除地图中的一个数据
	 * @param name 地图名称
	 * @param key 键
	 * @return
	 */
	protected boolean deleteMapData(String name,String key)
	{
		Element map=rootElement.getChild(name, ElementType.MAP.namespace);
		if(map==null)
			return false;
		return map.removeChild(name);
	}
	/**
	 * 删除整个地图
	 * @param name 地图名称
	 * @return
	 */
	protected boolean deleteMap(String name)
	{
		return rootElement.removeChild(name,ElementType.MAP.namespace);
	}

	/**
	 * 写出文件
	 */
	protected void writeDocument()
	{
		XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
		try
		{
			FileWriter fileWriter = new FileWriter(new File(path));
			outputter.output(document, fileWriter);
			fileWriter.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("数据XML写出失败");
		}
	}
}
