package commandMethod;

import connection.ReceiveMessageType;

public abstract class Father 
{
	ReceiveMessageType receiveMessageType;
	public void setReceiveMessageType(ReceiveMessageType receiveMessageType) 
	{
		this.receiveMessageType = receiveMessageType;
	}
}
