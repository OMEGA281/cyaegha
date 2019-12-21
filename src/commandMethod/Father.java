package commandMethod;

import commandMethod.register.OnGroupMemberChangeListener;
import commandMethod.register.OnMessageReceiveListener;
import commandMethod.register.OnMessageSendListener;
import commandMethod.register.Register;
import connection.ReceiveMessageType;

public abstract class Father 
{
	ReceiveMessageType receiveMessageType;
	
	public void setReceiveMessageType(ReceiveMessageType receiveMessageType) 
	{
		this.receiveMessageType = receiveMessageType;
	}
	public abstract void initialize();
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
}
