package pluginHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;

import connection.ReceiveMessageType;
import pluginHelper.annotations.AuthirizerListNeed;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import surveillance.Log;
import transceiver.event.MessageReceiveEvent;

public class CommandControler
{
	private static CommandControler commandControler;
	private ArrayList<CommandPackage> list=new ArrayList<CommandControler.CommandPackage>();
	public static CommandControler getCommandControler()
	{
		if(commandControler==null)
			commandControler=new CommandControler();
		return commandControler;
	}
	class CommandPackage
	{
		String commandhead;
		String help;
		SelfStartMethod method;
		Class<?>[] paramsType;
		Class<?> returnType;
		Object[] params;
		public CommandPackage(String name,String help,SelfStartMethod method) 
		{
			// TODO Auto-generated constructor stub
			this.commandhead=name;
			this.help=help;
			this.method=method;
			paramsType=method.getParameterTypes();
			returnType=method.getReturnType();
		}
		
		@Override
		public String toString() 
		{
			// TODO Auto-generated method stub
			return commandhead+" "+help+" "+" "+method;
		}
		public Object invoke()
		{
			if(paramsType.length==0)
				return method.startMethod();
			if(params.length!=paramsType.length)
			{
				Log.e("参数数量不符合");
				return null;
			}
			return method.startMethod(params);
		}
		/**
		 * 将长短的字符串拆分成方法要求的参数
		 * @param longParams 长段的字符串，写着参数，无参数则会返回null
		 * @return
		 * @throws Exception 不符合方法所要求的参数要求
		 */
		private Object[] paramsDivide(String longParams) throws Exception
		{
			if(paramsType.length==0)
				if(longParams.length()==0)
					return null;
				else
					throw new Exception("输入参数不符合正常要求");
			StringBuffer regex=new StringBuffer();
			Class<?> lastClass=null;
			for (int i=0;i<paramsType.length;i++)
			{
				Class<?> class1 = paramsType[i];
				if(i!=0)
				{
					if(class1==lastClass)
						regex.append("[ ]+");
					else 
						regex.append("[ ]*");
				}
				if(class1==String.class)
					regex.append("(\\D+)");
				else if(class1==int.class)
					regex.append("(\\d+)");
				else if (class1==boolean.class)
					regex.append("((?i)false|(?i)true)");
				else if(class1==Object.class)
					regex.append("(.)+");
				lastClass=class1;
			}
			
			Pattern pattern=Pattern.compile(regex.toString());
			java.util.regex.Matcher matcher=pattern.matcher(longParams);
			if(matcher.find())
			{
				Object[] objects=new Object[paramsType.length];
				for(int i=1;i<=matcher.groupCount();i++)
				{
					if(paramsType[i-1]==String.class)
						objects[i-1]=matcher.group(i);
					else if(paramsType[i-1]==int.class)
						objects[i-1]=Integer.parseInt(matcher.group(i));
					else if (paramsType[i-1]==boolean.class)
						objects[i-1]=Boolean.parseBoolean(matcher.group(i));
					else if(paramsType[i-1]==Object.class)
						objects[i-1]=matcher.group(i);
				}
				return objects;
			}
			else
				throw new Exception("输入参数不符合正常要求");
		}
		/**
		 * 检测是否符合命令
		 * 既检测命令部分，又检测参数部分
		 * 执行完本方法后，参数部分会缓存在其中以供调用
		 * @param string
		 * @return
		 */
		public boolean isMatch(String string)
		{
			StringBuffer head=new StringBuffer(commandhead);
			StringBuffer params=new StringBuffer(string);
			for(;head.length()>0;)
			{
				for(;head.length()!=0;)
					if(head.charAt(0)==' ')
						head.deleteCharAt(0);
					else
						break;
				for(;params.length()!=0;)
					if(params.charAt(0)==' ')
						params.deleteCharAt(0);
					else
						break;
				if(head.length()==0)
					break;
				int index=head.indexOf(" ");
				if(index==-1)
					index=head.length();
				if(params.length()<index)
					return false;
				if(params.substring(0, index).equals(head.substring(0, index)))
				{
					params.delete(0, index);
					head.delete(0, index);
				}
				else
					return false;
			}
			try
			{
				this.params=paramsDivide(params.toString().trim());
			} catch (Exception e)
			{
				return false;
			}
			return true;
		}
		public int commandLength()
		{
			return commandhead.trim().length();
		}
	}
	public void addCommand(SelfStartMethod method,RegistCommand command)
	{
		list.add(new CommandPackage(command.CommandString(), command.Help(), method));
	}
	private Object $startCommand(MessageReceiveEvent event)
	{
		String string=event.getMsg().substring(1);
		CommandPackage commandPackage = null;
		int i=0;
		for (CommandPackage p : list)
			if(p.isMatch(string))
				if(p.commandLength()>i)
				{
					commandPackage=p;
					i=p.commandLength();
				}
		if(commandPackage!=null)
		{
			if(accessible(commandPackage.method.getAnnotation(MinimumAuthority.class), 
					commandPackage.method.getAnnotation(AuthirizerListNeed.class)
					, event.getMsgType(), event.getUserNum(), event.getGroupNum(), commandPackage.method.getParentClass()))
			return commandPackage.invoke();
		}
		return null;
	}
	/**
	 * 检测一长串字符串是否是命令
	 * @param string
	 * @return
	 */
	public static boolean isCommand(String string)
	{
		return string.startsWith(".")||string.startsWith("。");
	}
	public Object startCommand(MessageReceiveEvent messageType)
	{
		if(!isCommand(messageType.getMsg()))
		{
			Log.e("非命令！");
			return null;
		}
		return $startCommand(messageType);
	}
	private boolean accessible(MinimumAuthority minimumAuthority,AuthirizerListNeed authirizerList,
			int type,long userNum,long groupNum,Class<?> className)
	{
		if(!AuthirizerListBook.getAuthirizerListBook().isAccessible(minimumAuthority, type, groupNum, userNum))
			return false;
		if(!AuthirizerListBook.getAuthirizerListBook().isAccessible(authirizerList, userNum, className))
			return false;
		return true;
	}
}
