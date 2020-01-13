package commandMethod.dataExchanger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import surveillance.Log;
import tools.XMLWriter;

class Exchanger 
{
	private Document document;
	private Element rootElement;
	private Element itemElement;
	private static final String itemElementName="item";
	private static final String listElementName="list";
	private Element listElement;
	private FileWriter fileWriter;
	protected Exchanger(String path) 
	{
		// TODO Auto-generated constructor stub
		try 
		{
			document=new XMLWriter(path).getDocument();
			fileWriter=new FileWriter(new File(path));
		} 
		catch (JDOMException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.f("创建数据XML失败");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.f("创建数据XML失败");
		}
		rootElement=document.getRootElement();
		itemElement=rootElement.getChild(itemElementName);
		listElement=rootElement.getChild(listElementName);
	}
	/**
	 * 添加或修改单独的数据<br>
	 * 在有相同名称存在的情况下会修改值
	 * @param name 数据名称
	 * @param text 数据内容
	 */
	protected void setItem(String name,String text)
	{
		Element element=itemElement.getChild(name);
		if(element==null)
		{
			element= new Element(name);
			element.setText(text);
			itemElement.addContent(element);
		}
		else
		{
			element.setText(text);
		}
	}
	/**
	 * 删除单独的数据
	 * 如果不存在则不会操作
	 * @param name 数据名称
	 * @return 返回是否进行了删除
	 */
	protected boolean deleteItem(String name)
	{
		return itemElement.removeChild(name);
	}
	/**
	 * 在列表中可以存在重复的元素<br>
	 * 若不存在表，则会新建一个表
	 * @param listName 列表名称
	 * @param name 表内的名称
	 * @param text 数据内容
	 */
	protected void setListItem(String listName,String name,String text)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
		{
			this.listElement.addContent(new Element(listName));
		}
		listElement.addContent(new Element(name).setText(text));
	}
	/**
	 * 删除列表中的某个数据
	 * @param listName 列表名称
	 * @param name 表内的名称
	 * @param text 数据内容
	 * @param ifAll 是否删除所有的相同的数据
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteListItem(String listName,String name,String text,boolean ifAll)
	{
		if(ifAll)
		{
			int deleteNum=0;
			while(deleteListItem(listName, name, text, false))
			{
				deleteNum++;
			}
			if(deleteNum>0)
			{
				return true;
			}
			return false;
		}
		Element listElement=this.itemElement.getChild(listName);
		int index=0;
		for(List<Element> list=listElement.getChildren();index<list.size();index++)
		{
			Element element=list.get(index);
			if(element.getName().equals(name)&&element.getText().equals(text))
			{
				listElement.removeContent(index);
				
				return true;
			}
		}
		return false;
	}
	/**
	 * 删除列表中一整类的数据
	 * @param listName 列表名
	 * @param typeName 类名
	 * @return 返回是否执行了删除
	 */
	protected boolean deleteListType(String listName,String typeName)
	{
		Element listElement=this.listElement.getChild(listName);
		return listElement.removeChildren(typeName);
	}
	/**
	 * 删除整个列表
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
		XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8"));
		try 
		{
			outputter.output(document,fileWriter);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("数据XML写出失败");
		}
	}
}
