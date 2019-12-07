package global;

import java.util.HashMap;
import java.util.Map;

import global.xml_correspond_class.FeedbackString;
import global.xml_correspond_class.XMLCorrespondCLass;

public class XMLReader 
{
	static Map<String,XMLReader> XMLReaderMap=new HashMap<String,XMLReader>();
	private XMLReader(XMLCorrespondCLass XmlType) 
	{
		// TODO Auto-generated constructor stub
		
	}
	public static XMLReader getXMLReader(XMLCorrespondCLass xmlCorrespondCLass)
	{
		if(!XMLReaderMap.containsKey(xmlCorrespondCLass.getPoint()))
		{
			XMLReader xmlReader=new XMLReader(xmlCorrespondCLass);
			XMLReaderMap.put(xmlCorrespondCLass.getPoint(), xmlReader);
		}
		return XMLReaderMap.get(xmlCorrespondCLass.getPoint());
	}
}
