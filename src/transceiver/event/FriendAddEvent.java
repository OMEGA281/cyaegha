package transceiver.event;

import global.UniversalConstantsTable;
import tools.TimeSimpleTool;

/**
 * 当有好友邀请的时候会生成该事件<br>
 * 本事件设定权限是没有意义的
 * @author GuoJiaCheng
 *
 */
public class FriendAddEvent extends Event
{
	long sendTime;
	String msg;
	String responseFlag;
	public FriendAddEvent(long userNum, String msg, String responseFlag)
	{
		type=UniversalConstantsTable.MSGTYPE_PERSON;
		this.userNum=userNum;
		
		this.sendTime=TimeSimpleTool.getNowTimeStamp();
		this.msg=msg;
		this.responseFlag=responseFlag;
	}
}
