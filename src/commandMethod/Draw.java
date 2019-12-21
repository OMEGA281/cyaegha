package commandMethod;


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

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	public void drawDefault()
	{
		Document document=XMLReader.getXMLReader(FindJarResources.getFindJarResources()
				.getJarResources("Touhou.xml")).getDocument();
		List<Element> element=document.getRootElement().getChildren("character");
		Random random=new Random();
		int x=random.nextInt(element.size()-1);
		String s=element.get(x).getText();
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				receiveMessageType.getMsgType(), receiveMessageType.getfromQQ()
				, receiveMessageType.getfromGroup(), s+"\n(仅仅是测试方法，没集成没参数没命令没帮助)"));
	}
}
