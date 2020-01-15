package commandMethod;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import connection.SendMessageType;
import surveillance.Log;
import tools.GetJarResources;
import tools.XMLDocument;
import transceiver.Transmitter;

public class Help extends Father
{

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	public void showHelp()
	{
		StringBuilder stringBuilder=new StringBuilder("命令如下：\n");
		Document document = null;
		try {
			document = XMLDocument.getDocument(
					new GetJarResources("CommandList.xml").getJarResources());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(document==null)
		{
			Log.e("未检测到命令列表");
			return;
		}
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
