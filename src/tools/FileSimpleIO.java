package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**一个简单文件读取类，所有的流都存在在本类之中
 * 所有的反馈类型都存在于本类中的{@link returnType}中*/
public class FileSimpleIO 
{
	/**反馈的类型，部分方法会反馈回其Name值*/
	public enum returnType{
		SUCCESS
		,FAILED_CLOSE_OUTSTREAM,FAILED_OPEN_OUTSTREAM,FAILED_CLOSE_INSTREAM,FAILED_OPEN_INSTREAM
		,FAILED_CRAETFILE
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
	FileSimpleIO(String aimFile)
	{
		// TODO Auto-generated constructor stub
		this.aimFile=aimFile;
		file=new File(this.aimFile);
	}
	/**关闭写出文件流
	 * @return 是否成功*/
	public returnType closeOutStream()
	{
		try 
		{
			if(fileWriter!=null)
				fileWriter.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return returnType.FAILED_CLOSE_OUTSTREAM;
		}
		bufferedReader=null;
//		System.out.println("文件流关闭");
		return returnType.SUCCESS;
	}
	/**产生一个写出流,如果文件不存在,则会自动创建<br>
	 * 如果已经存在写出流则关闭上一写出流并产生新的<br>
	 * @param append 是的话则会在末尾添加
	 * @return 是否成功*/
	public returnType openOutStream(boolean append)
	{
		if(bufferedWriter!=null)
			if(closeOutStream()!=returnType.SUCCESS)
				return returnType.FAILED_CLOSE_OUTSTREAM;
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
			return returnType.FAILED_OPEN_OUTSTREAM;
		}
		
		bufferedWriter=new BufferedWriter(fileWriter);
		return returnType.SUCCESS;
	}
	/**关闭读取文件流
	 * @return 是否成功*/
	public returnType closeInStream()
	{
		try 
		{
			if(fileReader!=null)
				fileReader.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return returnType.FAILED_CLOSE_INSTREAM;
		}
		bufferedReader=null;
		return returnType.SUCCESS;
	}
	/**产生一个读取流,如果文件不存在,则会报错<br>
	 * 如果已经存在读取流则关闭上一读取流并产生新的<br>
	 * @return 是否成功*/
	public returnType openInStream()
	{
		if(bufferedWriter!=null)
			if(closeOutStream()!=returnType.SUCCESS)
				return returnType.FAILED_CLOSE_INSTREAM;
		try 
		{
			fileWriter = new FileWriter(file);
		} 
		catch(FileNotFoundException e)
		{
			return returnType.FAILED_NULL_FILE;
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			return returnType.FAILED_OPEN_INSTREAM;
		}
		
		bufferedWriter=new BufferedWriter(fileWriter);
		return returnType.SUCCESS;
	}
	/**创建文件夹，可以直接输入整体的路径*/
	public static returnType createFolder(String aim)
	{
		new File(aim).mkdirs();
		return returnType.SUCCESS;
	}
	/**创建文件，可以直接输入整体的路径<br>
	 * 而且会创建相应的目录<br>
	 * 如果文件已经存在，则不会创建*/
	public static returnType createFile(String aim)
	{
		if(new File(aim).exists())
			return returnType.SUCCESS;
		String format=aim.replace('/', '\\');
		if(!format.contains("\\"))
		{
			try 
			{
				new File(format).createNewFile();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				return returnType.FAILED_CRAETFILE;
			}
		}
		else
		{
			int index=format.lastIndexOf('\\');
			String path=format.substring(0, index);
			String name=format.substring(index+1, format.length());
			createFolder(path);
			try 
			{
				new File(name).createNewFile();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				return returnType.FAILED_CRAETFILE;
			}
		}
		return returnType.SUCCESS;
	}
	/**向流中写入一句话<br>
	 * 写入的类型由{@link #openOutStream}的参数决定<br>
	 * 写完之后会刷新缓存
	 * @param s 写入数据
	 */
	public returnType writeLine(String s)
	{
		if(bufferedWriter==null)
			return returnType.FAILED_NULL_WRITESTREAM;
		try 
		{
			bufferedWriter.write(s);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			return returnType.FAILED_WRITELINE;
		}
		return flushStream();
	}
	/**
	 * 刷新本类里的写出流<br>
	 * 如果流为空，则直接返回成功
	 * @param ifWrite 是否为写出流
	 */
	public returnType flushStream()
	{
		if(bufferedWriter==null)
			return returnType.SUCCESS;
		try 
		{
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return returnType.FAILED_FLUSH_OUTSTREAM;
		}
		return returnType.SUCCESS;
	}
	/**从流中读取一句话
	 * */
	public String readLine()
	{
		if(bufferedReader==null)
			return returnType.FAILED_NULL_READSTREAM.name();
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return returnType.FAILED_READLINE.name();
		}
	}
	/**读取文件中的全部数据*/
	public String readAll()
	{
		if(bufferedReader==null)
			return returnType.FAILED_NULL_READSTREAM.name();
		StringBuilder stringBuilder=new StringBuilder();
		for(String s=readLine();s!=null;s=readLine())
		{
			if(s.equals(returnType.FAILED_READLINE.name()))
				return s;
			stringBuilder.append(s);
			stringBuilder.append('\n');
		}
		return stringBuilder.toString();
	}
	
}