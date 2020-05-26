package commandPointer;

import java.util.HashMap;

import connection.CQSender;
import global.UniversalConstantsTable;
import surveillance.Log;
import transceiver.event.MessageReceiveEvent;

public class AuthirizerListBook
{
	static AuthirizerListBook authirizerListBook;
	
	HashMap<String, AuthirizerList> map=new HashMap<>();
	HashMap<String, String> classListMap=new HashMap<>();
	
	public static AuthirizerListBook getAuthirizerListBook()
	{
		if(authirizerListBook==null)
			authirizerListBook=new AuthirizerListBook();
		return authirizerListBook;
	}
	
	protected void addNewAuthirizerList(String className,String authirizerListName)
	{
		if(classListMap.containsKey(className))
			Log.e("已存在该类，将刷新");
		classListMap.put(className, authirizerListName);
		if(map.containsKey(authirizerListName))
			map.get(authirizerListName).reflash();
		else
			map.put(authirizerListName, new AuthirizerList(
					UniversalConstantsTable.ROOTPATH+UniversalConstantsTable.PLUGIN_AUTHORITYPATH+authirizerListName));
	}
	public AuthirizerUser getNormalAuthirizerLevel(MessageReceiveEvent event)
	{
		int type=event.getMsgType();
		long groupNum=event.getFromGroup();
		long num=event.getFromQQ();
		return getNormalAuthirizerLevel(type, groupNum, num);
	}
	public AuthirizerUser getNormalAuthirizerLevel(int type,long groupNum,long num)
	{
		switch (type)
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			return AuthirizerUser.PERSON_CLIENT;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			switch (CQSender.getQQInfoInGroup(num, groupNum).getAuthority())
			{
			case OWNER:
				return AuthirizerUser.GROUP_OWNER;
			case ADMIN:
				return AuthirizerUser.GROUP_MANAGER;
			case MEMBER:
				return AuthirizerUser.GROUP_MEMBER;
				
			default:
				return AuthirizerUser.ERROR;
			}
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			return AuthirizerUser.DISCUSS_MEMBER;
		default:
			return AuthirizerUser.ERROR;
		}
	}
	/**
	 * 检测在权限表上是否是白名单<br>
	 * 注意：本方法是直接检测的默认的权限表，指定权限表使用同名方法
	 * @param num 号码
	 * @return
	 */
	public boolean isWhite(Class<?> fromClass,long num)
	{
		String name=fromClass.getName();
		String listName=classListMap.get(name);
		if(listName==null)
		{
			Log.e("请求了不存在的类："+listName);
			return false;
		}
		return isWhite(listName, num);
	}
	/**
	 * 检测指定权限表上是否是白名单
	 * @param num 号码
	 * @return
	 */
	public boolean isWhite(String listName,long num)
	{
		AuthirizerList authirizerList=map.get(listName);
		if(authirizerList==null)
		{
			Log.e("请求了不存在的权限表："+listName);
			return false;
		}
		return authirizerList.isWhite(num);
	}
	/**
	 * 检测在权限表上是否是黑名单<br>
	 * 注意：本方法是直接检测的默认的权限表，指定权限表使用同名方法
	 * @param num 号码
	 * @return
	 */
	public boolean isBlack(Class<?> fromClass,long num)
	{
		String name=fromClass.getName();
		String listName=classListMap.get(name);
		if(listName==null)
		{
			Log.e("请求了不存在的类："+listName);
			return false;
		}
		return isWhite(listName, num);
	}
	/**
	 * 检测指定权限表上是否是黑名单
	 * @param num 号码
	 * @return
	 */
	public boolean isBlack(String listName,long num)
	{
		AuthirizerList authirizerList=map.get(listName);
		if(authirizerList==null)
		{
			Log.e("请求了不存在的权限表："+listName);
			return false;
		}
		return authirizerList.isWhite(num);
	}
}
