package commandMethod;

import java.io.File;

import commandMethod.register.OnGroupMemberChangeListener;
import commandMethod.register.OnMessageReceiveListener;
import commandMethod.register.OnMessageSendListener;
import commandMethod.register.Register;
import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.ConstantTable;
import tools.FileSimpleIO;
import transceiver.Transmitter;

public abstract class Father 
{
	/**若是由命令调用，则一定会更新本变量，若是以listener调用，则需手动更新*/
	ReceiveMessageType receiveMessageType;
	String help;
	
	public void setReceiveMessageType(ReceiveMessageType receiveMessageType) 
	{
		this.receiveMessageType = receiveMessageType;
	}
	public abstract void initialize();
	public void setHelp(String help)
	{
		this.help=help;
	}
	public String getPluginDataFloder()
	{
		String path=ConstantTable.ROOTPATH+ConstantTable.PLUGIN_DATAPATH+this.getClass().getName();
		if(!new File(path).exists())
			FileSimpleIO.createFolder(path);
		return path;
	}
	public String getPluginSettingFloder()
	{
		String path=ConstantTable.ROOTPATH+ConstantTable.PLUGIN_SETTINGPATH+this.getClass().getName();
		if(!new File(path).exists())
			FileSimpleIO.createFolder(path);
		return path;
	}
	public void addMessageReceiveListener(OnMessageReceiveListener messageReceiveListener)
	{
		Register.getRegister().messageReceiveListeners.add(messageReceiveListener);
	}
	public void addGroupMemberChangeListener(OnGroupMemberChangeListener groupMemberChangeListener)
	{
		Register.getRegister().groupMemberChangeListeners.add(groupMemberChangeListener);
	}
	public void addMessageSendListener(OnMessageSendListener messageSendListener)
	{
		Register.getRegister().messageSendListeners.add(messageSendListener);
	}
	/**若是由命令调用，则一定会正常，若是以listener调用，则需手动更新receiveMessageType*/
	public void sendBackMsg(String string)
	{
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
				, receiveMessageType.getfromGroup(), string));
	}
}
