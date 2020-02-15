package commandMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;

public class PhraseListener extends Father 
{
	private static ArrayList<WordRefect> WordsMap=new ArrayList<>();
	private static final int ONLY_EXISTS=0;
	private static final int CONTAIN_EXISTS=1;
	private class WordRefect
	{
		ArrayList<String> words=new ArrayList<>();
		String returnString;
		boolean existStatus;
	}
	
	public static final String XMLHead="H";
	
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		addMessageReceiveListener(new OnMessageReceiveListener() {
			@Override
			public int run(ReceiveMessageType messageType) {
				// TODO Auto-generated method stub
				
				receiveMessageType=messageType;
				HashMap<String, String> word=getDataExchanger().getAllItem();
				for (Entry<String, String> entry : word.entrySet())
				{
					String secret=entry.getKey().substring(1, entry.getKey().length());
					
					char[] cs=new char[secret.length()/5];
					for(int i=0;i<secret.length();i=i+5)
					{
						cs[i/5]=(char) Integer.parseInt(secret.substring(i, i+5));
					}
					Pattern pattern=Pattern.compile(new String(cs));
					String msg=messageType.getMsg();
					if(pattern.matcher(msg).matches())
					{
						sendBackMsg(entry.getValue());
						break;
					}
				}
				
				
				return RETURN_PASS;
			}
		});
	}
	
	public void addanswer()
	{
		sendBackMsg("添加回应匹配式：.addanswer 表达式 回应");
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void addanswer(ArrayList<String> arrayList)
	{
		if(arrayList.size()<2)
		{
			sendBackMsg("回应不是这么添加的哦！");
			return;
		}
		String partten=arrayList.get(0);
		char[] text=partten.toCharArray();
		String tranS="";
		for (char c : text)
		{
			tranS=tranS+String.format("%05d", (int)c);
		}
		getDataExchanger().addItem(XMLHead+tranS, arrayList.get(1));
		sendBackMsg("成功添加了条目");
	}
	
	public void deleteanswer()
	{
		sendBackMsg("删除回应匹配式：.addanswer 表达式");
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void deleteanswer(ArrayList<String> arrayList)
	{
		if(arrayList.size()<1)
		{
			sendBackMsg("回应不是这么删除的哦！");
			return;
		}
		String partten=arrayList.get(0);
		char[] text=partten.toCharArray();
		String tranS="";
		for (char c : text)
		{
			tranS=tranS+String.format("%05d", (int)c);
		}
		boolean b= getDataExchanger().deleteItem(XMLHead+tranS);
		sendBackMsg(b?"成功删除了条目":"没有这个条目");
	}
}
