package commandMethod.register;

public abstract interface OnEventListener
{
	
	/**极高优先度*/
	public final int PRIORITY_MAX=100;
	/**较高优先度*/
	public final int PRIORITY_HIGH=75;
	/**中优先度*/
	public final int PRIORITY_NORMAL=50;
	/**较低优先度*/
	public final int PRIORITY_LOW=25;
	/**极低优先度*/
	public final int PRIORITY_MIN=0;
	
	/**继续将信息传递给下一个*/
	public final int RETURN_PASS=0;
	/**停止信息的传递*/
	public final int RETURN_STOP=1;
	
}
