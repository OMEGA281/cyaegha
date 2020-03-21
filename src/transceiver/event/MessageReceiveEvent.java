package transceiver.event;

import java.util.Iterator;
import java.util.List;

import org.meowy.cqp.jcq.message.CQCode;
import org.meowy.cqp.jcq.message.CQMsg;
import org.meowy.cqp.jcq.message.CoolQMsg;

import connection.CQSender;
import tools.TimeSimpleTool;

public class MessageReceiveEvent extends Event
{
	int MsgType;
	int SubType;
	int MsgID;
	long fromQQ;
	long fromGroup;
	String fromAnonymous;
	String Msg;
	long time;
	boolean shouldRespone;
	/**收到消息时的通用包形式
	 * @param MsgType 消息类型，常量池中有
	 * @param subType 子类型，常量池中有
	 * @param MsgID 消息的ID
	 * @param fromQQ 来源QQ
	 * @param fromGroup 来源群号或讨论组号
	 * @param fromAnonymous 来源匿名者
	 * @param Msg 消息内容
	 */
	public MessageReceiveEvent(int MsgType,int subType,int MsgID,long fromQQ,long fromGroup,String fromAnonymous,String Msg) 
	{
		// TODO Auto-generated constructor stub
		this.MsgType=MsgType;
		this.SubType=subType;
		this.MsgID=MsgID;
		this.fromQQ=fromQQ;
		this.fromGroup=fromGroup;
		this.fromAnonymous=fromAnonymous;
		this.Msg=Msg;
		formatMsg();
		time=TimeSimpleTool.getNowTimeStamp();
	}
	private void formatMsg()
	{
		CoolQMsg msg=new CoolQMsg(Msg);
		List<String> list=msg.getTextsScreen(null, null, null);
		boolean isPoint=false;
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();)
		{
			String string = (String) iterator.next();
			long num=new CQCode().getAt(string);
			if(num!=-1000)
			{
				isPoint=true;
				if(num!=-1&&num!=CQSender.getMyQQ())
					shouldRespone=false;
				else
					shouldRespone=true;
				iterator.remove();
			}
			else
			{
				if(isPoint)
					break;
				shouldRespone=true;
			}
		}
		for (int i = 0; i < list.size(); i++)
		{
			String string = list.get(i);
			long num=new CQCode().getAt(string);
			if(num!=-1000&&num!=-1)
				string=Long.toString(num);
		}
		if(list.size()==0)
			Msg="";
		else
		{
			StringBuilder builder=new StringBuilder();
			for (String string : list)
				builder.append(string+" ");
			builder.deleteCharAt(builder.length()-1);
		}
	}
}
