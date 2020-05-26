package commandPointer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

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
				document.getRootElement().addContent(new Element(Text_SOP)).addContent(new Element(Text_SOP));
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
			String x=rootElement.getChild(Text_SOP).getText();
			SOP=Long.parseLong(x);
			String[] xs=rootElement.getChild(Text_OP).getText().split(";");
			for (String string : xs)
				OP.add(Long.parseLong(string));
		} catch (NumberFormatException e)
		{
			Log.e("在读取权限表的时候出现了无法识别为号码的错误！");
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
			String x=rootElement.getChild(Text_SOP).getText();
			SOP=Long.parseLong(x);
			String[] xs=rootElement.getChild(Text_OP).getText().split(";");
			for (String string : xs)
				OP.add(Long.parseLong(string));
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
}
