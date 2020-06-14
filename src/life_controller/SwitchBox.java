package life_controller;

import org.meowy.cqp.jcq.entity.CoolQ;

import life_controller.LifeCycleController.Status;

public class SwitchBox
{
	private static LifeCycleController controller;

	private static LifeCycleController getLifeCycleController()
	{
		if (controller == null)
			controller = new LifeCycleController();
		return controller;
	}

	public static void initialize(CoolQ coolQ)
	{
		getLifeCycleController();
		if (controller.getStatus() == Status.NOT_RUNNING)
			controller.initialize(coolQ);
	}

	public static void enable()
	{
		getLifeCycleController();
		if (controller.getStatus() == Status.DISABLED)
			controller.enabled();
	}

	public static void disable()
	{
		getLifeCycleController();
		if (controller.getStatus() == Status.RUNNING)
			controller.disabled();
	}

	public static void shutdown()
	{
		getLifeCycleController();
		if (controller.getStatus() == Status.DISABLED)
			controller.exit();
	}
}
