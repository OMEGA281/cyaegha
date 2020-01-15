package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLDocument 
{
	/**
	 * 通过输入流来读取XML文件
	 * @param inputStream 输入流
	 * @throws JDOMException 文件不符合XML规范
	 * @throws IOException 文件不存在
	 */
	public static Document getDocument(InputStream inputStream) throws JDOMException, IOException
	{
		SAXBuilder saxBuilder=new SAXBuilder();
		Document document=saxBuilder.build(inputStream);
		return document;
	}
	/**
	 * 通过地址来读取XML文件
	 * @param url 地址
	 * @param creat 如果文件不存在，是否创建一个空XML
	 * @throws JDOMException 文件不符合XML规范
	 * @throws IOException 文件不存在
	 */
	public static Document getDocument(String url,boolean creat) throws JDOMException, IOException
	{
		// TODO Auto-generated constructor stub
		File file=new File(url);
		if(!file.exists()||file.isDirectory())
		{
			if(creat)
			{
				try 
				{
					file.createNewFile();
					FileWriter fileWriter=new FileWriter(file);
					Document document=new Document(new Element("root"));
					XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
					outputter.output(document, fileWriter);
					fileWriter.close();
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				throw new FileNotFoundException();
		}
		InputStream inputStream = null;
		inputStream=new FileInputStream(file);
		return getDocument(inputStream);
	}
}
