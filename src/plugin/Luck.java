package plugin;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import connection.CQSender;
import pluginHelper.annotations.RegistCommand;
import transceiver.event.MessageReceiveEvent;

public class Luck extends Father
{

	@RegistCommand(CommandString = "jrrp",Help = "今日人品")
	public void jrrp(MessageReceiveEvent event)
	{
//		获取群名片（昵称）
		String name=CQSender.getNickorCard(event);
//		获取日期
		Date date=new Date(System.currentTimeMillis());
		int luck=getNum(date, name);
//		字符串修改器
		StringBuilder builder=new StringBuilder();
		
		builder.append("今天"+name+"的运气是"+luck+"\n");
		switch (luck/10)
		{
		case 0:
			builder.append("emm……嗯……不错……也许吧");
			break;
		case 1:case 2:
			builder.append("走路小心脚下");
			break;
		case 3:case 4:
			builder.append("运气什么的……要由自己改变");
			break;
		case 5:case 6:
			builder.append("今天是普普通通的一天");
			break;
		case 7:case 8:
			builder.append("今天应该会发生什么不同寻常的事情吧");
			break;
		case 9:case 10:
			builder.append("哇！今天不试试做点有意思的事吗？");
			break;

		default:
			builder.append("数值异常");
			break;
		}
		builder.append("\n");
//		将日期减一天
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		date=calendar.getTime();
		
		int yesterdayLuck=getNum(date, name);
		switch ((yesterdayLuck-luck)/10)
		{
		case -20:case -19:case -18:case -17:case -16:case -15:
			builder.append("时来运转吗？");
			break;
		case -14:case -13:case -12:case -11:case -10:case -9:
			builder.append("今天会比昨天顺利不少呢");
			break;
		case -8:case -7:case -6:case -5:case -4:case -3:
			builder.append("今天会比昨天美好的");
			break;
		case -2:case -1:case 0:case 1:case 2:
			builder.append("稳定的日子挺好的");
			break;
		case 8:case 7:case 6:case 5:case 4:case 3:
			builder.append("不要太依赖于运气");
			break;
		case 14:case 13:case 12:case 11:case 10:case 9:
			builder.append("运气什么的……可是不会继承的");
			break;
		case 20:case 19:case 18:case 17:case 16:case 15:
			builder.append("emm……平静之后的暴风雨吗？");
			break;
		default:
			builder.append("数值异常");
			break;
		}
//		发送信息
		sendMsg(event,builder.toString());
	}
	
	private int getNum(Date date,String name)
	{
//		制作一个日期格式化器，将日期格式化为20200406的样子
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
//		将名称加在日期后面
		String text=format.format(date)+name;
//		新建一个大数字
		BigInteger integer = null;
//		获取上述字符串的MD5
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			byte s[] =m.digest(text.getBytes());
			integer=new BigInteger(1, s);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		将MD5变成10进制并且变成字符串
		String string=integer.toString(10);
//		取字符串的第一位数字，将他设为索引
		int index=Integer.parseInt(string.charAt(0)+"");
//		取字符串中索引位置及其下两个的数字
		String place=string.substring(index, index+2);
		return Integer.parseInt(place);
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		
	}
}
