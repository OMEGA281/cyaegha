package commandMethod;

import java.util.ArrayList;

public class Random extends Father
{
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		
	}
	public void point() 
	{
		// TODO Auto-generated method stub
		point(null);
	}
	public void point(ArrayList<String> arrayList)
	{
		// TODO Auto-generated method stub
		int part=100;
		int times=1;
		if(arrayList==null)
		{
			
		}
		else if(arrayList.size()>=2)
		{
			try
			{
				part=Integer.parseInt(arrayList.get(0));
				times=Integer.parseInt(arrayList.get(1));
			}catch(NumberFormatException e)
			{
				sendBackMsg("非法参数");
				return;
			}
		}
		else
		{
			try
			{
			part=Integer.parseInt(arrayList.get(0));
			}catch(NumberFormatException e)
			{
				sendBackMsg("非法参数");
				return;
			}
		}
		if(part<=500&&part>=2&&times<=100&&times>=1)
		{
			sendBackMsg("掷了"+times+"次"+part+"骰，得到结果："+getRandomNum(part, times));
			return;
		}
		else
		{
			sendBackMsg("参数溢出");
			return;
		}
	}
	private int getRandomNum(int parts,int times)
	{
		java.util.Random random=new java.util.Random();
		int num=0;
		for(int i=1;i<=times;i++)
		{
			num=num+random.nextInt(parts)+1;
		}
		return num;
	}
}
