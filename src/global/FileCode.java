package global;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import connection.MessageType;
import record.StringTrans;

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
			try {
				createFile(txtPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	private boolean createFile(String url) throws IOException
	{
		System.out.println("新建文件:"+url);
		return new File(url).createNewFile();
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
	
	public ArrayList<MessageType> getMsgList(String url)
	{
		ArrayList<MessageType> MsgList=new ArrayList<MessageType>();
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
			MsgList.add(StringTrans.getStringTrans().stringTrans(line));
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
	
}