package global.authorizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import tools.XMLDocument;

public class CoreAuthirizerList
{
	private static final String SOP="SOP";
	private static final String OP="OP";
	
	Document document;
	String path;
	public CoreAuthirizerList(String path) throws JDOMException, IOException
	{
		this.path=path;
		document=XMLDocument.getDocument(path, true);
	}
	public void setSOP(long l)
	{
		Element rootElement=document.getRootElement();
		Element sopElement=rootElement.getChild(SOP);
		if(sopElement==null)
		{
			sopElement=new Element(SOP);
			rootElement.addContent(sopElement);
		}
		sopElement.setText(Long.toString(l));
		saveDocument();
	}
	public long getSOP()
	{
		Element rootElement=document.getRootElement();
		Element sopElement=rootElement.getChild(SOP);
		if(sopElement==null)
		{
			return -1;
		}
		return Long.parseLong(sopElement.getText());
	}
	public void addOP(long l)
	{
		ArrayList<Long> list=getOPList();
		if(list.contains(l))
			return;
		list.add(l);
		StringBuilder builder=new StringBuilder();
		for (Long long1 : list)
		{
			builder.append(long1+",");
		}
		
		Element rootElement=document.getRootElement();
		Element opElement=rootElement.getChild(OP);
		if(opElement==null)
		{
			opElement=new Element(OP);
			rootElement.addContent(opElement);
		}
		opElement.setText(builder.toString());
		
		saveDocument();
	}
	public void removeOP(long l)
	{
		ArrayList<Long> list=getOPList();
		if(!list.contains(l))
			return;
		list.remove(l);
		StringBuilder builder=new StringBuilder();
		for (Long long1 : list)
		{
			builder.append(long1+",");
		}
		
		Element rootElement=document.getRootElement();
		Element opElement=rootElement.getChild(OP);
		if(opElement==null)
		{
			opElement=new Element(OP);
			rootElement.addContent(opElement);
		}
		opElement.setText(builder.toString());
		
		saveDocument();
	}
	public ArrayList<Long> getOPList()
	{
		Element rootElement=document.getRootElement();
		Element opElement=rootElement.getChild(OP);
		ArrayList<Long> arrayList=new ArrayList<Long>();
		if(opElement==null)
		{
			return arrayList;
		}
		String string=opElement.getText();
		if(string.isEmpty())
			return arrayList;
		for (String snum : string.split(","))
		{
			arrayList.add(Long.parseLong(snum));
		}
		return arrayList;
	}
	public boolean isOP(long l)
	{
		ArrayList<Long> arrayList=getOPList();
		return arrayList.contains(l);
	}
	public boolean isSOP(long l)
	{
		return getSOP()==l;
	}
	/**
	 * 保存本文件
	 */
	private void saveDocument()
	{
		XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
		try
		{
			FileWriter fileWriter=new FileWriter(path);
			outputter.output(document, fileWriter);
			fileWriter.close();
		} catch (IOException e)
		{
			// TODO 提交错误到log
			e.printStackTrace();
		}
	}
}
