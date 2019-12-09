package global;

import java.io.InputStream;

import global.xmlProcessor.XMLInputStream;

public class FindJarResources 
{
	static FindJarResources findJarResources;
	String resourcesPath;
	public FindJarResources() 
	{
		// TODO Auto-generated constructor stub
		resourcesPath=ConstantTable.PATH_JARRESOURCES;
	}
	public static FindJarResources getFindJarResources()
	{
		if (findJarResources==null) 
		{
			findJarResources=new FindJarResources();
		}
		return findJarResources;
	}
	public XMLInputStream getJarResources(String FileName)
	{
		InputStream inputStream=this.getClass().getResourceAsStream(resourcesPath+FileName);
		return new XMLInputStream(FileName,inputStream);
	}
}
