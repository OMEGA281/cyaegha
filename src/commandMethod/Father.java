package commandMethod;

import java.io.File;
import java.io.StringReader;

import commandMethod.dataExchanger.DataExchanger;
import commandMethod.register.OnGroupMemberChangeListener;
import commandMethod.register.OnMessageReceiveListener;
import commandMethod.register.OnMessageSendListener;
import commandMethod.register.Register;
import commandPointer.annotations.AuxiliaryClass;
import connection.CQSender;
import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.UniversalConstantsTable;
import tools.FileSimpleIO;
import transceiver.Transmitter;

@AuxiliaryClass
public abstract class Father 
{
	/**若是由命令调用，则一定会更新本变量，若是以listener调用，则需手动更新*/
	ReceiveMessageType receiveMessageType;
	private DataExchanger dataExchanger;
	
	public Father() 
	{
		// TODO Auto-generated constructor stub
		
	}
//	/**
//	 * 用于初始化时加载字符串文件，一般来说加载之后不会再次通过方法内再次调用
//	 * @return
//	 */
//	public boolean getStringResourcer()
//	{
//		
//	}
	/**
	 * 本方法仅在命令调用时可以保持实时信息
	 * @return 当前消息发送者的昵称
	 */
	public String getMessageSenderName()
	{
		switch(getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			return CQSender.getQQInfo(receiveMessageType.getfromQQ()).getNick();
		case UniversalConstantsTable.MSGTYPE_GROUP:
			return CQSender.getQQInfoInGroup(receiveMessageType.getfromQQ(), receiveMessageType.getfromGroup()).getCard();
		}
		return "";
	}
	/**
	 * 获得消息的类型，来源于群或是好友等等
	 * 本方法仅在命令调用时可以保持实时信息
	 * @return 消息的类型
	 */
	public int getMsgType()
	{
		return receiveMessageType.getMsgType();
	}
	public void setReceiveMessageType(ReceiveMessageType receiveMessageType) 
	{
		this.receiveMessageType = receiveMessageType;
	}
	/**初始化时启动的方法，所有的监听器全放在此处*/
	public abstract void initialize();
	/**获取本方法数据储存文件夹*/
	public String getPluginDataFloder()
	{
		String path=UniversalConstantsTable.ROOTPATH+UniversalConstantsTable.PLUGIN_DATAPATH+this.getClass().getSimpleName()+"\\";
		if(!new File(path).exists())
			FileSimpleIO.createFolder(path);
		return path;
	}
	/**获取本方法设置储存文件夹*/
	public String getPluginSettingFloder()
	{
		String path=UniversalConstantsTable.ROOTPATH+UniversalConstantsTable.PLUGIN_SETTINGPATH+this.getClass().getSimpleName()+"\\";
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
		if(string.endsWith("\n"))
			string=string.substring(0, string.length()-1);
		if(string.isEmpty())
			return;
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
				, receiveMessageType.getfromGroup(), string));
	}
	public void sendPrivateMsg(long QQ,String string)
	{
		if(string==null)
			return;
		if(string.endsWith("\n"))
			string=string.substring(0, string.length()-1);
		if(string.isEmpty())
			return;
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				UniversalConstantsTable.MSGTYPE_PERSON, QQ, 0, string));
	}
	public void sendGroupMsg(long group,long QQ,String string)
	{
		if(string==null)
			return;
		if(string.endsWith("\n"))
			string=string.substring(0, string.length()-1);
		if(string.isEmpty())
			return;
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				UniversalConstantsTable.MSGTYPE_GROUP, QQ, group, string));
	}
	public void sendDiscussMsg(long group,long QQ,String string)
	{
		if(string==null)
			return;
		if(string.endsWith("\n"))
			string=string.substring(0, string.length()-1);
		if(string.isEmpty())
			return;
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				UniversalConstantsTable.MSGTYPE_DISCUSS, QQ, group, string));
	}
	/**
	 * 获得数据交换器，用于储存数据<br>
	 */
	public DataExchanger getDataExchanger()
	{
		if(dataExchanger==null)
			dataExchanger=new DataExchanger(UniversalConstantsTable.ROOTPATH+UniversalConstantsTable.PLUGIN_DATAPATH+this.getClass().getSimpleName()+".xml");
		return dataExchanger;
	}
	
//	public String replaceString(String index,String...strings)
//	{
//		
//	}
}
