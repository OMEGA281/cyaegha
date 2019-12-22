package commandMethod;

import java.util.ArrayList;
import java.util.List;

import commandMethod.register.EventListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import connection.SendMessageType;
import global.ConstantTable;
import surveillance.Log;
import transceiver.Transmitter;

public class Ignorer extends Father {
	
	public static List<Long> groupList=new ArrayList<Long>();
	public static List<Long> discussList=new ArrayList<Long>();
	public static List<Long> peopleList=new ArrayList<Long>();
	/**是否为白名单*/
	public static boolean groupListWhite=true;
	/**是否为白名单*/
	public static boolean discussListWhite=true;
	/**是否为白名单*/
	public static boolean peopleListWhite=false;
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub	
		OnMessageReceiveListener listener=new OnMessageReceiveListener(){
			@Override
			public int run(ReceiveMessageType messageType) 
			{
				// TODO Auto-generated method stub
				switch (messageType.getMsgType()) 
				{
				case ConstantTable.MSGTYPE_PERSON:
					if(peopleList.contains(messageType.getfromQQ())==peopleListWhite)
						return RETURN_PASS;
					else
						return RETURN_STOP;
				case ConstantTable.MSGTYPE_GROUP:
					if(groupList.contains(messageType.getfromGroup())==groupListWhite)
						return RETURN_PASS;
					else
						return RETURN_STOP;
				case ConstantTable.MSGTYPE_DISCUSS:
					if(discussList.contains(messageType.getfromGroup())==discussListWhite)
						return RETURN_PASS;
					else
						return RETURN_STOP;

				default:
					Log.e("未知的消息类型");
					return RETURN_PASS;
				}
			}
			};
		listener.priority=EventListener.PRIORITY_MAX;
		addMessageReceiveListener(listener);
	}
	public void commandPointer()
	{
		sendBackMsg("本命令需携带子命令");
	}
	public void commandPointer(ArrayList<String> arrayList)
	{
		boolean ifAdd;
		switch (arrayList.get(0).toLowerCase()) 
		{
		case "add":
			ifAdd=true;
			break;
		case "remove":
			ifAdd=false;
			break;
		case "show":
			if(arrayList.size()<2)
			{
				Log.i("参数数量错误");
				sendBackHelp("参数数量错误");
				return;
			}
			StringBuilder stringBuilder=new StringBuilder();
			switch (arrayList.get(1).toLowerCase()) 
			{
			case "group":
				stringBuilder.append("名单类型："+(groupListWhite?"白名单":"黑名单")+"\n");
				for (Long listpart : groupList) 
				{
					stringBuilder.append(listpart+"\n");
				}
				sendBackMsg(stringBuilder.toString());
				break;
				
			case "discuss":
				stringBuilder.append("名单类型："+(discussListWhite?"白名单":"黑名单")+"\n");
				for (Long listpart : discussList) 
				{
					stringBuilder.append(listpart+"\n");
				}
				sendBackMsg(stringBuilder.toString());
				break;
				
			case "person":
				stringBuilder.append("名单类型："+(peopleListWhite?"白名单":"黑名单")+"\n");
				for (Long listpart : peopleList) 
				{
					stringBuilder.append(listpart+"\n");
				}
				sendBackMsg(stringBuilder.toString());
				break;

			default:
				Log.i("未知的参数类型：",arrayList.get(1).toLowerCase());
				sendBackHelp("未知的参数类型：",arrayList.get(1).toLowerCase());
				return;
			}
		case "reverse":
			if(arrayList.size()<2)
			{
				Log.i("参数数量错误");
				sendBackHelp("参数数量错误");
				return;
			}
			String type=arrayList.get(1);
			switch (type) 
			{
			case "group":
				groupListWhite=!groupListWhite;
				sendBackMsg(type+"名单已变为"+(groupListWhite?"白名单":"黑名单"));
				break;
				
			case "discuss":
				discussListWhite=!discussListWhite;
				sendBackMsg(type+"名单已变为"+(discussListWhite?"白名单":"黑名单"));
				break;
				
			case "person":
				peopleListWhite=!peopleListWhite;
				sendBackMsg(type+"名单已变为"+(peopleListWhite?"白名单":"黑名单"));
				break;

			default:
				Log.i("未知的参数类型：",type);
				sendBackHelp("未知的参数类型：",type);
				return;
			}
		default:
			Log.i("未知的子命令：",arrayList.get(0).toLowerCase());
			sendBackHelp("未知的子命令：",arrayList.get(0).toLowerCase());
			return;
		}
		
		if(arrayList.size()<3)
		{
			Log.i("参数数量错误");
			sendBackHelp("参数数量错误");
			return;
		}
		String type=arrayList.get(1).toLowerCase();
		Long num=Long.parseLong(arrayList.get(2));
		switch (type) {
		case "group":
			groupList.remove(num);
			if(groupListWhite==ifAdd)
				groupList.add(num);
			break;
			
		case "discuss":
			discussList.remove(num);
			if(discussListWhite==ifAdd)
				discussList.add(num);
			break;
			
		case "person":
			peopleList.remove(num);
			if(peopleListWhite==ifAdd)
				peopleList.add(num);
			break;
			
		default:
			Log.i("未知的参数类型：",type);
			sendBackHelp("未知的参数类型：",type);
			return;
		}
		sendBackMsg("成功"+(ifAdd?"添加":"删除")+"了"+"号码为："+num+"的"+type+"的监听");
	}
	public void sendBackHelp(String ...strings)
	{
		StringBuilder stringBuilder=new StringBuilder();
		for (String string : strings) 
		{
			stringBuilder.append(string);
		}
		sendBackMsg(stringBuilder.toString());
	}
}
