package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLReader 
{
	private static Map<String,XMLReader> mapper=new HashMap<String,XMLReader>();
	private Document document;
	
	/**
	 * 含InputStream以及标记储存用的mark
	 * */
	public static class XMLInputStream
	{
		protected String mark;
		protected InputStream inputStream;
		public XMLInputStream() 
		{
			// TODO Auto-generated constructor stub
		}
		public XMLInputStream(String mark,InputStream inputStream)
		{
			this.mark=mark;
			this.inputStream=inputStream;
		}
	}
	
	private XMLReader(InputStream inputStream) throws JDOMException, IOException
	{
		setDocument(inputStream);
	}
	private XMLReader(String url) throws JDOMException, IOException 
	{
		// TODO Auto-generated constructor stub
		File file=new File(url);
		InputStream inputStream = null;
		try 
		{
			inputStream=new FileInputStream(file);
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			throw e;
		}
		setDocument(inputStream);
	}
	private void setDocument(InputStream inputStream) throws JDOMException, IOException
	{
		SAXBuilder saxBuilder=new SAXBuilder();
		try 
		{
			document=saxBuilder.build(inputStream);
		} 
		catch (JDOMException e) 
		{
			// TODO Auto-generated catch block
			throw e;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			throw e;
		}
	}
	/**
	 * 获得Document，用于读写*/
	public Document getDocument()
	{
		return document;
	}
	/**用于获得读取XML的reader<br>
	 * 读取包内xml的方法
	 * @throws IOException 无法读取文件，文件可能不存在
	 * @throws JDOMException 文件不符合XML格式
	 * */
	public static XMLReader getXMLReader(XMLInputStream xmlInputStream) throws JDOMException, IOException
	{
		if(!mapper.containsKey(xmlInputStream.mark))
		{
			mapper.put(xmlInputStream.mark, new XMLReader(xmlInputStream.inputStream));
		}
		return mapper.get(xmlInputStream.mark);
	}
	/**用于获得读取XML的reader<br>
	 * 读取包外xml的方法
	 * @throws IOException 无法读取文件，文件可能不存在
	 * @throws JDOMException 文件不符合XML格式
	 * */
	public static XMLReader getXMLReader(String path) throws JDOMException, IOException
	{
		try
		{
			if(!mapper.containsKey(path)&&new FileSimpleIO(path).exists())
			{
				mapper.put(path, new XMLReader(path));
			}
		}
		catch (NullPointerException e) 
		{
			// TODO: handle exception
			mapper.put(path, new XMLReader(path));
		}
		return mapper.get(path);
	}
}
