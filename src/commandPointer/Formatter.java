package commandPointer;

public class Formatter 
{
	public static final String exMethodPath="commandMethod.";
	public static String getClassName(String point)
	{
		String[] part=point.split("\\.");
		StringBuilder stringBuilder=new StringBuilder(exMethodPath);
		for(int i=0;i<part.length-1;i++)
		{
			stringBuilder.append(part[i]);
			stringBuilder.append(".");
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		return stringBuilder.toString();
	}
	public static String getMethodName(String point)
	{
		String[] ss=point.split("\\.");
		return ss[ss.length-1];
	}
}
