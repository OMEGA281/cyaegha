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
		String path=resourcesPath+FileName;
		InputStream inputStream=FindJarResources.class.getResourceAsStream(path);
		return new XMLInputStream(FileName,inputStream);
	}
}
