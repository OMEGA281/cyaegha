package commandMethod.dataExchanger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import surveillance.Log;
import tools.XMLDocument;

class Exchanger 
{
	private Document document;
	private Element rootElement;
	private Element itemElement;
	private static final String itemElementName="item";
	private static final String listElementName="list";
	private Element listElement;
	private String path;
	/**
	 * 初始化一个交换器，如果不存在文件则会新建
	 * @param path
	 */
	protected Exchanger(String path) 
	{
		// TODO Auto-generated constructor stub
		try 
		{
			this.path=path;
			document=XMLDocument.getDocument(path,true);
		} 
		catch (JDOMException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.f("读取数据XML失败");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.f("读取数据XML失败");
		}
		rootElement=document.getRootElement();
		itemElement=rootElement.getChild(itemElementName);
		listElement=rootElement.getChild(listElementName);
		if(itemElement==null)
		{
			rootElement.addContent(new Element(itemElementName));
			writeDocument();
			itemElement=rootElement.getChild(itemElementName);
		}
		if (listElement==null) 
		{
			rootElement.addContent(new Element(listElementName));
			writeDocument();
			listElement=rootElement.getChild(listElementName);
		}
	}
	/**
	 * 添加或修改单独的数据<br>
	 * 在有相同名称存在的情况下会修改值
	 * @param name 数据名称
	 * @param text 数据内容
	 */
	protected void addItem(String name,String text)
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
	 * 获取单独的数据
	 * @param name 名称
	 * @return 数据，如果不存在则会返回null
	 */
	protected String getItem(String name)
	{
		Element element=itemElement.getChild(name);
		if(element==null)
		{
			return null;
		}
		return element.getText();
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
	 * 在列表储存元素<br>
	 * 表内的元素允许出现重复<br>
	 * 若不存在表，则会新建一个表
	 * @param listName 列表名称
	 * @param name 表内的名称
	 * @param text 数据内容
	 */
	protected void addListItem(String listName,String name,String text)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
		{
			this.listElement.addContent(new Element(listName));
			listElement=this.listElement.getChild(listName);
		}
		listElement.addContent(new Element(name).setText(text));
	}
	/**
	 * 返回列表中的数值，如果不存在则返回null
	 * @param listName 列表名称
	 * @param Name 项目名称
	 * @return
	 */
	protected ArrayList<String> getListItem(String listName,String Name)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
		{
			return null;
		}
		List<Element> element=listElement.getChildren(Name);
		ArrayList<String> stringList=new ArrayList<>();
		if(element==null)
		{
			return null;
		}
		if(element.size()==0)
		{
			return null;
		}
		for (Element element2 : element) {
			stringList.add(element2.getText());
		}
		return stringList;
	}
	/**
	 * 返回列表中的所有数值，如果不存在则返回null
	 * @param listName 列表名称
	 * @return 索引的第零个是名称，第一个为内容
	 */
	protected ArrayList<String[]> getList(String listName)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
		{
			return null;
		}
		ArrayList<String[]> result=new ArrayList<>();
		List<Element> subElement=listElement.getChildren();
		for (Element element : subElement) {
			result.add(new String[]{element.getName(),element.getText()});
		}
		return result;
	}
	/**
	 * 删除列表中的某个数据
	 * @param listName 列表名称
	 * @param name 表内的名称
	 * @param text 内容
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteListItem(String listName,String name,String text)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
			return false;
		List<Element> arrayList=listElement.getChildren();
		for(int i=0;i<arrayList.size();i++)
		{
			if(arrayList.get(i).getName()==name&&arrayList.get(i).getText()==text)
			{
				return deleteListItem(listName, i);
			}
		}
		return false;
	}
	/**
	 * 删除列表中的某个数据
	 * @param listName 列表名称
	 * @param index 索引
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteListItem(String listName,int index)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
			return false;
		if(listElement.removeContent(index)==null)
		{
			return false;
		}
		return true;
	}
	/**
	 * 删除列表中的某一种数据
	 * @param listName 列表名称
	 * @param itemName 数据名称
	 * @return 返回是否删除了数据
	 */
	protected boolean deleteListItem(String listName,String itemName)
	{
		Element listElement=this.listElement.getChild(listName);
		if(listElement==null)
			return false;
		return listElement.removeChildren(itemName);
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
		XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
		try 
		{
			FileWriter fileWriter=new FileWriter(new File(path));
			outputter.output(document,fileWriter);
			fileWriter.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("数据XML写出失败");
		}
	}
}
