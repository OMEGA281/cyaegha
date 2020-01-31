package commandMethod;

import java.util.ArrayList;

import commandMethod.register.OnGroupMemberChangeListener;
import connection.GroupChangeType;
import connection.ReceiveMessageType;
import global.UniversalConstantsTable;

public class Welcome extends Father
{
	
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		help=".welcome 欢迎语\n 不接函数则为清除";
		OnGroupMemberChangeListener groupMemberChangeListener=new OnGroupMemberChangeListener() {
			@Override
			public int run(GroupChangeType groupChangeType) {
				// TODO Auto-generated method stub
				if(groupChangeType.getCreaseType()==GroupChangeType.GROUP_MUMBER_CREASE)
				{
					String welcome=getString(groupChangeType.getGroupNum());
					if(welcome!=null)
						sendGroupMsg(groupChangeType.getGroupNum(), groupChangeType.getQQNum(), welcome);
				}
				return RETURN_PASS;
			}
		};
		groupMemberChangeListener.priority=30;
		addGroupMemberChangeListener(groupMemberChangeListener);
	}
	
	public void welcome()
	{
		deleteString();
		sendBackMsg("删除成功");
	}
	public void welcome(ArrayList<String> arrayList)
	{
		setString(arrayList.get(0));
		sendBackMsg("设置成功");
	}
	private void setString(String string)
	{
		String name = null;
		switch(receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			name="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			name="G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			name="D"+receiveMessageType.getfromGroup();
			break;
		}
		getDataExchanger().addItem(name, string);
	}
	private String getString(long GroupNum)
	{
		String name = "G"+GroupNum;
		return getDataExchanger().getItem(name);
	}
	private void deleteString()
	{
		String name = null;
		switch(receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			name="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			name="G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			name="D"+receiveMessageType.getfromGroup();
			break;
		}
		getDataExchanger().deleteItem(name);
	}
}
