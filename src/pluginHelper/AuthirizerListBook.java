package pluginHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import pluginHelper.annotations.AuthirizerListNeed;
import pluginHelper.annotations.MinimumAuthority;
import connection.CQSender;
import global.UniversalConstantsTable;
import surveillance.Log;
import transceiver.IdentitySymbol.SourceType;

public class AuthirizerListBook
{
	static AuthirizerListBook authirizerListBook;

	MainAuthirizerList mainAuthirizerList;
	HashMap<String, AuthirizerList> map = new HashMap<>();
	HashMap<String, String> classListMap = new HashMap<>();

	public static AuthirizerListBook getAuthirizerListBook()
	{
		if (authirizerListBook == null)
			authirizerListBook = new AuthirizerListBook();
		return authirizerListBook;
	}

	public AuthirizerListBook()
	{
		File pfile = new File(UniversalConstantsTable.PLUGIN_AUTHORITYPATH);
		File[] files = pfile.listFiles();
		for (File file : files)
			if (file.isFile() && file.getName().endsWith(".xml"))
				if (file.getName().equals("MAIN.xml"))
					mainAuthirizerList = new MainAuthirizerList(file.getAbsolutePath());
				else
					map.put(file.getName().split("\\.")[0], new AuthirizerList(file.getAbsolutePath()));
		if (mainAuthirizerList == null)
			mainAuthirizerList = new MainAuthirizerList(pfile.getAbsolutePath() + "\\MAIN.xml");

		authirizerListBook = this;
	}

	protected void connectClassWithAuthirizerList(String className, String authirizerListName)
	{
		if (classListMap.containsKey(className))
			Log.e("已存在该类，将刷新");
		classListMap.put(className, authirizerListName);
		if (map.containsKey(authirizerListName))
			map.get(authirizerListName).reflash();
		else
			map.put(authirizerListName, new AuthirizerList(UniversalConstantsTable.PLUGIN_AUTHORITYPATH
					+ authirizerListName.substring(authirizerListName.lastIndexOf('.') + 1) + ".xml"));
	}

	protected boolean createNewAuthirizerList(String name)
	{
		if (map.containsKey(name))
			return false;
		else
		{
			String file = UniversalConstantsTable.PLUGIN_AUTHORITYPATH + name + ".xml";
			AuthirizerList authirizerList = new AuthirizerList(file);
			map.put(name, authirizerList);
			return true;
		}
	}

	private static AuthirizerUser getNormalAuthirizerLevel(SourceType type, long groupNum, long num)
	{
		if (num == getSOP())
			return AuthirizerUser.SUPER_OP;
		if (isOP(num))
			return AuthirizerUser.OP;
		switch (type)
		{
//		FIXME:这里后来要加上避免不存在的人或群的查询
		case PERSON:
			return AuthirizerUser.PERSON_CLIENT;
		case GROUP:
			switch (CQSender.getQQInfoInGroup(num, groupNum).getAuthority())
			{
			case OWNER:
				return AuthirizerUser.GROUP_OWNER;
			case ADMIN:
				return AuthirizerUser.GROUP_MANAGER;
			case MEMBER:
				return AuthirizerUser.GROUP_MEMBER;

			default:
				return AuthirizerUser.ALL;
			}
		case DISCUSS:
			return AuthirizerUser.DISCUSS_MEMBER;
		default:
			return AuthirizerUser.ALL;
		}
	}

	/**
	 * 检测指定权限表上是否是白名单
	 * 
	 * @param num 号码
	 * @return
	 */
	public boolean isWhite(String listName, long num)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return false;
		}
		return authirizerList.isWhite(num);
	}

	/**
	 * 检测指定权限表上是否是黑名单
	 * 
	 * @param num 号码
	 * @return
	 */
	public boolean isBlack(String listName, long num)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return false;
		}
		return authirizerList.isBlack(num);
	}

	/**
	 * 设置白名单
	 * 
	 * @param num 号码
	 * @return
	 */
	public boolean setWhite(String listName, long num)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return false;
		}
		return authirizerList.setWhite(num);
	}

	/**
	 * 设置黑名单
	 * 
	 * @param num 号码
	 * @return
	 */
	public boolean setBlack(String listName, long num)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return false;
		}
		return authirizerList.setBlack(num);
	}

	/**
	 * 去除白名单
	 * 
	 * @param num 号码
	 * @return
	 */
	public boolean removeWhite(String listName, long num)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return false;
		}
		return authirizerList.setBlack(num);
	}

	/**
	 * 去除黑名单
	 * 
	 * @param num 号码
	 * @return
	 */
	public boolean removeBlack(String listName, long num)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return false;
		}
		return authirizerList.deleteBlack(num);
	}

	/**
	 * 获得所有白名单号码
	 * 
	 * @param num 号码
	 * @return
	 */
	public long[] getAllWhite(String listName)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return new long[0];
		}
		return authirizerList.getAllWhite();
	}

	/**
	 * 获得所有黑名单号码
	 * 
	 * @param num 号码
	 * @return
	 */
	public long[] getAllBlack(String listName)
	{
		AuthirizerList authirizerList = map.get(listName);
		if (authirizerList == null)
		{
			Log.e("请求了不存在的权限表：" + listName);
			return new long[0];
		}
		return authirizerList.getAllBlack();
	}

	public boolean isAccessible(MinimumAuthority authority, SourceType type, long groupNum, long num)
	{
		if (authority == null)
		{
			AuthirizerUser user = getNormalAuthirizerLevel(type, groupNum, num);
//			FIXME:这里后来要加上受到默认的影响的方式
			return AuthirizerUser.GROUP_MEMBER.ifAccessible(user);
		} else
		{
			AuthirizerUser askLevel = authority.value();
			AuthirizerUser user = getNormalAuthirizerLevel(type, groupNum, num);
			return askLevel.ifAccessible(user);
		}
	}

	public boolean isAccessible(AuthirizerListNeed authirizer, long num, Class<?> clazz)
	{
		if (authirizer == null)
			return true;
		else
		{
			String listName = authirizer.AuthirizerList();
			if (listName.isEmpty())
			{
				listName = classListMap.get(clazz.getName());
			}
			if (listName == null)
			{
				Log.e("找不到该类所在的权限表！");
				return false;
			}
			if (isWhite(listName, num) == true)
				if (authirizer.WhiteList_Accessible() == true)
					return true;
				else
					return false;
			if (isBlack(listName, num) == true)
				if (authirizer.BlackList_Accessible() == true)
					return true;
				else
					return false;
			if (authirizer.Normal_Accessible() == true)
				return true;
			else
				return false;
		}
	}

	public static long getSOP()
	{
		return authirizerListBook.mainAuthirizerList.SOP;
	}

	protected static void setSOP(long num)
	{
		authirizerListBook.mainAuthirizerList.setSOP(num);
	}

	public static long[] getOP()
	{
		ArrayList<Long> list = authirizerListBook.mainAuthirizerList.OP;
		long[] OP = new long[list.size()];
		for (int x = 0; x < OP.length; x++)
			OP[x] = list.get(x);
		return OP;
	}

	protected static boolean setOP(long num)
	{
		return authirizerListBook.mainAuthirizerList.setOP(num);
	}

	public static boolean isOP(long num)
	{
		long[] ls = getOP();
		for (long l : ls)
			if (num == l)
				return true;
		return false;
	}
}
