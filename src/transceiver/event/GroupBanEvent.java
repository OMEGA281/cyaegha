package transceiver.event;

import tools.TimeSimpleTool;

public class GroupBanEvent extends Event
{
	boolean type;
	long time;
	long QQNum;
	long opQQNum;
	long GroupNum;
	long duration;
	public GroupBanEvent(int subType, long fromGroup, long fromQQ, long beingOperateQQ, long duration)
	{
		type=subType==1;
		time=TimeSimpleTool.getNowTimeStamp();
		QQNum=fromQQ;
		opQQNum=beingOperateQQ;
		GroupNum=fromGroup;
		this.duration=duration;
	}
}
