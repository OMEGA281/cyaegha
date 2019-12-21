package commandMethod.register;

import java.util.Comparator;

public abstract class OnEventListener implements EventListener
{
	/**回应类型，接口内含常量*/
	public int response=RESPONSE_PASS;
	/**优先级，0~100*/
	public int priority=PRIORITY_NORMAL;
}
