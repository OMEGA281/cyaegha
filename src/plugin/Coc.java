package plugin;

import java.util.Random;

import connection.CQSender;
import pluginHelper.annotations.RegistCommand;
import transceiver.event.MessageReceiveEvent;

public class Coc extends Father 
{
	@RegistCommand(CommandString = "coc",Help = "根据COC7th规则生成一个随机属性")
	public void coc(MessageReceiveEvent e)
	{
		coc(e,1);
	}
	@RegistCommand(CommandString = "coc",Help = "根据COC7th规则生成一个随机属性")
	public void coc(MessageReceiveEvent e,Integer time)
	{
		StringBuilder stringBuilder=new StringBuilder();
		if(time>5||time<1)
		{
			sendMsg(e, "生成次数错误");
			return;
		}
		stringBuilder.append(CQSender.getNickorCard(e.getIdentitySymbol())+"的人物作成：\n");
		for(int i=1;i<=time;i++)
		{
			int smallSum = 0,bigSum=0;
			int x;
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("力量:"+x+" ");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("体质:"+x+" ");
			
			x=(getRandomNum(6, 2)+6)*5;
			smallSum+=x;
			stringBuilder.append("体型:"+x+"\n");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("敏捷:"+x+" ");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("外貌:"+x+" ");
			
			x=(getRandomNum(6, 2)+6)*5;
			smallSum+=x;
			stringBuilder.append("智力:"+x+"\n");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("意志:"+x+" ");
			
			x=(getRandomNum(6, 2)+6)*5;
			smallSum+=x;
			stringBuilder.append("教育:"+x+" ");
			
			x=getRandomNum(6, 3)*5;
			bigSum=smallSum+x;
			stringBuilder.append("幸运:"+x+"\n");
			
			stringBuilder.append("合计值:"+smallSum+"/"+bigSum+"\n");
		}
		sendMsg(e,stringBuilder.toString());
	}
	private int getRandomNum(int up,int times)
	{
		Random random=new Random();
		int num=0;
		for(int i=1;i<=times;i++)
		{
			num=num+random.nextInt(up)+1;
		}
		return num;
	}
}
