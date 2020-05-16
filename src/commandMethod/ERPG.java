package commandMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commandPointer.annotations.AuxiliaryClass;
import global.UniversalConstantsTable;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

@AuxiliaryClass
public class ERPG extends Father 
{
	private final String SAME_STRING="samestring";
	private final String SAME_STRING_LINE="string";
	private final String[] ShortCrazy = { 
			"失忆：发现自己只记得最后身处的安全地点，却没有任何来到这里的记忆", 
			"假性残疾：陷入了心理性的失明，失聪或躯体缺失感中",
			"暴力倾向：陷入了六亲不认的暴力行为中，对周围的敌人与友方进行着无差别的攻击",
			"偏执：陷入了严重的偏执妄想之中",
			"人际依赖：因为一些原因而降他人误认为了他重要的人并且努力的会与那个人保持那种关系",
			"昏厥：当场昏倒",
			"逃避行为：会用任何的手段试图逃离现在所处的位置",
			"竭嘶底里：表现出大笑，哭泣，嘶吼，害怕等的极端情绪表现",
			"恐惧：获得了一种恐惧症状(由kp决定，或.draw 恐怖症状)",
			"狂躁：获得了一种狂躁症状(由kp决定，或.draw 狂躁症状)" };
	private final String[] LongCrazy= {
			"失忆：发现自己身处一个陌生的地方，并忘记了自己是谁。记忆会随时间恢复。",
			"被窃：时间过后恢复清醒，发觉自己被盗，身体毫发无损。所有有价值的东西消失。",
			"遍体鳞伤：时间过后恢复清醒，发现自己身上满是拳痕和瘀伤。生命值减少到疯狂前的一半，但不会造成重伤。",
			"暴力倾向：陷入强烈的暴力与破坏欲之中。回过神来可能会理解自己做了什么也可能毫无印象。",
			"极端信念：采取极端和疯狂的表现手段展示他们的思想信念之一。",
			"重要之人：在持续或更久的时间中，将不顾一切地接近那个人，并为他们之间的关系做出行动。",
			"被收容：在精神病院病房或警察局牢房中回过神来，可能会慢慢回想起导致自己被关在这里的事情。",
			"逃避行为：恢复清醒时发现自己在很远的地方。",
			"恐惧：患上一个新的恐惧症。(由kp决定，或.draw 恐怖症状)时间过后将会尽力避开恐惧源。",
			"狂躁：患上一个新的狂躁症。(由kp决定，或.draw 狂躁症状)时间过后恢复理智"};
 	private enum LevelStatus{EX_SUCCESS("极难成功"),S_SUCCESS("困难成功"),SUCCESS("成功"),FAILED("失败");
		private String string;
		public String getString()
		{
			return string;
		}
	LevelStatus(String string) {
		// TODO Auto-generated constructor stub
		this.string=string;
	}}
	private enum SpecialStatus{BIGSUCCESS("大成功!"),BIGFAILED("大失败!");
		private String string;
		public String getString()
		{
			return string;
		}
	SpecialStatus(String string) {
		// TODO Auto-generated constructor stub
		this.string=string;
	}}
	/**
	 * 提升技能上限的情况
	 * @author GuoJiaCheng
	 *
	 */
	private enum SkillUBound{san("克苏鲁神话",-1);
		private String effectSkill;
		private int effectNum;
		public String getEffectSkill()
		{
			return effectSkill;
		}
		public int getEffect()
		{
			return effectNum;
		}
		SkillUBound(String string, int i) {
		// TODO Auto-generated constructor stub
			effectNum=i;
			effectSkill=string;
		}
		
	}
	/**
	 * 提升技能下限的情况
	 * @author GuoJiaCheng
	 *
	 */
	private enum SkillBBound{;
		private String effectSkill;
		private int effectNum;
		public String getEffectSkill()
		{
			return effectSkill;
		}
		public int getEffect()
		{
			return effectNum;
		}
		SkillBBound(String string, int i) {
		// TODO Auto-generated constructor stub
			effectNum=i;
			effectSkill=string;
		}
		
	}
	/**
	 * 用于返回检定情况的类，包含{@link LevelStatus}和{@link SpecialStatus}
	 * @author GuoJiaCheng
	 *
	 */
	private class CheckStatus
	{
		LevelStatus levelStatus;
		SpecialStatus specialStatus;
		int randomNum;
		public CheckStatus(LevelStatus levelStatus,SpecialStatus specialStatus,int randomNum) {
			// TODO Auto-generated constructor stub
			this.levelStatus=levelStatus;
			this.specialStatus=specialStatus;
			this.randomNum=randomNum;
		}
	}
	private enum Help{
		r(".r （次数（1~5），默认为1）[d][面数，默认为100]\\n");
		
		
		String help;
		Help(String string) {
			// TODO Auto-generated constructor stub
			this.help=string;
		}
		
	}
	
	private static ArrayList<ArrayList<String>> SameStringList;
	
	
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void r()
	{
		r(null);
	}
	public void r(ArrayList<String> arrayList)
	{
		String help=".r （次数（1~5），默认为1）[d][面数，默认为100]\n";
		int part=100,time;
		StringBuilder builder=new StringBuilder();
		if(arrayList==null)
		{
			sendBackMsg(help);
			return;
		}
		else
		{
			try
			{
				time=formatNum(arrayList.get(0), 1, 5);
			}
			catch(NumberFormatException exception)
			{
				int i=transRandomString(arrayList.get(0));
				if(i<0)
				{
					sendBackMsg("参数不合要求或溢出");
					return;
				}
				builder.append(getMessageSenderName()+"掷出了"+arrayList.get(0)+"="+i);
				sendBackMsg(builder.toString());
				return;
			}
			
		}
		int rnum=getRandomNum(part, time);
		builder.append(getMessageSenderName()+"掷出了"+time+"d"+part+"="+rnum);
		sendBackMsg(builder.toString());
	}
	
	public void rh()
	{
		rh(null);
	}
	public void rh(ArrayList<String> arrayList)
	{
		String help=".rh （次数（1~5），默认为1）[d][面数，默认为100]\n";
		int part=100,time;
		StringBuilder builder=new StringBuilder();
		if(arrayList==null)
		{
			sendBackMsg(help);
			return;
		}
		else
		{
			try
			{
				time=formatNum(arrayList.get(0), 1, 5);
			}
			catch(NumberFormatException exception)
			{
				int i=transRandomString(arrayList.get(0));
				if(i<0)
				{
					sendBackMsg("参数不合要求或溢出");
					return;
				}
				builder.append("你掷出了"+arrayList.get(0)+"="+i);
				sendPrivateMsg(receiveMessageType.getfromQQ(), builder.toString());
				return;
			}
			
		}
		int rnum=getRandomNum(part, time);
		builder.append("你掷出了"+time+"d"+part+"="+rnum);
		sendPrivateMsg(receiveMessageType.getfromQQ(), builder.toString());
	}

	public void rb()
	{
		rb(null);
	}
	public void rb(ArrayList<String> arrayList)
	{
		String help=".rb [奖励骰数目，默认为1]";
		StringBuilder stringBuilder=new StringBuilder();
		int time;
		if(arrayList==null)
			time=1;
		else
		{
			try
			{
				time=formatNum(arrayList.get(0), 1, 5);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				stringBuilder.append("数量错误，请输入1~5，按照默认数量\n");
				time=1;
			}
		}
		int[] arr=getRandomNumWithBP(time, true);
		StringBuilder otherNum=new StringBuilder();
		for(int i=2;i<arr.length;i++)
		{
			otherNum.append(Integer.toString(arr[i])+",");
		}
		otherNum.deleteCharAt(otherNum.length()-1);
		stringBuilder.append(getMessageSenderName()+"掷出了1d100="+arr[1]+"\n");
		stringBuilder.append("掷奖励骰1d10="+otherNum.toString()+",得"+arr[0]);
		sendBackMsg(stringBuilder.toString());
	}
	
	public void rp()
	{
		rp(null);
	}
	public void rp(ArrayList<String> arrayList)
	{
		String help=".rp [惩罚骰数目，默认为1]";
		StringBuilder stringBuilder=new StringBuilder();
		int time;
		if(arrayList==null)
			time=1;
		else
		{
			try
			{
				time=formatNum(arrayList.get(0), 1, 5);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				stringBuilder.append("数量错误，请输入1~5");
				time=1;
			}
		}
		int[] arr=getRandomNumWithBP(time, false);
		StringBuilder otherNum=new StringBuilder();
		for(int i=2;i<arr.length;i++)
		{
			otherNum.append(Integer.toString(arr[i])+",");
		}
		otherNum.deleteCharAt(otherNum.length()-1);
		stringBuilder.append(getMessageSenderName()+"掷出了1d100="+arr[1]+"\n");
		stringBuilder.append(getMessageSenderName()+"掷惩罚骰1d10="+otherNum.toString()+",得"+arr[0]);
		sendBackMsg(stringBuilder.toString());
	}
	
	public void st()
	{
		final String help=".st 项目名称 项目数值（1-100）\n"
				+ "可以同时录入多个，规则按照：\n"
				+ ".st 技能 数值 [技能 数值] [技能 数值]…\n"
				+"如果不包含项目数值，则为清除该条目\n";
		sendBackMsg("需要输入参数"+help);
	}
	public void st(ArrayList<String> arrayList)
	{
		final String help=".st 项目名称 项目数值（1-100）\n"
				+ "可以同时录入多个，规则按照：\n"
				+ ".st 技能 数值 [技能 数值] [技能 数值]…\n"
				+"如果不包含项目数值，则为清除该条目\n";
		if(arrayList.size()<2)
		{
//			检测是哪一种类型，可能是连续的输入，也可能是表达式
			if(Pattern.compile("[+-]").matcher(arrayList.get(0)).find())
			{
//				检测到了运算符
				String[] part=arrayList.get(0).split("[+-]");
				String skill=null;
//				检测并获得修改的名称
				for (String string : part) 
				{
					if(!Pattern.compile("[0-9d]*").matcher(string).matches())
					{
						if(skill==null||skill.equals(string))
							skill=string;
						else
						{
							sendBackMsg("算数表达式仅可以含有一种技能名");
							return;
						}
					}
				}
				
				if(skill==null)
				{
					sendBackMsg("没有找到技能名称");
					return;
				}
				int last=getSkill(skill);
				if(last<0)
				{
					sendBackMsg("该技能不存在");
					return;
				}
				
//				替换
				arrayList.set(0, arrayList.get(0).replaceAll(skill, Integer.toString(getSkill(skill))));
//				链接
				setSkill(skill, transRandomString(arrayList.get(0)));
				StringBuilder stringBuilder=new StringBuilder();
				stringBuilder.append(getMessageSenderName()+"的"+skill+"从"+last+"变成了"+getSkill(skill));
				sendBackMsg(stringBuilder.toString());
				return;
			}
			StringBuilder builder=new StringBuilder(arrayList.get(0));
			char[] cs=builder.toString().toCharArray();
			arrayList.clear();
			int lastIndex=0;
			for(int i=1;i<cs.length;i++)
			{
				if(('0'<=cs[i]&&cs[i]<='9'&&!('0'<=cs[i-1]&&cs[i-1]<='9'))||(!('0'<=cs[i]&&cs[i]<='9')&&('0'<=cs[i-1]&&cs[i-1]<='9')))
				{
					arrayList.add(builder.substring(lastIndex, i).toString());
					lastIndex=i;
				}
			}
			arrayList.add(builder.substring(lastIndex,builder.length()));
		}
		ArrayList<String> output=new ArrayList<>();
		for(int i=0;i<arrayList.size();i+=2)
		{
			if(arrayList.size()<=i+1)
				break;
			String skill=arrayList.get(i);
			int skillNum=-1;
			try
			{
				skillNum=formatNum(arrayList.get(i+1), 0, 100);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				skillNum=transRandomString(arrayList.get(i+1));
			}
			if(skillNum<0)
				continue;
			setSkill(skill, skillNum);
			output.add(skill+":"+skillNum);
		}
		StringBuilder builder=new StringBuilder();
		builder.append(getMessageSenderName()+"添加了以下属性：\n");
		for (String string : output) {
			builder.append(string+"  ");
		}
		sendBackMsg(builder.toString());
	}
	
	public void ra()
	{
		ra(null);
	}
	public void ra(ArrayList<String> arrayList)
	{
		String help=".ra 技能名 [技能数值，有此参数优先使用此；若没有记录过技能则必须填写此参数]\n";
		if(arrayList==null)
		{
			sendBackMsg("请填写参数\n"+help);
			return;
		}
		String skill=arrayList.get(0);
		int skillnum=getSkill(skill);
		if(arrayList.size()>1)
		{
			try
			{
				skillnum=formatNum(arrayList.get(1), 1, 100);
			}catch(NumberFormatException exception)
			{
				sendBackMsg("输入的数值错误，请设为1~100");
				return;
			}
		}
		if(skillnum==-1)
		{
			sendBackMsg("临时技能检定需要填写临时数值\n"+help);
			return;
		}
		CheckStatus checkStatus=numCheck(skillnum);
		StringBuilder builder=new StringBuilder();
		builder.append("对"+getMessageSenderName()+"的"+skill+"进行检定，掷出1d100="+checkStatus.randomNum+"/"+skillnum+"\n");
		builder.append("检定"+checkStatus.levelStatus.getString());
		if(checkStatus.specialStatus!=null)
			builder.append(","+checkStatus.specialStatus.getString());
		builder.append("\n");
		sendBackMsg(builder.toString());
	}
	
	public void rab()
	{
		rab(null);
	}
	public void rab(ArrayList<String> arrayList)
	{
		String help=".rab [奖励骰数量，缺省为1] 技能名 [技能值，若有此值，优先此值；若技能为临时技能，则必须填写本数]\n";
		StringBuilder builder=new StringBuilder();
		if(arrayList==null)
		{
			sendBackMsg(help);
			return;
		}
		int readPoint=0;
		int time=1;
		if(Pattern.compile("^[0-9].*").matcher(arrayList.get(readPoint)).matches())
		{
			try
			{
				time=formatNum(arrayList.get(readPoint), 1, 5);
				readPoint++;
			}
			catch (NumberFormatException e) {
				// TODO: handle exception
				builder.append("输入的数值错误，请设为1~5，按照默认值\n");
				time=1;
			}
		}
		if(arrayList.size()<=readPoint)
		{
			sendBackMsg("请输入进行检定的技能\n"+help);
			return;
		}
		String skill=arrayList.get(readPoint);
		readPoint++;
		int skillpoint=getSkill(skill);
		if(arrayList.size()>readPoint)
		{
			try
			{
				skillpoint=formatNum(arrayList.get(readPoint), 1, 100);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				sendBackMsg("输入的技能数值错误，应为1-100");
				return;
			}
		}
		if(skillpoint==-1)
		{
			sendBackMsg("临时技能检定需要填写临时数值\n"+help);
			return;
		}
		
		int[] arr=getRandomNumWithBP(time, true);
		builder.append("对"+getMessageSenderName()+"的"+skill+"进行检定，掷出1d100="+arr[1]+"\n");
		StringBuilder otherNum=new StringBuilder();
		for(int i=2;i<arr.length;i++)
		{
			otherNum.append(arr[i]+",");
		}
		otherNum.deleteCharAt(otherNum.length()-1);
		builder.append("掷奖励骰1d10="+otherNum.toString()+",得"+arr[0]+"/"+skillpoint+"\n");
		CheckStatus checkStatus=numCheck(skillpoint, arr[0]);
		builder.append("检定"+checkStatus.levelStatus.getString());
		if(checkStatus.specialStatus!=null)
			builder.append(","+checkStatus.specialStatus.getString());
		sendBackMsg(builder.toString());
	}
	
	public void rap()
	{
		rap(null);
	}
	public void rap(ArrayList<String> arrayList)
	{
		String help=".rap [惩罚骰数量，缺省为1] 技能名 [技能值，若有此值，优先此值；若技能为临时技能，则必须填写本数]\n";
		StringBuilder builder=new StringBuilder();
		if(arrayList==null)
		{
			sendBackMsg(help);
			return;
		}
		int readPoint=0;
		int time=1;
		if(Pattern.compile("^[0-9].*").matcher(arrayList.get(readPoint)).matches())
		{
			try
			{
				time=formatNum(arrayList.get(readPoint), 1, 5);
				readPoint++;
			}
			catch (NumberFormatException e) {
				// TODO: handle exception
				builder.append("输入的数值错误，请设为1~5，按照默认值\n");
				time=1;
			}
		}
		if(arrayList.size()<=readPoint)
		{
			sendBackMsg("请输入进行检定的技能\n"+help);
			return;
		}
		String skill=arrayList.get(readPoint);
		readPoint++;
		int skillpoint=getSkill(skill);
		if(arrayList.size()>readPoint)
		{
			try
			{
				skillpoint=formatNum(arrayList.get(readPoint), 1, 100);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				sendBackMsg("输入的技能数值错误，应为1-100");
				return;
			}
		}
		if(skillpoint==-1)
		{
			sendBackMsg("临时技能检定需要填写临时数值\n"+help);
			return;
		}
		
		int[] arr=getRandomNumWithBP(time, false);
		builder.append("对"+getMessageSenderName()+"的"+skill+"进行检定，掷出1d100="+arr[1]+"\n");
		StringBuilder otherNum=new StringBuilder();
		for(int i=2;i<arr.length;i++)
		{
			otherNum.append(arr[i]+",");
		}
		otherNum.deleteCharAt(otherNum.length()-1);
		builder.append("掷惩罚骰1d10="+otherNum.toString()+",得"+arr[0]+"/"+skillpoint+"\n");
		CheckStatus checkStatus=numCheck(skillpoint, arr[0]);
		builder.append("检定"+checkStatus.levelStatus.getString());
		if(checkStatus.specialStatus!=null)
			builder.append(","+checkStatus.specialStatus.getString());
		sendBackMsg(builder.toString());
	}
	
	public void sc()
	{
		sc(null);
	}
	public void sc(ArrayList<String> arrayList)
	{
		String help=".sc 成功减少值/失败减少值\n";
		if(arrayList==null)
		{
			sendBackMsg(help);
			return;
		}
		int san=getSkill("san");
		if(san==-1)
		{
			sendBackMsg("您尚未设置理智值");
			return;
		}
		String[] parts=arrayList.get(0).split("/");
		if(parts.length<2)
		{
			sendBackMsg("设置数据错误");
			return;
		}
		int a,b;
		try
		{
			a=formatNum(parts[0], 0, 100);
		}catch (NumberFormatException e) {
			// TODO: handle exception
			a=transRandomString(parts[0]);
		}
		try
		{
			b=formatNum(parts[1], 0, 100);
		}catch (NumberFormatException e) {
			// TODO: handle exception
			b=transRandomString(parts[1]);
		}
		if(a<0||b<0)
		{
			sendBackMsg("数值错误或溢出");
			return;
		}
		CheckStatus checkStatus=numCheck(san);
		int changeNum = 0;
		switch(checkStatus.levelStatus)
		{
		case EX_SUCCESS:
		case S_SUCCESS:
		case SUCCESS:
			changeNum=a;
			break;
		case FAILED:
			changeNum=b;
			break;
		}
		int pastSan=getSkill("san");
		formateAndSaveSkill("san", san-changeNum);
		StringBuilder builder=new StringBuilder();
		builder.append("对"+getMessageSenderName()+"进行理智检定\n");
		builder.append("掷出1d100="+checkStatus.randomNum+",检定"+checkStatus.levelStatus.getString());
		if(checkStatus.specialStatus!=null)
			builder.append(","+checkStatus.specialStatus.getString());
		builder.append("\n");
		builder.append(getMessageSenderName()+"的理智减少了"+changeNum+"，变为了"+getSkill("san"));
		if(getSkill("san")<=0)
			builder.append("\n你陷入了永久的疯狂！");
		if(getSkill("san")>0&&changeNum>5)
			builder.append("\n呀嘞~想要试着疯狂吗？");
		sendBackMsg(builder.toString());
	}
	
	public void en()
	{
		en(null);
	}
	public void en(ArrayList<String> arrayList)
	{
		String help=".en 技能名 [临时数值，若先前未设置技能数值，则必须填写]\n";
		if(arrayList==null)
		{
			sendBackMsg(help);
			return;
		}
		String string=transToMain(arrayList.get(0));
		int skillNum=getSkill(string);
		boolean temporaryNum=false;
		if(arrayList.size()>1)
		{
			try
			{
				skillNum=formatNum(arrayList.get(1), 0, 100);
				temporaryNum=true;
			}
			catch (NumberFormatException e) {
				// TODO: handle exception
				sendBackMsg("您设置的数值错误或溢出");
				return;
			}
		}
		if(skillNum==-1)
		{
			sendBackMsg("您尚未设置该技能值");
			return;
		}
		CheckStatus checkStatus=numCheck(skillNum);
		int upNum=getRandomNum(10, 1);
		switch(checkStatus.levelStatus)
		{
		case EX_SUCCESS:
		case S_SUCCESS:
		case SUCCESS:
			upNum=0;
			break;
		case FAILED:
			break;
		}
		
		int temporarySkillNum = 0;
		if(!temporaryNum)
			formateAndSaveSkill(string, skillNum+upNum);
		else
			temporarySkillNum=skillNum+upNum;
		
		StringBuilder builder=new StringBuilder();
		builder.append("对"+getMessageSenderName()+"进行"+string+"增长检定\n");
		builder.append("掷出1d100="+checkStatus.randomNum+",检定"+checkStatus.levelStatus.getString());
		if(checkStatus.specialStatus!=null)
			builder.append(","+checkStatus.specialStatus.getString());
		builder.append("\n"+getMessageSenderName()+"的"+string);
		builder.append(checkStatus.levelStatus!=LevelStatus.FAILED?"没有发生变化":"获得了1d10="+upNum+"点增长");
		builder.append(","+string+"现在是"+(temporaryNum?temporarySkillNum:getSkill(string)));
		sendBackMsg(builder.toString());
	}
	
	public void ti()
	{
		StringBuilder stringBuilder=new StringBuilder();
		int type=getRandomNum(10, 1);
		stringBuilder.append(getMessageSenderName()+"获得了疯狂症状1d10="+type+"：\n");
		stringBuilder.append(ShortCrazy[type-1]+"\n");
		stringBuilder.append("持续1d10="+getRandomNum(10, 1)+"轮");
		sendBackMsg(stringBuilder.toString());
	}
	public void ti(ArrayList<String> arrayList)
	{
		ti();
	}
	
	public void li()
	{
		StringBuilder stringBuilder=new StringBuilder();
		int type=getRandomNum(10, 1);
		stringBuilder.append(getMessageSenderName()+"获得了疯狂症状1d10="+type+"：\n");
		stringBuilder.append(LongCrazy[type-1]+"\n");
		stringBuilder.append("时间1d10="+getRandomNum(10, 1)+"小时");
		sendBackMsg(stringBuilder.toString());
	}
	public void li(ArrayList<String> arrayList)
	{
		li();
	}
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//													内部方法
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
	
	/**
	 * 从1~{@code upBound}中抽取{@code time}次，返回和
	 * @param upBound 抽取的数值的上限（包含该数），下限为1
	 * @param time 抽取的次数
	 * @return 抽取的数量之和
	 */
	private int getRandomNum(int upBound,int time)
	{
		Random random=new Random();
		int num=0;
		for(int i=0;i<time;i++)
		{
			num+=random.nextInt(upBound)+1;
		}
		return num;
	}
	/**
	 * 从百面中抽取一个数值，并且用奖励/惩罚骰来替换
	 * @param time 抽取的奖励/惩罚骰数量
	 * @param isBouse 是否是奖励骰
	 * @return 返回一个数组，索引0是进行替换之后的数字<br>
	 * 索引1为抽取到的百面骰数字<br>
	 * 之后的是抽取奖励/惩罚骰的数字
	 */
	private int[] getRandomNumWithBP(int time,boolean isBouse)
	{
		int[] arrayList=new int[time+1+1];
		int pnum=getRandomNum(100, 1);
		arrayList[1]=pnum;
		int[] otherNum=isBouse?getMinNum(9, time):getMaxNum(9, time);
		int a=pnum/10;
		int b=pnum%10;
		if(isBouse==a>otherNum[0])
		{
			a=otherNum[0];
		}
		arrayList[0]=a*10+b;
		for(int i=1;i<otherNum.length;i++)
		{
			arrayList[i+1]=otherNum[i];
		}
		return arrayList;
	}
	/**
	 * 从0~{@code upBound}中抽取{@code time}次，返回最小值
	 * @param upBound 抽取的数值的上限（包含该数），下限为0
	 * @param time 抽取的次数
	 * @return 索引0为抽取到的最小值<br>其他为抽取到的值
	 */
	private int[] getMinNum(int upBound,int time)
	{
		Random random=new Random();
		int[] numArr=new int[time+1];
		numArr[0]=random.nextInt(upBound+1);
		numArr[time]=numArr[0];
		time--;
		for(;time>0;time--)
		{
			int rnum=random.nextInt(upBound+1);
			numArr[time]=rnum;
			if(rnum<numArr[0])
				numArr[0]=rnum;
		}
		return numArr;
	}
	/**
	 * 从0~{@code upBound}中抽取{@code time}次，返回最大值
	 * @param upBound 抽取的数值的上限（包含该数），下限为0
	 * @param time 抽取的次数
	 * @return 索引0为抽取到的最大值<br>其他为抽取到的值
	 */
	private int[] getMaxNum(int upBound,int time)
	{
		Random random=new Random();
		int[] numArr=new int[time+1];
		numArr[0]=random.nextInt(upBound+1);
		numArr[time]=numArr[0];
		time--;
		for(;time>0;time--)
		{
			int rnum=random.nextInt(upBound+1);
			numArr[time]=rnum;
			if(rnum>numArr[0])
				numArr[0]=rnum;
		}
		return numArr;
	}
	/**
	 * 检定数值，返回{@link CheckStatus}
	 * @param point 待检定的技能点数
	 * @param randerNum 检定数
	 * @return 
	 */
	private CheckStatus numCheck(int point,int randerNum)
	{
		int ex_success=point/5;
		int s_success=point/2;
		int success=point;
		LevelStatus level;
		SpecialStatus special = null;
		boolean ifSuccess;
		if(randerNum<=success)
		{
			level=LevelStatus.SUCCESS;
			if(randerNum<=s_success)
			{
				level=LevelStatus.S_SUCCESS;
				if(randerNum<=ex_success)
					level=LevelStatus.EX_SUCCESS;
			}
			ifSuccess=true;
		}
		else
		{
			level=LevelStatus.FAILED;
			ifSuccess=false;
		}
		if(ifSuccess&&randerNum==1)
		{
			special=SpecialStatus.BIGSUCCESS;
			return new CheckStatus(level, special,randerNum);
		}
		if(!ifSuccess)
		{
			if(point<=50&&(randerNum>=96&&randerNum<=100))
			{
				special=SpecialStatus.BIGFAILED;
				return new CheckStatus(level, special,randerNum);
			}
			if(point>50&&(randerNum==100))
			{
				special=SpecialStatus.BIGFAILED;
				return new CheckStatus(level, special,randerNum);
			}
		}
		return new CheckStatus(level, special,randerNum);
	}
	/**
	 * 检定数值，随机抽取百面，返回{@link CheckStatus}
	 * @param point 待检定的技能点数
	 * @param randerNum 检定数
	 * @return 
	 */
	private CheckStatus numCheck(int point)
	{
		return numCheck(point, getRandomNum(100, 1));
	}
	/**
	 * 将包含有随机数的表达式计算出来
	 * @param string 字符串
	 * @return 按要求的随机数<br>如果不符合完整表达式则自动补全默认值，若无法补全则返回-1<br>如果数字溢出，则会返回-2
	 */
	private int transRandomString(String string)
	{
		Pattern pattern=Pattern.compile("([0-9]*)?d([0-9]*)?");
		Matcher matcher=pattern.matcher(string.toLowerCase());
		String resultString=new String(string);
		while(matcher.find())
		{
			String subString=string.substring(matcher.start(0), matcher.end(0));
			String[] ss=subString.split("d");
			int time=-1,part=-1;
			if(ss.length<2)
			{
				if(string.toLowerCase().startsWith("d"))
					time=1;
				if (string.toLowerCase().endsWith("d")) 
					part=100;
				if(time==-1&&part==-1)
					return -1;
			}
			else
			{
				if(ss[0].equals(""))
					ss[0]="1";
				if(ss[1].equals(""))
					ss[1]="100";
			}
			if(ss.length>1)
			{
				try
				{
					if(time==-1)
						time=formatNum(ss[0], 1, 10);
					else
						part=formatNum(ss[0], 2, 500);
					if(part==-1)
						part=formatNum(ss[1], 2, 500);
				}catch (NumberFormatException e) {
					// TODO: handle exception
					return -2;
				}
			}
			StringBuilder builder=new StringBuilder("(");
			for(int i=1;i<=time;i++)
			{
				builder.append(getRandomNum(Integer.parseInt(ss[1]), 1)+"+");
			}
			builder.deleteCharAt(builder.length()-1);
			builder.append(")");
			resultString=resultString.replace(matcher.group(0), builder.toString());
		}
		
		Evaluator evaluator=new Evaluator();
		try {
			return (int)evaluator.getNumberResult(resultString);
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			return -1;
		}
		
	}
	/**
	 * 格式化技能数值，将技能数值格式化,并能够储存下
	 * 技能的数值会受{@link SkillUBound}和{@link SkillBBound}的影响
	 * @param name 技能名称
	 * @param num 需要修改的数值
	 * @return 
	 * 0 修改成功<br>
	 * 1 修改成功，且原技能值超出下限<br>
	 * 2 修改成功，且原技能值超出上限
	 */
	private int formateAndSaveSkill(String name,int num)
	{
		int UBound=99,BBound=0;
		for (SkillBBound bBound : SkillBBound.values()) 
		{
			if(transToMain(bBound.getEffectSkill()).equals(transToMain(name)))
			{
				int effectSkillNum=getSkill(transToMain(bBound.effectSkill));
				if(effectSkillNum!=-1)
					BBound=0+bBound.effectNum*effectSkillNum;
			}
		}
		for (SkillUBound uBound : SkillUBound.values()) 
		{
			if(transToMain(uBound.getEffectSkill()).equals(transToMain(name)))
			{
				int effectSkillNum=getSkill(transToMain(uBound.effectSkill));
				if(effectSkillNum!=-1)
					UBound=99+uBound.effectNum*effectSkillNum;
			}
		}
		if(num>UBound)
		{
			num=UBound;
			setSkill(name, num);
			return 2;
		}
		if(num<BBound)
		{
			num=BBound;
			setSkill(name, num);
			return 1;
		}
		setSkill(name, num);
		return 0;
	}
	
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//													工具方法
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
	
	/**
	 * 加载或更新相同意义字符串文件<br>
	 * {@link SameStringList}会被更新
	 */
	private void loadSameString()
	{
		ArrayList<String[]> arrayList=getDataExchanger().getList(SAME_STRING);
		if(SameStringList==null)
			SameStringList=new ArrayList<>();
		if(arrayList==null)
			return;
		for (String[] strings : arrayList) {
			ArrayList<String> sameList=new ArrayList<>();
			Collections.addAll(sameList, strings[1].split(","));
			SameStringList.add(sameList);
		}
	}
	/**
	 * 保存相同意义字符串文件
	 */
	private void savaSameString()
	{
		getDataExchanger().deleteList(SAME_STRING);
		for (ArrayList<String> arrayList : SameStringList) {
			StringBuilder stringBuilder=new StringBuilder();
			for (String string : arrayList) {
				stringBuilder.append(string+",");
			}
			getDataExchanger().addListItem(SAME_STRING, SAME_STRING_LINE, stringBuilder.toString());
		}
	}
	/**
	 * 添加相同意义字符串<br>
	 * 这一系列的字符串中没有一个已经存在则会新添加一个类别<br>
	 * 新添加的类别的主要名字是{@code main}<br>
	 * 如果发现一系列的名字中有两个及以上的同类词，这会返回false<br>
	 * 如果已经存在某一种，则不会考虑主要和次要，全部按照已有的主要词<br>
	 * @param main 主要名字
	 * @param other 次要的名字
	 */
	private boolean addSameString(String main,String ...other)
	{
		ArrayList<String> arrayList=new ArrayList<String>();
		arrayList.add(main);
		Collections.addAll(arrayList, other);
		
		ArrayList<String> aim=null;
		for (String string : arrayList) 
		{
			ArrayList<String> list=findSameString(string);
			if(list!=null)
				if(aim==null)
					aim=list;
				else
					if(aim!=list)
						return false;
			
		}
		
		if(aim==null)
		{
			SameStringList.add(arrayList);
		}
		else
		{
			code_0:for (String string : arrayList) 
			{
				for (String string2 : aim) 
				{
					if(string.equals(string2))
						continue code_0;
				}
				aim.add(string);
			}
		}
		return true;
	}
	/**
	 * 寻找相同意义的字符串<br>
	 * 注意！本方法返回的是指针，会对本身产生影响
	 * @param aim 所寻找的字符串
	 * @return 与{@code aim}意义相同的字符串<br>如果不存在则返回{@code null}
	 */
	private ArrayList<String> findSameString(String aim)
	{
		loadSameString();
		for (ArrayList<String> arrayList : SameStringList) 
		{
			for (String string : arrayList) 
			{
				if(string.equals(aim))
				{
					return arrayList;
				}
			}
		}
		return null;
	}
	/**
	 * 寻找相同意义的字符串
	 * @param aim 所寻找的字符串
	 * @return 与{@code aim}意义相同的字符串(不包括自身)<br>如果不存在则返回{@code null}
	 */
	private String[] getSameString(String aim)
	{
		ArrayList<String> arrayList=findSameString(aim);
		if(arrayList==null)
		{
			return null;
		}
		arrayList=(ArrayList<String>) arrayList.clone();
		arrayList.remove(aim);
		return arrayList.toArray(new String[arrayList.size()]);
	}
	/**
	 * 将字符串替换为主相同字符串中的主要字符串
	 * @param string
	 * @return 相同字符串中的主字符串 不存在的话则返回原字符串
	 */
	private String transToMain(String string)
	{
		ArrayList<String> arrayList=findSameString(string);
		if(arrayList==null)
			return string;
		else
			return arrayList.get(0);
	}
	/**
	 * 将文字转换为数字
	 * @param num 待转换数字
	 * @param from 要求下限
	 * @param to 要求上限
	 * @return 正常数字
	 * @throws NumberFormatException 如果不是数字或不在限定中
	 */
	private int formatNum(String num,int from,int to) throws NumberFormatException
	{
		int formatNum;
		try
		{
			formatNum=Integer.parseInt(num);
		}
		catch(NumberFormatException exception)
		{
			throw exception;
		}
		if(formatNum>to||formatNum<from)
			throw new NumberFormatException("超出限制范围");
		return formatNum;
	}
	/**
	 * 注意本方法的执行需要最新的{@code receiveMsgType},如果是通过listener调用，一定需要更新！
	 * 记录技能数值
	 * @param name 技能名称(会自动替换成主要字符名的)
	 * @param num 技能数值
	 */
	private void setSkill(String name,int num)
	{
		int type=receiveMessageType.getMsgType();
		String mark;
		name=transToMain(name);
		switch (type) 
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			mark="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			mark="G"+receiveMessageType.getfromQQ()+"G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			mark="D"+receiveMessageType.getfromQQ()+"D"+receiveMessageType.getfromGroup();
			break;

		default:
			return;
		}
		getDataExchanger().deleteListItem(name, mark);
		getDataExchanger().addListItem(name, mark, Integer.toString(num));
	}
	/**
	 * 注意本方法的执行需要最新的{@code receiveMsgType},如果是通过listener调用，一定需要更新！
	 * 返回技能数值，如果不存在则会返回-1
	 * @param name 技能名称(会自动替换成主要字符名的)
	 */
	private int getSkill(String name)
	{
		int type=receiveMessageType.getMsgType();
		String mark;
		name=transToMain(name);
		switch (type) 
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			mark="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			mark="G"+receiveMessageType.getfromQQ()+"G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			mark="D"+receiveMessageType.getfromQQ()+"D"+receiveMessageType.getfromGroup();
			break;

		default:
			return -1;
		}
		ArrayList<String> arrayList=getDataExchanger().getListItem(name, mark);
		if(arrayList==null)
			return -1;
		return Integer.parseInt(arrayList.get(0));
	}
	/**
	 * 注意本方法的执行需要最新的{@code receiveMsgType},如果是通过listener调用，一定需要更新！
	 * 删除技能数值
	 * @param name 技能名称(会自动替换成主要字符名的)
	 */
	private void removeSkill(String name)
	{
		int type=receiveMessageType.getMsgType();
		String mark;
		name=transToMain(name);
		switch (type) 
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			mark="P"+receiveMessageType.getfromQQ();
			break;
		case UniversalConstantsTable.MSGTYPE_GROUP:
			mark="G"+receiveMessageType.getfromQQ()+"G"+receiveMessageType.getfromGroup();
			break;
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			mark="D"+receiveMessageType.getfromQQ()+"D"+receiveMessageType.getfromGroup();
			break;

		default:
			return;
		}
		getDataExchanger().deleteListItem(name, mark);
	}
}
