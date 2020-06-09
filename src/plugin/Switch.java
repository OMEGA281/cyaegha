package plugin;

import pluginHelper.AuthirizerListBook;
import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.NewAuthirizerList;
import pluginHelper.annotations.RegistCommand;
import pluginHelper.annotations.RegistListener.FriendAddListener;
import pluginHelper.annotations.RegistListener.GroupAddListener;
import pluginHelper.annotations.RegistListener.MessageReceiveListener;
import pluginHelper.annotations.UseAuthirizerList;
import surveillance.Log;
import transceiver.EventTrigger.EventResult;
import transceiver.IdentitySymbol;
import transceiver.IdentitySymbol.SourceType;
import transceiver.event.FriendAddEvent;
import transceiver.event.GroupAddEvent;
import transceiver.event.MessageReceiveEvent;

@NewAuthirizerList({ "monitor_person", "monitor_group", "monitor_discuss" })
@UseAuthirizerList("monitor_person")
public class Switch extends Father
{
	private static final String LISTENMODE = "MODE";
	private ListeningMode mode;

	private enum ListeningMode
	{
		/**
		 * 公用模式<br>
		 * 会自动同意邀请，被邀请成功之后默认自动启动
		 */
		PUBLIC("公用模式", "自动同意邀请，默认已经启动", true, true, false, true, true, false, true, true, false, true, true, false,
				true),
		/**
		 * 私用模式<br>
		 * 不会自动同意邀请，仅仅会同意白名单的用户的邀请，被邀请成功之后默认关闭
		 */
		PRIVATE("私用模式", "不自动同意邀请，默认未启动", true, false, false, true, false, false, true, false, false, true, false, false,
				false),
		/**
		 * 静止模式<br>
		 * 不会同意邀请，不做任何的应答
		 */
		STOP("静止模式", "不同意邀请，全体静默", false, false, false, false, false, false, false, false, false, false, false, false,
				false),

		/**
		 * 亢奋模式<br>
		 * 会同意所有邀请，对所有的应答回应
		 */
		HYPERACTIVITY("亢奋状态", "自动同意邀请，无视静默全体应答", true, true, true, true, true, true, true, true, true, true, true, true,
				true),
		/** 异常模式 */
		ABNORMAL("异常状态", "不应存在的状态", false, false, false, false, false, false, false, false, false, false, false, false,
				false),;

		String modeName, help;
		boolean MonitorWhiteListUser, MonitorNormalUser, MonitorBlackListUser;
		boolean MonitorWhiteListGroup, MonitorNormalGroup, MonitorBlackListGroup;
		boolean MonitorWhiteListDiscuss, MonitorNormalDiscuss, MonitorBlackListDiscuss;
		boolean AgreeWhenWhiteListUserInvite, AgreeWhenNormalUserInvite, AgreeWhenBlackListUserInvite;
		boolean DefaultOn;

		ListeningMode(String string, String string2, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g,
				boolean h, boolean i, boolean j, boolean k, boolean l, boolean m, boolean n)
		{
			// TODO Auto-generated constructor stub
			modeName = string;
			help = string2;
			MonitorWhiteListUser = b;
			MonitorNormalUser = c;
			MonitorBlackListUser = d;
			MonitorWhiteListGroup = e;
			MonitorNormalGroup = f;
			MonitorBlackListGroup = g;
			MonitorWhiteListDiscuss = h;
			MonitorNormalDiscuss = i;
			MonitorBlackListDiscuss = j;
			AgreeWhenWhiteListUserInvite = k;
			AgreeWhenNormalUserInvite = l;
			AgreeWhenBlackListUserInvite = m;
			DefaultOn = n;
		}
		
		/**
		 * 将文字格式化为状态
		 * 
		 * @param string 字符串
		 * @return 状态，若异常则会返回ABNORMAL
		 */
		static ListeningMode formant(String string)
		{
			for (ListeningMode listening_mode : ListeningMode.values())
			{
				if (listening_mode.name().toLowerCase().equals(string.toLowerCase()))
				{
					return listening_mode;
				}
			}
			return ABNORMAL;
		}
	}
	
	@MessageReceiveListener(priority = 100)
	@MinimumAuthority(AuthirizerUser.ALL)
	public EventResult messageInterceptor(MessageReceiveEvent event)
	{
		if(!accessibie(event))
			return EventResult.STOP;
		if(event.getMsg().toLowerCase().replaceAll(" ", "").equals(".boton")
				||event.getMsg().toLowerCase().replaceAll(" ", "").equals("。boton"))
			if(AuthirizerUser.GROUP_MANAGER.ifAccessible(getNormalAuthirizer(event)))
			{
				bot_on(event);
				return EventResult.STOP;
			}
		if(event.getMsg().toLowerCase().replaceAll(" ", "").equals(".botoff")
				||event.getMsg().toLowerCase().replaceAll(" ", "").equals("。botoff"))
			if(AuthirizerUser.GROUP_MANAGER.ifAccessible(getNormalAuthirizer(event)))
			{
				bot_off(event);
				return EventResult.STOP;
			}
		String string= getDataExchanger().getItem(getMark(event));
		if (string == null)
		{
			string=Boolean.toString(mode.DefaultOn);
			getDataExchanger().addItem(getMark(event), string);
		}
		return Boolean.parseBoolean(string)?EventResult.PASS:EventResult.STOP;
	}
	
	@MinimumAuthority(AuthirizerUser.ALL)
	@GroupAddListener(priority = 100)
	public EventResult groupAddInterceptor(GroupAddEvent event)
	{
		if(event.hasProcessed>0)
			return EventResult.PASS;
		boolean result;
		if(mode.AgreeWhenWhiteListUserInvite&&isWhite("monitor_person", event.userNum))
			result=event.deal(true);
		else if(mode.AgreeWhenBlackListUserInvite&&isBlack("monitor_person", event.userNum))
			result=event.deal(true);
		else if(mode.AgreeWhenNormalUserInvite)
			result=event.deal(true);
		else
			result=event.deal(false);
		if(!result)
			Log.e("处理群添加时出现问题！");
		return EventResult.PASS;
	}
	
	@MinimumAuthority(AuthirizerUser.ALL)
	@FriendAddListener(priority = 100)
	public EventResult friendAddInterceptor(FriendAddEvent event)
	{
		if(event.hasProcessed>0)
			return EventResult.PASS;
		boolean result;
		if(mode.AgreeWhenWhiteListUserInvite&&isWhite("monitor_person", event.userNum))
			result=event.deal(true);
		else if(mode.AgreeWhenBlackListUserInvite&&isBlack("monitor_person", event.userNum))
			result=event.deal(true);
		else if(mode.AgreeWhenNormalUserInvite)
			result=event.deal(true);
		else
			result=event.deal(false);
		if(!result)
			Log.e("处理好友添加时出现问题！");
		return EventResult.PASS;
	}

	@MinimumAuthority(AuthirizerUser.OP)
	@RegistCommand(CommandString = "mode change",Help = "切换全局监听模式")
	public void mode_change(MessageReceiveEvent event)
	{
		switch (mode)
		{
		case HYPERACTIVITY:
		case ABNORMAL:
		case STOP:
			setListeningMode(ListeningMode.PRIVATE);
			sendMsg(event, "切换为私用模式");
			return;
		case PRIVATE:
			setListeningMode(ListeningMode.PUBLIC);
			sendMsg(event, "切换为公用模式");
			return;
		case PUBLIC:
			setListeningMode(ListeningMode.PRIVATE);
			sendMsg(event, "切换为私用模式");
			return;
		default:
			break;
		}
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "mode change",Help = "切换全局监听模式")
	public void mode_change(MessageReceiveEvent event, String string)
	{
		ListeningMode mode = ListeningMode.formant(string);
		if (mode == ListeningMode.ABNORMAL)
		{
			sendMsg(event, "错误的模式名");
			return;
		}
		setListeningMode(mode);
		sendMsg(event, "模式调整为" + mode.modeName + "\n" + mode.help);
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor add person whitelist",Help = "添加个人白名单")
	public void addPW(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.PERSON, true, true, num);
		sendMsg(event, "添加"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor add group whitelist",Help = "添加群白名单")
	public void addGW(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.GROUP, true, true, num);
		sendMsg(event, "添加"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor add discuss whitelist",Help = "添加讨论组白名单")
	public void addDW(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.DISCUSS, true, true, num);
		sendMsg(event, "添加"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor add person blacklist",Help = "添加个人黑名单")
	public void addPB(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.PERSON, true, false, num);
		sendMsg(event, "添加"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor add group blacklist",Help = "添加群黑名单")
	public void addGB(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.GROUP, true, false, num);
		sendMsg(event, "添加"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor add discuss blacklist",Help = "添加讨论组黑名单")
	public void addDB(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.DISCUSS, true, false, num);
		sendMsg(event, "添加"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor remove person whitelist",Help = "删除个人白名单")
	public void removePW(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.PERSON, false, true, num);
		sendMsg(event, "移除"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor remove group whitelist",Help = "删除群组白名单")
	public void removeGW(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.GROUP, false, true, num);
		sendMsg(event, "移除"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor remove discuss whitelist",Help = "删除讨论组白名单")
	public void removeDW(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.DISCUSS, false, true, num);
		sendMsg(event, "移除"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor remove person blacklist",Help = "删除个人黑名单")
	public void removePB(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.PERSON, false, false, num);
		sendMsg(event, "移除"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor remove group blacklist",Help = "删除群组黑名单")
	public void removeGB(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.GROUP, false, false, num);
		sendMsg(event, "移除"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor remove discuss blacklist",Help = "删除讨论组黑名单")
	public void removeDB(MessageReceiveEvent event, Long num)
	{
		boolean hasW = addOrRemoveWhiteOrBlackList(SourceType.DISCUSS, false, false, num);
		sendMsg(event, "移除"+(hasW?"成功":"未执行"));
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor list person whitelist",Help = "列出个人白名单")
	public void listPW(MessageReceiveEvent event)
	{
		long[] ls=getAllList(SourceType.PERSON, true);
		StringBuilder result = new StringBuilder("名单如下：\n");
		for (int i = 0; i < ls.length; i++)
		{
			result.append(ls);
			if (i % 2 == 0)
				result.append('\t');
			else
				result.append('\n');
		}
		sendMsg(event, result.toString());
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor list group whitelist",Help = "列出群组白名单")
	public void listGW(MessageReceiveEvent event)
	{
		long[] ls=getAllList(SourceType.GROUP, true);
		StringBuilder result = new StringBuilder("名单如下：\n");
		for (int i = 0; i < ls.length; i++)
		{
			result.append(ls);
			if (i % 2 == 0)
				result.append('\t');
			else
				result.append('\n');
		}
		sendMsg(event, result.toString());
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor list discuss whitelist",Help = "列出讨论组白名单")
	public void listDW(MessageReceiveEvent event)
	{
		long[] ls=getAllList(SourceType.DISCUSS, true);
		StringBuilder result = new StringBuilder("名单如下：\n");
		for (int i = 0; i < ls.length; i++)
		{
			result.append(ls);
			if (i % 2 == 0)
				result.append('\t');
			else
				result.append('\n');
		}
		sendMsg(event, result.toString());
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor list person blacklist",Help = "列出个人黑名单")
	public void listPB(MessageReceiveEvent event)
	{
		long[] ls=getAllList(SourceType.PERSON, false);
		StringBuilder result = new StringBuilder("名单如下：\n");
		for (int i = 0; i < ls.length; i++)
		{
			result.append(ls);
			if (i % 2 == 0)
				result.append('\t');
			else
				result.append('\n');
		}
		sendMsg(event, result.toString());
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor list group blacklist",Help = "列出群组黑名单")
	public void listGB(MessageReceiveEvent event)
	{
		long[] ls=getAllList(SourceType.GROUP, false);
		StringBuilder result = new StringBuilder("名单如下：\n");
		for (int i = 0; i < ls.length; i++)
		{
			result.append(ls);
			if (i % 2 == 0)
				result.append('\t');
			else
				result.append('\n');
		}
		sendMsg(event, result.toString());
	}

	@MinimumAuthority(value = AuthirizerUser.OP)
	@RegistCommand(CommandString = "monitor list discuss blacklist",Help = "列出讨论组黑名单")
	public void listDB(MessageReceiveEvent event)
	{
		long[] ls=getAllList(SourceType.DISCUSS, false);
		StringBuilder result = new StringBuilder("名单如下：\n");
		for (int i = 0; i < ls.length; i++)
		{
			result.append(ls);
			if (i % 2 == 0)
				result.append('\t');
			else
				result.append('\n');
		}
		sendMsg(event, result.toString());
	}

	@MinimumAuthority(value = AuthirizerUser.GROUP_MANAGER)
	@RegistCommand(CommandString = "bot on",Help = "启动监听")
	public void bot_on(MessageReceiveEvent event)
	{
		String string= getDataExchanger().getItem(getMark(event));
		if (string == null)
		{
			string=Boolean.toString(mode.DefaultOn);
			getDataExchanger().addItem(getMark(event), string);
		}
		boolean b = Boolean.parseBoolean(string);
		if (b)
		{
			sendMsg(event, "已经处于启动模式");
			return;
		}
		getDataExchanger().addItem(getMark(event), Boolean.toString(true));
		sendMsg(event, "进入启动模式");
	}

	@MinimumAuthority(value = AuthirizerUser.GROUP_MANAGER)
	@RegistCommand(CommandString = "bot off",Help = "关闭监听")
	public void bot_off(MessageReceiveEvent event)
	{
		String string= getDataExchanger().getItem(getMark(event));
		if (string == null)
		{
			string=Boolean.toString(mode.DefaultOn);
			getDataExchanger().addItem(getMark(event), string);
		}
		boolean b = Boolean.parseBoolean(string);
		if (b)
		{
			sendMsg(event, "已经处于休眠模式");
			return;
		}
		getDataExchanger().addItem(getMark(event), Boolean.toString(true));
		sendMsg(event, "进入休眠模式");
	}

	private boolean addOrRemoveWhiteOrBlackList(SourceType type, boolean isAdd, boolean isWhite, long num)
	{
		String listName;
		switch (type)
		{
		case PERSON:
			listName = "monitor_person";
			break;
		case GROUP:
			listName = "monitor_group";
			break;
		case DISCUSS:
			listName = "monitor_discuss";
			break;
		default:
			return false;
		}
		if (isAdd)
			return isWhite?setWhite(listName, num):setBlack(listName, num);
		else
			return isWhite?removeWhite(listName, num):removeBlack(listName, num);
	}
	
	private long[] getAllList(SourceType type,boolean isWhite)
	{
		String listName;
		switch (type)
		{
		case PERSON:
			listName = "monitor_person";
			break;
		case GROUP:
			listName = "monitor_group";
			break;
		case DISCUSS:
			listName = "monitor_discuss";
			break;
		default:
			return new long[0];
		}
		return isWhite?getAllWhite(listName):getAllBlack(listName);
	}

	private String getMark(IdentitySymbol symbol)
	{
		String string = "";
		switch (symbol.type)
		{
		case PERSON:
			string = "P" + symbol.userNum;
			break;
		case GROUP:
			string = "G" + symbol.groupNum;
			break;
		case DISCUSS:
			string = "D" + symbol.groupNum;
			break;
		}
		return string;
	}

	/**
	 * 获得现在的监听状态，如果没有记录过状态则默认是私有状态
	 * 
	 * @return
	 */
	private ListeningMode getListeningMode()
	{
		String string = getDataExchanger().getItem(LISTENMODE);
		if (string == null)
			return null;
		return ListeningMode.formant(string);
	}
	
	private void setListeningMode(ListeningMode mode)
	{
		this.mode=mode;
		getDataExchanger().addItem(LISTENMODE, mode.modeName);
	}
	
	private boolean accessibie(IdentitySymbol symbol)
	{
		if(AuthirizerListBook.getSOP()==symbol.userNum)
			return true;
		for (long l : AuthirizerListBook.getOP())
			if(l==symbol.userNum)
				return true;
		boolean g=false;
		boolean p=false;
		
		switch (symbol.type)
		{
		case PERSON:
			g=true;
			if(isWhite("monitor_person", symbol.userNum)||mode.MonitorWhiteListUser)
				p=true;
			else if(isBlack("monitor_person", symbol.userNum)||mode.MonitorBlackListUser)
				p=true;
			else if(mode.MonitorNormalUser)
				p=true;
			break;
		case GROUP:
			if(isWhite("monitor_group", symbol.groupNum)||mode.MonitorWhiteListGroup)
				g=true;
			else if(isBlack("monitor_group", symbol.groupNum)||mode.MonitorBlackListGroup)
				g=true;
			else if(mode.MonitorNormalGroup)
				g=true;
			if(isWhite("monitor_person", symbol.userNum)||mode.MonitorWhiteListUser)
				p=true;
			else if(isBlack("monitor_person", symbol.userNum)||mode.MonitorBlackListUser)
				p=true;
			else
				p=true;
			break;
		case DISCUSS:
			if(isWhite("monitor_discuss", symbol.groupNum)||mode.MonitorWhiteListDiscuss)
				g=true;
			else if(isBlack("monitor_discuss", symbol.groupNum)||mode.MonitorBlackListDiscuss)
				g=true;
			else if(mode.MonitorNormalDiscuss)
				g=true;
			if(isWhite("monitor_person", symbol.userNum)||mode.MonitorWhiteListUser)
				p=true;
			else if(isBlack("monitor_person", symbol.userNum)||mode.MonitorBlackListUser)
				p=true;
			else
				p=true;
			break;
		default:
			break;
		}
		return g&&p;
	}

	@Override
	public void init()
	{
		Log.d("加载消息拦截插件中……");
		ListeningMode mode=getListeningMode();
		if(mode==null)
		{
			getDataExchanger().addItem(LISTENMODE, ListeningMode.PRIVATE.modeName);
			mode=ListeningMode.PRIVATE;
		}
		this.mode=mode;
	}
}
