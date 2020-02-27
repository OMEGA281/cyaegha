package commandMethod;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import connection.CQSender;
import global.UniversalConstantsTable;
import surveillance.Log;
import tools.FileSimpleIO;
import tools.GetJarResources;
import tools.XMLDocument;

public class Draw extends Father
{
	public static String CARDPOOL_PATH;

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		CARDPOOL_PATH=getPluginDataFloder();
	}
	private String drawCard(int time)
	{
		String defaultCardPool=getDataExchanger().getItem(getMark());
		if(defaultCardPool==null)
		{
			sendBackMsg("您尚未定义默认牌库，您可以使用.draw set …来设置默认牌库");
			return null;
		}
		return drawCard(defaultCardPool, time);
	}
	private String drawCard(String cardPool,int time)
	{
		if(time==0)
			return null;
		if(time>10)
			return "抽取次数过多";
		StringBuilder builder=new StringBuilder(receiveMessageType.getNick()+"抽到了这些：\n");
		for(int i=1;i<=time;i++)
		{
			String sigleCard=drawCard(cardPool);
			if(sigleCard==null)
				return null;
			builder.append(drawCard(cardPool)+"\n");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
	public void draw(ArrayList<String> arrayList)
	{
		String cardName=arrayList.get(0);
		int time;
		if(arrayList.size()>1)
		{
			String times=arrayList.get(1);
			try
			{
				time=Integer.parseInt(times);
			}catch (NumberFormatException e)
			{
				sendBackMsg("输入的次数不合法");
				return;
			}
		}
		else
		{
			Pattern pattern=Pattern.compile(".*[0-9]");
			if(pattern.matcher(cardName).matches())
			{
				String num="";
				for(;;)
				{
					if(cardName.isEmpty())
						break;
					char c=cardName.charAt(cardName.length()-1);
					if(Pattern.matches("[0-9]", Character.toString(c)))
					{
						num=c+num;
						cardName=cardName.substring(0,cardName.length()-1);
					}
					else
						break;
				}
				time=Integer.parseInt(num);
			}
			else
				time=1;
		}
		if(cardName.isEmpty())
		{
			drawCard(time);
		}
		else
		{
			drawCard(cardName, time);
		}
	}
	public void draw()
	{
		String s=drawCard(1);
		if(s!=null)
			sendBackMsg(s);
	}
	
	private String drawCard(String cardPoolFile)
	{
		if(!cardPoolFile.endsWith(".xml"))
			cardPoolFile=cardPoolFile+".xml";
		Document document=null;
		if(new FileSimpleIO(CARDPOOL_PATH+cardPoolFile).exists())
		{
			try {
				document=XMLDocument.getDocument(CARDPOOL_PATH+cardPoolFile,false);
			} catch (JDOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(new GetJarResources(cardPoolFile).exist())
		{
			try {
				document=XMLDocument.getDocument(
						new GetJarResources(cardPoolFile).getJarResources());
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Log.i("未查询到牌库：",cardPoolFile);
			sendBackMsg("未查询到牌库:"+cardPoolFile);
			return null;
		}
		List<Element> element=document.getRootElement().getChildren();
		Random random=new Random();
		int x=random.nextInt(element.size());
		return element.get(x).getText();
	}
	public void drawlist()
	{
		File[] files=new File(getPluginDataFloder()).listFiles();
		StringBuilder stringBuilder=new StringBuilder("目前存在着如下牌库：\n");
		for (File file : files)
		{
			if(file.getName().endsWith(".xml"))
			{
				String filename=file.getName();
				stringBuilder.append(filename.substring(0, filename.length()-4));
				stringBuilder.append("/");
			}
		}
		if(stringBuilder.length()>0)
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		sendBackMsg(stringBuilder.toString());
	}
	public void drawset()
	{
		sendBackMsg(getDataExchanger().deleteItem(getMark())?"删除了默认牌库":"还未设置默认牌库");
	}
	public void drawset(ArrayList<String> arrayList)
	{
		String card=arrayList.get(0);
		if(!card.endsWith("\\.xml"))
			card+=".xml";
		if(!new File(CARDPOOL_PATH+card).exists())
			sendBackMsg("未查询到牌库："+card);
		else
		{
			getDataExchanger().addItem(getMark(), card);
			sendBackMsg("将"+card+"设置为默认牌库");
		}
	}
	private String getMark()
	{
		String string;
		switch (receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			string="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			string="G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			string="D"+receiveMessageType.getfromGroup();
			break;
		default:
			string=null;
			break;
		}
		return string;
	}
}
