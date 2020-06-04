package pluginHelper.stringHelper;

import org.jdom2.Document;
import org.jdom2.Element;

public class StringResourcer
{
	private Document document;
	private Element rootElement;
	public StringResourcer(Document document)
	{
		this.document=document;
		rootElement=document.getRootElement();
	}
	public String getString(String name)
	{
		return rootElement.getChildText(name);
	}
}
