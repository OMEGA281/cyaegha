package global;

import java.text.SimpleDateFormat;
import java.util.Date;

import surveillance.Log;

public class TimeCode 
{
	static TimeCode timeCode;
	public static TimeCode getTimecode()
	{
		if(timeCode==null)
			Log.d("初始化时间控制");
			timeCode=new TimeCode();
		return timeCode;
	}
	public long getTime()
	{
		return System.currentTimeMillis();
	}
	private String timeTrans(long time)
	{
		Date date=new Date(time);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}
	public String getDate()
	{
		Date date=new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}
}
