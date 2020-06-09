package transceiver.event;

import org.meowy.cqp.jcq.entity.IRequest;

import connection.CQSender;
import tools.TimeSimpleTool;
/**
 * 当被邀请加入某群的时候会触发该事件<br>
 * 本事件设定权限是没有意义的
 * @author GuoJiaCheng
 *
 */
public class GroupAddEvent extends Event
{
	public String responseFlag;
	public int hasProcessed=-1;
	public GroupAddEvent(long groupNum, long userNum, String responseFlag)
	{
		super(SourceType.PERSON, userNum, groupNum, TimeSimpleTool.getNowTimeStamp());
		this.responseFlag=responseFlag;
	}
	public boolean deal(boolean accept)
	{
		if(hasProcessed>0)
			return false;
		boolean i=CQSender.setGroupAddRequest(this, accept);
		if(i)
			hasProcessed=accept?IRequest.REQUEST_ADOPT:IRequest.REQUEST_REFUSE;
		return i;
	}
}
