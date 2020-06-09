package plugin;

import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.AuxiliaryClass;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import pluginHelper.annotations.RegistListener.GroupMemberChangeListener;
import transceiver.EventTrigger.EventResult;
import transceiver.IdentitySymbol;
import transceiver.event.GroupMemberChangeEvent;
import transceiver.event.MessageReceiveEvent;

@AuxiliaryClass
public class Welcome extends Father
{
	
	@Override
	public void init() 
	{
		
	}
	
	@GroupMemberChangeListener
	public EventResult welcomeSender(GroupMemberChangeEvent event)
	{
		String s=getString(event);
		if(s!=null)
			sendMsg(event, s);
		return EventResult.PASS;
	}
	
	@MinimumAuthority(AuthirizerUser.GROUP_MANAGER)
	@RegistCommand(CommandString = "删除群成员入群欢迎词")
	public void welcome(MessageReceiveEvent event)
	{
		deleteString(event);
		sendMsg(event,"删除成功");
	}
	@MinimumAuthority(AuthirizerUser.GROUP_MANAGER)
	@RegistCommand(CommandString = "设置群成员入群欢迎词")
	public void welcome(MessageReceiveEvent event,Object object)
	{
		setString(event,(String)object);
		sendMsg(event,"设置成功");
	}
	private void setString(IdentitySymbol symbol,String string)
	{
		String name = null;
		switch(symbol.type)
		{
		case PERSON:
			name="P"+symbol.userNum;
			break;
		case GROUP:
			name="G"+symbol.groupNum;
			break;
		case DISCUSS:
			name="D"+symbol.groupNum;
			break;
		}
		getDataExchanger().addItem(name, string);
	}
	private void deleteString(IdentitySymbol symbol)
	{
		String name = null;
		switch(symbol.type)
		{
		case PERSON:
			name="P"+symbol.userNum;
			break;
		case GROUP:
			name="G"+symbol.groupNum;
			break;
		case DISCUSS:
			name="D"+symbol.groupNum;
			break;
		}
		getDataExchanger().deleteItem(name);
	}
	private String getString(IdentitySymbol symbol)
	{
		String name = null;
		switch(symbol.type)
		{
		case PERSON:
			name="P"+symbol.userNum;
			break;
		case GROUP:
			name="G"+symbol.groupNum;
			break;
		case DISCUSS:
			name="D"+symbol.groupNum;
			break;
		}
		return getDataExchanger().getItem(name);
	}
}
