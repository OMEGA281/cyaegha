package plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import connection.CQSender;
import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import tools.XMLDocument;
import transceiver.IdentitySymbol;
import transceiver.event.MessageReceiveEvent;

public class Draw extends Father
{
	public static String CARDPOOL_PATH;

	public Draw()
	{
		CARDPOOL_PATH = getPluginDataFloder();
	}

	@RegistCommand(CommandString = "draw",Help = "抽牌")
	public void draw(MessageReceiveEvent e, String name, int time)
	{
		ArrayList<String> pool;
		try
		{
			pool = getDraw(name);
		} catch (Exception e1)
		{
			sendMsg(e.getIdentitySymbol(), e1.getMessage());
			return;
		}
		StringBuilder builder = new StringBuilder(CQSender.getNickorCard(e.getIdentitySymbol()) + "抽到了：\n");
		for (int i = 1; i <= time; i++)
		{
			builder.append(drawCard(pool) + "\n");
		}
	}

	@RegistCommand(CommandString = "draw",Help = "抽牌")
	public void draw(MessageReceiveEvent e, int time)
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
		getDataExchanger().addItem(getMark(event.getIdentitySymbol()), string);
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

	private ArrayList<String> getDraw(String name) throws Exception
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
		List<Element> es = document.getRootElement().getChildren();
		ArrayList<String> ss = new ArrayList<String>();
		for (Element element : es)
			ss.add(element.getText());
		return ss;
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
}
