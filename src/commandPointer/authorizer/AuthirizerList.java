package commandPointer.authorizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import surveillance.Log;
import tools.XMLDocument;

public class AuthirizerList
{
	String Text_WhiteList="WhiteList";
	String Text_BlackList="BlackList";
	
	String listPath;
	Document document;
	Element rootElement=document.getRootElement();
	
	ArrayList<Long> WhiteList=new ArrayList<Long>();
	ArrayList<Long> BlackList=new ArrayList<Long>();
	
	public AuthirizerList(String listPath)
	{
		this.listPath=listPath;
		File file=new File(listPath);
		if(!file.exists())
		{
			try
			{
				document=XMLDocument.getDocument(listPath, true);
				document.getRootElement().addContent(new Element(Text_BlackList));
				document.getRootElement().addContent(new Element(Text_WhiteList));
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
			String[] x=rootElement.getChild(Text_WhiteList).getText().split(";");
			for (String string : x)
				WhiteList.add(Long.parseLong(string));
			x=rootElement.getChild(Text_BlackList).getText().split(";");
			for (String string : x)
				BlackList.add(Long.parseLong(string));
		} catch (NumberFormatException e)
		{
			Log.e("在读取权限表的时候出现了无法识别为号码的错误！");
		}
	}
	public void reflash()
	{
		try
		{
			document=XMLDocument.getDocument(listPath, false);
		} catch (JDOMException | IOException e)
		{
			Log.e("在读取权限表的时候出现错误！");
		}
	}
	public boolean isWhite(long l)
	{
		if(WhiteList.contains(l))
			return true;
		return false;
	}
	public boolean isBlack(long l)
	{
		if(BlackList.contains(l))
			return true;
		return false;
	}
}
