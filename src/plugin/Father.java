package plugin;

import java.io.File;
import global.UniversalConstantsTable;
import plugin.dataExchanger.DataExchanger;
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
}
