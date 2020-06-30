package plugin.dataExchanger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
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
		private String uri;
		private Namespace namespace;

		ElementType(String string, String uri)
		{
			this.string = string;
			this.uri = uri;
			namespace = Namespace.getNamespace(string, uri);
		}

		@Override
		public String toString()
		{
			return string;
		}

		private boolean equal(String string)
		{
			return string.equals(this.string);
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
		rootElement.setNamespace(ElementType.ITEM.namespace);
		rootElement.setNamespace(ElementType.LIST.namespace);
		rootElement.setNamespace(ElementType.MAP.namespace);
	}

	/**
	 * 添加或修改单独的数据<br>
	 * 在有相同名称存在的情况下会修改值
	 * 
	 * @param name 数据名称
	 * @param text 数据内容
	 */
	protected void setItem(String name, String text)
	{
		Element element = rootElement.getChild(name, ElementType.ITEM.namespace);
		if (element == null)
			element = new Element(name);
		element.setText(text);
	}

	/**
	 * 获取单独的数据
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
	 * 获取全部单独的数据
	 * 
	 * @return 数据，如果不存在则会返回空列表
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

	protected boolean hasItem(String name)
	{
		return rootElement.getChild(name, ElementType.ITEM.namespace) == null ? false : true;
	}

	/**
	 * 删除单独的数据 如果不存在则不会操作
	 * 
	 * @param name 数据名称
	 * @return 返回是否进行了删除
	 */
	protected boolean deleteItem(String name)
	{
		return rootElement.removeChild(name, ElementType.ITEM.namespace);
	}

	protected boolean hasList(String name)
	{
		return rootElement.getChild(name, ElementType.LIST.namespace) == null ? false : true;
	}

	/**
	 * 创建一个表
	 * @param name 表名
	 * @param allowRepetition 是否允许表中存在重复
	 * @return
	 */
	public Element creatList(String name, boolean allowRepetition)
	{
		if (!hasList(name))
			rootElement.addContent(new Element(name, ElementType.LIST.namespace)
					.setAttribute(allowRepetition ? ALLOWREPETITION : FORBIDDENREPETITION));
		return rootElement.getChild(name, ElementType.LIST.namespace);
	}

	/**
	 * 在列表储存元素<br>
	 * 若不存在表，则会新建一个表
	 * 
	 * @param listName        列表名称
	 * @param text            数据内容
	 * @param allowRepetition 如果不存在该表则创建表格，设置为是否是可重复的
	 */
	protected void addList(String listName, String text, boolean allowRepetition)
	{
		Element listElement = rootElement.getChild(listName, ElementType.LIST.namespace);
		if (listElement == null)
			listElement = creatList(listName, allowRepetition);
		if (!allowRepetition)
			if (containList(listName, text))
				return;
		listElement.addContent(new Element(LISTELEMENTNAME).setText(text));
	}
	/**
	 * 在列表储存元素<br>
	 * 若不存在表，则会新建一个表<br>
	 * 表内的元素允许出现重复
	 * 
	 * @param listName        列表名称
	 * @param text            数据内容
	 */
	public void addList(String listName, String text)
	{
		addList(listName, text, true);
	}

	/**
	 * 返回列表中的数值，如果不存在则返回null
	 * 
	 * @param listName 列表名称
	 * @param Name     项目名称
	 * @return
	 */
	protected ArrayList<String> getList(String listName)
	{
		Element listElement = rootElement.getChild(listName, ElementType.LIST.namespace);
		if (listElement == null)
			return null;
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
	public boolean containList(String listName, String text)
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
		if(!containList(listName, text))
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
	 * 删除列表中的某个数据
	 * 
	 * @param listName 列表名称
	 * @param index    索引
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteListItem(String listName, int index)
	{
		Element listElement = this.listElement.getChild(listName);
		if (listElement == null)
			return false;
		List<Element> elements = listElement.getChildren();
		if (elements.size() <= index)
			return false;
		return listElement.removeContent(elements.get(index));
	}

	/**
	 * 删除列表中的某一种数据
	 * 
	 * @param listName 列表名称
	 * @param itemName 数据名称
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteListItem(String listName, String itemName)
	{
		Element listElement = this.listElement.getChild(listName);
		if (listElement == null)
			return false;
		return listElement.removeChildren(itemName);
	}

	/**
	 * 删除整个列表
	 * 
	 * @param listName 列表名
	 * @return 是否进行了删除
	 */
	protected boolean deleteList(String listName)
	{
		return listElement.removeChild(listName);
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
