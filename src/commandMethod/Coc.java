package commandMethod;

import java.util.ArrayList;
import java.util.Random;

public class Coc extends Father 
{

	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub

	}
	
	public void coc()
	{
		coc(null);
	}
	public void coc(ArrayList<String> arrayList)
	{
		StringBuilder stringBuilder=new StringBuilder();
		int time=1;
		if(arrayList==null)
			time=1;
		else
		{
			try
			{
				time=Integer.parseInt(arrayList.get(0));
			}
			catch(NumberFormatException e)
			{
				stringBuilder.append("非法参数，按默认值\n");
			}
		}
		if(time>5||time<1)
		{
			stringBuilder.append("参数溢出，按默认值\n");
			time=1;
		}
		stringBuilder.append(getMessageSenderName()+"的人物作成：\n");
		for(int i=1;i<=time;i++)
		{
			int smallSum = 0,bigSum=0;
			int x;
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("力量:"+x+"  ");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("体质:"+x+"  ");
			
			x=(getRandomNum(6, 2)+6)*5;
			smallSum+=x;
			stringBuilder.append("体型:"+x+"  ");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("敏捷:"+x+"  ");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("外貌:"+x+"  ");
			
			x=(getRandomNum(6, 2)+6)*5;
			smallSum+=x;
			stringBuilder.append("智力:"+x+"  ");
			
			x=getRandomNum(6, 3)*5;
			smallSum+=x;
			stringBuilder.append("意志:"+x+"  ");
			
			x=(getRandomNum(6, 2)+6)*5;
			smallSum+=x;
			stringBuilder.append("教育:"+x+"  ");
			
			x=getRandomNum(6, 3)*5;
			bigSum=smallSum+x;
			stringBuilder.append("幸运:"+x+"\n");
			
			stringBuilder.append("合计值:"+smallSum+"/"+bigSum+"\n");
		}
		sendBackMsg(stringBuilder.toString());
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
