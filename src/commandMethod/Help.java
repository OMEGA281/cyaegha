package commandMethod;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import connection.SendMessageType;
import global.FindJarResources;
import global.xmlProcessor.XMLReader;
import transceiver.Transmitter;

public class Help extends Father
{

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	public void showHelp()
	{
		StringBuilder stringBuilder=new StringBuilder("命令如下");
		Document document=XMLReader.getXMLReader(FindJarResources.getFindJarResources()
				.getJarResources("CommandList.xml")).getDocument();
		List<Element> elements=document.getRootElement().getChildren();
		for (Element element : elements) {
			stringBuilder.append(".");
			stringBuilder.append(element.getChildText("string"));
			stringBuilder.append(" ");
			stringBuilder.append(element.getChildText("help"));
			stringBuilder.append("\n");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		Transmitter.getTransmitter().addMsg(new SendMessageType(
				receiveMessageType.getMsgType(), receiveMessageType.getfromQQ(),
				receiveMessageType.getfromGroup(), stringBuilder.toString()));
	}
}
