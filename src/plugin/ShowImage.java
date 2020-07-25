package plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.meowy.cqp.jcq.entity.CQImage;
import org.meowy.cqp.jcq.message.CQCode;
import org.meowy.cqp.jcq.util.DigestUtils;

import connection.CQSender;
import pluginHelper.AuthirizerUser;
import pluginHelper.annotations.MinimumAuthority;
import pluginHelper.annotations.RegistCommand;
import transceiver.IdentitySymbol;
import transceiver.event.MessageReceiveEvent;

public class ShowImage extends Father
{
	@RegistCommand(CommandString = "image",Help = "抽一张图片")
	public void image(MessageReceiveEvent event) throws IOException
	{
		if (!CQSender.canSendImage())
		{
			sendMsg(event, "哎呀……图片发不出去……");
			return;
		}
		boolean access = Boolean.parseBoolean(getDataExchanger().getItem(mark(event)));
		if (!access)
		{
			sendMsg(event, "现在不行，还没有接到许可的命令");
			return;
		}
//		http://www.dmoe.cc/random.php
//		URL url=new URL("https://v1.alapi.cn/api/acg");
//		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
//		connection.setRequestMethod("GET");
//		connection.setConnectTimeout(15000);
//		connection.setReadTimeout(60000);
//		connection.connect();
//		String result;
//		if (connection.getResponseCode() == 200) 
//		{
//            InputStream is = connection.getInputStream();
//            // 封装输入流is，并指定字符集
//            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            // 存放数据
//            StringBuffer sbf = new StringBuffer();
//            String temp = null;
//            while ((temp = br.readLine()) != null) {
//                sbf.append(temp);
//                sbf.append("\r\n");
//            }
//            result = sbf.toString();
//            br.close();
//            is.close();
//        }
//		connection.disconnect();

		CQImage cqImage = new CQImage("https://v1.alapi.cn/api/acg");
		File image = cqImage.download(getPluginDataFloder() + "data.jpg");

		String md5 = DigestUtils.md5Hex(new FileInputStream(image));

		File realimage = new File(getPluginDataFloder() + md5 + ".jpg");
		image.renameTo(realimage);
		sendMsg(event, new CQCode().image(realimage));
	}

	@MinimumAuthority(AuthirizerUser.GROUP_MANAGER)
	public void image_off(MessageReceiveEvent event)
	{
		boolean access = Boolean.parseBoolean(getDataExchanger().getItem(mark(event)));
		if (!access)
		{
			sendMsg(event, "停止的命令已经下达了……");
			return;
		}
		access = !access;
		getDataExchanger().setItem(mark(event), Boolean.toString(access));
		sendMsg(event, "了解了……我会安静会");
		return;
	}

	@MinimumAuthority(AuthirizerUser.GROUP_MANAGER)
	public void image_on(MessageReceiveEvent event)
	{
		boolean access = Boolean.parseBoolean(getDataExchanger().getItem(mark(event)));
		if (access)
		{
			sendMsg(event, "我在工作啊……");
			return;
		}
		access = !access;
		getDataExchanger().setItem(mark(event), Boolean.toString(access));
		sendMsg(event, "了解了……那就开始吧");
		return;
	}

	private String mark(IdentitySymbol symbol)
	{
		switch (symbol.type)
		{
		case PERSON:
			return "P" + symbol.userNum;
		case GROUP:
			return "G" + symbol.groupNum;
		case DISCUSS:
			return "D" + symbol.groupNum;

		default:
			return null;
		}
	}

	@Override
	public void personDelete(long num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupDelete(long num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discussDelete(long num)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void switchOff()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllDate()
	{
		// TODO Auto-generated method stub
		
	}
}
