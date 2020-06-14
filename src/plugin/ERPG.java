package plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connection.CQSender;
import net.sourceforge.jeval.Evaluator;
import pluginHelper.annotations.RegistCommand;
import transceiver.IdentitySymbol;
import transceiver.IdentitySymbol.SourceType;
import transceiver.event.MessageReceiveEvent;

public class ERPG extends Father
{
	private final String SAME_STRING = "samestring";
	private final String SAME_STRING_LINE = "string";
	private final String[] ShortCrazy = { "失忆：发现自己只记得最后身处的安全地点，却没有任何来到这里的记忆", "假性残疾：陷入了心理性的失明，失聪或躯体缺失感中",
			"暴力倾向：陷入了六亲不认的暴力行为中，对周围的敌人与友方进行着无差别的攻击", "偏执：陷入了严重的偏执妄想之中", "人际依赖：因为一些原因而降他人误认为了他重要的人并且努力的会与那个人保持那种关系",
			"昏厥：当场昏倒", "逃避行为：会用任何的手段试图逃离现在所处的位置", "竭嘶底里：表现出大笑，哭泣，嘶吼，害怕等的极端情绪表现", "恐惧：获得了一种恐惧症状(由kp决定，或.draw 恐怖症状)",
			"狂躁：获得了一种狂躁症状(由kp决定，或.draw 狂躁症状)" };
	private final String[] LongCrazy = { "失忆：发现自己身处一个陌生的地方，并忘记了自己是谁。记忆会随时间恢复。", "被窃：时间过后恢复清醒，发觉自己被盗，身体毫发无损。所有有价值的东西消失。",
			"遍体鳞伤：时间过后恢复清醒，发现自己身上满是拳痕和瘀伤。生命值减少到疯狂前的一半，但不会造成重伤。", "暴力倾向：陷入强烈的暴力与破坏欲之中。回过神来可能会理解自己做了什么也可能毫无印象。",
			"极端信念：采取极端和疯狂的表现手段展示他们的思想信念之一。", "重要之人：在持续或更久的时间中，将不顾一切地接近那个人，并为他们之间的关系做出行动。",
			"被收容：在精神病院病房或警察局牢房中回过神来，可能会慢慢回想起导致自己被关在这里的事情。", "逃避行为：恢复清醒时发现自己在很远的地方。",
			"恐惧：患上一个新的恐惧症。(由kp决定，或.draw 恐怖症状)时间过后将会尽力避开恐惧源。", "狂躁：患上一个新的狂躁症。(由kp决定，或.draw 狂躁症状)时间过后恢复理智" };

	private enum LevelStatus
	{
		EX_SUCCESS("极难成功"), S_SUCCESS("困难成功"), SUCCESS("成功"), FAILED("失败");

		private String string;

		public String getString()
		{
			return string;
		}

		LevelStatus(String string)
		{
			// TODO Auto-generated constructor stub
			this.string = string;
		}
	}

	private enum SpecialStatus
	{
		BIGSUCCESS("大成功!"), BIGFAILED("大失败!");

		private String string;

		public String getString()
		{
			return string;
		}

		SpecialStatus(String string)
		{
			// TODO Auto-generated constructor stub
			this.string = string;
		}
	}

	/**
	 * 提升技能上限的情况
	 * 
	 * @author GuoJiaCheng
	 *
	 */
	private enum SkillUBound
	{
		san("克苏鲁神话", -1);

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

		SkillUBound(String string, int i)
		{
			// TODO Auto-generated constructor stub
			effectNum = i;
			effectSkill = string;
		}

	}

	/**
	 * 提升技能下限的情况
	 * 
	 * @author GuoJiaCheng
	 *
	 */
	private enum SkillBBound
	{
		;
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

		SkillBBound(String string, int i)
		{
			// TODO Auto-generated constructor stub
			effectNum = i;
			effectSkill = string;
		}

	}

	/**
	 * 用于返回检定情况的类，包含{@link LevelStatus}和{@link SpecialStatus}
	 * 
	 * @author GuoJiaCheng
	 *
	 */
	private class CheckStatus
	{
		LevelStatus levelStatus;
		SpecialStatus specialStatus;
		int randomNum;

		public CheckStatus(LevelStatus levelStatus, SpecialStatus specialStatus, int randomNum)
		{
			// TODO Auto-generated constructor stub
			this.levelStatus = levelStatus;
			this.specialStatus = specialStatus;
			this.randomNum = randomNum;
		}
	}

	private static ArrayList<ArrayList<String>> SameStringList;

	@RegistCommand(CommandString = "r",Help = "随机骰数字")
	public void r(MessageReceiveEvent event)
	{
		r(event, new String("d"));
	}

	@RegistCommand(CommandString = "r",Help = "随机骰数字")
	public void r(MessageReceiveEvent event, Object object)
	{
		long result;
		try
		{
			result = transRandomString((String) object);
		} catch (Exception e)
		{
			sendMsg(event, "表达式格式不正确");
			return;
		}
		sendMsg(event, CQSender.getNickorCard(event) + "掷出了" + (String) object + "=" + result);
	}

	@RegistCommand(CommandString = "rh",Help = "进行暗骰，结果会私聊发送")
	public void rh(MessageReceiveEvent event)
	{
		rh(event, "d");
	}

	@RegistCommand(CommandString = "rh",Help = "进行暗骰，结果会私聊发送")
	public void rh(MessageReceiveEvent event, Object object)
	{
		long result;
		try
		{
			result = transRandomString((String) object);
		} catch (Exception e)
		{
			sendMsg(event, "表达式格式不正确");
			return;
		}
		sendMsg(event, CQSender.getNickorCard(event) + "进行了一次暗骰");
		sendMsg(new IdentitySymbol(SourceType.PERSON, event.userNum, 0), "你掷出了" + (String) object + "=" + result);
	}

	@RegistCommand(CommandString = "rb",Help = "投掷带奖励骰的百分骰")
	public void rb(MessageReceiveEvent event)
	{
		rb(event, 1);
	}

	@RegistCommand(CommandString = "rb",Help = "投掷带奖励骰的百分骰")
	public void rb(MessageReceiveEvent event, Integer integer)
	{
		if (integer > 10 || integer < 1)
		{
			sendMsg(event, "奖励骰数量错误");
			return;
		}
		int[] ex = new int[integer];
		for (int i = 1; i <= integer; i++)
		{
			ex[i - 1] = getRandomNum(0, 10, 1);
		}
		int r = getRandomNum(1, 101, 1);
		int p = replaceByReward(r, ex);
		StringBuilder s = new StringBuilder();
		for (int i : ex)
			s.append(i + ",");
		s.deleteCharAt(s.length() - 1);
		sendMsg(event, CQSender.getNickorCard(event) + "掷出了1d100=" + r + "，奖励骰：{" + s.toString() + "}，得到1d100b"
				+ integer + "=" + p);
	}

	@RegistCommand(CommandString = "rb",Help = "投掷带惩罚骰的百分骰")
	public void rp(MessageReceiveEvent event)
	{
		rp(event, 1);
	}

	@RegistCommand(CommandString = "rb",Help = "投掷带惩罚骰的百分骰")
	public void rp(MessageReceiveEvent event, Integer integer)
	{
		if (integer > 10 || integer < 1)
		{
			sendMsg(event, "惩罚骰数量错误");
			return;
		}
		int[] ex = new int[integer];
		for (int i = 1; i <= integer; i++)
		{
			ex[i - 1] = getRandomNum(0, 10, 1);
		}
		int r = getRandomNum(1, 101, 1);
		int p = replaceByPunish(r, ex);
		StringBuilder s = new StringBuilder();
		for (int i : ex)
			s.append(i + ",");
		s.deleteCharAt(s.length() - 1);
		sendMsg(event, CQSender.getNickorCard(event) + "掷出了1d100=" + r + "，惩罚骰：{" + s.toString() + "}，得到1d100p"
				+ integer + "=" + p);
	}

	@RegistCommand(CommandString = "st",Help = "设置技能数值")
	public void st(MessageReceiveEvent event, Object object)
	{
		Pattern oper = Pattern.compile("[-+*/]");
		Matcher m = oper.matcher((String) object);
		if (m.find())
		{
			StringBuilder builder = new StringBuilder(((String) object).toLowerCase());
			Pattern num = Pattern.compile("[-+*/0-9d]");
			String mainSkill = null;
			for (int i = 0; i < builder.length(); i++)
			{
				if (!num.matcher(String.valueOf(builder.charAt(i))).matches())
				{
					int x = i + 1;
					for (; x < builder.length(); x++)
					{
						if (num.matcher(String.valueOf(builder.charAt(x))).matches())
							break;
					}
					String s = builder.substring(i, x);
					if (mainSkill != null)
						if (!transToMain(mainSkill).equals(transToMain(s)))
						{
							sendMsg(event, "你设置的表达式有误");
							return;
						}
					mainSkill = s;
					int kn = getSkill(event, s);
					if (kn < 0)
					{
						sendMsg(event, "不存在该技能");
						return;
					}
					builder.replace(i, x, Integer.toString(kn));
					i = x;
				}
			}
			if (mainSkill == null)
			{
				sendMsg(event, "你设置的表达式有误");
				return;
			}
			long l;
			try
			{
				l = transRandomString(builder.toString());
			} catch (Exception e)
			{
				sendMsg(event, "你设置的表达式有误");
				return;
			}
			setSkill(event, mainSkill, (int) l);
			sendMsg(event, "把" + mainSkill + "设置成了" + l);
			return;
		}
		Pattern word = Pattern.compile("[\u4e00-\u9fa5A-Za-z]");
		Pattern num = Pattern.compile("[0-9]");
		StringBuilder s = new StringBuilder((String) object);
		for (int i = 0; i < s.length() - 1; i++)
		{
			char c = s.charAt(i);
			if (c == ' ')
				continue;
			if (num.matcher(String.valueOf(c)).matches())
			{
				char b = s.charAt(i + 1);
				if (b == ' ')
					continue;
				else if (word.matcher(String.valueOf(b)).matches())
				{
					s.insert(i + 1, ' ');
					i++;
					continue;
				}
			}
			if (word.matcher(String.valueOf(c)).matches())
			{
				char b = s.charAt(i + 1);
				if (b == ' ')
					continue;
				else if (num.matcher(String.valueOf(b)).matches())
				{
					s.insert(i + 1, ' ');
					i++;
					continue;
				}
			}
		}
		String[] ss = s.toString().split(" ");
		if (ss.length % 2 == 1)
		{
			sendMsg(event, "数值格式错误");
			return;
		}
		for (int i = 0; i < ss.length; i = i + 2)
			setSkill(event, ss[i], Integer.parseInt(ss[i + 1]));
		sendMsg(event, "属性设置完毕");
	}

	@RegistCommand(CommandString = "ra",Help = "对技能进行检定")
	public void ra(MessageReceiveEvent event, String name)
	{
		int skillnum = getSkill(event, name);
		if (skillnum == -1)
		{
			sendMsg(event, "你还没有设置技能数值");
			return;
		}
		CheckStatus status = numCheck(skillnum, getRandomNum(1, 101, 1));
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + name + "进行检定，掷出1d100=" + status.randomNum + "/"
				+ skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "ra",Help = "对技能进行检定")
	public void ra(MessageReceiveEvent event, String name, Integer num)
	{
		int skillnum = num;
		CheckStatus status = numCheck(skillnum, getRandomNum(1, 101, 1));
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + name + "进行检定，掷出1d100=" + status.randomNum + "/"
				+ skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "rab",Help = "对技能进行含奖励骰的检定")
	public void rab(MessageReceiveEvent event, String name)
	{
		rab(event, 1, name);
	}

	@RegistCommand(CommandString = "rab",Help = "对技能进行含奖励骰的检定")
	public void rab(MessageReceiveEvent event, String name, Integer q)
	{
		rab(event, 1, name, q);
	}

	@RegistCommand(CommandString = "rab",Help = "对技能进行含奖励骰的检定")
	public void rab(MessageReceiveEvent event, Integer i, String name)
	{
		if (i > 5 || i < 1)
		{
			sendMsg(event, "奖励骰的数量异常");
			return;
		}
		int[] bs = getRandomNumArr(0, 10, i);
		int skillnum = getSkill(event, name);
		if (skillnum == -1)
		{
			sendMsg(event, "你还没有设置技能数值");
			return;
		}
		int rnum = getRandomNum(1, 101, 1);
		int num = replaceByReward(rnum, bs);
		CheckStatus status = numCheck(skillnum, num);
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + name + "进行检定，掷出1d100=" + rnum + "奖励骰："
				+ transArrToString(bs) + "=" + num + "/" + skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "rab",Help = "对技能进行含奖励骰的检定")
	public void rab(MessageReceiveEvent event, Integer i, String name, Integer q)
	{
		if (i > 5 || i < 1)
		{
			sendMsg(event, "奖励骰的数量异常");
			return;
		}
		int[] bs = getRandomNumArr(0, 10, i);
		int skillnum = q;
		int rnum = getRandomNum(1, 101, 1);
		int num = replaceByReward(rnum, bs);
		CheckStatus status = numCheck(skillnum, num);
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + name + "进行检定，掷出1d100=" + rnum + "奖励骰："
				+ transArrToString(bs) + "=" + num + "/" + skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "rap",Help = "对技能进行含惩罚骰的检定")
	public void rap(MessageReceiveEvent event, String name)
	{
		rap(event, 1, name);
	}

	@RegistCommand(CommandString = "rap",Help = "对技能进行含惩罚骰的检定")
	public void rap(MessageReceiveEvent event, String name, Integer q)
	{
		rap(event, 1, name, q);
	}

	@RegistCommand(CommandString = "rap",Help = "对技能进行含惩罚骰的检定")
	public void rap(MessageReceiveEvent event, Integer i, String name)
	{
		if (i > 5 || i < 1)
		{
			sendMsg(event, "惩罚骰的数量异常");
			return;
		}
		int[] bs = getRandomNumArr(0, 10, i);
		int skillnum = getSkill(event, name);
		if (skillnum == -1)
		{
			sendMsg(event, "你还没有设置技能数值");
			return;
		}
		int rnum = getRandomNum(1, 101, 1);
		int num = replaceByPunish(rnum, bs);
		CheckStatus status = numCheck(skillnum, num);
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + name + "进行检定，掷出1d100=" + rnum + "惩罚骰："
				+ transArrToString(bs) + "=" + num + "/" + skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "rap",Help = "对技能进行含惩罚骰的检定")
	public void rap(MessageReceiveEvent event, Integer i, String name, Integer q)
	{
		if (i > 5 || i < 1)
		{
			sendMsg(event, "惩罚骰的数量异常");
			return;
		}
		int[] bs = getRandomNumArr(0, 10, i);
		int skillnum = q;
		int rnum = getRandomNum(1, 101, 1);
		int num = replaceByPunish(rnum, bs);
		CheckStatus status = numCheck(skillnum, num);
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + name + "进行检定，掷出1d100=" + rnum + "惩罚骰："
				+ transArrToString(bs) + "=" + num + "/" + skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "sc",Help = "理智检定")
	public void sc(MessageReceiveEvent event, Object object)
	{
		String a, b;
		{
			String x = (String) object;
			String[] k = x.split("/");
			if (k.length < 2)
			{
				sendMsg(event, "表达式有误");
				return;
			}
			a = k[0];
			b = k[1];
		}
		long q, p;
		try
		{
			q = transRandomString(a);
			p = transRandomString(b);
		} catch (Exception e)
		{
			sendMsg(event, "表达式有误");
			return;
		}
		int skillnum = getSkill(event, "理智");
		if (skillnum < 0)
		{
			sendMsg(event, "你还没有设置理智数值");
			return;
		}
		int num = getRandomNum(0, 101, 1);
		CheckStatus status = numCheck(skillnum, num);
		if (status.specialStatus == SpecialStatus.BIGFAILED)
			try
			{
				p = transRandomStringToMax(b);
			} catch (Exception e)
			{
//				其实这里是不可能发生的
				sendMsg(event, "表达式有误");
				return;
			}
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "进行理智检定，掷出1d100=" + num + "/" + skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		builder.append("\n理智减少了" + (status.levelStatus == LevelStatus.FAILED ? p : q));
		skillnum -= (status.levelStatus == LevelStatus.FAILED ? p : q);
		int con = getSkill(event, "意志");
		int kk = getSkill(event, "克苏鲁知识");
		if (con < 0)
		{
			sendMsg(event, "你没有意志，何来的理智？");
			return;
		}
		if (kk < 0)
			kk = 0;
		if (skillnum <= 0)
		{
			skillnum = 0;
			builder.append("，理智归零\n你陷入了永久疯狂");
		} else if (skillnum > con - kk)
		{
			skillnum = con - kk;
			builder.append("，还剩" + skillnum);
		} else if ((status.levelStatus == LevelStatus.FAILED ? p : q) > 5)
		{
			builder.append("，还剩" + skillnum);
			builder.append("\n你临时的疯狂了");
		} else
			builder.append("，还剩" + skillnum);
		setSkill(event, "理智", skillnum);
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "sc",Help = "理智检定")
	public void sc(MessageReceiveEvent event, Object object, Integer i)
	{
		String a, b;
		{
			String x = (String) object;
			String[] k = x.split("/");
			if (k.length < 2)
			{
				sendMsg(event, "表达式有误");
				return;
			}
			a = k[0];
			b = k[1];
		}
		long q, p;
		try
		{
			q = transRandomString(a);
			p = transRandomString(b);
		} catch (Exception e)
		{
			sendMsg(event, "表达式有误");
			return;
		}
		int skillnum = i;
		int num = getRandomNum(0, 101, 1);
		CheckStatus status = numCheck(skillnum, num);
		if (status.specialStatus == SpecialStatus.BIGFAILED)
			try
			{
				p = transRandomStringToMax(b);
			} catch (Exception e)
			{
//				其实这里是不可能发生的
				sendMsg(event, "表达式有误");
				return;
			}
		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "进行理智检定，掷出1d100=" + num + "/" + skillnum + "\n");
		builder.append("检定" + status.levelStatus.getString());
		if (status.specialStatus != null)
			builder.append("," + status.specialStatus.getString());
		builder.append("\n理智减少了" + (status.levelStatus == LevelStatus.FAILED ? p : q));
		skillnum -= status.levelStatus == LevelStatus.FAILED ? p : q;
		int con = 100;
		int kk = 0;
		if (skillnum <= 0)
		{
			skillnum = 0;
			builder.append("，理智归零\n你陷入了永久疯狂");
		} else if (skillnum > con - kk)
		{
			skillnum = con - kk;
			builder.append("，还剩" + skillnum);
		} else if ((status.levelStatus == LevelStatus.FAILED ? p : q) > 5)
		{
			builder.append("，还剩" + skillnum);
			builder.append("\n你临时的疯狂了");
		} else
			builder.append("，还剩" + skillnum);
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "en",Help = "技能增长检定")
	public void en(MessageReceiveEvent event, String skill)
	{
		int rnum = getSkill(event, skill);
		int num;
		if (rnum < 0)
		{
			sendMsg(event, "你尚未设置技能值");
			return;
		}
		int d = getRandomNum(1, 101, 1);
		int grow;
		if (d > rnum || (d <= 100 && d > 95))
			grow = getRandomNum(1, 11, 1);
		else
			grow = 0;
		num = rnum + grow;

		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + skill + "技能进行增长检定，掷出1d100=" + d + "/" + rnum + "\n");
		builder.append("检定" + (grow == 0 ? "失败" : "成功"));
		if (grow != 0)
		{
			builder.append(skill + "提升了" + grow + "，变成了" + num);
			if (rnum < 90 && num > 90 && !transToMain(skill).equals(transToMain("信用评级"))
					&& !transToMain(skill).equals(transToMain("克苏鲁知识"))
					&& !transToMain(skill).equals(transToMain("san")) && !transToMain(skill).equals(transToMain("力量"))
					&& !transToMain(skill).equals(transToMain("体质")) && !transToMain(skill).equals(transToMain("体型"))
					&& !transToMain(skill).equals(transToMain("敏捷")) && !transToMain(skill).equals(transToMain("智力"))
					&& !transToMain(skill).equals(transToMain("意志")) && !transToMain(skill).equals(transToMain("教育"))
					&& !transToMain(skill).equals(transToMain("幸运")) && !transToMain(skill).equals(transToMain("外貌")))
				builder.append("\n" + CQSender.getNickorCard(event) + "的" + skill + "已达精通！（可选获得2D6的理智恢复）");
		}
		setSkill(event, skill, num);
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "en",Help = "技能增长检定")
	public void en(MessageReceiveEvent event, String skill, Integer i)
	{
		int rnum = i;
		int num;
		int d = getRandomNum(1, 101, 1);
		int grow;
		if (d >= rnum || (d <= 100 && d > 95))
			grow = getRandomNum(1, 11, 1);
		else
			grow = 0;
		num = rnum + grow;

		StringBuilder builder = new StringBuilder();
		builder.append("对" + CQSender.getNickorCard(event) + "的" + skill + "技能进行增长检定，掷出1d100=" + d + "/" + rnum + "\n");
		builder.append("检定" + (grow == 0 ? "失败" : "成功"));
		if (grow != 0)
		{
			builder.append(skill + "提升了" + grow + "，变成了" + num);
			if (rnum < 90 && num > 90 && !transToMain(skill).equals(transToMain("信用评级"))
					&& !transToMain(skill).equals(transToMain("克苏鲁知识"))
					&& !transToMain(skill).equals(transToMain("san")) && !transToMain(skill).equals(transToMain("力量"))
					&& !transToMain(skill).equals(transToMain("体质")) && !transToMain(skill).equals(transToMain("体型"))
					&& !transToMain(skill).equals(transToMain("敏捷")) && !transToMain(skill).equals(transToMain("智力"))
					&& !transToMain(skill).equals(transToMain("意志")) && !transToMain(skill).equals(transToMain("教育"))
					&& !transToMain(skill).equals(transToMain("幸运")) && !transToMain(skill).equals(transToMain("外貌")))
				builder.append("\n" + CQSender.getNickorCard(event) + "的" + skill + "已达精通！（可选获得2D6的理智恢复）");
		}
		sendMsg(event, builder.toString());
	}

	@RegistCommand(CommandString = "ti",Help = "短期疯狂")
	public void ti(MessageReceiveEvent event)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int type = getRandomNum(1, 10, 1);
		stringBuilder.append(CQSender.getNickorCard(event) + "获得了疯狂症状1d10=" + type + "：\n");
		stringBuilder.append(ShortCrazy[type - 1] + "\n");
		stringBuilder.append("持续1d10=" + getRandomNum(1, 10, 1) + "轮");
		sendMsg(event, stringBuilder.toString());
	}

	@RegistCommand(CommandString = "li",Help = "长期疯狂")
	public void li(MessageReceiveEvent event)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int type = getRandomNum(1, 10, 1);
		stringBuilder.append(CQSender.getNickorCard(event) + "获得了疯狂症状1d10=" + type + "：\n");
		stringBuilder.append(LongCrazy[type - 1] + "\n");
		stringBuilder.append("时间1d10=" + getRandomNum(1, 10, 1) + "小时");
		sendMsg(event, stringBuilder.toString());
	}

//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//													内部方法
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------
//	------------------------------------------------------------------------------------------------------

	/**
	 * 从a（包含）~b（不包含）中抽取一个数，返回和
	 * 
	 * @param a
	 * @param b
	 * @param time
	 * @return
	 */
	private int getRandomNum(int a, int b, int time)
	{
		if (a > b)
		{
			int x = a;
			a = b;
			b = x;
		} else if (a == b)
			return a;
		if (a == b - 1)
			return a;
		Random random = new Random();
		int num = 0;
		for (int i = 1; i <= time; i++)
			num += random.nextInt(b - a) + a;
		return num;
	}

	/**
	 * 从a（包含）~b（不包含）中抽取一个数，返回数组
	 * 
	 * @param a
	 * @param b
	 * @param time
	 * @return
	 */
	private int[] getRandomNumArr(int a, int b, int time)
	{
		if (a > b)
		{
			int x = a;
			a = b;
			b = x;
		}
		Random random = new Random();
		int[] x = new int[time];
		for (int i = 1; i <= time; i++)
			x[i - 1] = random.nextInt(b - a) + a;
		return x;
	}

	/**
	 * 用奖励骰来置换数字
	 * 
	 * @param num
	 * @param rewards
	 * @return
	 */
	private int replaceByReward(int num, int... rewards)
	{
		int a = num / 10;
		int b = num % 10;
		for (int i : rewards)
			if (i < a)
				a = i;
		return a * 10 + b;
	}

	/**
	 * 用惩罚骰来置换数字
	 * 
	 * @param num
	 * @param rewards
	 * @return
	 */
	private int replaceByPunish(int num, int... punishs)
	{
		int a = num / 10;
		int b = num % 10;
		for (int i : punishs)
			if (i > a)
				a = i;
		return a * 10 + b;
	}

	/**
	 * 检定数值，返回{@link CheckStatus}
	 * 
	 * @param point     待检定的技能点数
	 * @param randerNum 检定数
	 * @return
	 */
	private CheckStatus numCheck(int point, int randerNum)
	{
		int ex_success = point / 5;
		int s_success = point / 2;
		int success = point;
		LevelStatus level;
		SpecialStatus special = null;
		boolean ifSuccess;
		if (randerNum <= success)
		{
			level = LevelStatus.SUCCESS;
			if (randerNum <= s_success)
			{
				level = LevelStatus.S_SUCCESS;
				if (randerNum <= ex_success)
					level = LevelStatus.EX_SUCCESS;
			}
			ifSuccess = true;
		} else
		{
			level = LevelStatus.FAILED;
			ifSuccess = false;
		}
		if (ifSuccess && randerNum == 1)
		{
			special = SpecialStatus.BIGSUCCESS;
			return new CheckStatus(level, special, randerNum);
		}
		if (!ifSuccess)
		{
			if (point <= 50 && (randerNum >= 96 && randerNum <= 100))
			{
				special = SpecialStatus.BIGFAILED;
				return new CheckStatus(level, special, randerNum);
			}
			if (point > 50 && (randerNum == 100))
			{
				special = SpecialStatus.BIGFAILED;
				return new CheckStatus(level, special, randerNum);
			}
		}
		return new CheckStatus(level, special, randerNum);
	}

	/**
	 * 将包含有随机数的表达式计算出来
	 * 
	 * @param string 字符串
	 * @return 按要求的随机数
	 * @throws Exception
	 */
	private long transRandomString(String string) throws Exception
	{
		{
			Pattern pattern = Pattern.compile("[-0-9+*/d]+");
			Matcher matcher = pattern.matcher(string);
			if (!matcher.matches())
				throw new Exception();
		}

		Pattern pattern = Pattern.compile("([0-9]*)?d([0-9]*)?");
		Matcher matcher = pattern.matcher(string.toLowerCase());
		String resultString = new String(string);
		while (matcher.find())
		{
			int start = matcher.start(0);
			int end = matcher.end(0);
			String $a = matcher.group(1);
			String $b = matcher.group(2);
			int a, b;
			if ($a.isEmpty())
				a = 1;
			else
				a = Integer.parseInt($a);
			if ($b.isEmpty())
				b = 100;
			else
				b = Integer.parseInt($b);
			if (a > 1000 || a < 1)
				a = 1;
			if (b > 10000 || b < 1)
				b = 100;
			int result = getRandomNum(1, b, a);
			resultString = string.replaceFirst(string.substring(start, end), Integer.toString(result));
		}
		Evaluator evaluator = new Evaluator();
		return (long) evaluator.getNumberResult(resultString);
	}

	/**
	 * 将包含有随机数的表达式计算出其最大值
	 * 
	 * @param string 字符串
	 * @return 按要求的随机数
	 * @throws Exception
	 */
	private long transRandomStringToMax(String string) throws Exception
	{
		{
			Pattern pattern = Pattern.compile("[-0-9+*/d]+");
			Matcher matcher = pattern.matcher(string);
			if (!matcher.matches())
				throw new Exception();
		}

		Pattern pattern = Pattern.compile("([0-9]*)?d([0-9]*)?");
		Matcher matcher = pattern.matcher(string.toLowerCase());
		String resultString = new String(string);
		while (matcher.find())
		{
			int start = matcher.start(0);
			int end = matcher.end(0);
			String $a = matcher.group(1);
			String $b = matcher.group(2);
			int a, b;
			if ($a.isEmpty())
				a = 1;
			else
				a = Integer.parseInt($a);
			if ($b.isEmpty())
				b = 100;
			else
				b = Integer.parseInt($b);
			if (a > 1000 || a < 1)
				a = 1;
			if (b > 10000 || b < 1)
				b = 100;
			int result = a * b;
			string.replaceFirst(string.substring(start, end), Integer.toString(result));
		}
		Evaluator evaluator = new Evaluator();
		return (long) evaluator.getNumberResult(resultString);
	}

	/**
	 * 将数组变成“[,,,,]”的形式
	 * 
	 * @param a
	 * @return
	 */
	private String transArrToString(int[] a)
	{
		StringBuilder builder = new StringBuilder("[");
		for (int i : a)
		{
			builder.append(i);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("]");
		return builder.toString();
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
		ArrayList<String[]> arrayList = getDataExchanger().getList(SAME_STRING);
		if (SameStringList == null)
			SameStringList = new ArrayList<>();
		if (arrayList == null)
			return;
		for (String[] strings : arrayList)
		{
			ArrayList<String> sameList = new ArrayList<>();
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
		for (ArrayList<String> arrayList : SameStringList)
		{
			StringBuilder stringBuilder = new StringBuilder();
			for (String string : arrayList)
			{
				stringBuilder.append(string + ",");
			}
			getDataExchanger().setListItem(SAME_STRING, SAME_STRING_LINE, stringBuilder.toString());
		}
	}

	/**
	 * 添加相同意义字符串<br>
	 * 这一系列的字符串中没有一个已经存在则会新添加一个类别<br>
	 * 新添加的类别的主要名字是{@code main}<br>
	 * 如果发现一系列的名字中有两个及以上的同类词，这会返回false<br>
	 * 如果已经存在某一种，则不会考虑主要和次要，全部按照已有的主要词<br>
	 * 
	 * @param main  主要名字
	 * @param other 次要的名字
	 */
	private boolean addSameString(String main, String... other)
	{
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(main);
		Collections.addAll(arrayList, other);

		ArrayList<String> aim = null;
		for (String string : arrayList)
		{
			ArrayList<String> list = findSameString(string);
			if (list != null)
				if (aim == null)
					aim = list;
				else if (aim != list)
					return false;

		}

		if (aim == null)
		{
			SameStringList.add(arrayList);
		} else
		{
			code_0: for (String string : arrayList)
			{
				for (String string2 : aim)
				{
					if (string.equals(string2))
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
	 * 
	 * @param aim 所寻找的字符串
	 * @return 与{@code aim}意义相同的字符串<br>
	 *         如果不存在则返回{@code null}
	 */
	private ArrayList<String> findSameString(String aim)
	{
		loadSameString();
		for (ArrayList<String> arrayList : SameStringList)
		{
			for (String string : arrayList)
			{
				if (string.equals(aim))
				{
					return arrayList;
				}
			}
		}
		return null;
	}

	/**
	 * 寻找相同意义的字符串
	 * 
	 * @param aim 所寻找的字符串
	 * @return 与{@code aim}意义相同的字符串(不包括自身)<br>
	 *         如果不存在则返回{@code null}
	 */
	private String[] getSameString(String aim)
	{
		ArrayList<String> arrayList = findSameString(aim);
		if (arrayList == null)
		{
			return null;
		}
		arrayList = (ArrayList<String>) arrayList.clone();
		arrayList.remove(aim);
		return arrayList.toArray(new String[arrayList.size()]);
	}

	/**
	 * 将字符串替换为主相同字符串中的主要字符串
	 * 
	 * @param string
	 * @return 相同字符串中的主字符串 不存在的话则返回原字符串
	 */
	private String transToMain(String string)
	{
		ArrayList<String> arrayList = findSameString(string);
		if (arrayList == null)
			return string;
		else
			return arrayList.get(0);
	}

	/**
	 * 将文字转换为数字
	 * 
	 * @param num  待转换数字
	 * @param from 要求下限
	 * @param to   要求上限
	 * @return 正常数字
	 * @throws NumberFormatException 如果不是数字或不在限定中
	 */
	private int formatNum(String num, int from, int to) throws NumberFormatException
	{
		int formatNum;
		try
		{
			formatNum = Integer.parseInt(num);
		} catch (NumberFormatException exception)
		{
			throw exception;
		}
		if (formatNum > to || formatNum < from)
			throw new NumberFormatException("超出限制范围");
		return formatNum;
	}

	/**
	 * 记录技能数值
	 * 
	 * @param name 技能名称(会自动替换成主要字符名的)
	 * @param num  技能数值
	 */
	private void setSkill(IdentitySymbol symbol, String name, int num)
	{
		String mark;
		name = transToMain(name);
		switch (symbol.type)
		{
		case PERSON:
			mark = "P" + symbol.userNum;
			break;
		case GROUP:
			mark = "G" + symbol.userNum + "G" + symbol.groupNum;
			break;
		case DISCUSS:
			mark = "D" + symbol.userNum + "D" + symbol.groupNum;
			break;

		default:
			return;
		}
		getDataExchanger().deleteListItem(name, mark);
		getDataExchanger().setListItem(name, mark, Integer.toString(num));
	}

	/**
	 * 返回技能数值，如果不存在则会返回-1
	 * 
	 * @param name 技能名称(会自动替换成主要字符名的)
	 */
	private int getSkill(IdentitySymbol symbol, String name)
	{
		String mark;
		name = transToMain(name);
		switch (symbol.type)
		{
		case PERSON:
			mark = "P" + symbol.userNum;
			break;
		case GROUP:
			mark = "G" + symbol.userNum + "G" + symbol.groupNum;
			break;
		case DISCUSS:
			mark = "D" + symbol.userNum + "D" + symbol.groupNum;
			break;

		default:
			return -1;
		}
		ArrayList<String> arrayList = getDataExchanger().getListItem(name, mark);
		if (arrayList == null)
			return -1;
		return Integer.parseInt(arrayList.get(0));
	}

	/**
	 * 删除技能数值
	 * 
	 * @param name 技能名称(会自动替换成主要字符名的)
	 */
	private void removeSkill(IdentitySymbol symbol, String name)
	{
		String mark;
		name = transToMain(name);
		switch (symbol.type)
		{
		case PERSON:
			mark = "P" + symbol.userNum;
			break;
		case GROUP:
			mark = "G" + symbol.userNum + "G" + symbol.groupNum;
			break;
		case DISCUSS:
			mark = "D" + symbol.userNum + "D" + symbol.groupNum;
			break;

		default:
			return;
		}
		getDataExchanger().deleteListItem(name, mark);
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}
}
