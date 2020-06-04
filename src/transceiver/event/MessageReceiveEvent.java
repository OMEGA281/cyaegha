package transceiver.event;

import java.util.Iterator;
import java.util.List;

import org.meowy.cqp.jcq.message.CQCode;
import org.meowy.cqp.jcq.message.CoolQMsg;

import com.alibaba.fastjson.JSONObject;

import connection.CQSender;
import tools.TimeSimpleTool;

/**
 * 当受到信息的时候会触发该事件
 * @author GuoJiaCheng
 *
 */
public class MessageReceiveEvent extends Event
{
	int SubType;
	int MsgID;
	String fromAnonymous;
	String Msg;
	boolean shouldRespone;
	/**收到消息时的通用包形式
	 * @param MsgType 消息类型，常量池中有
	 * @param subType 子类型，常量池中有
	 * @param MsgID 消息的ID
	 * @param userNum 来源QQ
	 * @param groupNum 来源群号或讨论组号
	 * @param fromAnonymous 来源匿名者
	 * @param Msg 消息内容
	 */
	public MessageReceiveEvent(SourceType MsgType,int subType,int MsgID,long userNum,long groupNum,String fromAnonymous,String Msg) 
	{
		super(MsgType, userNum, groupNum, TimeSimpleTool.getNowTimeStamp());
		
		this.SubType=subType;
		this.MsgID=MsgID;
		this.fromAnonymous=fromAnonymous;
		this.Msg=Msg.trim();
		formatMsg();
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
	@Override
	public String toString()
	{
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("来源", type);
		jsonObject.put("来源人", userNum);
		if(type!=SourceType.PERSON)
		{
			jsonObject.put("群组", groupNum);
		}
		if(!shouldRespone)
			jsonObject.put("响应",shouldRespone);
		jsonObject.put("时间",time);
		jsonObject.put("序号",MsgID);
		jsonObject.put("信息",Msg);
		return jsonObject.toJSONString();
	}
	public SourceType getMsgType()
	{
		return type;
	}
	public int getSubType()
	{
		return SubType;
	}
	public int getMsgID()
	{
		return MsgID;
	}
	public long getTime()
	{
		return time;
	}
	public long getUserNum()
	{
		return userNum;
	}
	public long getGroupNum()
	{
		return groupNum;
	}
	public String getFromAnonymous()
	{
		return fromAnonymous;
	}
	public String getMsg()
	{
		return Msg;
	}
}
