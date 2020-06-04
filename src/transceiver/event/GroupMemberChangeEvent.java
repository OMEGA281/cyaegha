package transceiver.event;

import global.UniversalConstantsTable;
import tools.TimeSimpleTool;

/**
 * 当群内人员发生变化的时候将触发该事件<br>
 * 本事件设定权限是没有意义的
 * @author GuoJiaCheng
 *
 */
public class GroupMemberChangeEvent extends Event
{
	boolean increase;
	boolean byAdmin;
	long time;
	long adminNum;
	public GroupMemberChangeEvent(boolean ifIncrease, int subtype, long groupNum, long userNum, long beingOperateQQ) 
	{
		type=UniversalConstantsTable.MSGTYPE_GROUP;
		this.userNum=userNum;
		this.groupNum=groupNum;
		
		increase=ifIncrease;
		byAdmin=subtype==1;
		time=TimeSimpleTool.getNowTimeStamp();
		adminNum=beingOperateQQ;
	}
}
