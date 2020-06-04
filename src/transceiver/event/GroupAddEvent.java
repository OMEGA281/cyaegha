package transceiver.event;

import global.UniversalConstantsTable;
import tools.TimeSimpleTool;
/**
 * 当被邀请加入某群的时候会触发该事件<br>
 * 本事件设定权限是没有意义的
 * @author GuoJiaCheng
 *
 */
public class GroupAddEvent extends Event
{
	long TimeStamp;
	String responseFlag;
	public GroupAddEvent(long groupNum, long userNum, String responseFlag)
	{
		type=UniversalConstantsTable.MSGTYPE_PERSON;
		this.groupNum=groupNum;
		this.userNum=userNum;
		
		TimeStamp=TimeSimpleTool.getNowTimeStamp();
		this.responseFlag=responseFlag;
	}
}
