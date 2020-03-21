package transceiver.event;

import tools.TimeSimpleTool;

public class GroupAddEvent extends Event
{
	long TimeStamp;
	long GroupNum;
	long QQNum;
	String responseFlag;
	public GroupAddEvent(long fromGroup, long fromQQ, String responseFlag)
	{
		TimeStamp=TimeSimpleTool.getNowTimeStamp();
		GroupNum=fromQQ;
		QQNum=fromQQ;
		this.responseFlag=responseFlag;
	}
}
