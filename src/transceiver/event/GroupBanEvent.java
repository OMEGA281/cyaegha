package transceiver.event;

import tools.TimeSimpleTool;

/**
 * 当群中有人被禁言的时候会触发该事件<br>
 * 本事件设定权限是没有意义的
 * 
 * @author GuoJiaCheng
 *
 */
public class GroupBanEvent extends Event
{
	boolean banType;
	long opQQNum;
	long duration;

	public GroupBanEvent(int subType, long groupNum, long userNum, long beingOperateQQ, long duration)
	{
		super(SourceType.GROUP, userNum, groupNum, TimeSimpleTool.getNowTimeStamp());

		banType = subType == 1;
		opQQNum = beingOperateQQ;
		this.duration = duration;
	}
}
