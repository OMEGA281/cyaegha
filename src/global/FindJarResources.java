package global;

import java.io.InputStream;

import global.xmlProcessor.XMLInputStream;
import surveillance.Log;

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
	/**获取包内的文件<br>
	 * @param FileName 资源文件下的文件名称
	 * @return 返回的是一个XMLInputStream，内含标记及流
	 * */
	public XMLInputStream getJarResources(String FileName)
	{
		String path=resourcesPath+FileName;
		InputStream inputStream=FindJarResources.class.getResourceAsStream(path);
		if(inputStream==null)
		{
			Log.e("指向",path,"的文件不存在");
		}
		return new XMLInputStream(FileName,inputStream);
	}
}
