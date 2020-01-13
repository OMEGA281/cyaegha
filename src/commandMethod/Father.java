package commandMethod;

import java.io.File;

import commandMethod.dataExchanger.AuthorityExchanger;
import commandMethod.dataExchanger.DataExchanger;
import commandMethod.dataExchanger.SettingExchanger;
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
	private AuthorityExchanger authorityExchanger;
	private DataExchanger dataExchanger;
	private SettingExchanger settingExchanger;
	
	public Father() 
	{
		// TODO Auto-generated constructor stub
		
	}
	public void setReceiveMessageType(ReceiveMessageType receiveMessageType) 
	{
		this.receiveMessageType = receiveMessageType;
	}
	/**初始化时启动的方法，所有的监听器全放在此处*/
	public abstract void initialize();
	public void setHelp(String help)
	{
		this.help=help;
	}
	/**获取本方法数据储存文件夹*/
	public String getPluginDataFloder()
	{
		String path=ConstantTable.ROOTPATH+ConstantTable.PLUGIN_DATAPATH+this.getClass().getSimpleName()+"\\";
		if(!new File(path).exists())
			FileSimpleIO.createFolder(path);
		return path;
	}
	/**获取本方法方法储存文件夹*/
	public String getPluginSettingFloder()
	{
		String path=ConstantTable.ROOTPATH+ConstantTable.PLUGIN_SETTINGPATH+this.getClass().getSimpleName()+"\\";
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
	/**若是由命令调用，则一定会更新，若是以listener调用，则需手动更新<code>receiveMessageType</code>*/
	public void sendBackMsg(String string)
	{
		if(string==null)
			return;
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
				, receiveMessageType.getfromGroup(), string));
	}
	/**
	 * 手动加载交换器<br>
	 * 在读写的时候都会进行检测与加载<br>
	 * 若数据量过多的话，为避免第一次加载的延迟，可手动加载<br>
	 */
	public void loadDataExchanger()
	{
		this.getClass().getName();
	}
	/**
	 * 手动加载交换器<br>
	 * 在读写的时候都会进行检测与加载<br>
	 * 若数据量过多的话，为避免第一次加载的延迟，可手动加载<br>
	 */
	public void loadAuthorityExchanger()
	{
		
	}
	/**
	 * 手动加载交换器<br>
	 * 在读写的时候都会进行检测与加载<br>
	 * 若数据量过多的话，为避免第一次加载的延迟，可手动加载<br>
	 */
	public void loadSettingExchanger()
	{
		
	}
}
