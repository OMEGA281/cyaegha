package surveillance;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log 
{
//	获得堆栈的信息，返回迭代的指针
	private StringBuffer getStackPoint()
	{
		StackTraceElement stackTraceElement[]=Thread.currentThread().getStackTrace();
		StringBuffer stringBuffer=new StringBuffer();
//		从2取得是为了消除调用的本方法以及日志打印方法
		for(int i=2;i<stackTraceElement.length;i++)
		{
//			当打印了10条消息之后，便将剩下的信息折叠
			if(i>=13)
			{
				for(int j=0;j<i-3;j++)
				{
					stringBuffer.append(" ");
				}
				stringBuffer.append("还有"+(stackTraceElement.length-i)+"条未被显示");
				break;
			}
			for(int j=0;j<i-3;j++)
			{
				stringBuffer.append(" ");
			}
			if(i!=2)
			{
				stringBuffer.append("⬆");
			}
			stringBuffer.append("位置:"+stackTraceElement[i].getClassName()+":"
					+stackTraceElement[i].getMethodName()+" ");
			stringBuffer.append("行数:"+stackTraceElement[i].getLineNumber());
			if(i!=stackTraceElement.length)
				stringBuffer.append("\n");
		}
		return stringBuffer;
	}
//	独立的获得时间的字符串
	private String getTime()
	{
		long time=System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(time));
	}
//	调试信息
	public static void d(String ...s)
	{
		StringBuffer m=new StringBuffer();
		for (String i : s) 
		{
			m.append(i);
		}
		System.out.println(m);
	}
//	提示信息
	public static void i(String ...s)
	{
		StringBuffer m=new StringBuffer();
		for (String i : s) 
		{
			m.append(i);
		}
		System.out.println(m);
	}
//	警告信息
	public static void w(String ...s)
	{
		StringBuffer m=new StringBuffer();
		for (String i : s) 
		{
			m.append(i);
		}
		System.out.println(m);
	}
//	错误信息
	public static void e(String ...s)
	{
		StringBuffer m=new StringBuffer();
		for (String i : s) 
		{
			m.append(i);
		}
		System.out.println(m);
	}
//	致命错误信息
	public static void f(String ...s)
	{
		StringBuffer m=new StringBuffer();
		for (String i : s) 
		{
			m.append(i);
		}
		System.out.println(m);
	}
}
