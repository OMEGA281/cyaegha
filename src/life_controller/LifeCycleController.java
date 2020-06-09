package life_controller;

import java.io.File;
import java.util.Set;

import org.meowy.cqp.jcq.entity.CoolQ;

import connection.CQSender;
import global.ReadDisclaimer;
import global.UniversalConstantsTable;
import pluginHelper.AuthirizerListBook;
import pluginHelper.ClassLoader;
import surveillance.Log;
import tools.ClassUtils;
import transceiver.EventTrigger;

class LifeCycleController
{
	class checkSOP extends Thread
	{
		long SOP;

		@Override
		public void run()
		{
			for (;;)
			{
				SOP = AuthirizerListBook.getSOP();
				if (SOP == 0)
					try
					{
						Thread.sleep(20);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				else
					break;
			}
		}
	}

	protected enum Status
	{
		NOT_RUNNING, DISABLED, RUNNING
	}

	private Status status = Status.NOT_RUNNING;

	protected Status getStatus()
	{
		return status;
	}

	protected void initialize(CoolQ CQ)
	{
		// 获取应用数据目录(无需储存数据时，请将此行注释)
		// 返回如：D:\CoolQ\data\app\org.meowy.cqp.jcq\data\app\com.example.demo\
		// 应用的所有数据、配置【必须】存放于此目录，避免给用户带来困扰。
		Log.i("初始化中……");
		UniversalConstantsTable.ROOTPATH = CQ.getAppDirectory() + "\\" + CQ.getLoginQQ() + "\\";
		UniversalConstantsTable.PLUGIN_DATAPATH = UniversalConstantsTable.ROOTPATH + "data\\";
		UniversalConstantsTable.PLUGIN_AUTHORITYPATH = UniversalConstantsTable.ROOTPATH + "authority\\";
		
		new File(UniversalConstantsTable.ROOTPATH).mkdirs();
		new File(UniversalConstantsTable.PLUGIN_DATAPATH).mkdirs();
		new File(UniversalConstantsTable.PLUGIN_AUTHORITYPATH).mkdirs();

		new CQSender(CQ);
		new AuthirizerListBook();
		new EventTrigger();

		long SOP = AuthirizerListBook.getSOP();
		if (SOP == 0)
		{
			checkSOP thread = new checkSOP();
			thread.start();
			ReadDisclaimer.start();
			try
			{
				thread.join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			SOP = thread.SOP;

		}

		Set<String> className = ClassUtils.getClassName("plugin", false);
		for (String string : className)
		{
			try
			{
				new ClassLoader(Class.forName(string)).reloadClass();
			} catch (ClassNotFoundException e)
			{
				Log.e("找不到类：" + string);
			}
		}

		Log.d("初始化完毕");
		status = Status.DISABLED;
	}

	protected void enabled()
	{
		status = Status.RUNNING;
	}

	protected void disabled()
	{
		status = Status.DISABLED;
	}

	protected void exit()
	{
		status = Status.NOT_RUNNING;
	}
}
