package transceiver.event;

import global.UniversalConstantsTable;
import tools.TimeSimpleTool;

/**
 * 当群中有人被禁言的时候会触发该事件<br>
 * 本事件设定权限是没有意义的
 * @author GuoJiaCheng
 *
 */
public class GroupBanEvent extends Event
{
	boolean banType;
	long opQQNum;
	long time;
	long duration;
	public GroupBanEvent(int subType, long fromGroup, long fromQQ, long beingOperateQQ, long duration)
	{
		type=UniversalConstantsTable.MSGTYPE_GROUP;
		userNum=fromQQ;
		groupNum=fromGroup;
		
		banType=subType==1;
		time=TimeSimpleTool.getNowTimeStamp();
		opQQNum=beingOperateQQ;
		this.duration=duration;
	}
}
