package commandMethod;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;

import global.xmlProcessor.XMLReader;
import surveillance.Log;
import tools.FileSimpleIO;
import tools.GetJarResources;

public class Draw extends Father
{
	public static String CARDPOOL_PATH;
	static String defaultCardPool;
	/**这里记录着返回时使用的语言<br>
	 * 第一维顺序是按照{@link subCommandList}来的<br>
	 * 第二维顺序是按照{@link status}来的<br>
	 * 将来也许会改为外部XML文件读取*/
	public static final String[][] returnMsg= {{"设置默认牌库成功","没有找到相应的文件","参数错误"}};
	
	public interface subCommandMethod
	{
		/**参数按序传入*/
		status run(ArrayList<String> pramas);
	}
	private enum status
	{
		SUCCESS(0),NULL(1),FAILED(2);
		int index;
		int getIndex()
		{
			return index;
		}
		status(int j) 
		{
			// TODO Auto-generated constructor stub
			index=j;
		}
	}
	/**子命令枚举*/
	private enum subCommandList
	{
		set(0,new subCommandMethod() {

			@Override
			public status run(ArrayList<String> pramas) {
				// TODO Auto-generated method stub
				if(pramas.size()<1)
					return status.FAILED;
				String cardPool=pramas.get(0);
				if(!cardPool.endsWith("\\.xml"))
					cardPool=cardPool+".xml";
				if(!new FileSimpleIO(CARDPOOL_PATH+cardPool).exists())
					return status.NULL;
				else
				{
					defaultCardPool=cardPool;
					return status.SUCCESS;
				}
			}
			
		});
		
		
		private int index;
		private subCommandMethod commandMethod;
		int getIndex()
		{
			return index;
		}
		subCommandMethod getMethod()
		{
			return commandMethod;
		}
		subCommandList(int index, subCommandMethod subCommandMethod) 
		{
			// TODO Auto-generated constructor stub
			this.index=index;
			this.commandMethod=subCommandMethod;
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		CARDPOOL_PATH=getPluginDataFloder();
	}
	private String drawCard(int time)
	{
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
		if(time>20)
		{
			sendBackMsg("抽取次数过多");
			return null;
		}
		StringBuilder builder=new StringBuilder();
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
	public void commandPointer(ArrayList<String> arrayList)
	{
		String prama=arrayList.get(0).toLowerCase();
		arrayList.remove(0);
		for (subCommandList subCommand : subCommandList.values()) 
		{
			if(prama.equals(subCommand.name()))
			{
				sendBackMsg(returnMsg[subCommand.getIndex()][subCommand.getMethod().run(arrayList).getIndex()]);
				return;
			}
		}
		if(Pattern.compile("[0-9]*").matcher(prama).matches())
		{
			sendBackMsg(drawCard(Integer.parseInt(prama)));
			return;
		}
		else
		{
			int time=1;
			if(arrayList.size()>=2)
			{
				time=0;
				if(Pattern.compile("[0-9]*").matcher(arrayList.get(1)).matches())
					time=Integer.parseInt(arrayList.get(1));
			}
			sendBackMsg(drawCard(prama, time));
			return;
		}
		
	}
	public void commandPointer()
	{
		String s=drawCard(1);
		if(s!=null)
			sendBackMsg(s);
	}
	
	private String drawCard(String cardPoolFile)
	{
		if(!cardPoolFile.endsWith(".xml"))
			cardPoolFile=cardPoolFile+".xml";
		Document document;
		try
		{
			document=XMLReader.getXMLReader(CARDPOOL_PATH+cardPoolFile).getDocument();
		}
		catch(NullPointerException exception)
		{
			try
			{
				document=XMLReader.getXMLReader(GetJarResources.getJarResources(cardPoolFile)).getDocument();
			}
			catch (NullPointerException e) 
			{
				// TODO: handle exception
				Log.e("未查询到牌库：",cardPoolFile);
				sendBackMsg("未查询到牌库:"+cardPoolFile);
				return null;
			}
		}
		List<Element> element=document.getRootElement().getChildren();
		Random random=new Random();
		int x=random.nextInt(element.size()-1);
		return element.get(x).getText();
	}
}
