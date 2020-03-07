package commandMethod;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Luck extends Father
{

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void jrrp()
	{
		String name=receiveMessageType.getNick();
		Date date=new Date(System.currentTimeMillis());
		int luck=getNum(date, name);
		StringBuilder builder=new StringBuilder();
		builder.append("今天"+receiveMessageType.getNick()+"的运气是"+luck+"\n");
		switch (luck/10)
		{
		case 0:
			builder.append("emm……嗯……不错……也许吧");
			break;
		case 1:
		case 2:
			builder.append("走路小心脚下");
			break;
		case 3:
		case 4:
			builder.append("运气什么的……要由自己改变");
			break;
		case 5:
		case 6:
			builder.append("今天是普普通通的一天");
			break;
		case 7:
		case 8:
			builder.append("今天应该会发生什么不同寻常的事情吧");
			break;
		case 9:
		case 10:
			builder.append("哇！今天不试试做点有意思的事吗？");
			break;

		default:
			builder.append("数值异常");
			break;
		}
		builder.append("\n");
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		date=calendar.getTime();
		int yesterdayLuck=getNum(date, name);
		switch ((yesterdayLuck-luck)/10)
		{
		case -20:
		case -19:
		case -18:
		case -17:
		case -16:
		case -15:
			builder.append("时来运转吗？");
			break;
		case -14:
		case -13:
		case -12:
		case -11:
		case -10:
		case -9:
			builder.append("今天会比昨天顺利不少呢");
			break;
		case -8:
		case -7:
		case -6:
		case -5:
		case -4:
		case -3:
			builder.append("今天会比昨天美好的");
			break;
		case -2:
		case -1:
		case 0:
		case 1:
		case 2:
			builder.append("稳定的日子挺好的");
			break;
		case 8:
		case 7:
		case 6:
		case 5:
		case 4:
		case 3:
			builder.append("不要太依赖于运气");
			break;
		case 14:
		case 13:
		case 12:
		case 11:
		case 10:
		case 9:
			builder.append("运气什么的……可是不会继承的");
			break;
		case 20:
		case 19:
		case 18:
		case 17:
		case 16:
		case 15:
			builder.append("emm……平静之后的暴风雨吗？");
			break;
		default:
			builder.append("数值异常");
			break;
		}
		sendBackMsg(builder.toString());
	}
	
	private int getNum(Date date,String name)
	{
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		String text=format.format(date)+name;
		BigInteger integer = null;
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			byte s[] =m.digest(text.getBytes());
			integer=new BigInteger(1, s);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String string=integer.toString(10);
		int index=Integer.parseInt(string.charAt(0)+"");
		String place=string.substring(index, index+2);
		return Integer.parseInt(place);
	}
}
