package commandPointer.authorizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jdom2.JDOMException;

import global.UniversalConstantsTable;
import surveillance.Log;

public class AuthorizerListGetter 
{
	private static final String AuthirizerListPath=UniversalConstantsTable.ROOTPATH+"AuthirizerList\\";
	private static final String CoreAuthirizerListPath=AuthirizerListPath+"CoreAuthirizerList.xml";
	
	private static HashMap<String, AppAuthirizerList> appAuthorizerMap=new HashMap<>();
	private static CoreAuthirizerList coreAuthirizerList;
	/**
	 * 初始化用方法
	 */
	public static void init()
	{
		new AuthorizerListGetter().loadLocalList();
	}
	/**
	 * 加载存在于本地磁盘的权限表<br>
	 * 每次使用本方法都会重新刷新{@code appAuthorizerMap}<br>
	 * 对于大量的数据时请慎重！<br>
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
			AppAuthirizerList authirizerList;
			try
			{
				authirizerList=new AppAuthirizerList(file.getPath());
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
	public static AppAuthirizerList getAppAuthirizerList(String string)
	{
		return appAuthorizerMap.get(string);
	}
	@Deprecated
	public static void addNewAuthirizerList(String string)
	{
		try
		{
			appAuthorizerMap.put(string, new AppAuthirizerList("test.xml"));
		} catch (JDOMException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static CoreAuthirizerList getCoreAuthirizerList()
	{
		if(coreAuthirizerList==null)
		{
			try
			{
				coreAuthirizerList=new CoreAuthirizerList(CoreAuthirizerListPath);
			} catch (JDOMException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return coreAuthirizerList;
	}
}
