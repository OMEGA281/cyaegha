package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import surveillance.Log;

public class TimeSimpleTool 
{
	/**获得当前的时间戳*/
	public static long getNowTimeStamp()
	{
		return System.currentTimeMillis();
	}
	/**获得当前时间（已格式化为"yyyy-MM-dd HH:mm:ss"）*/
	public static String getTime()
	{
		return getTime(getNowTimeStamp(), "yyyy-MM-dd HH:mm:ss");
	}
	/**获得当前时间
	 * @param formatString 格式字符串*/
	public static String getTime(String formatString)
	{
		return getTime(getNowTimeStamp(), formatString);
	}
	/**获得时间戳对应格式符
	 * @param timeStamp 时间戳
	 * @param formatString 格式字符串*/
	public static String getTime(long timeStamp,String formatString)
	{
		Date date=new Date(timeStamp);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(formatString);
		return simpleDateFormat.format(date);
	}
}
