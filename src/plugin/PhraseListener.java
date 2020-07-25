package plugin;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
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
		ArrayList<String> list = getDataExchanger().getList(LISTNAME);
		if (list == null)
			return EventResult.PASS;
		for (String string : list)
		{
			String[] ss=string.split(Split,2);
			if(ss.length<2)
				continue;
			String patternString=ss[0];
			Pattern pattern = Pattern.compile(patternString);
			if (pattern.matcher(event.getMsg()).matches())
			{
				String[] answer = ss[1].split(Split);
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
		ArrayList<String> texts = getDataExchanger().getList(LISTNAME);
//		完全空的表
		if (texts == null||texts.isEmpty())
		{
			getDataExchanger().addList(LISTNAME, index1 + Split + index2,false);
			sendMsg(event, "成功添加了条目");
			return;
		}
//		表中有了
		for (int i = 0; i < texts.size(); i++)
		{
			String string=texts.get(i);
			String[] ss = string.split(Split,2);
			String pattern;
			String[] answer;
			if(ss.length<2)
			{
				getDataExchanger().deleteList(LISTNAME, string);
				continue;
			}
			else
			{
				pattern=ss[0];
				answer=ss[1].split(Split);
			}
			if (!pattern.equals(index1))
				continue;
			for (String s : answer)
				if (s.equals(index2))
				{
					sendMsg(event, "表中已有相同条目");
					return;
				}
			getDataExchanger().deleteList(LISTNAME, string);
			getDataExchanger().addList(LISTNAME, string+Split+answer,false);
			sendMsg(event, "成功添加了条目");
			return;
		}
		getDataExchanger().addList(LISTNAME,  index1 + Split + index2,false);
		sendMsg(event, "成功添加了条目");
	}

	@MinimumAuthority(AuthirizerUser.OP)
	@RegistCommand(CommandString = "answer delete",Help = "删除一个监听器回复")
	public void deleteanswer(MessageReceiveEvent event, Object object1, Object object2)
	{
		String patten = (String) object1;
		String aim = (String) object2;

		ArrayList<String> list = getDataExchanger().getList(LISTNAME);
		boolean b = false;
		for (int i = 0; i < list.size(); i++)
		{
			String text=list.get(i);
			String[] source=text.split(Split, 2);
			String pattern;
			String[] answer;
			if(source.length<2)
			{
				getDataExchanger().deleteList(LISTNAME, text);
				continue;
			}
			else
			{
				pattern=source[0];
				answer=source[1].split(Split);
			}
			
			if (!pattern.equals(patten))
				continue;
			if (aim != null&&!aim.isEmpty())
			{
				boolean find = false;
				for (int o = 0; o < answer.length; o++)
				{
					String string = answer[o];
					if (string.equals(aim))
					{
						answer[o] = "";
						find = true;
						break;
					}
				}
				if (find)
				{
					StringBuffer buffer = new StringBuffer();
					for (String string : answer)
						if (!string.isEmpty())
							buffer.append(string + Split);
					b = getDataExchanger().deleteList(LISTNAME, text);
					if (buffer.length() > 0)
						getDataExchanger().addList(LISTNAME,pattern + Split + buffer.toString());
					sendMsg(event, b ? "成功删除了条目" : "没有这个条目");
					return;
				} else
				{
					sendMsg(event, "未检测指定的回复");
					return;
				}
			} else
			{
				b = getDataExchanger().deleteList(LISTNAME, text);
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

		ArrayList<String> list = getDataExchanger().getList(LISTNAME);
		boolean b = false;
		for (int i = 0; i < list.size(); i++)
		{
			String text = list.get(i);
			String pattern = text.split(Split, 2)[0];
			if (!pattern.equals(patten))
				continue;

			b = getDataExchanger().deleteList(LISTNAME, text);
			break;
		}
		sendMsg(event, b ? "成功删除了条目" : "没有这个条目");
	}

	@Override
	public void personDelete(long num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupDelete(long num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discussDelete(long num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void switchOff()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllDate()
	{
		// TODO Auto-generated method stub
		
	}
}
