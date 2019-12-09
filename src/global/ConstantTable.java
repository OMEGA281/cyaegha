package global;

public class ConstantTable
{
	public static final int NUM_NULL=-1;
	
	public static final int MSGTYPE_PERSON=0;
	public static final int MSGTYPE_GROUP=1;
	public static final int MSGTYPE_DISCUSS=2;
	
	public static final int SUBTYPE_PERSON_FRIEND=11;
	public static final int SUBTYPE_PERSON_ONLINE=1;
	public static final int SUBTYPE_PERSON_GROUP=2;
	public static final int SUBTYPE_PERSON_DISCUSS=3;
	
	public static final long QQ_ANONYMOUS=80000000L;
	
	public static final String STRING_MSGTYPE="MsgType";
	public static final String STRING_SUBTYPE="SubType";
	public static final String STRING_MSGID="MsgID";
	public static final String STRING_TIME="time";
	public static final String STRING_FROMQQ="fromQQ";
	public static final String STRING_FROMGROUP="fromGroup";
	public static final String STRING_FROMANONYMOUS="fromAnonymous";
	public static final String STRING_FROMDISCUSS="fromDiscuss";
	
	public static final String SYMBOL_COMMENT="#";
	
	public static final int XMLMODE_DEFAULT=-1;
	public static final int XMLMODE_READ=0;
	public static final int XMLMODE_WRITE=1;
	public static final int XMLMODE_READANDWRITE=2;
	
	public static final String PATH_JARRESOURCES="/resources/";
}