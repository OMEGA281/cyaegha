package transceiver.event;

import com.alibaba.fastjson.JSONObject;
import tools.TimeSimpleTool;
import transceiver.IdentitySymbol;

public class MessageSendEvent extends Event
{
	public String Msg;

	public MessageSendEvent(SourceType type, long userNum, long groupNum, String Msg)
	{
		super(type, userNum, groupNum, TimeSimpleTool.getNowTimeStamp());

		this.Msg = Msg;
	}

	public MessageSendEvent(IdentitySymbol symbol, String Msg)
	{
		this(symbol.type, symbol.userNum, symbol.groupNum, Msg);
	}

	@Override
	public String toString()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("去向", type == SourceType.PERSON ? "私聊" : "群组");
		jsonObject.put("号码", type == SourceType.PERSON ? userNum : groupNum);
		jsonObject.put("时间", time);
		jsonObject.put("消息", Msg);
		return jsonObject.toJSONString();
	}
}
