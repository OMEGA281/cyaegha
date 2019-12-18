package global.xmlProcessor;

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
	static Map<String,XMLReader> mapper=new HashMap<String,XMLReader>();
	Document document;
	XMLReader(InputStream inputStream)
	{
		setDocument(inputStream);
	}
	XMLReader(String url) 
	{
		// TODO Auto-generated constructor stub
		File file=new File(url);
		InputStream inputStream = null;
		try {
			inputStream=new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDocument(inputStream);
	}
	private void setDocument(InputStream inputStream)
	{
		SAXBuilder saxBuilder=new SAXBuilder();
		try {
			document=saxBuilder.build(inputStream);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获得Document，用于读写
	 * */
	public Document getDocument()
	{
		return document;
	}
	/**用于获得读取XML的reader<br>
	 * 读取包内xml的方法
	 * */
	public static XMLReader getXMLReader(XMLInputStream xmlInputStream)
	{
		if(!mapper.containsKey(xmlInputStream.mark))
		{
			mapper.put(xmlInputStream.mark, new XMLReader(xmlInputStream.inputStream));
		}
		return mapper.get(xmlInputStream.mark);
	}
	/**用于获得读取XML的reader<br>
	 * 读取包外xml的方法
	 * */
	public static XMLReader getXMLReader(String path)
	{
		if(!mapper.containsKey(path))
		{
			mapper.put(path, new XMLReader(path));
		}
		return mapper.get(path);
	}
}
