package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	 * 
	 * @param inputStream 输入流
	 * @throws JDOMException 文件不符合XML规范
	 * @throws IOException   文件不存在
	 */
	public static Document getDocument(File file) throws JDOMException, IOException
	{
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = saxBuilder.build(file);
		return document;
	}

	/**
	 * 通过地址来读取XML文件
	 * 
	 * @param url   地址
	 * @param creat 如果文件不存在，是否创建一个空XML<br>
	 *              新建的XML文件只有一个root的根元素
	 * @throws JDOMException 文件不符合XML规范
	 * @throws IOException   文件读写错误
	 */
	public static Document getDocument(String url, boolean creat) throws JDOMException, IOException
	{
		// TODO Auto-generated constructor stub
		File file = new File(url);
		if (!file.isDirectory())
		{
			if (!file.exists())
			{
				if (creat)
				{
					try
					{
						FileSimpleIO.createFile(url);
						FileWriter fileWriter = new FileWriter(file);
						Document document = new Document(new Element("root"));
						XMLOutputter outputter = new XMLOutputter(
								Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
						outputter.output(document, fileWriter);
						fileWriter.close();
						return document;
					} catch (IOException e)
					{
						throw new JDOMException();
					}
				} else
					throw new FileNotFoundException();
			} else
			{
				return getDocument(file);
			}
		} else
			throw new FileNotFoundException();
	}
}
