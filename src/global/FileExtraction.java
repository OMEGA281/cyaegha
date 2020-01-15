package global;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import tools.FileSimpleIO;
import tools.GetJarResources;
import tools.XMLDocument;

public class FileExtraction 
{
	public static void extractionFile()
	{
		Document document = null;
		try {
			document = XMLDocument.getDocument(
					new GetJarResources("FileList.xml").getJarResources());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(document==null)
		{
			return;
		}
		List<Element> childrenElement=document.getRootElement().getChildren();
		for (Element element : childrenElement) {
			FileSimpleIO fileSimpleIO=new FileSimpleIO(element.getChild("name").getText());
		}
	}
}
