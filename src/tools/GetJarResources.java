package tools;

import java.io.IOException;
import java.io.InputStream;

import global.UniversalConstantsTable;
import surveillance.Log;
/**获取包内的文件<br>
 * */
public class GetJarResources 
{
	
	private String FileName;
	/**
	 * @param FileName 资源文件名称
	 * */
	public GetJarResources(String FileName) 
	{
		// TODO Auto-generated constructor stub
		this.FileName=FileName;
	}
	/**获取包内的文件<br>
	 * @return 返回{@link XMLInputStream}
	 * */
	public InputStream getJarResources()
	{
		InputStream inputStream=GetJarResources.class
				.getResourceAsStream(UniversalConstantsTable.PATH_JARRESOURCES+FileName);
		if(inputStream==null)
		{
			Log.e(FileName,"不存在");
			return null;
		}
		return inputStream;
	}
	/**
	 * 检测包内是否存在该文件
	 */
	public boolean exist()
	{
		InputStream inputStream= GetJarResources.class.getResourceAsStream(FileName);
		if (inputStream==null) 
		{
			return false;
		}
		try 
		{
			inputStream.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
