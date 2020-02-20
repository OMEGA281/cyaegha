package commandMethod;

import java.util.ArrayList;

import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;
import surveillance.Log;

public class Switch extends Father
{
	private static final String LISTENMODE="MODE";
	private enum ListeningMode{
		/**公用模式*/
		PUBLIC("公用模式","自动同意邀请，默认已经启动"),
		/**私用模式*/
		PRIVATE("私用模式","不自动同意邀请，默认未启动"),
		/**静止模式*/
		STOP("静止模式","不同意邀请，全体静默"),
		
		/**亢奋模式*/
		HYPERACTIVITY("亢奋状态","自动同意邀请，无视静默全体应答"),
		/**异常模式*/
		ABNORMAL("异常状态","不应存在的状态"),;

		String modeName,help;
		
		ListeningMode(String string, String string2)
		{
			// TODO Auto-generated constructor stub
			modeName=string;
			help=string2;
		}
		static ListeningMode formant(String string)
		{
			for (ListeningMode listening_mode : ListeningMode.values())
			{
				if(listening_mode.name().toLowerCase().equals(string.toLowerCase()))
				{
					return listening_mode;
				}
			}
			return ABNORMAL;
		}
		int getIndex()
		{
			for(int i=0;i<this.values().length;i++)
			{
				if(this.values()[i]==this)
					return i;
			}
			return 4;
		}
	}
	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
//		OnMessageReceiveListener interceptor=new OnMessageReceiveListener() {
//			@Override
//			public int run(ReceiveMessageType messageType)
//			{
//				// TODO Auto-generated method stub
//				receiveMessageType=messageType;
//				
//			}
//		};
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void mode_change()
	{
		String string=getDataExchanger().getItem(LISTENMODE);
		if(string==null)
		{
			string=ListeningMode.PRIVATE.name();
			getDataExchanger().addItem(LISTENMODE, string);
		}
		ListeningMode mode=ListeningMode.formant(string);
		if(mode==null)
		if(mode==ListeningMode.ABNORMAL)
		{
			Log.f("异常的监听模式！停止运行！");
			return;
		}
		if(mode==ListeningMode.HYPERACTIVITY)
		{
			Log.i("停止了亢奋模式");
			getDataExchanger().addItem(LISTENMODE, ListeningMode.PRIVATE.name());
			sendBackMsg("停止了亢奋模式，转为私用模式");
			return;
		}
		int newIndex=mode.getIndex()+1;
		if(newIndex>2)
			newIndex=0;
		mode=ListeningMode.values()[newIndex];
		getDataExchanger().addItem(LISTENMODE, mode.name());
		sendBackMsg("模式调整为"+mode.modeName+"\n"+mode.help);
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void mode_change(ArrayList<String> arrayList)
	{
		String string=arrayList.get(0);
		ListeningMode mode=ListeningMode.formant(string);
		if(mode==ListeningMode.ABNORMAL)
		{
			sendBackMsg("错误的模式名");
		}
		getDataExchanger().addItem(LISTENMODE, mode.name());
		sendBackMsg("模式调整为"+mode.modeName+"\n"+mode.help);
	}
	
	
}
