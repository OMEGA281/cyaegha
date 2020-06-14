package transceiver.event;

import tools.TimeSimpleTool;

/**
 * 当群内人员发生变化的时候将触发该事件<br>
 * 本事件设定权限是没有意义的
 * 
 * @author GuoJiaCheng
 *
 */
public class GroupMemberChangeEvent extends Event
{
	public boolean increase;
	public boolean byAdmin;
	public long adminNum;

	public GroupMemberChangeEvent(boolean ifIncrease, int subtype, long groupNum, long userNum, long beingOperateQQ)
	{
		super(SourceType.GROUP, userNum, groupNum, TimeSimpleTool.getNowTimeStamp());

		increase = ifIncrease;
		byAdmin = subtype == 1;
		adminNum = beingOperateQQ;
	}
}
