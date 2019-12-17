package global;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import connection.ReceiveMessageType;
import surveillance.Log;
import transceiver.Receiver;
import transceiver.Translator;

public class FileCode 
{
	static FileCode fileCode;
	String dirPath;
	String nowDate;
	String txtPath;
	FileWriter fileWriter;
	BufferedWriter bufferedWriter;
//	无参启动，获得指针
	public static FileCode getFileCode()
	{
		return getFileCode(null);
	}
//	启动时初始化使用，带有参数，用于确定目录位置
	public static FileCode getFileCode(String dirPath) 
	{
		if(fileCode==null)
		{
			fileCode=new FileCode(dirPath);
			System.out.println("文件处理初始化");
		}
		if(!TimeCode.getTimecode().getDate().equals(fileCode.nowDate))
		{
			fileCode.nowDate=TimeCode.getTimecode().getDate();
			fileCode.txtPath=fileCode.dirPath+"\\"+fileCode.nowDate+".txt";
			fileCode.flashStream();
		}
		return fileCode;
	}
//	关闭写出文件流
	public boolean close()
	{
		try 
		{
			if(fileWriter!=null)
				fileWriter.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("文件流关闭");
		return true;
	}
//	打开写出文件流
	private boolean openOutStream()
	{
//		检测文件夹是否存在，不存在则创建
		if(!IfFileExist(dirPath))
			createFolder(dirPath);
//		检测本日文件是否存在，不存在则创建
		if(!IfFileExist(txtPath))
			createFile(txtPath);
		try {
//			开启文件流
			System.out.println("开启文件流：指向"+txtPath);
			fileWriter=new FileWriter(new File(txtPath),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		开启写入器
		bufferedWriter=new BufferedWriter(fileWriter);
		System.out.println("文件流已打开");
		return true;
	}
//	重载打开文件流
	private boolean flashStream()
	{
		System.out.println("重载文件流");
		try 
		{
			if(fileWriter!=null)
				fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openOutStream();
	}
//	构造方法，确定了路径
	FileCode(String dirPath)
	{
		// TODO Auto-generated constructor stub
		this.dirPath=dirPath;
		nowDate=TimeCode.getTimecode().getDate();
		txtPath=dirPath+"\\"+nowDate+".txt";
		openOutStream();
	}
	private boolean IfFileExist(String url)
	{
		System.out.println("检测文件（夹）存在？："+url);
		return new File(url).exists();
	}
	private boolean createFolder(String url)
	{
		System.out.println("新建文件夹:"+url);
		return new File(url).mkdir();
	}
	private boolean createFile(String url)
	{
		System.out.println("新建文件:"+url);
		try 
		{
			return new File(url).createNewFile();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("创建文件失败：",url);
		}
		return false;
	}
	public boolean writeLine(String s)
	{
		try {
			bufferedWriter.write(s);
			bufferedWriter.flush();
			System.out.println("写了"+s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public ArrayList<ReceiveMessageType> getMsgList(String url)
	{
		ArrayList<ReceiveMessageType> MsgList=new ArrayList<ReceiveMessageType>();
		if(IfFileExist(url)!=true)
			return null;
		FileReader fileReader = null;
		try {
			fileReader=new FileReader(new File(url));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			其实根本不会走到这里
			e.printStackTrace();
		}
		BufferedReader bufferedReader;
		bufferedReader=new BufferedReader(fileReader);
		System.out.println("启动读取文件流");
		String line;
		for(;(line=readLine(bufferedReader))!=null;)
		{
			MsgList.add(Translator.stringTrans(line));
		}
		try {
			fileReader.close();
			System.out.println("关闭读取文件流");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MsgList;
	}
	
	private String readLine(BufferedReader bufferedReader)
	{
		String s=null;
		try {
			s=bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	protected boolean copyFile(String fromURL,String toURL)
	{
		if(!IfFileExist(fromURL))
		{
			Log.e("未查询到源文件");
			return false;
		}
		if(!IfFileExist(toURL))
		{
			createFile(toURL);
			Log.e("未查询到目标文件","已新建文件");
		}
		File fromFile=new File(fromURL);
		File toFile=new File(toURL);
		return copyFile(fromFile, toFile);
	}
	
	protected boolean copyFile(File from,File to)
	{
		if(from==null)
		{
			Log.e("未查询到源文件");
			return false;
		}
		if(to==null)
		{
			try 
			{
				to.createNewFile();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("未查询到目标文件","已新建文件");
		}
		InputStream i;
		OutputStream o;
		try
		{
		i=new FileInputStream(from);
		o=new FileOutputStream(to);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		return $copyFile(i, o);
	}
	
	protected boolean copyFile(InputStream from,OutputStream to)
	{
		return $copyFile(from, to);
	}
	private boolean $copyFile(InputStream from,OutputStream to)
	{
		byte[] buf = new byte[8 * 1024];
		int len = 0;
		try 
		{
			while ((len = from.read(buf)) != -1)
			{
				to.write(buf, 0, len);
				to.flush();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			// TODO: handle finally clause
			try 
			{
				from.close();
				to.close();
			} catch (Exception e2) 
			{
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return true;
	}
}