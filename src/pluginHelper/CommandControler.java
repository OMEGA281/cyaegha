package pluginHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import pluginHelper.annotations.AuthirizerListNeed;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import surveillance.Log;
import transceiver.IdentitySymbol.SourceType;
import transceiver.event.Event;
import transceiver.event.MessageReceiveEvent;

public class CommandControler
{
	private static CommandControler commandControler;
	private ArrayList<CommandPackage> list = new ArrayList<CommandControler.CommandPackage>();

	public static CommandControler getCommandControler()
	{
		if (commandControler == null)
			commandControler = new CommandControler();
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

		public CommandPackage(String name, String help, SelfStartMethod method)
		{
			this.commandhead = name;
			this.help = help;
			this.method = method;
			paramsType = method.getParameterTypes();
			returnType = method.getReturnType();
		}

		@Override
		public String toString()
		{
			return commandhead + " " + help + " " + " " + method;
		}

		public Object invoke(Event event)
		{
			if (paramsType.length == 1)
				return method.startMethod(event);
			if (params.length != paramsType.length - 1)
			{
				Log.e("参数数量不符合");
				return null;
			}
			List<Object> $arrayList = Arrays.asList(params);
			ArrayList<Object> arrayList = new ArrayList<>($arrayList);
			arrayList.add(0, event);
			return method.startMethod(arrayList.toArray(new Object[0]));
		}

		/**
		 * 将长短的字符串拆分成方法要求的参数
		 * 
		 * @param longParams 长段的字符串，写着参数，无参数则会返回null
		 * @return
		 * @throws Exception 不符合方法所要求的参数要求
		 */
		private Object[] paramsDivide(String longParams) throws Exception
		{
			if (paramsType.length == 0)
				throw new Exception("自定义方法中第一个事件变量不存在！");
			if (paramsType[0] != MessageReceiveEvent.class)
				throw new Exception("自定义方法中第一个不是事件变量！");
			if (paramsType.length == 1)
				if (longParams == null || longParams.isEmpty())
					return null;
				else
					throw new Exception("输入参数不符合正常要求");
			StringBuffer regex = new StringBuffer();
			Class<?> lastClass = null;
			for (int i = 1; i < paramsType.length; i++)
			{
				Class<?> class1 = paramsType[i];
				if (i != 0)
				{
					if (class1 == lastClass || lastClass == Object.class)
						regex.append("[ ]+");
					else
						regex.append("[ ]*");
				}
				if (class1 == String.class)
					regex.append("(\\D+)");
				else if (class1 == Integer.class || class1 == Long.class)
					regex.append("(\\d+)");
				else if (class1 == Boolean.class)
					regex.append("((?i)false|(?i)true)");
				else if (class1 == Object.class)
					regex.append("(.+)");
				lastClass = class1;
			}

			Pattern pattern = Pattern.compile(regex.toString());
			java.util.regex.Matcher matcher = pattern.matcher(longParams);
			if (matcher.matches())
			{
				Object[] objects = new Object[paramsType.length - 1];
				for (int i = 1; i <= matcher.groupCount(); i++)
				{
					if (paramsType[i] == String.class)
						objects[i - 1] = matcher.group(i);
					else if (paramsType[i] == Integer.class)
						objects[i - 1] = Integer.parseInt(matcher.group(i));
					else if (paramsType[i] == Long.class)
						objects[i - 1] = Long.parseLong(matcher.group(i));
					else if (paramsType[i] == Boolean.class)
						objects[i - 1] = Boolean.parseBoolean(matcher.group(i));
					else if (paramsType[i] == Object.class)
						objects[i - 1] = matcher.group(i);
				}
				return objects;
			} else
				throw new Exception("输入参数不符合正常要求");
		}

		/**
		 * 检测是否符合命令 既检测命令部分，又检测参数部分 执行完本方法后，参数部分会缓存在其中以供调用
		 * 
		 * @param string
		 * @return
		 */
		public boolean isMatch(String string)
		{
			StringBuffer head = new StringBuffer(commandhead);
			StringBuffer params = new StringBuffer(string);
			for (; head.length() > 0;)
			{
				for (; head.length() != 0;)
					if (head.charAt(0) == ' ')
						head.deleteCharAt(0);
					else
						break;
				for (; params.length() != 0;)
					if (params.charAt(0) == ' ')
						params.deleteCharAt(0);
					else
						break;
				if (head.length() == 0)
					break;
				int index = head.indexOf(" ");
				if (index == -1)
					index = head.length();
				if (params.length() < index)
					return false;
				if (params.substring(0, index).equals(head.substring(0, index)))
				{
					params.delete(0, index);
					head.delete(0, index);
				} else
					return false;
			}
			try
			{
				this.params = paramsDivide(params.toString().trim());
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

	public void addCommand(SelfStartMethod method, RegistCommand command)
	{
		list.add(new CommandPackage(command.CommandString(), command.Help(), method));
	}

	private Object $startCommand(MessageReceiveEvent event)
	{
		String string = event.getMsg().substring(1);
		CommandPackage commandPackage = null;
		int i = 0;
		for (CommandPackage p : list)
			if (p.isMatch(string))
				if (p.commandLength() > i
						|| (p.commandLength() == i && p.paramsType.length > commandPackage.paramsType.length))
				{
					commandPackage = p;
					i = p.commandLength();
				}
		if (commandPackage != null)
		{
			if (accessible(commandPackage.method.getAnnotation(MinimumAuthority.class),
					commandPackage.method.getAnnotation(AuthirizerListNeed.class), event.getMsgType(),
					event.getUserNum(), event.getGroupNum(), commandPackage.method.getParentClass()))
				return commandPackage.invoke(event);
		}
		return null;
	}

	/**
	 * 检测一长串字符串是否可能是命令，仅检查是不是在前面有个点
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isCommand(String string)
	{
		return string.startsWith(".") || string.startsWith("。");
	}

	public Object startCommand(MessageReceiveEvent messageType)
	{
		if (!isCommand(messageType.getMsg()))
		{
			Log.e("非命令！");
			return null;
		}
		return $startCommand(messageType);
	}

	private boolean accessible(MinimumAuthority minimumAuthority, AuthirizerListNeed authirizerList, SourceType type,
			long userNum, long groupNum, Class<?> className)
	{
		if (!AuthirizerListBook.getAuthirizerListBook().isAccessible(minimumAuthority, type, groupNum, userNum))
			return false;
		if (!AuthirizerListBook.getAuthirizerListBook().isAccessible(authirizerList, userNum, className))
			return false;
		return true;
	}
}
