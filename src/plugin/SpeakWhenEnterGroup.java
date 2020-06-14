package plugin;

import pluginHelper.annotations.RegistCommand;
import pluginHelper.annotations.RegistListener.GroupAddListener;
import pluginHelper.annotations.RegistListener.GroupMemberChangeListener;
import transceiver.EventTrigger.EventResult;
import transceiver.IdentitySymbol;
import transceiver.IdentitySymbol.SourceType;

import org.meowy.cqp.jcq.entity.IRequest;

import connection.CQSender;
import transceiver.event.GroupMemberChangeEvent;
import transceiver.event.GroupAddEvent;
import transceiver.event.MessageReceiveEvent;

public class SpeakWhenEnterGroup extends Father
{

	@Override
	public void init()
	{
		if (getDataExchanger().getItem("switch") == null)
			getDataExchanger().setItem("switch", Boolean.toString(true));
	}

	@GroupAddListener
	public EventResult speak(GroupAddEvent event)
	{
		if (event.hasProcessed == IRequest.REQUEST_ADOPT)
			if (Boolean.parseBoolean(getDataExchanger().getItem("switch")))
			{
				String s = getDataExchanger().getItem("word");
				if (s != null)
				{
					Thread thread = new Thread() {
						@Override
						public void run()
						{
							try
							{
								sleep(2000);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
					try
					{
						thread.join();
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendMsg(new IdentitySymbol(SourceType.GROUP, 0, event.groupNum), s);
				}
			}
		return EventResult.PASS;
	}

	@GroupMemberChangeListener
	public EventResult speak(GroupMemberChangeEvent event)
	{
		if (event.increase && event.userNum == CQSender.getMyQQ())
			if (Boolean.parseBoolean(getDataExchanger().getItem("switch")))
			{
				String s = getDataExchanger().getItem("word");
				if (s != null)
				{
					Thread thread = new Thread() {
						@Override
						public void run()
						{
							try
							{
								sleep(2000);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
					try
					{
						thread.join();
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendMsg(new IdentitySymbol(SourceType.GROUP, 0, event.groupNum), s);
				}
			}
		return EventResult.PASS;
	}

	@RegistCommand(CommandString = "group enter speak",Help = "删除入群广播")
	public void enterSpeak(MessageReceiveEvent event)
	{
		getDataExchanger().deleteItem("word");
		sendMsg(event, "删除了入群广播");
	}

	@RegistCommand(CommandString = "group enter speak",Help = "设置入群广播")
	public void enterSpeak(MessageReceiveEvent event, Object object)
	{
		getDataExchanger().setItem("word", (String) object);
		sendMsg(event, "设置了入群广播");
	}

	@RegistCommand(CommandString = "group enter speak on",Help = "开启入群广播")
	public void on(MessageReceiveEvent event)
	{
		getDataExchanger().setItem("switch", Boolean.toString(true));
		sendMsg(event, "入群广播开启");
	}

	@RegistCommand(CommandString = "group enter speak off",Help = "关闭入群广播")
	public void off(MessageReceiveEvent event)
	{
		getDataExchanger().setItem("switch", Boolean.toString(false));
		sendMsg(event, "入群广播关闭");
	}
}
