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
	} 
	public static Matcher getMatcher()
	{
		if(matcher==null)
		{
			matcher=new Matcher();
			Log.d("初始化命令匹配器");
		}
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
	 * @param s 待检测语句
	 * @return 返回搜寻到的命令集，如果没有则返回空*/
	private CommandPackage ifCommandExist(String root,String s)
	{
		String[] commandString=s.toLowerCase().split(" ");
		for (CommandPackage commandPackage : commandList) 
		{
			if(commandString[0].equals(commandPackage.name))
			{
				return commandPackage;
			}
		}
		return null;
	}
	/**处理命令用的集成方法
	 * @param CommandMsg 源信息，不需要进行任何的处理*/
	public void CommandProcesser(ReceiveMessageType messagePackage)
	{
		StringBuilder stringBuilder=new StringBuilder(messagePackage.getMsg());
		stringBuilder.deleteCharAt(0);
		CommandPackage commandPackage=ifCommandExist(null, stringBuilder.toString());
		if(commandPackage==null)
		{
			Log.i("无效命令：",stringBuilder.toString());
			Transmitter.getTransmitter().addMsg(new SendMessageType(messagePackage.getMsgType()
					, messagePackage.getfromQQ(),messagePackage.getfromGroup(), "无效命令"));
		}
		else
		{
			Reflector.getReflector(Formatter.getClassName(commandPackage.method))
				.startMethod(Formatter.getMethodName(commandPackage.method),messagePackage);
		}
	}
}
