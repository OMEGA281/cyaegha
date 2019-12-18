package commandPointer;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.ConstantTable;
import global.FindJarResources;
import global.xmlProcessor.XMLReader;
import surveillance.Log;
import transceiver.Transmitter;

public class Matcher 
{
	static Matcher matcher;
	List<CommandPackage> commandList=new ArrayList<CommandPackage>();
	class CommandPackage
	{
		String name;
		String help;
		String format;
		String method;
		public CommandPackage(String name,String help,String format,String method) 
		{
			// TODO Auto-generated constructor stub
			this.name=name;
			this.help=help;
			this.format=format;
			this.method=method;
		}
		CommandPackage()
		{
			
		}
		@Override
		public String toString() 
		{
			// TODO Auto-generated method stub
			return name+" "+help+" "+format+" "+method;
		}
	}
	/**根据方向将XML内的指令储存在List中*/
	public Matcher() 
	{
		// TODO Auto-generated constructor stub
		Document document= XMLReader.getXMLReader(FindJarResources.getFindJarResources()
				.getJarResources("CommandList.xml")).getDocument();
		List<Element> elements=document.getRootElement().getChildren("first");
		for (Element element : elements) 
		{
			CommandPackage commandPackage=new CommandPackage();
			commandPackage.name=element.getChild("string").getText();
			commandPackage.help=element.getChild("help").getText();
			commandPackage.format=element.getChild("format").getText();
			commandPackage.method=element.getChild("method").getText();
			commandList.add(commandPackage);
		}
		for (CommandPackage commandPackage : commandList) 
		{
			Log.i(commandPackage.toString());
		}
	} 
	public static Matcher getMatcher()
	{
		if(matcher==null)
			matcher=new Matcher();
		return matcher;
	}
	/**用于验证是否传递来的文字是否可能是命令*/
	public static boolean ifCommand(String s)
	{
		boolean result=false;
		String[] start=CommandIndicator.START_CHATACTER;
		for(int i=0;i<start.length;i++)
		{
			if(s.startsWith(start[i]))
			{
				result=true;
				break;
			}
		}
		return result;
	}
	/**用于检测是否存在该命令
	 * @param root 所处的父级命令 若为null则为根
	 * @param s 待检测语句*/
	private boolean ifCommandExist(String root,String s)
	{
		boolean result=false;
		for (CommandPackage commandPackage : commandList) 
		{
			if(s.toLowerCase().startsWith(commandPackage.name))
			{
				result=true;
				break;
			}
		}
		return result;
	}
	/**处理命令用的集成方法
	 * @param CommandMsg 源信息，不需要进行任何的处理*/
	public void CommandProcesser(ReceiveMessageType commandPackage)
	{
		StringBuilder stringBuilder=new StringBuilder(commandPackage.getMsg());
		stringBuilder.deleteCharAt(0);
		if(!ifCommandExist(null, stringBuilder.toString()))
		{
			Log.i("无效命令：",stringBuilder.toString());
			long toClient;
			switch (commandPackage.getMsgType()) 
			{
			case ConstantTable.MSGTYPE_PERSON:
				toClient=commandPackage.getfromQQ();
				break;
			case ConstantTable.MSGTYPE_GROUP:
				toClient=commandPackage.getfromGroup();
				break;
			case ConstantTable.MSGTYPE_DISCUSS:
				toClient=commandPackage.getfromGroup();
				break;
			default:
				Log.e("未发现相应的发送类型");
				toClient=-1;
				break;
			}
			
			Transmitter.getTransmitter().addMsg(
					new SendMessageType(commandPackage.getMsgType(), toClient, "无效命令"));
		}
		else
		{
			
		}
	}
}
