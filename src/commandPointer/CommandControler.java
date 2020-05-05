package commandPointer;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import surveillance.Log;

public class CommandControler
{
	class CommandPackage
	{
		String commandhead;
		String help;
		SelfStartMethod method;
		Class<?>[] params;
		Class<?> returnType;
		public CommandPackage(String name,String help,SelfStartMethod method) 
		{
			// TODO Auto-generated constructor stub
			this.commandhead=name;
			this.help=help;
			this.method=method;
			params=method.getParameterTypes();
			returnType=method.getReturnType();
		}
		
		@Override
		public String toString() 
		{
			// TODO Auto-generated method stub
			return commandhead+" "+help+" "+" "+method;
		}
		public Object startMethod(Object...objects)
		{
			
		}
		public Object startMethod(String longParams)
		{
			try
			{
				Object[] objects=paramsDivide(longParams);
				return method.startMethod(objects);
			} catch (Exception e)
			{
				Log.e(e.getMessage());
				return null;
			}
		}
		/**
		 * 将长短的字符串拆分成方法要求的参数
		 * @param longParams 长段的字符串，写着参数，不符合参数的要求的话则会返回null
		 * @return
		 * @throws Exception 不符合方法所要求的参数要求
		 */
		private Object[] paramsDivide(String longParams) throws Exception
		{
			String regex="";
			Class<?> lastClass=null;
			for (int i=0;i<params.length;i++)
			{
				Class<?> class1 = params[i];
				if(i!=0)
				{
					if(class1==lastClass)
						regex+="[ ]+";
					else 
						regex+="[ ]*";
				}
				if(class1==String.class)
					regex+="(\\D+)";
				else if(class1==int.class)
					regex+="(\\d+)";
				else if (class1==boolean.class)
					regex+="((?i)false|(?i)true)";
				else if(class1==Object.class)
					regex+="(.)+";
			}
			
			Pattern pattern=Pattern.compile(regex);
			java.util.regex.Matcher matcher=pattern.matcher(longParams);
			if(matcher.find())
			{
				Object[] objects=new Object[params.length];
				for(int i=1;i<=matcher.groupCount();i++)
				{
					objects[i]=matcher.group(i);
				}
				return objects;
			}
			else
				throw new Exception("输入参数不符合正常要求");
		}
		/**
		 * 检测是否符合命令
		 * @param string
		 * @return
		 */
		public boolean isMatch(String string)
		{
			return commandhead.toLowerCase().trim().startsWith(string.toLowerCase().trim());
		}
		public int commandLength()
		{
			return commandhead.trim().length();
		}
	}
}
