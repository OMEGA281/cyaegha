package commandPointer;

import java.util.HashMap;

import commandPointer.annotations.AuthirizerListNeed;
import commandPointer.annotations.MinimumAuthority;
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
	private AuthirizerUser getNormalAuthirizerLevel(MessageReceiveEvent event)
	{
		int type=event.getMsgType();
		long groupNum=event.getFromGroup();
		long num=event.getFromQQ();
		return getNormalAuthirizerLevel(type, groupNum, num);
	}
	private AuthirizerUser getNormalAuthirizerLevel(int type,long groupNum,long num)
	{
		switch (type)
		{
//		FIXME:这里后来要加上避免不存在的人或群的查询
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
	 * 检测指定权限表上是否是白名单
	 * @param num 号码
	 * @return
	 */
	private boolean isWhite(String listName,long num)
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
	 * 检测指定权限表上是否是黑名单
	 * @param num 号码
	 * @return
	 */
	private boolean isBlack(String listName,long num)
	{
		AuthirizerList authirizerList=map.get(listName);
		if(authirizerList==null)
		{
			Log.e("请求了不存在的权限表："+listName);
			return false;
		}
		return authirizerList.isWhite(num);
	}
	public boolean isAccessible(MinimumAuthority authority,int type,long groupNum,long num)
	{
		if(authority==null)
		{
			AuthirizerUser user=getNormalAuthirizerLevel(type, groupNum, num);
//			FIXME:这里后来要加上受到默认的影响的方式
			return AuthirizerUser.GROUP_MEMBER.ifAccessible(user);
		}
		else
		{
			AuthirizerUser askLevel=authority.authirizerUser();
			AuthirizerUser user=getNormalAuthirizerLevel(type, groupNum, num);
			return askLevel.ifAccessible(user);
		}
	}
	public boolean isAccessible(AuthirizerListNeed authirizer,long num,Class<?> clazz)
	{
		if(authirizer==null)
			return true;
		else
		{
			String listName=authirizer.AuthirizerList();
			if(listName.isEmpty())
			{
				listName=classListMap.get(clazz.getName());
			}
			if(listName==null)
			{
				Log.e("找不到该类所在的权限表！");
				return false;
			}
			if(isWhite(listName, num)==true)
				if(authirizer.WhiteList_Accessible()==true)
					return true;
				else
					return false;
			if(isBlack(listName, num)==true)
				if(authirizer.BlackList_Accessible()==true)
					return true;
				else
					return false;
			if(authirizer.Normal_Accessible()==true)
				return true;
			else
				return false;
		}
	}
}