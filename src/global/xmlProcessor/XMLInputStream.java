package global.xmlProcessor;

import java.io.InputStream;
/**
 * 将inputstream进行标记，用于指示
 * */
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
