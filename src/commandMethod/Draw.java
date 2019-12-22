package commandMethod;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;

import connection.SendMessageType;
import global.FindJarResources;
import global.xmlProcessor.XMLReader;
import surveillance.Log;
import transceiver.Transmitter;

public class Draw extends Father
{
	public static final String CARDPOOL_PATH="draw/";
	static String defaultCardPool;
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	public void drawDefault()
	{
		if(defaultCardPool==null)
			sendMsgBack("您尚未定义默认牌库，您可以使用\\.draw set …来设置默认牌库");
		else
		{
			String s=drawCard(defaultCardPool);
			if(s!=null)
			{
				sendMsgBack(drawCard(defaultCardPool));
			}
		}
	}
	public void commandPointer(ArrayList<String> arrayList)
	{
		switch (arrayList.get(0)) 
		{
		case "set":
			if(arrayList.size()<2)
				break;
			String cardPool=arrayList.get(1);
			if(!cardPool.endsWith("\\.xml"))
				cardPool=cardPool+".xml";
			if(FindJarResources.getFindJarResources().getJarResources(cardPool)==null)
				sendMsgBack("未查询到牌库："+cardPool.substring(0, cardPool.length()-4));
			else
			{
				defaultCardPool=cardPool;
				sendMsgBack("设置默认牌库成功");
			}
			break;

		default:
			break;
		}
	}
	public void commandPointer()
	{
		drawDefault();
	}
	private String drawCard(String cardPoolFile)
	{
		Document document;
		try
		{
			document=XMLReader.getXMLReader(FindJarResources.getFindJarResources()
					.getJarResources(cardPoolFile)).getDocument();
		}catch(NullPointerException exception)
		{
			Log.e("未查询到牌库：",cardPoolFile);
			sendMsgBack("未查询到牌库:"+cardPoolFile);
			return null;
		}
		List<Element> element=document.getRootElement().getChildren();
		Random random=new Random();
		int x=random.nextInt(element.size()-1);
		return element.get(x).getText();
	}
	private void sendMsgBack(String s) 
	{
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
				, receiveMessageType.getfromGroup(), s));
	}
}
