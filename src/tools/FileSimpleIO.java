package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**一个简单文件读取类，所有的流都存在在本类之中
 * 所有的反馈类型都存在于本类中的{@link fileReturnType}中*/
public class FileSimpleIO 
{
	/**反馈的类型，部分方法会反馈回其Name值*/
	public enum fileReturnType{
		SUCCESS
		,FAILED_CLOSE_OUTSTREAM,FAILED_OPEN_OUTSTREAM,FAILED_CLOSE_INSTREAM,FAILED_OPEN_INSTREAM
		,FAILED_CRAETFILE,FAILED_COPYFILE
		,FAILED_WRITELINE,FAILED_READLINE
		,FAILED_FLUSH_OUTSTREAM,FAILED_FLUSH_INSTREAM
		,FAILED_NULL_WRITESTREAM,FAILED_NULL_READSTREAM,FAILED_NULL_FILE};
	
	private String aimFile;
	private File file;
	
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	
	/**初始化一个文件操作类
	 * @param aimFile 目标文件的位置*/
	public FileSimpleIO(String aimFile)
	{
		// TODO Auto-generated constructor stub
		this.aimFile=aimFile;
		file=new File(this.aimFile);
	}
	/**关闭写出文件流
	 * @return 是否成功*/
	public fileReturnType closeOutStream()
	{
		try 
		{
			if(fileWriter!=null)
				fileWriter.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_CLOSE_OUTSTREAM;
		}
		bufferedReader=null;
//		System.out.println("文件流关闭");
		return fileReturnType.SUCCESS;
	}
	/**产生一个写出流,如果文件不存在,则会自动创建<br>
	 * 如果已经存在写出流则关闭上一写出流并产生新的<br>
	 * @param append 是的话则会在末尾添加
	 * @return 是否成功*/
	public fileReturnType openOutStream(boolean append)
	{
		if(bufferedWriter!=null)
			if(closeOutStream()!=fileReturnType.SUCCESS)
				return fileReturnType.FAILED_CLOSE_OUTSTREAM;
		try 
		{
			fileWriter = new FileWriter(file,append);
		} 
		catch(FileNotFoundException e)
		{
			createFile(aimFile);
			return openOutStream(append);
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_OPEN_OUTSTREAM;
		}
		
		bufferedWriter=new BufferedWriter(fileWriter);
		return fileReturnType.SUCCESS;
	}
	/**关闭读取文件流
	 * @return 是否成功*/
	public fileReturnType closeInStream()
	{
		try 
		{
			if(fileReader!=null)
				fileReader.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_CLOSE_INSTREAM;
		}
		bufferedReader=null;
		return fileReturnType.SUCCESS;
	}
	/**产生一个读取流,如果文件不存在,则会报错<br>
	 * 如果已经存在读取流则关闭上一读取流并产生新的<br>
	 * @return 是否成功*/
	public fileReturnType openInStream()
	{
		if(bufferedWriter!=null)
			if(closeOutStream()!=fileReturnType.SUCCESS)
				return fileReturnType.FAILED_CLOSE_INSTREAM;
		try 
		{
			fileWriter = new FileWriter(file);
		} 
		catch(FileNotFoundException e)
		{
			return fileReturnType.FAILED_NULL_FILE;
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_OPEN_INSTREAM;
		}
		
		bufferedWriter=new BufferedWriter(fileWriter);
		return fileReturnType.SUCCESS;
	}
	/**创建文件夹，可以直接输入整体的路径*/
	public static fileReturnType createFolder(String aim)
	{
		new File(aim).mkdirs();
		return fileReturnType.SUCCESS;
	}
	/**创建文件，可以直接输入整体的路径<br>
	 * 而且会创建相应的目录<br>
	 * 如果文件已经存在，则不会创建*/
	public static fileReturnType createFile(String aim)
	{
		File file=new File(aim);
		if(file.exists())
			return fileReturnType.SUCCESS;
		String path=file.getParent();
		if(path!=null)
			new File(path).mkdirs();
		try {
			new File(aim).createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_CRAETFILE;
		}
		return fileReturnType.SUCCESS;
	}
	/**向流中写入一句话<br>
	 * 写入的类型由{@link #openOutStream}的参数决定<br>
	 * 写完之后会刷新缓存
	 * @param s 写入数据
	 */
	public fileReturnType writeLine(String s)
	{
		if(bufferedWriter==null)
			return fileReturnType.FAILED_NULL_WRITESTREAM;
		try 
		{
			bufferedWriter.write(s);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_WRITELINE;
		}
		return flushStream();
	}
	/**
	 * 刷新本类里的写出流<br>
	 * 如果流为空，则直接返回成功
	 */
	public fileReturnType flushStream()
	{
		if(bufferedWriter==null)
			return fileReturnType.SUCCESS;
		try 
		{
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_FLUSH_OUTSTREAM;
		}
		return fileReturnType.SUCCESS;
	}
	/**从流中读取一句话
	 * */
	public String readLine()
	{
		if(bufferedReader==null)
			return fileReturnType.FAILED_NULL_READSTREAM.name();
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_READLINE.name();
		}
	}
	/**读取文件中的全部数据*/
	public String readAll()
	{
		if(bufferedReader==null)
			return fileReturnType.FAILED_NULL_READSTREAM.name();
		StringBuilder stringBuilder=new StringBuilder();
		for(String s=readLine();s!=null;s=readLine())
		{
			if(s.equals(fileReturnType.FAILED_READLINE.name()))
				return s;
			stringBuilder.append(s);
			stringBuilder.append('\n');
		}
		return stringBuilder.toString();
	}
	/**
	 * 复制本实例中的文件到另一处
	 * @param path 路径
	 * @param name 文件名称，如果为null则与源文件名称相同
	 */
	public fileReturnType copyFile(String path,String name)
	{
		if(!file.exists())
			return fileReturnType.FAILED_NULL_FILE;
		if(name==null)
			name=file.getName();
		if(!new File(path,name).exists())
			createFile(path+name);
		FileChannel inChannel=null;
		FileChannel outChannel=null;
		try 
		{
			inChannel=new FileInputStream(file).getChannel();
			outChannel=new FileOutputStream(new File(path,name)).getChannel();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
//			理论上不可能这样
			e.printStackTrace();
		}
		
		try 
		{
			outChannel.transferFrom(inChannel, 0, inChannel.size());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_COPYFILE;
		}
		
		try 
		{
			inChannel.close();
			outChannel.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			return fileReturnType.FAILED_COPYFILE;
		}
		return fileReturnType.SUCCESS;
	}
	public boolean exists()
	{
		return file.exists();
	}
	public boolean isDirectory()
	{
		return file.isDirectory();
	}
	public boolean isFile()
	{
		return file.isFile();
	}
}