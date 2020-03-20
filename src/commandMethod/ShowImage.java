package commandMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;

import org.meowy.cqp.jcq.entity.CQImage;
import org.meowy.cqp.jcq.message.CQCode;
import org.meowy.cqp.jcq.util.DigestUtils;

import connection.CQSender;
import global.UniversalConstantsTable;
import global.authorizer.AuthirizerUser;
import global.authorizer.MinimumAuthority;

public class ShowImage extends Father
{

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub

	}
	
	public void image() throws IOException
	{
		if(!CQSender.canSendImage())
		{
			sendBackMsg("哎呀……图片发不出去……");
			return;
		}
		boolean access=Boolean.parseBoolean(getDataExchanger().getItem(mark()));
		if(!access)
		{
			sendBackMsg("现在不行，还没有接到许可的命令");
			return;
		}
		CQImage cqImage=new CQImage("http://www.dmoe.cc/random.php");
		File image=cqImage.download(getPluginDataFloder()+"data.jpg");
		
		String md5=DigestUtils.md5Hex(new FileInputStream(image));
		
		File realimage=new File(getPluginDataFloder()+md5+".jpg");
		image.renameTo(realimage);
		sendBackMsg(new CQCode().image(realimage));
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
	public void image_off()
	{
		boolean access=Boolean.parseBoolean(getDataExchanger().getItem(mark()));
		if(!access)
		{
			sendBackMsg("停止的命令已经下达了……");
			return;
		}
		access=!access;
		getDataExchanger().addItem(mark(), Boolean.toString(access));
		sendBackMsg("了解了……我会安静会");
		return;
	}
	@MinimumAuthority(authirizerUser = AuthirizerUser.GROUP_MANAGER)
	public void image_on()
	{
		boolean access=Boolean.parseBoolean(getDataExchanger().getItem(mark()));
		if(access)
		{
			sendBackMsg("我在工作啊……");
			return;
		}
		access=!access;
		getDataExchanger().addItem(mark(), Boolean.toString(access));
		sendBackMsg("了解了……那就开始吧");
		return;
	}
	private String mark()
	{
		switch (receiveMessageType.getMsgType())
		{
		case UniversalConstantsTable.MSGTYPE_PERSON:
			return "P"+receiveMessageType.getfromQQ();
		case UniversalConstantsTable.MSGTYPE_GROUP:
			return "G"+receiveMessageType.getfromGroup();
		case UniversalConstantsTable.MSGTYPE_DISCUSS:
			return "D"+receiveMessageType.getfromGroup();

		default:
			return null;
		}
	}
}
