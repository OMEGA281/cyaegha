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

import surveillance.Log;
import tools.XMLDocument;

public class AuthirizerList
{
	String Text_WhiteList = "WhiteList";
	String Text_BlackList = "BlackList";

	String listPath;
	Document document;
	Element rootElement;

	ArrayList<Long> WhiteList = new ArrayList<Long>();
	ArrayList<Long> BlackList = new ArrayList<Long>();

	public AuthirizerList(String listPath)
	{
		this.listPath = listPath;
		File file = new File(listPath);
		if (!file.exists())
		{
			try
			{
				document = XMLDocument.getDocument(listPath, true);
				rootElement = document.getRootElement();
				document.getRootElement().addContent(new Element(Text_BlackList));
				document.getRootElement().addContent(new Element(Text_WhiteList));
				save();
			} catch (JDOMException | IOException e)
			{
				Log.e("在创建新的权限表的时候出现错误！");
				return;
			}
		} else
			try
			{
				document = XMLDocument.getDocument(listPath, false);
				rootElement = document.getRootElement();
			} catch (JDOMException | IOException e)
			{
				Log.e("在读取权限表的时候出现错误！");
			}
		try
		{
			{
				Element e = rootElement.getChild(Text_WhiteList);
				if (e != null)
				{
					if (!e.getText().isEmpty())
					{
						String[] x = e.getText().split(";");
						for (String string : x)
							WhiteList.add(Long.parseLong(string));
					}
				}
			}
			{
				Element e = rootElement.getChild(Text_BlackList);
				if (e != null)
				{
					if (!e.getText().isEmpty())
					{
						String[] x = e.getText().split(";");
						for (String string : x)
							BlackList.add(Long.parseLong(string));
					}
				}
			}
		} catch (NumberFormatException e)
		{
			Log.e("在读取权限表的时候出现了无法识别为号码的错误！");
		}
	}

	public void save()
	{
		StringBuilder ws = new StringBuilder();
		for (Long long1 : WhiteList)
			ws.append(long1.toString() + ";");
		rootElement.getChild(Text_WhiteList).setText(ws.toString());
		StringBuilder bs = new StringBuilder();
		for (Long long1 : BlackList)
			bs.append(long1.toString() + ";");
		rootElement.getChild(Text_BlackList).setText(bs.toString());

		XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
		try
		{
			FileWriter fileWriter = new FileWriter(new File(listPath));
			outputter.output(document, fileWriter);
			fileWriter.close();
		} catch (IOException e)
		{
			Log.e("XML写出失败");
		}
	}

	public void reflash()
	{
		try
		{
			document = XMLDocument.getDocument(listPath, false);
		} catch (JDOMException | IOException e)
		{
			Log.e("在读取权限表的时候出现错误！");
		}
		try
		{
			Element e = rootElement.getChild(Text_WhiteList);
			if (e != null)
				if (!e.getText().isEmpty())
				{
					String[] x = e.getText().split(";");
					for (String string : x)
						WhiteList.add(Long.parseLong(string));
				}
			e = rootElement.getChild(Text_BlackList);
			if (e != null)
				if (!e.getText().isEmpty())
				{
					String[] x = e.getText().split(";");
					for (String string : x)
						BlackList.add(Long.parseLong(string));
				}
		} catch (NumberFormatException e)
		{
			Log.e("在读取权限表的时候出现了无法识别为号码的错误！");
		}
	}

	protected boolean isWhite(long l)
	{
		if (WhiteList.contains(l))
			return true;
		return false;
	}

	protected boolean isBlack(long l)
	{
		if (BlackList.contains(l))
			return true;
		return false;
	}

	protected boolean setWhite(long l)
	{
		boolean re = WhiteList.add(l);
		re = re || BlackList.remove(l);
		save();
		return re;
	}

	protected boolean setBlack(long l)
	{
		boolean re = BlackList.add(l);
		re = re || WhiteList.remove(l);
		save();
		return re;
	}

	protected boolean deleteWhite(long l)
	{
		boolean re = WhiteList.remove(l);
		save();
		return re;
	}

	protected boolean deleteBlack(long l)
	{
		boolean re = BlackList.remove(l);
		save();
		return re;
	}

	protected long[] getAllWhite()
	{
		long[] x = new long[WhiteList.size()];
		for (int i = 0; i < x.length; i++)
			x[i] = WhiteList.get(i);
		return x;
	}

	protected long[] getAllBlack()
	{
		long[] x = new long[BlackList.size()];
		for (int i = 0; i < x.length; i++)
			x[i] = BlackList.get(i);
		return x;
	}
}
