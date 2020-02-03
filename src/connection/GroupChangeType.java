package connection;

public class GroupChangeType 
{
	public static final int GROUP_MUMBER_CREASE=0;
	public static final int GROUP_MUMBER_DECREASE=1;
	private int CreaseType;
	private int SubType;
	private long GroupNum;
	private long QQNum;
	private long AdminNum;
	public GroupChangeType(int CreaseType,int SubType,long GroupNum,long QQNum,long AdminNum) 
	{
		// TODO Auto-generated constructor stub
		this.CreaseType=CreaseType;
		this.SubType=SubType;
		this.GroupNum=GroupNum;
		this.QQNum=QQNum;
		this.AdminNum=AdminNum;
	}
	public int getCreaseType() {
		return CreaseType;
	}
	public int getSubType() {
		return SubType;
	}
	public long getGroupNum() {
		return GroupNum;
	}
	public long getQQNum() {
		return QQNum;
	}
	public long getAdminNum() {
		return AdminNum;
	}
}
