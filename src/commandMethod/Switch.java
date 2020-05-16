package commandMethod;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import commandMethod.register.OnMessageReceiveListener;
import commandPointer.annotations.AuxiliaryClass;
import connection.CQSender;
import connection.ReceiveMessageType;
import global.UniversalConstantsTable;
import global.authorizer.AuthirizerUser;
import global.authorizer.AuthorizerListGetter;
import global.authorizer.MinimumAuthority;
import surveillance.Log;

@AuxiliaryClass
public class Switch extends Father
{
	private static final String LISTENMODE="MODE";
	private static final String DORMANTLIST="dormant";
	private static long SOP=-1;
	private static boolean frameShowing=false;
	private enum SpecificStateString{
		MonitorWhiteListUser,MonitorNormalUser,MonitorBlackListUser,
		MonitorWhiteListGroup,MonitorNormalGroup,MonitorBlackListGroup,
		MonitorWhiteListDiscuss,MonitorNormalDiscuss,MonitorBlackListDiscuss,
		AgreeWhenWhiteListUserInvite,AgreeWhenNormalUserInvite,AgreeWhenBlackListUserInvite,
		DefaultDormant}
	private enum ListType{PERSONWHITELIST,GROUPWHITELIST,DISCUSSWHITELIST,
		PERSONBLACKLIST,GROUPBLACKLIST,DISCUSSBLACKLIST}
	private enum ListeningMode{
		/**公用模式<br>会自动同意邀请，被邀请成功之后默认自动启动*/
		PUBLIC("公用模式","自动同意邀请，默认已经启动",true,true,false,true,true,false,true,true,false,true,true,false,true),
		/**私用模式<br>不会自动同意邀请，仅仅会同意白名单的用户的邀请，被邀请成功之后默认关闭*/
		PRIVATE("私用模式","不自动同意邀请，默认未启动",true,false,false,true,false,false,true,false,false,true,false,false,false),
		/**静止模式<br>不会同意邀请，不做任何的应答*/
		STOP("静止模式","不同意邀请，全体静默",false,false,false,false,false,false,false,false,false,false,false,false,false),
		
		/**亢奋模式<br>会同意所有邀请，对所有的应答回应*/
		HYPERACTIVITY("亢奋状态","自动同意邀请，无视静默全体应答",true,true,true,true,true,true,true,true,true,true,true,true,true),
		/**异常模式*/
		ABNORMAL("异常状态","不应存在的状态",false,false,false,false,false,false,false,false,false,false,false,false,false),;

		String modeName,help;
		boolean MonitorWhiteListUser,MonitorNormalUser,MonitorBlackListUser;
		boolean MonitorWhiteListGroup,MonitorNormalGroup,MonitorBlackListGroup;
		boolean MonitorWhiteListDiscuss,MonitorNormalDiscuss,MonitorBlackListDiscuss;
		boolean AgreeWhenWhiteListUserInvite,AgreeWhenNormalUserInvite,AgreeWhenBlackListUserInvite;
		boolean DefaultDormant;
		
		ListeningMode(String string, String string2, boolean b, boolean c, 
				boolean d, boolean e, boolean f, boolean g, boolean h, 
				boolean i, boolean j, boolean k, boolean l, boolean m, boolean n)
		{
			// TODO Auto-generated constructor stub
			modeName=string;
			help=string2;
			MonitorWhiteListUser=b;
			MonitorNormalUser=c;
			MonitorBlackListUser=d;
			MonitorWhiteListGroup=e;
			MonitorNormalGroup=f;
			MonitorBlackListGroup=g;
			MonitorWhiteListDiscuss=h;
			MonitorNormalDiscuss=i;
			MonitorBlackListDiscuss=j;
			AgreeWhenWhiteListUserInvite=k;
			AgreeWhenNormalUserInvite=l;
			AgreeWhenBlackListUserInvite=m;
			DefaultDormant=n;
		}
		/**
		 * 将文字格式化为状态
		 * @param string 字符串
		 * @return 状态，若异常则会返回ABNORMAL
		 */
		static ListeningMode formant(String string)
		{
			for (ListeningMode listening_mode : ListeningMode.values())
			{
				if(listening_mode.name().toLowerCase().equals(string.toLowerCase()))
				{
					return listening_mode;
				}
			}
			return ABNORMAL;
		}
		int getIndex()
		{
			for(int i=0;i<this.values().length;i++)
			{
				if(this.values()[i]==this)
					return i;
			}
			return 4;
		}
	}
	@Override
	public void initialize()
	{
		OnMessageReceiveListener interceptor=new OnMessageReceiveListener() {
			@Override
			@MinimumAuthority(authirizerUser = AuthirizerUser.BANNED_USER)
			public int run(ReceiveMessageType messageType)
			{
				receiveMessageType=messageType;
				if(SOP==-1)
				{
					SOP=AuthorizerListGetter.getCoreAuthirizerList().getSOP();
					if(SOP==-1)
					{
						if(!frameShowing)
							new SOPSetFrame();
						return RETURN_STOP;
					}
				}
				boolean accessible=thisAccessiable();
				if(accessible)
				{
					if(receiveMessageType.getMsg().replaceAll(" ", "").equals(".boton")
							||receiveMessageType.getMsg().replaceAll(" ", "").equals(".boton"))
					{
						if(AuthirizerUser.GROUP_MANAGER.ifAccessible(CQSender.getAuthirizer(receiveMessageType)))
							bot_on();
						else
							sendBackMsg("权限不足");
					}
					else
					{
						if(Boolean.parseBoolean(getDormantStatus()))
							return RETURN_PASS;
					}
				}
				return RETURN_STOP;
			}
		};
		interceptor.priority=100;
		addMessageReceiveListener(interceptor);
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void mode_change()
	{
		ListeningMode mode=getListeningMode();
		if(mode==ListeningMode.ABNORMAL)
		{
			Log.f("异常的监听模式！停止运行！");
			return;
		}
		if(mode==ListeningMode.HYPERACTIVITY)
		{
			Log.i("停止了亢奋模式");
			getDataExchanger().addItem(LISTENMODE, ListeningMode.PRIVATE.name());
			setDefault(mode);
			sendBackMsg("停止了亢奋模式，转为私用模式");
			return;
		}
		int newIndex=mode.getIndex()+1;
		if(newIndex>2)
			newIndex=0;
		mode=ListeningMode.values()[newIndex];
		getDataExchanger().addItem(LISTENMODE, mode.name());
		setDefault(mode);
		sendBackMsg("模式调整为"+mode.modeName+"\n"+mode.help);
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void mode_change(ArrayList<String> arrayList)
	{
		String string=arrayList.get(0);
		ListeningMode mode=ListeningMode.formant(string);
		if(mode==ListeningMode.ABNORMAL)
		{
			sendBackMsg("错误的模式名");
			return;
		}
		getDataExchanger().addItem(LISTENMODE, mode.name());
		setDefault(mode);
		sendBackMsg("模式调整为"+mode.modeName+"\n"+mode.help);
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addPW(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		boolean hasW=addNumToList(ListType.PERSONWHITELIST, num);
		string+=hasW?"将"+num+"添加到了白名单":"白名单中已存在";
		string+="\n";
		string+=deleteNumToList(ListType.PERSONBLACKLIST, num)?"并移出了黑名单":"";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addGW(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		boolean hasW=addNumToList(ListType.GROUPWHITELIST, num);
		string+=hasW?"将"+num+"添加到了白名单":"白名单中已存在";
		string+="\n";
		string+=deleteNumToList(ListType.GROUPBLACKLIST, num)?"并移出了黑名单":"";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addDW(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		boolean hasW=addNumToList(ListType.DISCUSSWHITELIST, num);
		string+=hasW?"将"+num+"添加到了白名单":"白名单中已存在";
		string+="\n";
		string+=deleteNumToList(ListType.DISCUSSBLACKLIST, num)?"并移出了黑名单":"";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addPB(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		boolean hasW=addNumToList(ListType.PERSONBLACKLIST, num);
		string+=hasW?"将"+num+"添加到了黑名单":"黑名单中已存在";
		string+="\n";
		string+=deleteNumToList(ListType.PERSONWHITELIST, num)?"并移出了白名单":"";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addGB(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		boolean hasW=addNumToList(ListType.GROUPBLACKLIST, num);
		string+=hasW?"将"+num+"添加到了黑名单":"黑名单中已存在";
		string+="\n";
		string+=deleteNumToList(ListType.GROUPWHITELIST, num)?"并移出了白名单":"";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addDB(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		boolean hasW=addNumToList(ListType.DISCUSSBLACKLIST, num);
		string+=hasW?"将"+num+"添加到了黑名单":"黑名单中已存在";
		string+="\n";
		string+=deleteNumToList(ListType.DISCUSSWHITELIST, num)?"并移出了白名单":"";
		sendBackMsg(string);
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void removePW(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		string+=deleteNumToList(ListType.PERSONWHITELIST, num)?"移出了名单":"不存在于名单中";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void removeGW(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		string+=deleteNumToList(ListType.GROUPWHITELIST, num)?"移出了名单":"不存在于名单中";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void removeDW(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		string+=deleteNumToList(ListType.DISCUSSWHITELIST, num)?"移出了名单":"不存在于名单中";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void removePB(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		string+=deleteNumToList(ListType.PERSONBLACKLIST, num)?"移出了名单":"不存在于名单中";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void removeGB(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		string+=deleteNumToList(ListType.GROUPBLACKLIST, num)?"移出了名单":"不存在于名单中";
		sendBackMsg(string);
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void removeDB(ArrayList<String> arrayList)
	{
		long num=Long.parseLong(arrayList.get(0));
		String string="";
		string+=deleteNumToList(ListType.DISCUSSBLACKLIST, num)?"移出了名单":"不存在于名单中";
		sendBackMsg(string);
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void listPW()
	{
		String string=getDataExchanger().getItem(ListType.PERSONWHITELIST.name());
		if(string==null)
		{
			sendBackMsg("名单为空");
			return;
		}
		String[] strings=string.split(",");
		StringBuilder result=new StringBuilder("名单如下：\n");
		for(int i=0;i<strings.length;i++)
		{
			result.append(strings);
			if(i%2==0)
				result.append(' ');
			else
				result.append('\n');
		}
		sendBackMsg(result.toString());
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void listGW()
	{
		String string=getDataExchanger().getItem(ListType.GROUPWHITELIST.name());
		if(string==null)
		{
			sendBackMsg("名单为空");
			return;
		}
		String[] strings=string.split(",");
		StringBuilder result=new StringBuilder("名单如下：\n");
		for(int i=0;i<strings.length;i++)
		{
			result.append(strings);
			if(i%2==0)
				result.append(' ');
			else
				result.append('\n');
		}
		sendBackMsg(result.toString());
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void listDW()
	{
		String string=getDataExchanger().getItem(ListType.DISCUSSWHITELIST.name());
		if(string==null)
		{
			sendBackMsg("名单为空");
			return;
		}
		String[] strings=string.split(",");
		StringBuilder result=new StringBuilder("名单如下：\n");
		for(int i=0;i<strings.length;i++)
		{
			result.append(strings);
			if(i%2==0)
				result.append(' ');
			else
				result.append('\n');
		}
		sendBackMsg(result.toString());
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void listPB()
	{
		String string=getDataExchanger().getItem(ListType.PERSONBLACKLIST.name());
		if(string==null)
		{
			sendBackMsg("名单为空");
			return;
		}
		String[] strings=string.split(",");
		StringBuilder result=new StringBuilder("名单如下：\n");
		for(int i=0;i<strings.length;i++)
		{
			result.append(strings);
			if(i%2==0)
				result.append(' ');
			else
				result.append('\n');
		}
		sendBackMsg(result.toString());
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void listGB()
	{
		String string=getDataExchanger().getItem(ListType.GROUPBLACKLIST.name());
		if(string==null)
		{
			sendBackMsg("名单为空");
			return;
		}
		String[] strings=string.split(",");
		StringBuilder result=new StringBuilder("名单如下：\n");
		for(int i=0;i<strings.length;i++)
		{
			result.append(strings);
			if(i%2==0)
				result.append(' ');
			else
				result.append('\n');
		}
		sendBackMsg(result.toString());
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void listDB()
	{
		String string=getDataExchanger().getItem(ListType.DISCUSSBLACKLIST.name());
		if(string==null)
		{
			sendBackMsg("名单为空");
			return;
		}
		String[] strings=string.split(",");
		StringBuilder result=new StringBuilder("名单如下：\n");
		for(int i=0;i<strings.length;i++)
		{
			result.append(strings);
			if(i%2==0)
				result.append(' ');
			else
				result.append('\n');
		}
		sendBackMsg(result.toString());
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
	public void bot_on()
	{
		ArrayList<String> arrayList=getDataExchanger().getListItem(DORMANTLIST, getMark());
		if(arrayList==null)
		{
			getDataExchanger().addListItem(DORMANTLIST, getMark(), Boolean.toString(Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.DefaultDormant.name()))));
			arrayList=getDataExchanger().getListItem(DORMANTLIST, getMark());
		}
		boolean b=Boolean.parseBoolean(arrayList.get(0));
		if(b==true)
		{
			sendBackMsg("已经处于启动模式");
			return;
		}
		getDataExchanger().deleteListItem(DORMANTLIST, getMark());
		getDataExchanger().addListItem(DORMANTLIST, getMark(), Boolean.toString(true));
		sendBackMsg("进入启动模式");
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
	public void bot_off()
	{
		ArrayList<String> arrayList=getDataExchanger().getListItem(DORMANTLIST, getMark());
		if(arrayList==null)
		{
			getDataExchanger().addListItem(DORMANTLIST, getMark(), Boolean.toString(Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.DefaultDormant.name()))));
		}
		boolean b=Boolean.parseBoolean(arrayList.get(0));
		if(b==false)
		{
			sendBackMsg("已经处于休眠模式");
			return;
		}
		getDataExchanger().deleteListItem(DORMANTLIST, getMark());
		getDataExchanger().addListItem(DORMANTLIST, getMark(), Boolean.toString(false));
		sendBackMsg("进入休眠模式");
	}

	private boolean addNumToList(ListType type,long num)
	{
		String string=getDataExchanger().getItem(type.name());
		if(string==null)
		{
			getDataExchanger().addItem(type.name(), Long.toString(num));
			return true;
		}
		String[] strings=string.split(",");
		for (String string2 : strings)
		{
			if(string2.isEmpty())
				continue;
			if(Long.parseLong(string2)==num)
				return false;
		}
		StringBuilder builder=new StringBuilder();
		for (String string2 : strings)
		{
			if(string2.isEmpty())
				continue;
			builder.append(string2);
			builder.append(',');
		}
		builder.append(num);
		getDataExchanger().addItem(type.name(), builder.toString());
		return true;
	}
	private boolean deleteNumToList(ListType type,long num)
	{
		String string=getDataExchanger().getItem(type.name());
		if(string==null)
		{
			return false;
		}
		String[] strings=string.split(",");
		
		boolean find=false;
		
		StringBuilder builder=new StringBuilder();
		for (String string2 : strings)
		{
			if(string2.isEmpty())
				continue;
			if(Long.parseLong(string2)==num)
			{
				find=true;
				continue;
			}
			builder.append(string2);
			builder.append(',');
		}
		if(find)
			getDataExchanger().addItem(type.name(), builder.toString());
		return find;
	}
	
	
	private String getMark()
	{
		String string="";
		switch (receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			string="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			string="G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			string="D"+receiveMessageType.getfromGroup();
			break;
		}
		return string;
	}
	
	private void setDefault(ListeningMode listeningMode)
	{
		getDataExchanger().addItem(SpecificStateString.MonitorWhiteListUser.name(), Boolean.toString(listeningMode.MonitorWhiteListUser));
		getDataExchanger().addItem(SpecificStateString.MonitorBlackListUser.name(), Boolean.toString(listeningMode.MonitorBlackListUser));
		getDataExchanger().addItem(SpecificStateString.MonitorNormalUser.name(), Boolean.toString(listeningMode.MonitorNormalUser));
		
		getDataExchanger().addItem(SpecificStateString.MonitorWhiteListGroup.name(), Boolean.toString(listeningMode.MonitorWhiteListGroup));
		getDataExchanger().addItem(SpecificStateString.MonitorBlackListGroup.name(), Boolean.toString(listeningMode.MonitorBlackListGroup));
		getDataExchanger().addItem(SpecificStateString.MonitorNormalGroup.name(), Boolean.toString(listeningMode.MonitorNormalGroup));
		
		getDataExchanger().addItem(SpecificStateString.MonitorWhiteListDiscuss.name(), Boolean.toString(listeningMode.MonitorWhiteListDiscuss));
		getDataExchanger().addItem(SpecificStateString.MonitorBlackListDiscuss.name(), Boolean.toString(listeningMode.MonitorBlackListDiscuss));
		getDataExchanger().addItem(SpecificStateString.MonitorNormalDiscuss.name(), Boolean.toString(listeningMode.MonitorNormalDiscuss));
		
		getDataExchanger().addItem(SpecificStateString.AgreeWhenWhiteListUserInvite.name(), Boolean.toString(listeningMode.AgreeWhenWhiteListUserInvite));
		getDataExchanger().addItem(SpecificStateString.AgreeWhenBlackListUserInvite.name(), Boolean.toString(listeningMode.AgreeWhenBlackListUserInvite));
		getDataExchanger().addItem(SpecificStateString.AgreeWhenNormalUserInvite.name(), Boolean.toString(listeningMode.AgreeWhenNormalUserInvite));
		
		getDataExchanger().addItem(SpecificStateString.DefaultDormant.name(), Boolean.toString(listeningMode.DefaultDormant));
	}
	/**
	 * 检测是否对这个用户进行响应，只检测是否可以响应，跟是否休眠没有关系
	 * @param num QQ号
	 * @return
	 */
	private boolean userAccessiable(long num)
	{
		ListeningMode listeningMode=getListeningMode();
		if(listeningMode==ListeningMode.ABNORMAL)
		{
			Log.e("处于异常状态！");
			return false;
		}
		if(listeningMode==ListeningMode.HYPERACTIVITY)
			return true;
		if(listeningMode==ListeningMode.STOP)
			return false;
		
		String line=getDataExchanger().getItem(ListType.PERSONWHITELIST.name());
		if(line!=null)
		{
			String[] list=line.split(",");
			for (String string : list)
			{
				if(Long.parseLong(string)==num)
				{
					boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorWhiteListUser.name()));
					if(b)
						return true;
					else
						return false;
				}
			}
		}
		line=getDataExchanger().getItem(ListType.PERSONBLACKLIST.name());
		if(line!=null)
		{
			String[] list=line.split(",");
			for (String string : list)
			{
				if(Long.parseLong(string)==num)
				{
					boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorBlackListUser.name()));
					if(b)
						return true;
					else
						return false;
				}
			}
		}
		boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorNormalUser.name()));
		if(b)
			return true;
		else
			return false;
	}
	/**
	 * 检测是否对这个群进行响应，只检测是否可以响应，跟是否休眠没有关系
	 * @param num 群号
	 * @return
	 */
	private boolean groupAccessiable(long num)
	{
		ListeningMode listeningMode=getListeningMode();
		if(listeningMode==ListeningMode.ABNORMAL)
		{
			Log.e("处于异常状态！");
			return false;
		}
		if(listeningMode==ListeningMode.HYPERACTIVITY)
			return true;
		if(listeningMode==ListeningMode.STOP)
			return false;
		
		String line=getDataExchanger().getItem(ListType.GROUPWHITELIST.name());
		if(line!=null)
		{
			String[] list=line.split(",");
			for (String string : list)
			{
				if(string.isEmpty())
					continue;
				if(Long.parseLong(string)==num)
				{
					boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorWhiteListGroup.name()));
					if(b)
						return true;
					else
						return false;
				}
			}
		}
		line=getDataExchanger().getItem(ListType.GROUPBLACKLIST.name());
		if(line!=null)
		{
			String[] list=line.split(",");
			for (String string : list)
			{
				if(Long.parseLong(string)==num)
				{
					boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorBlackListGroup.name()));
					if(b)
						return true;
					else
						return false;
				}
			}
		}
		boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorNormalGroup.name()));
		if(b)
			return true;
		else
			return false;
	}
	/**
	 * 检测是否对这个讨论组进行响应，只检测是否可以响应，跟是否休眠没有关系
	 * @param num QQ号
	 * @return
	 */
	private boolean discussAccessiable(long num)
	{
		ListeningMode listeningMode=getListeningMode();
		if(listeningMode==ListeningMode.ABNORMAL)
		{
			Log.e("处于异常状态！");
			return false;
		}
		if(listeningMode==ListeningMode.HYPERACTIVITY)
			return true;
		if(listeningMode==ListeningMode.STOP)
			return false;
		
		String line=getDataExchanger().getItem(ListType.DISCUSSWHITELIST.name());
		if(line!=null)
		{
			String[] list=line.split(",");
			for (String string : list)
			{
				if(Long.parseLong(string)==num)
				{
					boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorWhiteListDiscuss.name()));
					if(b)
						return true;
					else
						return false;
				}
			}
		}
		line=getDataExchanger().getItem(ListType.DISCUSSBLACKLIST.name());
		if(line!=null)
		{
			String[] list=line.split(",");
			for (String string : list)
			{
				if(Long.parseLong(string)==num)
				{
					boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorBlackListDiscuss.name()));
					if(b)
						return true;
					else
						return false;
				}
			}
		}
		boolean b=Boolean.parseBoolean(getDataExchanger().getItem(SpecificStateString.MonitorNormalDiscuss.name()));
		if(b)
			return true;
		else
			return false;
	}
	/**
	 * 检测是否对这个环境中的这个用户进行响应，只检测是否可以响应，跟是否休眠没有关系
	 * @return
	 */
	private boolean thisAccessiable()
	{
		switch (receiveMessageType.getMsgType())
		{
		//FIXME:在检测群中和讨论组中的时候，用户的权限也会影响，理论上要遵从父级（黑名单除外）
		case UniversalConstantsTable.MSGTYPE_PERSON:
			return userAccessiable(receiveMessageType.getfromQQ());
		case UniversalConstantsTable.MSGTYPE_GROUP:
			if(groupAccessiable(receiveMessageType.getfromGroup()))
				return true;
			return false;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			if(discussAccessiable(receiveMessageType.getfromGroup()))
				return true;
			return false;
		default:
			Log.e("异常消息种类");
			return false;
		} 
	}
	private String getDormantStatus()
	{
		String mark;
		switch (receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			mark="P"+Long.toString(receiveMessageType.getfromQQ());
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			mark="G"+Long.toString(receiveMessageType.getfromGroup());
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			mark="D"+Long.toString(receiveMessageType.getfromGroup());
			break;
		default:
			Log.e("异常的消息类型");
			return null;
		}
		ArrayList<String> arrayList=getDataExchanger().getListItem(DORMANTLIST, mark);
		if(arrayList==null)
			return null;
		return arrayList.get(0);
	}
	
	class SOPSetFrame
	{
		public SOPSetFrame()
		{
			frameShowing=true;
			final JFrame frame=new JFrame("设置superOP");
			frame.setBounds(100, 100, 300, 120);
			frame.setUndecorated(true);
			frame.setAlwaysOnTop(true);
			
			Container container=frame.getContentPane();
			container.setLayout(null);
			JLabel label=new JLabel("请设置superOP的号码：");
			label.setBounds(10, 10, 280, 20);
			
			final JLabel label2=new JLabel();
			label2.setBounds(10, 90, 280, 20);
			label2.setForeground(Color.RED);
			
			final JTextField field=new JTextField();
			field.setBounds(10, 40, 200, 40);
			field.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if(label2.getText()!="")
					{
						field.setText("");
						label2.setText("");
					}
				}
			});
			container.add(label);
			container.add(field);
			
			
			
			JButton button=new JButton("确定");
			button.setBounds(220, 40, 70, 40);
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String string=field.getText();
					try
					{
						long SOP=Long.parseLong(string);
						setSOP(SOP);
						addNumToList(ListType.PERSONWHITELIST, SOP);
						AuthorizerListGetter.getCoreAuthirizerList().setSOP(SOP);
						sendPrivateMsg(SOP, 
								CQSender.getMyName()+"将您设为superOP");
					}
					catch(NumberFormatException exception)
					{
						label2.setText("输入错误！");
						return;
					}
					frame.dispose();
					frameShowing=false;
				}
			});
			
			container.add(button);
			container.add(label2);
			frame.setVisible(true);
		}
	}
	private void setSOP(long num)
	{
		AuthorizerListGetter.getCoreAuthirizerList().setSOP(num);
	}
	/**
	 * 获得现在的监听状态，如果没有记录过状态则默认是私有状态
	 * @return
	 */
	private ListeningMode getListeningMode()
	{
		String string=getDataExchanger().getItem(LISTENMODE);
		if(string==null)
		{
			string=ListeningMode.PRIVATE.name();
			getDataExchanger().addItem(LISTENMODE, string);
			setDefault(ListeningMode.PRIVATE);
		}
		return ListeningMode.formant(string);
	}
}
