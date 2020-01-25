package commandMethod;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import commandMethod.register.OnEventListener;
import commandMethod.register.OnMessageReceiveListener;
import connection.CQSender;
import connection.ReceiveMessageType;
import global.ConstantTable;
import surveillance.Log;

public class Ignorer extends Father {
	final String WHITELIST_PERSON="WLpreson";
	final String WHITELIST_GROUP="WLgroup";
	final String WHITELIST_DISCUSS="WLdiscuss";
	final String BLACKLIST_PERSON="BLpreson";
	final String BLACKLIST_GROUP="BLgroup";
	final String BLACKLIST_DISCUSS="BLdiscuss";
	final String IFWL_PERSON="IfWLperson";
	final String IFWL_GROUP="IfWLgroup";
	final String IFWL_DISCUSS="IfWLdiscuss";
	
	private static long superOP=-1;
	private static boolean onDialog=false;
	
	final String HELP="子命令：add,remove,show\n"
			+ "add group(g)/discuss(d)/person(p) blacklist(b)/whitelist(w) 号码\n"
			+ "remove group(g)/discuss(d)/person(p) blacklist(b)/whitelist(w) 号码\n"
			+ "list group(g)/discuss(d)/person(p)\n";
	
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub	
		OnMessageReceiveListener listener=new OnMessageReceiveListener(){
			@Override
			public int run(ReceiveMessageType messageType) 
			{
				receiveMessageType=messageType;
				if(superOP==-1)
				{
					if(!onDialog)
					{
						final JFrame frame=new JFrame("设置superOP");
						frame.setBounds(100, 100, 300, 120);
						frame.setUndecorated(true);
						frame.setAlwaysOnTop(true);
						
						Container container=frame.getContentPane();
						container.setLayout(null);
						JLabel label=new JLabel("请设置superOP的号码：");
						label.setBounds(10, 10, 280, 20);
						
						final JLabel label2=new JLabel();
						label2.setBounds(10, 90, 280, 20);
						label2.setForeground(Color.RED);
						
						final JTextField field=new JTextField();
						field.setBounds(10, 40, 200, 40);
						field.addMouseListener(new MouseListener() {
							
							@Override
							public void mouseReleased(MouseEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void mousePressed(MouseEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void mouseExited(MouseEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void mouseEntered(MouseEvent e) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void mouseClicked(MouseEvent e) {
								// TODO Auto-generated method stub
								if(label2.getText()!="")
								{
									field.setText("");
									label2.setText("");
								}
							}
						});
						container.add(label);
						container.add(field);
						
						
						
						JButton button=new JButton("确定");
						button.setBounds(220, 40, 70, 40);
						button.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								String string=field.getText();
								try
								{
									superOP=Long.parseLong(string);
									addList(WHITELIST_PERSON, superOP);
									sendPrivateMsg(superOP, 
											CQSender.getSender().getMyName()+"将您设为superOP");
								}
								catch(NumberFormatException exception)
								{
									label2.setText("输入错误！");
									return;
								}
								frame.dispose();
							}
						});
						
						container.add(button);
						container.add(label2);
						frame.setVisible(true);
						onDialog=true;
					}
					return RETURN_STOP;
				}
				// TODO Auto-generated method stub
				switch (messageType.getMsgType()) 
				{
				case ConstantTable.MSGTYPE_PERSON:
					if(!getList(BLACKLIST_PERSON).contains(messageType.getfromQQ())&&
							(getList(WHITELIST_PERSON).contains(messageType.getfromQQ())|
									ifWL(IFWL_PERSON)))
						return RETURN_PASS;
					else
						return RETURN_STOP;
				case ConstantTable.MSGTYPE_GROUP:
					if(!getList(BLACKLIST_GROUP).contains(messageType.getfromGroup())&&
							(getList(WHITELIST_GROUP).contains(messageType.getfromGroup())|
									ifWL(IFWL_GROUP)))
						return RETURN_PASS;
					else
						return RETURN_STOP;
				case ConstantTable.MSGTYPE_DISCUSS:
					if(!getList(BLACKLIST_DISCUSS).contains(messageType.getfromGroup())&&
							(getList(WHITELIST_DISCUSS).contains(messageType.getfromGroup())|
									ifWL(IFWL_DISCUSS)))
						return RETURN_PASS;
					else
						return RETURN_STOP;

				default:
					Log.e("未知的消息类型");
					return RETURN_PASS;
				}
			}
			};
			
		listener.priority=OnEventListener.PRIORITY_MAX;
		
		addMessageReceiveListener(listener);
	}
	public void commandPointer()
	{
		sendBackMsg("本命令需携带子命令\n"+HELP);
	}
	public void commandPointer(ArrayList<String> arrayList)
	{
		switch (arrayList.get(0).toLowerCase()) 
		{
		case "add":
			Boolean ifW;
			int type;
			if(arrayList.size()<4)
			{
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			switch (arrayList.get(1).toLowerCase()) 
			{
			case "group":
			case "g":
				type=ConstantTable.MSGTYPE_GROUP;
				break;
			case "discuss":
			case "d":
				type=ConstantTable.MSGTYPE_DISCUSS;
				break;
			case "person":
			case "p":
				type=ConstantTable.MSGTYPE_PERSON;
				break;

			default:
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			switch (arrayList.get(2).toLowerCase()) 
			{
			case "blacklist":
			case "b":
				ifW=false;
				break;
			case "whitelist":
			case "w":
				ifW=true;
				break;

			default:
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			long num;
			try 
			{
				num=Long.parseLong(arrayList.get(3).toLowerCase());
			} 
			catch (NumberFormatException e) 
			{
				// TODO Auto-generated catch block
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			String string=ifW?"WL":"BL";
			switch (type) 
			{
			case ConstantTable.MSGTYPE_PERSON:
				string+="person";
				break;
			case ConstantTable.MSGTYPE_GROUP:
				string+="group";
				break;
			case ConstantTable.MSGTYPE_DISCUSS:
				string+="discuss";
				break;

			default:
				break;
			}
			addList(string, num);
			string=(!ifW?"WL":"BL")+string.substring(2);
			removeList(string, num);
			sendBackMsg("将"+num+"加入了名单");
			break;
		case "remove":
			Boolean ifW1;
			int type1;
			if(arrayList.size()<4)
			{
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			switch (arrayList.get(1).toLowerCase()) 
			{
			case "group":
			case "g":
				type1=ConstantTable.MSGTYPE_GROUP;
				break;
			case "discuss":
			case "d":
				type1=ConstantTable.MSGTYPE_DISCUSS;
				break;
			case "person":
			case "p":
				type1=ConstantTable.MSGTYPE_PERSON;
				break;

			default:
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			switch (arrayList.get(2).toLowerCase()) 
			{
			case "blacklist":
			case "b":
				ifW1=false;
				break;
			case "whitelist":
			case "w":
				ifW1=true;
				break;

			default:
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			long num1;
			try 
			{
				num1=Long.parseLong(arrayList.get(3).toLowerCase());
			} 
			catch (NumberFormatException e) 
			{
				// TODO Auto-generated catch block
				sendBackMsg("参数错误\n"+HELP);
				return;
			}
			String string1=ifW1?"WL":"BL";
			switch (type1) 
			{
			case ConstantTable.MSGTYPE_PERSON:
				string1+="person";
				break;
			case ConstantTable.MSGTYPE_GROUP:
				string1+="group";
				break;
			case ConstantTable.MSGTYPE_DISCUSS:
				string1+="discuss";
				break;

			default:
				break;
			}
			removeList(string1, num1);
			sendBackMsg("将"+num1+"移出了名单");
			break;
		case "list":
			if(arrayList.size()<2)
			{
				sendBackMsg("参数错误"+HELP);
				return;
			}
			StringBuilder stringBuilder=new StringBuilder();
			switch (arrayList.get(1).toLowerCase()) 
			{
			case "group":
			case "g":
				stringBuilder.append("监听类型："+(ifWL(IFWL_GROUP)?"不监听黑名单":"仅监听白名单")+"\n");
				stringBuilder.append("白名单如下:");
				for (Long listpart : getList(WHITELIST_GROUP)) 
				{
					stringBuilder.append(listpart+"\n");
				}
				stringBuilder.append("黑名单如下:");
				for (Long listpart : getList(BLACKLIST_GROUP)) 
				{
					stringBuilder.append(listpart+"\n");
				}
				sendBackMsg(stringBuilder.toString());
				break;
				
			case "discuss":
			case "d":
				stringBuilder.append("监听类型："+(ifWL(IFWL_DISCUSS)?"不监听黑名单":"仅监听白名单")+"\n");
				stringBuilder.append("白名单如下:");
				for (Long listpart : getList(WHITELIST_DISCUSS)) 
				{
					stringBuilder.append(listpart+"\n");
				}
				stringBuilder.append("黑名单如下:");
				for (Long listpart : getList(BLACKLIST_DISCUSS)) 
				{
					stringBuilder.append(listpart+"\n");
				}
				sendBackMsg(stringBuilder.toString());
				break;
				
			case "person":
			case "p":
				stringBuilder.append("监听类型："+(ifWL(IFWL_PERSON)?"不监听黑名单":"仅监听白名单")+"\n");
				stringBuilder.append("白名单如下:");
				for (Long listpart : getList(WHITELIST_PERSON)) 
				{
					stringBuilder.append(listpart+"\n");
				}
				stringBuilder.append("黑名单如下:");
				for (Long listpart : getList(BLACKLIST_PERSON)) 
				{
					stringBuilder.append(listpart+"\n");
				}
				sendBackMsg(stringBuilder.toString());
				break;
				
			
			default:
				sendBackMsg("子命令错误"+HELP);
				return;
			}
			break;
		case "change":
			if(arrayList.size()<2)
			{
				sendBackMsg("参数错误"+HELP);
				return;
			}
			switch (arrayList.get(1).toLowerCase()) 
			{
			case "group":
			case "g":
				sendBackMsg(changeWBListener(IFWL_GROUP)?
						("逆转了群监听类型，现在为"+(ifWL(IFWL_GROUP)?"不监听黑名单":"仅监听白名单"))
						:"未知错误");
				break;
			case "discuss":
			case "d":
				sendBackMsg(changeWBListener(IFWL_DISCUSS)?
						("逆转了群监听类型，现在为"+(ifWL(IFWL_DISCUSS)?"不监听黑名单":"仅监听白名单"))
						:"未知错误");
				break;
			case "person":
			case "p":
				sendBackMsg(changeWBListener(IFWL_PERSON)?
						("逆转了群监听类型，现在为"+(ifWL(IFWL_PERSON)?"不监听黑名单":"仅监听白名单"))
						:"未知错误");
				break;
			default:
				sendBackMsg("参数错误"+HELP);
				return;
			}
			break;
		default:
			sendBackMsg("参数错误"+HELP);
			return;
		}
	}
	/**
	 * 向表中添加成员
	 * @param type 表名
	 * @param l 号码
	 * @return {@code true}如果添加成功<br>{@code false}已经有此号码
	 */
	private boolean addList(String type,long l)
	{
		StringBuilder builder=new StringBuilder();
		ArrayList<Long> past=getList(type);
		if(past.contains(l))
		{
			return false;
		}
		past.add(l);
		for (Long long1 : past) 
		{
			builder.append(long1+",");
		}
		getDataExchanger().addItem(type, builder.toString());
		return true;
	}
	/**
	 * 向表中删除成员
	 * @param type 表名
	 * @param l 号码
	 * @return {@code true}如果删除成功<br>{@code false}没有此号码
	 */
	private boolean removeList(String type,long l)
	{
		StringBuilder builder=new StringBuilder();
		ArrayList<Long> past=getList(type);
		if(!past.contains(l))
		{
			return false;
		}
		past.remove(l);
		for (Long long1 : past) 
		{
			builder.append(long1+",");
		}
		getDataExchanger().addItem(type, builder.toString());
		return true;
	}
	private boolean changeWBListener(String type)
	{
		boolean b=ifWL(type);
		 if(!setWBListener(type, !b))
			 return false;
		 return true;
	}
	private boolean setWBListener(String type,boolean b)
	{
		switch(type)
		{
		case IFWL_DISCUSS:	
		case IFWL_GROUP:
		case IFWL_PERSON:
			getDataExchanger().addItem(type, b+"");
			return true;
			
			default:
				return false;
		}
	}
	private boolean ifWL(String type)
	{
		switch(type)
		{
		case IFWL_PERSON:
			return Boolean.parseBoolean(getDataExchanger().getItem(IFWL_PERSON));
		case IFWL_GROUP:
			return Boolean.parseBoolean(getDataExchanger().getItem(IFWL_GROUP));
		case IFWL_DISCUSS:
			return Boolean.parseBoolean(getDataExchanger().getItem(WHITELIST_DISCUSS));
			
			default:
				return false;
		}
	}
	private ArrayList<Long> getList(String type)
	{
		String list = null;
		ArrayList<Long> result=new ArrayList<>();
		switch (type) 
		{
		case WHITELIST_PERSON:
			list=getDataExchanger().getItem(WHITELIST_PERSON);
			break;
		case WHITELIST_GROUP:
			list=getDataExchanger().getItem(WHITELIST_GROUP);
			break;
		case WHITELIST_DISCUSS:
			list=getDataExchanger().getItem(WHITELIST_DISCUSS);
			break;
			
		case BLACKLIST_PERSON:
			list=getDataExchanger().getItem(BLACKLIST_PERSON);
			break;
		case BLACKLIST_GROUP:
			list=getDataExchanger().getItem(BLACKLIST_GROUP);
			break;
		case BLACKLIST_DISCUSS:
			list=getDataExchanger().getItem(BLACKLIST_DISCUSS);
			break;
			
		default:
			break;
		}
		if(list==null)
			return new ArrayList<Long>();
		for (String string : list.split(",")) 
		{
			result.add(Long.parseLong(string));
		}
		return result;
	}
}
