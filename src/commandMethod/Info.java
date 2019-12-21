package commandMethod;

public class Info
{
	public static final String[] IGNORE_CLASS={"Father","Info"};
	/**检测是否该类被忽略加载
	 * @param className 全类名*/
	public static boolean ifIgnore(String className)
	{
		String[] s=className.split("\\.");
		String name=s[s.length-1];
		for (String string : IGNORE_CLASS) {
			if(name.equals(string))
				return true;
		}
		return false;
	}
}

