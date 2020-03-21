package transceiver.event;

import tools.TimeSimpleTool;

/**
 * 当有好友邀请的时候会生成该事件
 * @author GuoJiaCheng
 *
 */
public class FriendAddEvent extends Event
{
	long sendTime;
	long fromQQ;
	String msg;
	String responseFlag;
	public FriendAddEvent(long fromQQ, String msg, String responseFlag)
	{
		this.sendTime=TimeSimpleTool.getNowTimeStamp();
		this.fromQQ=fromQQ;
		this.msg=msg;
		this.responseFlag=responseFlag;
	}
}
