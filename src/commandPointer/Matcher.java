package commandPointer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.UniversalConstantsTable;
import surveillance.Log;
import tools.GetJarResources;
import tools.XMLDocument;
import transceiver.Transmitter;

public class Matcher 
{
	static Matcher matcher;
	List<CommandPackage> commandList=new ArrayList<CommandPackage>();
	class CommandPackage
	{
		String name;
		String help;
		String method;
		public CommandPackage(String name,String help,String method) 
		{
			// TODO Auto-generated constructor stub
			this.name=name;
			this.help=help;
			this.method=method;
		}
		CommandPackage()
		{
			
		}
		@Override
		public String toString() 
		{
			// TODO Auto-generated method stub
			return name+" "+help+" "+" "+method;
		}
	}
	/**根据方向将XML内的指令储存在List中*/
	public Matcher() 
	{
		// TODO Auto-generated constructor stub
		Document document = null;
		try {
			document = XMLDocument.getDocument(new GetJarResources("CommandList.xml")
					.getJarResources());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(document==null)
		{
			Log.f("未发现命令列表！");
			return;
		}
		List<Element> elements=document.getRootElement().getChildren();
		for (Element element : elements) 
		{
			CommandPackage commandPackage=new CommandPackage();
			commandPackage.name=element.getChild("string").getText();
			commandPackage.help=element.getChild("help").getText();
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
		//FIXME:
		String[] start= {".","。"};
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
	/**用于检测是否存在该命令<br>
	 * 匹配原则：优先按照字符串全部分隔开匹配，若未检测到命令则去除所有空格<br>
	 * 越长的命令的优先级越高
	 * 同时会删除s的前面的命令部分
	 * @param s 待检测语句,删除了前面的点，以及前后的空格
	 * @return 返回搜寻到的命令集，如果没有则返回空*/
	private CommandPackage ifCommandExist(StringBuilder s)
	{
		CommandPackage result = null;
		for (CommandPackage commandPackage : commandList) 
		{
			if(s.toString().startsWith(commandPackage.name))
			{
				if(result!=null)
				{
					if(result.name.length()<commandPackage.name.length())
						result=commandPackage;
				}
				else
					result=commandPackage;
			}
		}
		if(result==null)
		{
			for (CommandPackage commandPackage : commandList) 
			{
				if(s.toString().replaceAll(" ", "").startsWith(commandPackage.name.replaceAll(" ", "")))
				{
					if(result!=null)
					{
						if(result.name.replaceAll(" ", "").length()<commandPackage.name.replaceAll(" ", "").length())
							result=commandPackage;
					}
					else
						result=commandPackage;
				}
			}
		}
		if(result!=null)
		{
			String command=result.name;
			for (char c : command.toCharArray())
			{
				if(c==' '&&s.charAt(0)!=' ')
					continue;
				if(s.charAt(0)==' '||s.charAt(0)==c)
				{
					s.deleteCharAt(0);
				}
				else
				{
					Log.e("命令处理时出现异常情况！");
					return null;
				}
			}
		}
		return result;
	}
	/**启动命令用的集成方法
	 * @param messagePackage 源信息，不需要进行任何的处理*/
	public void CommandProcesser(ReceiveMessageType messagePackage)
	{
		StringBuilder stringBuilder=new StringBuilder(messagePackage.getMsg().trim());
		stringBuilder.deleteCharAt(0);
		CommandPackage commandPackage=ifCommandExist(stringBuilder);
		
		String[] commandparts=stringBuilder.toString().trim().split(" ");
		ArrayList<String> params=new ArrayList<String>();
		for(int i=0;i<commandparts.length;i++)
		{
			if(!commandparts[i].isEmpty())
				params.add(commandparts[i]);
		}
		
		
		if(commandPackage==null)
		{
			Log.i("无效命令：",stringBuilder.toString());
//			Transmitter.getTransmitter().addMsg(new SendMessageType(messagePackage.getMsgType()
//					, messagePackage.getfromQQ(),messagePackage.getfromGroup(), "无效命令\n您可以使用.help来显示所有命令"));
		}
		else
		{
			Reflector.getReflector(Formatter.getClassName(commandPackage.method))
			.startMethod(Formatter.getMethodName(commandPackage.method), messagePackage, params);
		}
	}
}
