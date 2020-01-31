package global.authorizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jdom2.JDOMException;

import global.UniversalConstantsTable;
import surveillance.Log;

class AuthorizerListGetter 
{
	private static final String AuthirizerListPath=UniversalConstantsTable.ROOTPATH+"AuthirizerList\\";
	private HashMap<String, AuthirizerList> appAuthorizerMap=new HashMap<>();
	/**
	 * 初始化用方法
	 */
	protected static void init()
	{
		
	}
	/**
	 * 加载存在于本地磁盘的权限表<br>
	 * 每次使用本方法都会重新刷新{@code appAuthorizerMap}<br>
	 * 对于超大量的数据时请慎重！<br>
	 * 若仅仅只是刷新某个表内的数据请使用表内的刷新方法！
	 */
	private void loadLocalList()
	{
		appAuthorizerMap.clear();
		Log.i("权限表已清除");
		Log.i("加载权限表中……");
		File listDictory=new File(AuthirizerListPath);
		Log.d("权限表位置位于："+listDictory.getAbsolutePath());
		if (!listDictory.exists())
		{
			listDictory.mkdir();
		}
		File[] lists=listDictory.listFiles();
		for (File file : lists)
		{
			String allName=file.getName();
			String name=allName.substring(0,allName.indexOf('.'));
			AuthirizerList authirizerList;
			try
			{
				authirizerList=new AuthirizerList(file.getPath());
			} catch (JDOMException e)
			{
//				非正常的XML文件
				Log.i("文件"+allName+"不符合XML格式！");
				continue;
			} catch (IOException e)
			{
				//读写文件出错
				Log.e("无法读取文件"+allName);
				continue;
			}
			appAuthorizerMap.put(name, authirizerList);
			Log.i("载入了表："+name);
		}
		Log.i("加载完毕，共有"+appAuthorizerMap.size()+"个表被加载");
	}
}
