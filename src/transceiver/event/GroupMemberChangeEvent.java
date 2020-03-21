package transceiver.event;

import tools.TimeSimpleTool;

public class GroupMemberChangeEvent extends Event
{
	boolean Increase;
	boolean byAdmin;
	long time;
	long GroupNum;
	long QQNum;
	long AdminNum;
	public GroupMemberChangeEvent(boolean ifIncrease, int subtype, long fromGroup, long fromQQ, long beingOperateQQ) 
	{
		// TODO Auto-generated constructor stub
		Increase=ifIncrease;
		byAdmin=subtype==1;
		time=TimeSimpleTool.getNowTimeStamp();
		GroupNum=fromGroup;
		QQNum=fromQQ;
		AdminNum=beingOperateQQ;
	}
}
