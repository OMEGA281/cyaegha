package transceiver.event;

import transceiver.IdentitySymbol;

public class Event extends IdentitySymbol
{
	public long time;
	private Event(SourceType type, long userNum, long groupNum)
	{
		super(type, userNum, groupNum);
	}
	public Event(SourceType type, long userNum, long groupNum,long time)
	{
		this(type, userNum, groupNum);
		this.time=time;
	}
	public IdentitySymbol getIdentitySymbol()
	{
		return new IdentitySymbol(type,userNum,groupNum);
	}
}
