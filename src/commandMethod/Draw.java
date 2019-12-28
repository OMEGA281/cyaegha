package commandMethod;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;

import connection.SendMessageType;
import global.xmlProcessor.XMLReader;
import surveillance.Log;
import tools.GetJarResources;
import transceiver.Transmitter;

public class Draw extends Father
{
	public static final String CARDPOOL_PATH="draw/";
	static String defaultCardPool;
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	public String drawDefault()
	{
		if(defaultCardPool==null)
			sendBackMsg("您尚未定义默认牌库，您可以使用.draw set …来设置默认牌库");
		else
		{
			String s=drawCard(defaultCardPool);
			return s;
		}
		return null;
	}
	public void commandPointer(ArrayList<String> arrayList)
	{
		String subCommand=arrayList.get(0);
		code_0:switch (subCommand) 
		{
		case "set":
			if(arrayList.size()<2)
				break;
			String cardPool=arrayList.get(1);
			if(!cardPool.endsWith("\\.xml"))
				cardPool=cardPool+".xml";
			if(GetJarResources.getJarResources(cardPool)==null)
				sendBackMsg("未查询到牌库："+cardPool.substring(0, cardPool.length()-4));
			else
			{
				defaultCardPool=cardPool;
				sendBackMsg("设置默认牌库成功");
			}
			break;

		default:
			if(Pattern.compile("[0-9]*").matcher(subCommand).matches())
			{
				int times=Integer.parseInt(subCommand);
				if(times==0)
					break;
				if(times>20)
				{
					sendBackMsg("抽取次数过多");
					break;
				}
				StringBuilder sb=new StringBuilder();
				for(int i=1;i<=times;i++)
				{
					String s=drawDefault();
					if(s==null)
						break code_0;
					sb.append(drawDefault()+"\n");
				}
				sb.deleteCharAt(sb.length()-1);
				if(sb!=null)
					sendBackMsg(sb.toString());
			}
			break;
		}
	}
	public void commandPointer()
	{
		String s=drawDefault();
		if(s!=null)
			sendBackMsg(s);
	}
	
	private String drawCard(String cardPoolFile)
	{
		Document document;
		try
		{
			document=XMLReader.getXMLReader(GetJarResources.getJarResources(cardPoolFile)).getDocument();
		}catch(NullPointerException exception)
		{
			Log.e("未查询到牌库：",cardPoolFile);
			sendBackMsg("未查询到牌库:"+cardPoolFile);
			return null;
		}
		List<Element> element=document.getRootElement().getChildren();
		Random random=new Random();
		int x=random.nextInt(element.size()-1);
		return element.get(x).getText();
	}
}
