package global;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import global.xmlProcessor.XMLReader;
import tools.FileSimpleIO;
import tools.GetJarResources;

public class FileExtraction 
{
	public static void extractionFile()
	{
		Document document= XMLReader.getXMLReader(GetJarResources.getJarResources("FileList.xml")).getDocument();
		List<Element> childrenElement=document.getRootElement().getChildren();
		for (Element element : childrenElement) {
			FileSimpleIO fileSimpleIO=new FileSimpleIO(element.getChild("name").getText());
		}
	}
}
