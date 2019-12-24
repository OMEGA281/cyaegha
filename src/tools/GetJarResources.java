package tools;

import java.io.InputStream;

import global.ConstantTable;
import global.xmlProcessor.XMLInputStream;
import surveillance.Log;

public class GetJarResources 
{
	/**获取包内的文件<br>
	 * @param FileName 资源文件下的文件名称
	 * @return 返回的是一个XMLInputStream，内含标记及流
	 * */
	public static XMLInputStream getJarResources(String FileName)
	{
		InputStream inputStream=GetJarResources.class.getResourceAsStream(ConstantTable.PATH_JARRESOURCES+FileName);
		if(inputStream==null)
		{
			Log.e(FileName,"不存在");
			return null;
		}
		return new XMLInputStream(FileName,inputStream);
	}
}
