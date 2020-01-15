package commandMethod;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import surveillance.Log;

public class Judge extends Father
{
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void rd() 
	{
		// TODO Auto-generated method stub
		rd(null);
	}
	
	public void rd(ArrayList<String> arrayList)
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
	
	public void st()
	{
		st(null);
	}
	
	public void st(ArrayList<String> arrayList)
	{
		String help=".st 名称 数值\n"
				+"子命令：\n"
				+"remove 名称\t删除该名称的数值\n";
		String name;
		int num;
		if(arrayList==null)
		{
			sendBackMsg("参数数量错误，帮助如下：\n"+help);
			return;
		}
		if(arrayList.size()<2)
		{
			sendBackMsg("参数数量错误，帮助如下：\n"+help);
			return;
		}
		switch(arrayList.get(0))
		{
		case "remove":
			name=arrayList.get(1);
			getDataExchanger().deleteListItem(name, getQQAndGroupString());
			sendBackMsg("擦除了"+getMessageSenderName()+"的"+name+"属性");
			break;
			
			default:
				name=arrayList.get(0);
				Pattern pattern=Pattern.compile("[0-9]|xml");
				if(pattern.matcher(name.charAt(0)+"").matches())
				{
					sendBackMsg("不得以数字开头");
					return;
				}
				try
				{
					num=Integer.parseInt(arrayList.get(1));
				}
				catch(NumberFormatException e)
				{
					sendBackMsg("设置的数值错误，应为1-100");
					return;
				}
				if(num<1||num>100)
				{
					sendBackMsg("设置的数值错误，应为1-100");
					return;
				}
				getDataExchanger().setListItem(name, getQQAndGroupString(), Integer.toString(num));
				sendBackMsg("记录下"+getMessageSenderName()+"的"+name+"数值了");
				break;
		}
	}
	
	public void ra()
	{
		ra(null);
	}
	
	public void ra(ArrayList<String> arrayList)
	{
		String help=".ra 技能名 [未定数的技能名需在此填写临时数值]";
		if(arrayList==null)
		{
			sendBackMsg("参数数量错误，请参照：\n"+help);
			return;
		}
		if(arrayList.size()<1)
		{
			sendBackMsg("参数数量错误，请参照：\n"+help);
			return;
		}
		String name=arrayList.get(0);
		String $data=getDataExchanger().getListItem(name, getQQAndGroupString());
		int data;
		if($data!=null)
		{
			data=Integer.parseInt($data);
			if(arrayList.size()>1)
			{
				int $$data=-1;
				try {
					$$data=Integer.parseInt(arrayList.get(1));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
				}
				if($$data>=1&&$$data<=100)
				{
					data=$$data;
				}
			}
		}
		else
		{
			if(arrayList.size()<2)
			{
				sendBackMsg("未查询到已储存的数据，临时判定需加临时数值\n"+help);
				return;
			}
			try
			{
				data=Integer.parseInt(arrayList.get(1));
			}
			catch(NumberFormatException e)
			{
				sendBackMsg("数值非法"+help);
				return;
			}
			if(data<1||data>100)
			{
				sendBackMsg("数值溢出"+help);
				return;
			}
		}
		int rdNum=getRandomNum(100, 1);
		int success=data;
		int bigSuccess=data/2;
		int superSuccess=data/5;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("对"+name+"鉴定，掷出：1d"+rdNum+"\n");
		boolean ifSuccess;
		if(rdNum<=success)
		{
			ifSuccess=true;
			if(rdNum<=bigSuccess)
				if(rdNum<=superSuccess)
					stringBuilder.append("检定极难成功");
				else
					stringBuilder.append("检定困难成功");
			else
				stringBuilder.append("检定成功");
		}
		else
		{
			ifSuccess=false;
			stringBuilder.append("检定失败");
		}
		if(rdNum==1&&ifSuccess)
		{
			stringBuilder.append(",大成功!");
		}
		else if((rdNum<=100&&rdNum>=96)&&!ifSuccess)
		{
			stringBuilder.append(",大失败!");
		}
		sendBackMsg(stringBuilder.toString());
	}
	
	public void rb()
	{
		rb(null);
	}
	
	public void rb(ArrayList<String> arrayList)
	{
		String back="";
		int num=1;
		if(arrayList!=null)
		{
			String s=arrayList.get(0);
			int $num=1;
			try
			{
				$num=Integer.parseInt(s);
			}
			catch (NumberFormatException e) 
			{
				// TODO: handle exception
				Log.i("不符合要求");
				back=back+"参数错误，应为1~10，现按1进行投掷\n";
			}
			if($num<=10&&$num>=1)
				num=$num;
			else
				back=back+"参数错误，应为1~10，现按1进行投掷\n";
		}
		int first=getRandomNum(100, 1);
		int quotient=first%10;
		int replace=first/10;
		for(int i=1;i<=num;i++)
		{
			int $num=getRandomNum(10, 1)-1;
			if($num<replace)
				replace=$num;
		}
		int result=replace*10+quotient;
		back=back+"掷骰子得：1d"+first+"\n掷"+num+"次奖励骰，得到：1d"+replace+"\n结果：掷出"+result;
		sendBackMsg(back);
	}

	public void rp()
	{
		rp(null);
	}
	
	public void rp(ArrayList<String> arrayList)
	{
		String back="";
		int num=1;
		if(arrayList!=null)
		{
			String s=arrayList.get(0);
			int $num=1;
			try
			{
				$num=Integer.parseInt(s);
			}
			catch (NumberFormatException e) 
			{
				// TODO: handle exception
				Log.i("不符合要求");
				back=back+"参数错误，应为1~10，现按1进行投掷\n";
			}
			if($num<=10&&$num>=1)
				num=$num;
			else
				back=back+"参数错误，应为1~10，现按1进行投掷\n";
		}
		int first=getRandomNum(100, 1);
		int quotient=first%10;
		int replace=first/10;
		for(int i=1;i<=num;i++)
		{
			int $num=getRandomNum(10, 1)-1;
			if($num>replace)
				replace=$num;
		}
		int result=replace*10+quotient;
		back=back+"掷骰子得：1d"+first+"\n掷"+num+"次惩罚骰，得到：1d"+replace+"\n结果：掷出"+result;
		sendBackMsg(back);
	}

	public void rab()
	{
		rab(null);
	}
	
	public void rab(ArrayList<String> arrayList)
	{
		String help=".rab [投掷次数，缺省为1] 项目 [技能数值，若未记录，则必须填写]";
		StringBuilder stringBuilder=new StringBuilder();
		int num=1;
		if(arrayList==null)
		{
			sendBackMsg("参数数量错误\n"+help);
			return;
		}
		if(arrayList.size()<1)
		{
			sendBackMsg("参数数量错误\n"+help);
			return;
		}
		String firstString=arrayList.get(0);
		String name;
		int pointIndex;
		Pattern pattern=Pattern.compile("[0-9]*");
		if(pattern.matcher(firstString).matches())
		{
			int $num=1;
			try {
				$num=Integer.parseInt(firstString);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				sendBackMsg("数量含非法字符");
				return;
			}
			if($num<1||$num>10)
			{
				stringBuilder.append("投掷次数不合法，回复默认");
			}
			num=$num;
			if(arrayList.size()<2)
			{
				sendBackMsg("参数数量错误\n"+help);
				return;
			}
			name=arrayList.get(1);
			pointIndex=2;
		}
		else
		{
			name=firstString;
			pointIndex=1;
		}
		
		int data=-1;
		String $data= getDataExchanger().getListItem(name, getQQAndGroupString());
		if($data==null)
		{
			if(arrayList.size()>=pointIndex+1)
			{
				int $point;
				try {
					$point=Integer.parseInt(arrayList.get(pointIndex));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					sendBackMsg("数量含非法字符");
					return;
				}
				if($point<100&&$point>1)
				{
					data=$point;
				}
				else
				{
					sendBackMsg("数值溢出");
					return;
				}
			}
			else
			{
				sendBackMsg("未查询到记录，需要填写临时数值");
				return;
			}
			data=Integer.parseInt($data);
		}
		else if(arrayList.size()>=pointIndex+1)
		{
			int $point=-1;
			try {
				$point=Integer.parseInt(arrayList.get(pointIndex));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
			}
			if($point<100&&$point>1)
			{
				data=$point;
			}
		}
		else
		{
			data=Integer.parseInt($data);
		}
		if(data==-1)
		{
			sendBackMsg("来源未知的点数错误");
			return;
		}
		
		
		int first=getRandomNum(100, 1);
		int quotient=first%10;
		int replace=first/10;
		for(int i=1;i<=num;i++)
		{
			int $num=getRandomNum(10, 1)-1;
			if($num<replace)
				replace=$num;
		}
		int result=replace*10+quotient;
		
		int rdNum=result;
		int success=data;
		int bigSuccess=data/2;
		int superSuccess=data/5;
		stringBuilder.append("对"+name+"鉴定，掷出：1d"+first+"\n");
		stringBuilder.append("掷"+num+"个奖励骰，获得1d"+replace+"，得"+rdNum+"\n");
		stringBuilder.append("技能"+name+"值为："+data+"\t");
		boolean ifSuccess;
		if(rdNum<=success)
		{
			ifSuccess=true;
			if(rdNum<=bigSuccess)
				if(rdNum<=superSuccess)
					stringBuilder.append("检定极难成功");
				else
					stringBuilder.append("检定困难成功");
			else
				stringBuilder.append("检定成功");
		}
		else
		{
			ifSuccess=false;
			stringBuilder.append("检定失败");
		}
		if(rdNum==1&&ifSuccess)
		{
			stringBuilder.append(",大成功!");
		}
		else if((rdNum<=100&&rdNum>=96)&&!ifSuccess)
		{
			stringBuilder.append(",大失败!");
		}
		sendBackMsg(stringBuilder.toString());
	}

	public void rap()
	{
		rap(null);
	}
	
	public void rap(ArrayList<String> arrayList)
	{
		String help=".rab [投掷次数，缺省为1] 项目 [技能数值，若未记录，则必须填写]";
		StringBuilder stringBuilder=new StringBuilder();
		int num=1;
		if(arrayList==null)
		{
			sendBackMsg("参数数量错误\n"+help);
			return;
		}
		if(arrayList.size()<1)
		{
			sendBackMsg("参数数量错误\n"+help);
			return;
		}
		String firstString=arrayList.get(0);
		String name;
		int pointIndex;
		Pattern pattern=Pattern.compile("[0-9]*");
		if(pattern.matcher(firstString).matches())
		{
			int $num=1;
			try {
				$num=Integer.parseInt(firstString);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				sendBackMsg("数量含非法字符");
				return;
			}
			if($num<1||$num>10)
			{
				stringBuilder.append("投掷次数不合法，回复默认");
			}
			num=$num;
			if(arrayList.size()<2)
			{
				sendBackMsg("参数数量错误\n"+help);
				return;
			}
			name=arrayList.get(1);
			pointIndex=2;
		}
		else
		{
			name=firstString;
			pointIndex=1;
		}
		
		int data=-1;
		String $data= getDataExchanger().getListItem(name, getQQAndGroupString());
		if($data==null)
		{
			if(arrayList.size()>=pointIndex+1)
			{
				int $point;
				try {
					$point=Integer.parseInt(arrayList.get(pointIndex));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					sendBackMsg("数量含非法字符");
					return;
				}
				if($point<100&&$point>1)
				{
					data=$point;
				}
				else
				{
					sendBackMsg("数值溢出");
					return;
				}
			}
			else
			{
				sendBackMsg("未查询到记录，需要填写临时数值");
				return;
			}
			data=Integer.parseInt($data);
		}
		else if(arrayList.size()>=pointIndex+1)
		{
			int $point=-1;
			try {
				$point=Integer.parseInt(arrayList.get(pointIndex));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
			}
			if($point<100&&$point>1)
			{
				data=$point;
			}
		}
		else
		{
			data=Integer.parseInt($data);
		}
		if(data==-1)
		{
			sendBackMsg("来源未知的点数错误");
			return;
		}
		
		
		int first=getRandomNum(100, 1);
		int quotient=first%10;
		int replace=first/10;
		for(int i=1;i<=num;i++)
		{
			int $num=getRandomNum(10, 1)-1;
			if($num>replace)
				replace=$num;
		}
		int result=replace*10+quotient;
		
		int rdNum=result;
		int success=data;
		int bigSuccess=data/2;
		int superSuccess=data/5;
		stringBuilder.append("对"+name+"鉴定，掷出：1d"+first+"\n");
		stringBuilder.append("掷"+num+"个惩罚骰，获得1d"+replace+"，得"+rdNum+"\n");
		stringBuilder.append("技能"+name+"值为："+data+"\t");
		boolean ifSuccess;
		if(rdNum<=success)
		{
			ifSuccess=true;
			if(rdNum<=bigSuccess)
				if(rdNum<=superSuccess)
					stringBuilder.append("检定极难成功");
				else
					stringBuilder.append("检定困难成功");
			else
				stringBuilder.append("检定成功");
		}
		else
		{
			ifSuccess=false;
			stringBuilder.append("检定失败");
		}
		if(rdNum==1&&ifSuccess)
		{
			stringBuilder.append(",大成功!");
		}
		else if((rdNum<=100&&rdNum>=96)&&!ifSuccess)
		{
			stringBuilder.append(",大失败!");
		}
		sendBackMsg(stringBuilder.toString());
	}

	/**
	 * 投掷一个骰子,大小为1~<code>parts</code>
	 * @param parts 最大数
	 * @param times 投掷次数
	 * @return <code>times</code>次1~<code>parts</code>之间的和
	 */
	private int getRandomNum(int parts,int times)
	{
		Random random=new Random();
		int num=0;
		for(int i=1;i<=times;i++)
		{
			num=num+random.nextInt(parts)+1;
		}
		return num;
	}
	
	/**
	 * 返回触发者的XML库标志
	 * @return 带QQ和群号
	 */
	private String getQQAndGroupString()
	{
		return getQQAndGroupString(receiveMessageType.getfromQQ(), receiveMessageType.getfromGroup());
	}
	/**
	 * 返回XML库标志
	 * @return 带QQ和群号
	 */
	private String getQQAndGroupString(long QQ,long Group)
	{
		return "S"+QQ+"S"+Group;
	}
}
