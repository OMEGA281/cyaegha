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
			int[] num=new int[9];
			int A1=0,A2=0;
			for (int m=0;m<num.length;m++) 
			{
				num[m]=getRandomNum(6, 3)*5;
				A2=A2+num[m];
			}
			A1=A2-num[8];
			stringBuilder.append("力量："+num[0]+"\t");
			stringBuilder.append("体质："+num[1]+"\t");
			stringBuilder.append("体型："+num[2]+"\n");
			stringBuilder.append("敏捷："+num[3]+"\t");
			stringBuilder.append("外貌："+num[4]+"\t");
			stringBuilder.append("智力："+num[5]+"\n");
			stringBuilder.append("意志："+num[6]+"\t");
			stringBuilder.append("教育："+num[7]+"\t");
			stringBuilder.append("幸运："+num[8]+"\n");
			stringBuilder.append("合计值："+A1+"/"+A2+"\n");
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
