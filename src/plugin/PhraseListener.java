package plugin;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import pluginHelper.annotations.RegistListener.MessageReceiveListener;
import transceiver.EventTrigger.EventResult;
import transceiver.event.MessageReceiveEvent;

public class PhraseListener extends Father
{
	private static final String Split = "##";
	private static final String LISTNAME = "return";
	private static final String HEAD = "head";

	@MessageReceiveListener
	public EventResult listener(MessageReceiveEvent event)
	{
		ArrayList<String[]> arrayList = getDataExchanger().getList(LISTNAME);
		if (arrayList == null)
			return EventResult.PASS;
		for (String[] strings : arrayList)
		{
			String $patten = strings[1].split(Split, 2)[0];
			Pattern pattern = Pattern.compile($patten);
			if (pattern.matcher(event.getMsg()).matches())
			{
				String[] answer = strings[1].split(Split, 2)[1].split(Split);
				if (answer.length == 1)
					sendMsg(event, answer[0]);
				else
				{
					Random random = new Random();
					sendMsg(event, answer[random.nextInt(answer.length)]);
					return EventResult.PASS;
				}
			}
		}
		return EventResult.PASS;
	}

	@MinimumAuthority(AuthirizerUser.OP)
	@RegistCommand(CommandString = "answer add",Help = "添加一个监听器回复")
	public void addanswer(MessageReceiveEvent event, Object object1, Object object2)
	{
		String index1 = (String) object1;
		String index2 = (String) object2;
		ArrayList<String[]> texts = getDataExchanger().getList(LISTNAME);
//		完全空的表
		if (texts == null)
		{
			getDataExchanger().setListItem(LISTNAME, HEAD, index1 + Split + index2);
			sendMsg(event, "成功添加了条目");
			return;
		}
//		表中有了
		for (int i = 0; i < texts.size(); i++)
		{
			String[] strings = texts.get(i);
			String line = strings[1];
			String patten = line.split(Split, 2)[0];
			if (!patten.equals(index1))
				continue;
			String[] answer = line.split(Split, 2)[1].split(Split);
			for (String string : answer)
			{
				if (string.equals(index2))
				{
					sendMsg(event, "表中已有相同条目");
					return;
				}
			}
			StringBuffer buffer = new StringBuffer();
			for (String string : answer)
			{
				buffer.append(string + Split);
			}
			buffer.append(index2);
			getDataExchanger().deleteListItem(LISTNAME, i);
			getDataExchanger().setListItem(LISTNAME, HEAD, patten + Split + buffer.toString());
			sendMsg(event, "成功添加了条目");
			return;
		}
		getDataExchanger().setListItem(LISTNAME, HEAD, index1 + Split + index2);
		sendMsg(event, "成功添加了条目");
	}

	@MinimumAuthority(AuthirizerUser.OP)
	@RegistCommand(CommandString = "answer delete",Help = "删除一个监听器回复")
	public void deleteanswer(MessageReceiveEvent event, Object object1, Object object2)
	{
		String patten = (String) object1;
		String aim = (String) object2;

		ArrayList<String[]> list = getDataExchanger().getList(LISTNAME);
		boolean b = false;
		for (int i = 0; i < list.size(); i++)
		{
			String text = list.get(i)[1];
			String $patten = text.split(Split, 2)[0];
			if (!$patten.equals(patten))
				continue;
			String[] $answer = text.split(Split, 2)[1].split(Split);
			if (aim != null)
			{
				boolean find = false;
				for (int o = 0; o < $answer.length; o++)
				{
					String string = $answer[o];
					if (string.equals(aim))
					{
						$answer[o] = "";
						find = true;
						break;
					}
				}
				if (find)
				{
					StringBuffer buffer = new StringBuffer();
					for (String string : $answer)
					{
						if (!string.isEmpty())
						{
							buffer.append(string + Split);
						}
					}
					b = getDataExchanger().deleteListItem(LISTNAME, i);
					if (buffer.length() > 0)
						getDataExchanger().setListItem(LISTNAME, HEAD, $patten + Split + buffer.toString());
					sendMsg(event, b ? "成功删除了条目" : "没有这个条目");
					return;
				} else
				{
					sendMsg(event, "未检测指定的回复");
					return;
				}
			} else
			{
				b = getDataExchanger().deleteListItem(LISTNAME, i);
				break;
			}
		}
		sendMsg(event, b ? "成功删除了条目" : "没有这个条目");
	}

	@MinimumAuthority(AuthirizerUser.OP)
	@RegistCommand(CommandString = "answer delete",Help = "删除一个监听器回复")
	public void deleteanswer(MessageReceiveEvent event, Object object1)
	{
		String patten = (String) object1;

		ArrayList<String[]> list = getDataExchanger().getList(LISTNAME);
		boolean b = false;
		for (int i = 0; i < list.size(); i++)
		{
			String text = list.get(i)[1];
			String $patten = text.split(Split, 2)[0];
			if (!$patten.equals(patten))
				continue;

			b = getDataExchanger().deleteListItem(LISTNAME, i);
			break;
		}
		sendMsg(event, b ? "成功删除了条目" : "没有这个条目");
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}
}
