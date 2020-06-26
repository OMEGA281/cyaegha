package plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.meowy.cqp.jcq.message.CoolQMsg;

import connection.CQSender;
import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import surveillance.Log;
import tools.XMLDocument;
import transceiver.IdentitySymbol;
import transceiver.event.MessageReceiveEvent;

public class Draw extends Father
{
	public static String CARDPOOL_PATH;

	private int stack = 0;

	public Draw()
	{
		CARDPOOL_PATH = getPluginDataFloder();
	}

	@RegistCommand(CommandString = "draw",Help = "抽牌")
	public void draw(MessageReceiveEvent e, String name, Integer time)
	{
		Document file;
		try
		{
			file = getDraw(name);
		} catch (Exception e1)
		{
			sendMsg(e, e1.getMessage());
			return;
		}
		String string = advanceDraw(file, time, e);
		sendMsg(e, string);
	}

	@RegistCommand(CommandString = "draw",Help = "抽牌")
	public void draw(MessageReceiveEvent e, String name)
	{
		draw(e, name, 1);
	}

	@RegistCommand(CommandString = "draw",Help = "抽牌")
	public void draw(MessageReceiveEvent e, Integer time)
	{
		try
		{
			draw(e, getDefaultDrawName(e.getIdentitySymbol()), time);
		} catch (Exception e1)
		{
			sendMsg(e.getIdentitySymbol(), e1.getMessage());
			return;
		}
	}

	@RegistCommand(CommandString = "draw",Help = "抽牌")
	public void draw(MessageReceiveEvent e)
	{
		draw(e, 1);
	}

	@RegistCommand(CommandString = "draw list",Help = "列出所有牌库")
	public void drawlist(MessageReceiveEvent e)
	{
		ArrayList<String> arrayList = list();
		StringBuilder stringBuilder = new StringBuilder("目前存在着如下牌库：\n");
		for (String string : arrayList)
		{
			stringBuilder.append(string + "/");
		}
		if (stringBuilder.length() > 0)
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		sendMsg(e, stringBuilder.toString());
	}

	@MinimumAuthority(AuthirizerUser.GROUP_MANAGER)
	@RegistCommand(CommandString = "draw set",Help = "在本环境里设置默认牌库")
	public void drawset(MessageReceiveEvent event, String string)
	{
		ArrayList<String> arrayList = list();
		if (!arrayList.contains(string))
		{
			sendMsg(event, "未查询到牌库：" + string);
			return;
		}
		getDataExchanger().setItem(getMark(event.getIdentitySymbol()), string);
		sendMsg(event, "将" + string + "设置为默认牌库");
	}

	private String drawCard(ArrayList<String> pool)
	{
		Random random = new Random();
		return pool.get(random.nextInt(pool.size()));
	}

	private ArrayList<String> list()
	{
		File[] files = new File(getPluginDataFloder()).listFiles();
		ArrayList<String> arrayList = new ArrayList<String>();
		for (File file : files)
		{
			if (file.getName().endsWith(".xml"))
			{
				String filename = file.getName();
				String simpleName = filename.substring(0, filename.length() - 4);
				arrayList.add(simpleName);
			}
		}
		return arrayList;
	}

	private Document getDraw(String name) throws Exception
	{
		String file = CARDPOOL_PATH + name + ".xml";
		Document document;
		try
		{
			document = XMLDocument.getDocument(file, false);
		} catch (JDOMException e)
		{
			throw new Exception("读取出现错误！");
		} catch (IOException e)
		{
			throw new FileNotFoundException("找不到牌库：" + name);
		}
		return document;
	}

	private String getDefaultDrawName(IdentitySymbol symbol) throws Exception
	{
		String mark = getMark(symbol);
		String name = getDataExchanger().getItem(mark);
		if (name == null)
			throw new Exception("没有设置默认牌库");
		return name;
	}

	private String getMark(IdentitySymbol symbol)
	{
		String string;
		switch (symbol.type)
		{
		case PERSON:
			string = "P" + symbol.userNum;
			break;
		case GROUP:
			string = "G" + symbol.groupNum;
			break;
		case DISCUSS:
			string = "D" + symbol.groupNum;
			break;
		default:
			string = null;
			break;
		}
		return string;
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	private String advanceDraw(Document document, int num, IdentitySymbol symbol)
	{
		ArrayList<String> list = new ArrayList<>();
		int limit;

		Element root = document.getRootElement();
		Element ver = root.getChild("version");
		Element e_limit = root.getChild("limit");
		if (e_limit == null)
			limit = 10;
		try
		{
			limit = Integer.parseInt(e_limit.getText());
		} catch (Exception e)
		{
			Log.e("读取限制数字有问题");
			limit = 10;
		}
		if(num>limit)
			num=limit;
		int version;
		if (ver == null)
			version = 1;
		else
			try
			{
				version = Integer.parseInt(ver.getText());
			} catch (NumberFormatException e)
			{
				return null;
			}
		if (version < 2)
		{
			list.add(CQSender.getNickorCard(symbol) + "抽到了：\n");
			List<Element> es = document.getRootElement().getChildren();
			ArrayList<String> ss = new ArrayList<String>();
			for (Element element : es)
				ss.add(element.getText());
			for (int i = 0; i < num; i++)
				list.add(drawCard(ss) + "\n");
		} else
		{
			stack = 0;
			Element start = root.getChild("start");
			Element main = root.getChild("main");
			Element end = root.getChild("end");
			if (start != null)
				list.add(getText(root, start.getText()));
			if (main != null)
				for (int i = 0; i < num; i++)
					list.add(getText(root, main.getText()));
			if (end != null)
				list.add(getText(root, end.getText()));
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : list)
			stringBuilder.append(string);
		return stringBuilder.toString().replaceAll("@name#", CQSender.getNickorCard(symbol))
				.replaceAll("@at#", new CoolQMsg().at(symbol.userNum).msg())
				.replaceAll("@me#", CQSender.getMyName());
		
	}

	public String getText(Element root, String text)
	{
		stack++;
		if (stack > 30)
			return null;		
		
		if(text==null)
			return "";
		
		Pattern pattern = Pattern.compile("\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			int start = matcher.start();
			int end = matcher.end();
			String[] part = matcher.group(1).split(",");
			Random random = new Random();
			String name = part[random.nextInt(part.length)];
			String aim = getText(root, getElementText(root, name));
			if(aim==null)
				aim="";
			StringBuilder builder = new StringBuilder(text);
			builder.replace(start, end, aim);
			text = builder.toString();
			matcher = pattern.matcher(text);
		}
		stack--;
		return text;
	}

	private String getElementText(Element root, String element)
	{
		Element e = root.getChild(element);
		if (e == null)
			return null;
		else
			return getElementText(e);
	}

	private String getElementText(Element element)
	{
		if (element.getChildren().size() == 0)
			return element.getText();
		Element main = element.getChild("main");
		if (main != null)
			return main.getText();
		else
		{
			List<Element> elements = element.getChildren();
			Random random = new Random();
			Element choose = elements.get(random.nextInt(elements.size()));
			return choose.getText();
		}
	}
}
