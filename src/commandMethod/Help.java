package commandMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import commandPointer.annotations.RegistListener;
import surveillance.Log;
import tools.GetJarResources;
import tools.XMLDocument;

public class Help extends Father
{

	private class Command
	{
		String name;
		String help;
		String type;
		String point;
		String visible;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	public void showHelp()
	{
		StringBuilder stringBuilder=new StringBuilder("命令如下：\n");
		
		ArrayList<Command> arrayList=getCommands();
		ArrayList<String> type=new ArrayList<>();
		code_0:for (Command command : arrayList) {
			boolean visible=ifCommandVisible(command);
			if(visible)
			{
				if(command.type!=null)
				{
					for (String string : type) 
					{
						if(command.type.equals(string))
							continue code_0;
					}
					type.add(command.type);
				}
			}
		}
		
		for (Command command : arrayList) {
			boolean visible=true;
			if(command.visible==null)
				visible=true;
			else
			{
				if(command.visible.toLowerCase().equals("false"))
					visible=false;
			}
			if(visible&&command.type==null)
			{
				stringBuilder.append("."+command.name+" "+command.help+"\n");
			}
		}
		stringBuilder.append("部分命令被折叠可通过添加以下命令来查看：\n");
		for (String string : type) 
		{
			stringBuilder.append(".help "+string+"\n");
		}
		
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		sendBackMsg(stringBuilder.toString());
	}
	public void showHelp(ArrayList<String> arrayList)
	{
		String string=arrayList.get(0);
		ArrayList<Command> commands=getCommands();
		ArrayList<String> subCommand=new ArrayList<>();
		for (Command command : commands) {
			if(command.type!=null)
				if(command.type.equals(string)&&ifCommandVisible(command))
					subCommand.add("."+command.name+" "+command.help);
		}
		if(subCommand.size()<1)
		{
			sendBackMsg("没有发现该类型的命令");
			return;
		}
		StringBuilder builder=new StringBuilder();
		builder.append(string+"类型的命令有：\n");
		for (String string2 : subCommand) {
			builder.append(string2+"\n");
		}
		sendBackMsg(builder.toString());
	}
	private ArrayList<Command> getCommands()
	{
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
			return null;
		}
		ArrayList<Command> arrayList=new ArrayList<>();
		List<Element> elements=document.getRootElement().getChildren();
		for (Element element : elements) {
			Command command=new Command();
			command.name=element.getChildText("string");
			command.help=element.getChildText("help");
			command.point=element.getChildText("method");
			command.type=element.getChildText("type");
			command.visible=element.getChildText("visible");
			if(command.name==null||command.help==null||command.point==null)
				continue;
			arrayList.add(command);
		}
		return arrayList;
	}
	private boolean ifCommandVisible(Command command)
	{
		boolean visible=true;
		if(command.visible==null)
			visible=true;
		else
		{
			if(command.visible.toLowerCase().equals("false"))
				visible=false;
		}
		return visible;
	}
}
