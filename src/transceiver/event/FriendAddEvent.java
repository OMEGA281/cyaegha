package transceiver.event;

import org.meowy.cqp.jcq.entity.IRequest;

import connection.CQSender;
import surveillance.Log;
import tools.TimeSimpleTool;

/**
 * 当有好友邀请的时候会生成该事件<br>
 * 本事件设定权限是没有意义的
 * 
 * @author GuoJiaCheng
 *
 */
public class FriendAddEvent extends Event
{
	String msg;
	public String responseFlag;
	public int hasProcessed = -1;

	public FriendAddEvent(long userNum, String msg, String responseFlag)
	{
		super(SourceType.PERSON, userNum, 0, TimeSimpleTool.getNowTimeStamp());

		this.msg = msg;
		this.responseFlag = responseFlag;
	}

	public boolean deal(boolean accept)
	{
		if (hasProcessed > 0)
			return false;
		boolean i = CQSender.setFriendAddRequest(this, accept);
		if (i)
			hasProcessed = accept ? IRequest.REQUEST_ADOPT : IRequest.REQUEST_REFUSE;
		Log.i(groupNum + "的邀请请求被" + (accept ? "同意" : "拒绝"));
		return i;
	}
}
