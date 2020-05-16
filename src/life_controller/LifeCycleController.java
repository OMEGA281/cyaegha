package life_controller;

import org.meowy.cqp.jcq.entity.CoolQ;

import commandMethod.register.Register;
import commandPointer.Matcher;
import connection.CQSender;
import global.UniversalConstantsTable;
import surveillance.Log;
import transceiver.EventTrigger;
import transceiver.Receiver;
import transceiver.Transmitter;

class LifeCycleController 
{
	protected enum Status{NOT_RUNNING,DISABLED,RUNNING}
	private Status status=Status.NOT_RUNNING;
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
    	UniversalConstantsTable.ROOTPATH=CQ.getAppDirectory()+"\\"+CQ.getLoginQQ()+"\\";
    	//    	启动注册器
    	new Register();
    	new EventTrigger();
    	
		Receiver.getReceiver();
		Transmitter.getTransmitter();
		Matcher.getMatcher();
		new CQSender(CQ);
		Log.d("初始化完毕");
		status=Status.DISABLED;
	}
	protected void enabled()
	{
		Register.getRegister().reloadAllClass();
    	Receiver.getReceiver().startThread();
    	Transmitter.getTransmitter().startThread();
    	status=Status.RUNNING;
	}
	protected void disabled()
	{
		Receiver.getReceiver().endThread();
		status=Status.DISABLED;
	}
	protected void exit()
	{
		Receiver.getReceiver().endThread();
		Transmitter.getTransmitter().endThread();
		status=Status.NOT_RUNNING;
	}
}
