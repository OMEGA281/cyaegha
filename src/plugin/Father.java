package plugin;

import java.io.File;

import connection.CQSender;
import global.UniversalConstantsTable;
import plugin.dataExchanger.DataExchanger;
import pluginHelper.AuthirizerListBook;
import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.AuxiliaryClass;
import tools.FileSimpleIO;
import transceiver.EventTrigger;
import transceiver.IdentitySymbol;
import transceiver.event.Event;
import transceiver.event.MessageSendEvent;

@AuxiliaryClass
public abstract class Father
{
	private DataExchanger dataExchanger;

	public Father()
	{
		// TODO Auto-generated constructor stub

	}

	public abstract void init();

	/** 获取本方法数据储存文件夹 */
	public String getPluginDataFloder()
	{
		String path = UniversalConstantsTable.PLUGIN_DATAPATH + this.getClass().getSimpleName() + "\\";
		if (!new File(path).exists())
			FileSimpleIO.createFolder(path);
		return path;
	}

	/** 若是由命令调用，则一定会更新，若是以listener调用，则需手动更新<code>receiveMessageType</code> */
	public void sendMsg(Event event, String msg)
	{
		sendMsg(event.getIdentitySymbol(), msg);
	}

	public void sendMsg(IdentitySymbol symbol, String msg)
	{
		EventTrigger.getEventTrigger().messageSend(new MessageSendEvent(symbol, msg));
	}

	/**
	 * 获得数据交换器，用于储存数据<br>
	 */
	public DataExchanger getDataExchanger()
	{
		if (dataExchanger == null)
			dataExchanger = new DataExchanger(
					UniversalConstantsTable.PLUGIN_DATAPATH + this.getClass().getSimpleName() + ".xml");
		return dataExchanger;
	}

	public boolean isWhite(String name, long num)
	{
		return AuthirizerListBook.getAuthirizerListBook().isWhite(name, num);
	}

	public boolean isBlack(String name, long num)
	{
		return AuthirizerListBook.getAuthirizerListBook().isBlack(name, num);
	}

	public boolean setWhite(String name, long num)
	{
		return AuthirizerListBook.getAuthirizerListBook().setWhite(name, num);
	}

	public boolean setBlack(String name, long num)
	{
		return AuthirizerListBook.getAuthirizerListBook().setBlack(name, num);
	}

	public boolean removeWhite(String name, long num)
	{
		return AuthirizerListBook.getAuthirizerListBook().removeWhite(name, num);
	}

	public boolean removeBlack(String name, long num)
	{
		return AuthirizerListBook.getAuthirizerListBook().removeBlack(name, num);
	}

	public long[] getAllWhite(String name)
	{
		return AuthirizerListBook.getAuthirizerListBook().getAllWhite(name);
	}

	public long[] getAllBlack(String name)
	{
		return AuthirizerListBook.getAuthirizerListBook().getAllBlack(name);
	}

	public AuthirizerUser getNormalAuthirizer(IdentitySymbol symbol)
	{
		if (symbol.userNum == AuthirizerListBook.getSOP())
			return AuthirizerUser.SUPER_OP;
		for (long op : AuthirizerListBook.getOP())
			if (op == symbol.userNum)
				return AuthirizerUser.OP;
		switch (symbol.type)
		{
		case PERSON:
			return AuthirizerUser.PERSON_CLIENT;
		case GROUP:
			switch (CQSender.getQQInfoInGroup(symbol.userNum, symbol.groupNum).getAuthority())
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
}
