package transceiver;

public class IdentitySymbol
{
	public enum SourceType{PERSON,GROUP,DISCUSS;}
	public SourceType type;
	public long userNum;
	public long groupNum;
	public IdentitySymbol(SourceType type,long userNum,long groupNum)
	{
		this.type=type;
		this.userNum=userNum;
		this.groupNum=groupNum;
	}
}
