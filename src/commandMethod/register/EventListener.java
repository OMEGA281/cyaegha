package commandMethod.register;

public abstract interface EventListener
{
	/**执行方法之后继续向下传递*/
	public final int RESPONSE_PASS=0;
	/**不执行该方法，跳过本方法*/
	public final int RESPONSE_DORMANT=1;
	/**执行该方法，并截停不继续传递*/
	public final int RESPONSE_STOP=2;
	
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
