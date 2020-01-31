package global.authorizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import tools.XMLDocument;

class AuthirizerList 
{
	private Document document;
	private String path;
	
	
	private static final String ListeningStatusSuffix="T";
	private static final String XMLGroupHead="H";
	/**
	 * 监听的类型<br>
	 * 依次分为：全监听，不监听黑名单，仅监听白名单，全不监听
	 * @author GuoJiaCheng
	 *
	 */
	private enum ListeningStatus{ALL("A"),EXCEPT_BLACK("W"),ONLY_WHITE("B"),NONE("N");
		private String letter;
	ListeningStatus(String string)
	{
		letter=string;
	}};
	/**
	 * 名单的类型<br>
	 * 依次分为：黑名单，白名单<br>
	 * 携带有标记字母BW
	 * @author GuoJiaCheng
	 *
	 */
	private enum ListTypeSuffix{WHITE("W"),BLACK("B");
		private String letter;
		ListTypeSuffix(String string) 
		{
			letter=string;
	}};
	/**
	 * 名单的类型<br>
	 * 依次分为：群，讨论组，私聊，群中人，讨论组中人<br>
	 * 携带有标记字母G D P PG PD
	 * @author GuoJiaCheng
	 *
	 */
	private enum ListAimSuffix{GROUP("G"),DISCUSS("D"),PERSON("P"),PERSONINGROUP("PG"),PERSONINDISCUSS("PD");
		private String letter;
	ListAimSuffix(String string)
	{
		letter=string;
	}};
	
	
	protected AuthirizerList(String path) throws JDOMException, IOException 
	{
		this.path=path;
		document=XMLDocument.getDocument(path, true);
	}
	
	/**
	 * 获得指定的表
	 * @param name 表的名字
	 * @param aimSuffix 指定的目标类型
	 * @param typeSuffix 类型（黑名单，白名单）
	 * @return 表中是数据，若为空则会返回一个长度为0的空表
	 */
	private ArrayList<Long> getList(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix)
	{
		Element rootElement=document.getRootElement();
		Element aimElement=rootElement.getChild(name);
		addNewPart(name);
//		FIXME:之后要加上容错及排错机制，还有避免查询群中人及讨论组中人的代码
		Element pointElement=aimElement.getChild(aimSuffix.letter+typeSuffix.letter);
		String[] list=pointElement.getText().split(",");
		ArrayList<Long> arrayList=new ArrayList<Long>();
		for (String string : list)
		{
			arrayList.add(Long.parseLong(string));
		}
		return arrayList;
	}
	/**
	 * 获得指定的双重表，用于群中人和讨论组中人
	 * @param name 表的名字
	 * @param aimSuffix 指定的目标类型
	 * @param typeSuffix 类型（黑名单，白名单）
	 * @param number 群或讨论组的号码
	 * @return 表中是数据，若为空则会返回一个长度为0的空表
	 */
	private ArrayList<Long> getDoubleList(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix,long number)
	{
		Element rootElement=document.getRootElement();
		Element aimElement=rootElement.getChild(name);
		addNewPart(name);
//		FIXME:之后要加上容错及排错机制，还有避免查询群中人及讨论组中人的代码
		Element pointElement=aimElement.getChild(aimSuffix.letter+typeSuffix.letter);
		Element groupElement=pointElement.getChild(XMLGroupHead+number);
		
		if(groupElement==null)
		{
			groupElement=new Element(XMLGroupHead+number);
			pointElement.addContent(groupElement);
		}
		saveDocument();
		String[] list=groupElement.getText().split(",");
		ArrayList<Long> arrayList=new ArrayList<Long>();
		for (String string : list)
		{
			arrayList.add(Long.getLong(string));
		}
		return arrayList;
	}

	/**
	 * 保存本文件
	 */
	private void saveDocument()
	{
		XMLOutputter outputter=new XMLOutputter(Format.getCompactFormat().setEncoding("UTF-8").setIndent("\t"));
		try
		{
			FileWriter fileWriter=new FileWriter(path);
			outputter.output(document, fileWriter);
			fileWriter.close();
		} catch (IOException e)
		{
			// TODO 提交错误到log
			e.printStackTrace();
		}
	}
	/**
	 * 重新刷新文档，主要是为了避免出现人工修改的情况<br>
	 * 警告：如果表内的信息量很大的话，大量的重复刷新会造成性能下降！
	 */
	private void refreshDocument()
	{
		try
		{
			document=XMLDocument.getDocument(path, true);
		} catch (JDOMException e)
		{
			// TODO 提交到log
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO 提交到log
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建新的表格，如果{@code name}已经存在，则不会做任何操作
	 * @param name
	 */
	private void addNewPart(String name)
	{
		Element rootElement=document.getRootElement();
		Element aimElement=rootElement.getChild(name);
		if(aimElement!=null)
		{
			return;
		}
		aimElement=new Element(name);
		for (ListAimSuffix aim : ListAimSuffix.values())
		{
			for (ListTypeSuffix type : ListTypeSuffix.values())
			{
				aimElement.addContent(new Element(aim.letter+type.letter));
			}
			Element element=new Element(ListeningStatusSuffix);
//			FIXME:这里之后要加入一个由设置的默认值而影响的
			element.setText(ListeningStatus.ONLY_WHITE.letter);
			rootElement.addContent(element);
		}
		
		aimElement.addContent(new Element(ListAimSuffix.PERSONINGROUP.letter+ListTypeSuffix.WHITE.letter));
		aimElement.addContent(new Element(ListAimSuffix.PERSONINGROUP.letter+ListTypeSuffix.BLACK.letter));
		aimElement.addContent(new Element(ListAimSuffix.PERSONINDISCUSS.letter+ListTypeSuffix.WHITE.letter));
		aimElement.addContent(new Element(ListAimSuffix.PERSONINDISCUSS.letter+ListTypeSuffix.BLACK.letter));
		
		rootElement.addContent(aimElement);
		saveDocument();
	}
	/**
	 * 添加号码到名单中，本方法用于群，讨论组，个人，不适用于双重<br>
	 * 注意：将一个号码添加到相应名单代表着会从其相反名单中移除！
	 * @param name 项目的名称
	 * @param aimSuffix 对象类型
	 * @param typeSuffix 名单类型
	 * @param num 对象号码
	 * @return 是否成功添加，如果没有进行添加（已经存在或对象类型不对）则返回{@code false}
	 */
	private boolean addClient(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix,long num)
	{
		if(aimSuffix==ListAimSuffix.PERSONINDISCUSS||aimSuffix==ListAimSuffix.PERSONINGROUP)
			return false;
		Element rootElement=document.getRootElement();
		addNewPart(name);
		Element partElement=rootElement.getChild(name);
		Element listElement=partElement.getChild(aimSuffix.letter+typeSuffix.letter);
		ArrayList<Long> list=getList(name, aimSuffix, typeSuffix);
		if(list.contains(num))
			return false;
		list.add(num);
		StringBuilder builder=new StringBuilder();
		for (Long long1 : list)
		{
			builder.append(long1+",");
		}
		listElement.setText(builder.toString());
		saveDocument();
		removeClient(name, aimSuffix, typeSuffix, num);
		return true;
	}
	
	/**
	 * 添加号码到名单中，本方法适用于双重<br>
	 * 注意：将一个号码添加到相应名单代表着会从其相反名单中移除！
	 * @param name 项目的名称
	 * @param aimSuffix 对象类型
	 * @param typeSuffix 名单类型
	 * @param groupNum 群或讨论组的号码
	 * @param num 对象号码
	 * @return 是否成功添加，如果没有进行添加（已经存在或对象类型不对）则返回{@code false}
	 */
	private boolean addDoubleClient(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix,long groupNum,long num)
	{
		if(aimSuffix!=ListAimSuffix.PERSONINDISCUSS&&aimSuffix!=ListAimSuffix.PERSONINGROUP)
			return false;
		Element rootElement=document.getRootElement();
		addNewPart(name);
		Element partElement=rootElement.getChild(name);
		Element groupElement=partElement.getChild(aimSuffix.letter+typeSuffix.letter);
		Element listElement=groupElement.getChild(ListeningStatusSuffix+groupNum);
		ArrayList<Long> list=getDoubleList(name, aimSuffix, typeSuffix,groupNum);
		if(list.contains(num))
			return false;
		list.add(num);
		StringBuilder builder=new StringBuilder();
		for (Long long1 : list)
		{
			builder.append(long1+",");
		}
		listElement.setText(builder.toString());
		saveDocument();
		removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
		return true;
	}
	
	/**
	 * 删除名单中的号码，本方法用于群，讨论组，个人，不适用于双重<br>
	 * @param name 项目的名称
	 * @param aimSuffix 对象类型
	 * @param typeSuffix 名单类型
	 * @param num 对象号码
	 * @return 是否成功删除，如果没有进行删除（不存在或对象类型不对）则返回{@code false}
	 */
	private boolean removeClient(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix,long num)
	{
		if(aimSuffix==ListAimSuffix.PERSONINDISCUSS||aimSuffix==ListAimSuffix.PERSONINGROUP)
			return false;
		Element rootElement=document.getRootElement();
		addNewPart(name);
		Element partElement=rootElement.getChild(name);
		Element listElement=partElement.getChild(aimSuffix.letter+typeSuffix.letter);
		ArrayList<Long> list=getList(name, aimSuffix, typeSuffix);
		if(!list.contains(num))
			return false;
		list.remove(num);
		StringBuilder builder=new StringBuilder();
		for (Long long1 : list)
		{
			builder.append(long1+",");
		}
		listElement.setText(builder.toString());
		saveDocument();
		return true;
	}
	
	/**
	 * 删除名单中的号码，本方法适用于双重<br>
	 * 注意：将一个号码添加到相应名单代表着会从其相反名单中移除！
	 * @param name 项目的名称
	 * @param aimSuffix 对象类型
	 * @param typeSuffix 名单类型
	 * @param groupNum 群或讨论组的号码
	 * @param num 对象号码
	 * @return 是否成功删除，如果没有进行删除（不存在或对象类型不对）则返回{@code false}
	 */
	private boolean removeDoubleClient(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix,long groupNum,long num)
	{
		if(aimSuffix!=ListAimSuffix.PERSONINDISCUSS&&aimSuffix!=ListAimSuffix.PERSONINGROUP)
			return false;
		Element rootElement=document.getRootElement();
		addNewPart(name);
		Element partElement=rootElement.getChild(name);
		Element groupElement=partElement.getChild(aimSuffix.letter+typeSuffix.letter);
		Element listElement=groupElement.getChild(ListeningStatusSuffix+groupNum);
		ArrayList<Long> list=getDoubleList(name, aimSuffix, typeSuffix,groupNum);
		if(!list.contains(num))
			return false;
		list.remove(num);
		StringBuilder builder=new StringBuilder();
		for (Long long1 : list)
		{
			builder.append(long1+",");
		}
		listElement.setText(builder.toString());
		saveDocument();
		return true;
	}
	
	/**
	 * 获得群白名单
	 * @param name 项目的名字
	 * @return 白名单列表
	 */
 	protected ArrayList<Long> getGroupWhiteList(String name)
	{
		ListAimSuffix aim=ListAimSuffix.GROUP;
		ListTypeSuffix type=ListTypeSuffix.WHITE;
		ArrayList<Long> arrayList=getList(name,aim,type);
		return arrayList;
	}
	/**
	 * 获得讨论组白名单
	 * @param name 项目的名字
	 * @return 白名单列表
	 */
	protected ArrayList<Long> getDiscussWhiteList(String name)
	{
		ListAimSuffix aim=ListAimSuffix.DISCUSS;
		ListTypeSuffix type=ListTypeSuffix.WHITE;
		ArrayList<Long> arrayList=getList(name,aim,type);
		return arrayList;
	}
	/**
	 * 获得个人白名单
	 * @param name 项目的名字
	 * @return 白名单列表
	 */
	protected ArrayList<Long> getPersonWhiteList(String name)
	{
		ListAimSuffix aim=ListAimSuffix.DISCUSS;
		ListTypeSuffix type=ListTypeSuffix.WHITE;
		ArrayList<Long> arrayList=getList(name,aim,type);
		return arrayList;
	}
	/**
	 * 获得群黑名单
	 * @param name 项目的名字
	 * @return 白名单列表
	 */
	protected ArrayList<Long> getGroupBlackList(String name)
	{
		ListAimSuffix aim=ListAimSuffix.GROUP;
		ListTypeSuffix type=ListTypeSuffix.BLACK;
		ArrayList<Long> arrayList=getList(name,aim,type);
		return arrayList;
	}
	/**
	 * 获得讨论组黑名单
	 * @param name 项目的名字
	 * @return 白名单列表
	 */
	protected ArrayList<Long> getDiscussBlackList(String name)
	{
		ListAimSuffix aim=ListAimSuffix.DISCUSS;
		ListTypeSuffix type=ListTypeSuffix.BLACK;
		ArrayList<Long> arrayList=getList(name,aim,type);
		return arrayList;
	}
	/**
	 * 获得在群中某个人黑名单
	 * @param name 项目的名字
	 * @return 白名单列表
	 */
	protected ArrayList<Long> getPersonBlackList(String name)
	{
		ListAimSuffix aim=ListAimSuffix.PERSON;
		ListTypeSuffix type=ListTypeSuffix.BLACK;
		ArrayList<Long> arrayList=getList(name,aim,type);
		return arrayList;
	}
	
	protected ArrayList<Long> getGroupDetialedWhiteList(String name,long GroupNumber)
	{
		ListAimSuffix aim=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix type=ListTypeSuffix.WHITE;
		ArrayList<Long> arrayList=getDoubleList(name, aim, type, GroupNumber);
		return arrayList;
	}
	protected ArrayList<Long> getGroupDetialedBlackList(String name,long GroupNumber)
	{
		ListAimSuffix aim=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix type=ListTypeSuffix.BLACK;
		ArrayList<Long> arrayList=getDoubleList(name, aim, type, GroupNumber);
		return arrayList;
	}
	protected ArrayList<Long> getDiscussDetialedWhiteList(String name,long GroupNumber)
	{
		ListAimSuffix aim=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix type=ListTypeSuffix.WHITE;
		ArrayList<Long> arrayList=getDoubleList(name, aim, type, GroupNumber);
		return arrayList;
	}
	protected ArrayList<Long> getDiscussDetialedBlackList(String name,long GroupNumber)
	{
		ListAimSuffix aim=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix type=ListTypeSuffix.BLACK;
		ArrayList<Long> arrayList=getDoubleList(name, aim, type, GroupNumber);
		return arrayList;
	}
	
	protected boolean addGroupWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean addGroupBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean addDiscussWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean addDiscussBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean addPersonWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean addPersonBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean addGroupDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	protected boolean addGroupDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	protected boolean addDiscussDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	protected boolean addDiscussDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	
	protected boolean removeGroupWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean removeGroupBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean removeDiscussWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean removeDiscussBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean removePersonWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean removePersonBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	protected boolean removeGroupDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	protected boolean removeGroupDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	protected boolean removeDiscussDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	protected boolean removeDiscussDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
}
