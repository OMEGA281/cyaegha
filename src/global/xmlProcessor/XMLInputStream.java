package global.xmlProcessor;

import java.io.InputStream;

public class XMLInputStream
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
