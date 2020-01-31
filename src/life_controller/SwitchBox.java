package life_controller;

import org.meowy.cqp.jcq.entity.CoolQ;

public class SwitchBox 
{
	private static LifeCycleController controller;
	private static LifeCycleController getLifeCycleController()
	{
		if(controller==null)
			controller=new LifeCycleController();
		return controller;
	}
	public static void initialize(CoolQ coolQ)
	{
		getLifeCycleController();
	}
	public static void enable()
	{
		getLifeCycleController();
	}
	public static void disable()
	{
		getLifeCycleController();
	}
	public static void shutdown()
	{
		getLifeCycleController();
	}
}
