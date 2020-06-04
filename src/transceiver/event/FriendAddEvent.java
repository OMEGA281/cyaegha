package transceiver.event;

import tools.TimeSimpleTool;

/**
 * 当有好友邀请的时候会生成该事件<br>
 * 本事件设定权限是没有意义的
 * @author GuoJiaCheng
 *
 */
public class FriendAddEvent extends Event
{
	String msg;
	String responseFlag;
	public FriendAddEvent(long userNum, String msg, String responseFlag)
	{
		super(SourceType.PERSON, userNum, 0, TimeSimpleTool.getNowTimeStamp());
		
		this.msg=msg;
		this.responseFlag=responseFlag;
	}
}
