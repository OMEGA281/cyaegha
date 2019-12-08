package global;

import java.io.IOException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLReader 
{
	static Map<String,XMLReader> mapper=new HashMap<String,XMLReader>();
	Document document;
	XMLReader(String url) 
	{
		// TODO Auto-generated constructor stub
		SAXBuilder saxBuilder=new SAXBuilder();
		try {
			document=saxBuilder.build(url);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Document getDocument()
	{
		return document;
	}
	public static XMLReader getXMLReader(String url)
	{
		if(!mapper.containsKey(url))
		{
			mapper.put(url, new XMLReader(url));
		}
		return mapper.get(url);
	}
}
