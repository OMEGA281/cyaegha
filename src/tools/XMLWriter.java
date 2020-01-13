package tools;

import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLWriter 
{
	private Document document;
	public XMLWriter(String path) throws JDOMException, IOException
	{
		FileSimpleIO fileSimpleIO=new FileSimpleIO(path);
		// TODO Auto-generated constructor stub
		if(!fileSimpleIO.exists())
		{
			FileSimpleIO.createFile(path);
			FileWriter fileWriter=new FileWriter("path");
			Document document=new Document(new Element("root"));
			XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8"));
			outputter.output(document, fileWriter);
			fileWriter.close();
		}
		SAXBuilder saxBuilder=new SAXBuilder();
		document=saxBuilder.build(path);
	}
	public Document getDocument()
	{
		return document;
	}
}
