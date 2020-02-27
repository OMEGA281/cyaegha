package commandMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;

import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;

public class PhraseListener extends Father 
{
	private static final String Split="##";
	private static final String LISTNAME="return";
	private static final String HEAD="head";
	
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		addMessageReceiveListener(new OnMessageReceiveListener() {
			@Override
			public int run(ReceiveMessageType messageType) {
				// TODO Auto-generated method stub
				
				receiveMessageType=messageType;
				
				ArrayList<String[]> arrayList=getDataExchanger().getList(LISTNAME);
				if(arrayList==null)
					return RETURN_PASS;
				for (String[] strings : arrayList)
				{
					String $patten=strings[1].split(Split, 2)[0];
					Pattern pattern=Pattern.compile($patten);
					if(pattern.matcher(receiveMessageType.getMsg()).matches())
					{
						String[] answer=strings[1].split(Split, 2)[1].split(Split);
						if(answer.length==1)
						{
							sendBackMsg(answer[0]);
						}
						else
						{
							Random random=new Random();
							sendBackMsg(answer[random.nextInt(answer.length)]);
							return RETURN_PASS;
						}
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
		String index1=arrayList.get(0);
		String index2=arrayList.get(1);
		ArrayList<String[]> texts=getDataExchanger().getList(LISTNAME);
//		完全空的表
		if(texts==null)
		{
			getDataExchanger().addListItem(LISTNAME, HEAD, index1+Split+index2);
			sendBackMsg("成功添加了条目");
			return;
		}
//		表中有了
		for (int i=0;i<texts.size();i++)
		{
			String[] strings=texts.get(i);
			String line=strings[1];
			String patten=line.split(Split, 2)[0];
			if(!patten.equals(index1))
				continue;
			String[] answer=line.split(Split, 2)[1].split(Split);
			for (String string : answer)
			{
				if(string.equals(index2))
				{
					sendBackMsg("表中已有相同条目");
					return;
				}
			}
			StringBuffer buffer=new StringBuffer();
			for (String string : answer)
			{
				buffer.append(string+Split);
			}
			buffer.append(index2);
			getDataExchanger().deleteListItem(LISTNAME, i);
			getDataExchanger().addListItem(LISTNAME, HEAD, patten+Split+buffer.toString());
			sendBackMsg("成功添加了条目");
			return;
		}
		getDataExchanger().addListItem(LISTNAME, HEAD, index1+Split+index2);
		sendBackMsg("成功添加了条目");
	}
	
	public void deleteanswer()
	{
		sendBackMsg("删除回应匹配式：.addanswer 表达式");
	}
	
	@MinimumAuthority(authirizerUser = AuthirizerUser.OP)
	public void deleteanswer(ArrayList<String> arrayList)
	{
		String aim=null;
		if(arrayList.size()<1)
		{
			sendBackMsg("回应不是这么删除的哦！");
			return;
		}
		String patten=arrayList.get(0);
		if(arrayList.size()>1)
			aim=arrayList.get(1);
		
		ArrayList<String[]> list=getDataExchanger().getList(LISTNAME);
		boolean b=false;
		for (int i=0;i<list.size();i++)
		{
			String text=list.get(i)[1];
			String $patten=text.split(Split, 2)[0];
			if(!$patten.equals(patten))
				continue;
			String[] $answer=text.split(Split,2)[1].split(Split);
			if(aim!=null)
			{
				boolean find=false;
				for (int o=0;o<$answer.length;o++)
				{
					String string=$answer[o];
					if(string.equals(aim))
					{
						$answer[o]="";
						find=true;
						break;
					}
				}
				if(find)
				{
					StringBuffer buffer=new StringBuffer();
					for (String string : $answer)
					{
						if(!string.isEmpty())
						{
							buffer.append(string+Split);
						}
					}
					b=getDataExchanger().deleteListItem(LISTNAME, i);
					if(buffer.length()>0)
						getDataExchanger().addListItem(LISTNAME, HEAD, $patten+Split+buffer.toString());
					sendBackMsg(b?"成功删除了条目":"没有这个条目");
					return;
				}
				else
				{
					sendBackMsg("未检测指定的回复");
					return;
				}
			}
			else
			{
				b=getDataExchanger().deleteListItem(LISTNAME, i);
				break;
			}
		}
		sendBackMsg(b?"成功删除了条目":"没有这个条目");
	}
}
