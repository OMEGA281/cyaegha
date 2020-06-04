package pluginHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import global.UniversalConstantsTable;
import surveillance.Log;
import tools.XMLDocument;

public class MainAuthirizerList
{
	String Text_SOP="SOP";
	String Text_OP="OP";
	
	long SOP;
	ArrayList<Long> OP=new ArrayList<Long>();
	
	String listPath;
	Document document;
	Element rootElement;
	
	public MainAuthirizerList(String listPath)
	{
		this.listPath=listPath;
		File file=new File(listPath);
		if(!file.exists())
		{
			try
			{
				document=XMLDocument.getDocument(listPath, true);
				rootElement=document.getRootElement();
				rootElement.addContent(new Element(Text_SOP));
				rootElement.addContent(new Element(Text_OP));
				save();
			} catch (JDOMException | IOException e)
			{
				Log.e("在创建新的权限表的时候出现错误！");
				return;
			}
		} 
		else
			try
			{
				document=XMLDocument.getDocument(listPath, false);
			} catch (JDOMException | IOException e)
			{
				Log.e("在读取权限表的时候出现错误！");
			}
		try
		{
			rootElement=document.getRootElement();
			Element x=rootElement.getChild(Text_SOP);
			if(x!=null)
				SOP=Long.parseLong(x.getText());
			x=rootElement.getChild(Text_OP);
			if(x!=null)
			{
				String[] xs=x.getText().split(";");
				for (String string : xs)
					OP.add(Long.parseLong(string));
			}
		} catch (NumberFormatException e)
		{
			Log.e("在读取权限表的时候出现了无法识别为号码的错误！");
		}
	}
	protected void save()
	{
		XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
		try 
		{
			FileWriter fileWriter=new FileWriter(new File(listPath));
			outputter.output(document,fileWriter);
			fileWriter.close();
		} 
		catch (IOException e) 
		{
			Log.e("XML写出失败");
		}
	}
	protected void reflash()
	{
		File file=new File(listPath);
		if(!file.exists())
		{
			Log.e("刷新出错");
			return;
		}
		else
			try
			{
				document=XMLDocument.getDocument(listPath, false);
			} catch (JDOMException | IOException e)
			{
				Log.e("在读取权限表的时候出现错误！");
			}
		try
		{
			Element x=rootElement.getChild(Text_SOP);
			if(x!=null)
				SOP=Long.parseLong(x.getText());
			x=rootElement.getChild(Text_OP);
			if(x!=null)
			{
				String[] xs=x.getText().split(";");
				for (String string : xs)
					OP.add(Long.parseLong(string));
			}
		} catch (NumberFormatException e)
		{
			Log.e("在读取权限表的时候出现了无法识别为号码的错误！");
		}
	}
	protected boolean isSOP(long l)
	{
		if(SOP==l)
			return true;
		return false;
	}
	protected boolean isOP(long l)
	{
		if(OP.contains(l))
			return true;
		return false;
	}
	protected void setSOP(long num)
	{
		SOP=num;
		rootElement.getChild(Text_SOP).setText(Long.toString(num));
		save();
	}
	protected boolean setOP(long num)
	{
		if(SOP==num)
			return false;
		if(!OP.contains(num))
		{
			OP.add(num);
			StringBuilder s=new StringBuilder();
			for (Long long1 : OP)
			{
				s.append(long1+";");
			}
			rootElement.getChild(Text_OP).setText(s.toString());
			return true;
		}
		else
			return false;
	}
}
