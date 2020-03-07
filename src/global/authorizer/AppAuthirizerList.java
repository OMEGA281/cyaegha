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
import tools.FileSimpleIO.fileReturnType;

public class AppAuthirizerList 
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
	
	
	public AppAuthirizerList(String path) throws JDOMException, IOException 
	{
		this.path=path;
		document=XMLDocument.getDocument(path, true);
	}
	
	/**
	 * 获得指定的表
	 * @param name 表的名字
	 * @param aimSuffix 指定的目标类型
	 * @param typeSuffix 类型（黑名单，白名单）
	 * @return 表中是数据，若为空则会返回{@code null}
	 */
	private ArrayList<Long> getList(String name,ListAimSuffix aimSuffix,ListTypeSuffix typeSuffix)
	{
		Element rootElement=document.getRootElement();
		Element aimElement=rootElement.getChild(name);
		addNewPart(name);
//		FIXME:之后要加上容错及排错机制，还有避免查询群中人及讨论组中人的代码
		Element pointElement=aimElement.getChild(aimSuffix.letter+typeSuffix.letter);
		String line=pointElement.getText();
		if(line.isEmpty())
			return null;
		String[] list=line.split(",");
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
			Element statuesElement=aimElement.getChild(aimSuffix.letter+ListeningStatusSuffix);
			Element subElement=statuesElement.getChild(XMLGroupHead+number);
			if(subElement==null)
			{
				subElement=new Element(XMLGroupHead+number);
				subElement.setAttribute(ListeningStatusSuffix, statuesElement.getAttributeValue(ListeningStatusSuffix));
			}
			saveDocument();
		}
		String line=groupElement.getText();
		if(line.isEmpty())
			return null;
		String[] list=line.split(",");
		ArrayList<Long> arrayList=new ArrayList<Long>();
		for (String string : list)
		{
			arrayList.add(Long.parseLong(string));
		}
		return arrayList;
	}
	
	/**
	 * 获得指定表的监听状态
	 * @param name 项目名称
	 * @param listAimSuffix 指定的目标类型
	 * @param num 群或讨论组的号码（当非双重表的时候，此参数无用）
	 * @return 现在的状态
	 */
	private ListeningStatus getListeningStatus(String name,ListAimSuffix listAimSuffix,long num)
	{
		refreshDocument();
		Element rootElement=document.getRootElement();
		Element aimElement=rootElement.getChild(name);
		addNewPart(name);
		switch(listAimSuffix)
		{
		case DISCUSS:
		case PERSON:
		case GROUP:
			Element childElement=aimElement.getChild(listAimSuffix.letter+ListeningStatusSuffix);
			String sStatus=childElement.getAttributeValue(ListeningStatusSuffix);
			ListeningStatus status = null;
			for (ListeningStatus listeningStatus : ListeningStatus.values())
			{
				if(sStatus.equals(listeningStatus.letter))
				{
					status=listeningStatus;
					break;
				}
			}
			if(status==null)
			{
				return ListeningStatus.NONE;
			}
			return status;
		case PERSONINDISCUSS:
		case PERSONINGROUP:
			Element childElement2=aimElement.getChild(listAimSuffix.letter+ListeningStatusSuffix);
			String superStatus=childElement2.getAttributeValue(ListeningStatusSuffix);
			ListeningStatus superListeningStatus=null;
			for (ListeningStatus listeningStatus : ListeningStatus.values())
			{
				if(superStatus.equals(listeningStatus.letter))
				{
					superListeningStatus=listeningStatus;
					break;
				}
			}
			if(superListeningStatus==null)
			{
				return ListeningStatus.NONE;
			}
			Element subElement=childElement2.getChild(XMLGroupHead+num);
			if(subElement==null)
			{
				return superListeningStatus;
			}
			String subStatus=subElement.getAttributeValue(ListeningStatusSuffix);
			ListeningStatus subListeningStatus=null;
			for (ListeningStatus listeningStatus : ListeningStatus.values())
			{
				if(subStatus.equals(listeningStatus.letter))
				{
					subListeningStatus=listeningStatus;
					break;
				}
			}
			if(subListeningStatus==null)
			{
				return ListeningStatus.NONE;
			}
			return subListeningStatus;
		}
		return ListeningStatus.NONE;
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
				Element subElement=new Element(aim.letter+type.letter);
				aimElement.addContent(subElement);
			}
			Element statusElement=new Element(aim.letter+ListeningStatusSuffix);
//			FIXME:这里之后要加入一个由设置的默认值而影响的
			statusElement.setAttribute(ListeningStatusSuffix, ListeningStatus.ONLY_WHITE.letter);
			aimElement.addContent(statusElement);
		}	
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
		if(list==null)
			list=new ArrayList<Long>();
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
		removeClient(name, aimSuffix, typeSuffix==ListTypeSuffix.WHITE?ListTypeSuffix.BLACK:ListTypeSuffix.WHITE, num);
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
		Element listElement=groupElement.getChild(XMLGroupHead+groupNum);
		if(listElement==null)
		{
			listElement=new Element(XMLGroupHead+groupNum);
			groupElement.addContent(listElement);
		}
		
		ArrayList<Long> list=getDoubleList(name, aimSuffix, typeSuffix,groupNum);
		if(list==null)
			list=new ArrayList<Long>();
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
		removeDoubleClient(name, aimSuffix, typeSuffix==ListTypeSuffix.WHITE?ListTypeSuffix.BLACK:ListTypeSuffix.WHITE, groupNum, num);
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
		if(list==null)
			list=new ArrayList<Long>();
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
		Element listElement=groupElement.getChild(XMLGroupHead+groupNum);
		if(listElement==null)
		{
			listElement=new Element(XMLGroupHead+groupNum);
			groupElement.addContent(listElement);
		}
		ArrayList<Long> list=getDoubleList(name, aimSuffix, typeSuffix,groupNum);
		if(list==null)
			list=new ArrayList<Long>();
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
	
	public boolean addGroupWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean addGroupBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean addDiscussWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean addDiscussBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean addPersonWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean addPersonBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean addGroupDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	public boolean addGroupDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	public boolean addDiscussDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	public boolean addDiscussDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return addDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	
	public boolean removeGroupWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean removeGroupBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.GROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean removeDiscussWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean removeDiscussBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.DISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean removePersonWhiteList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean removePersonBlackList(String name,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSON;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeClient(name, aimSuffix, typeSuffix, num);
	}
	public boolean removeGroupDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	public boolean removeGroupDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINGROUP;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	public boolean removeDiscussDetialedWhiteList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.WHITE;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	public boolean removeDiscussDetialedBlackList(String name,long groupNum,long num)
	{
		ListAimSuffix aimSuffix=ListAimSuffix.PERSONINDISCUSS;
		ListTypeSuffix typeSuffix=ListTypeSuffix.BLACK;
		return removeDoubleClient(name, aimSuffix, typeSuffix, groupNum, num);
	}
	
	/**
	 * 返回名单情况
	 * @param name 权限名称
	 * @param num 查询号码
	 * @return 返回数字：-1/在黑名单中；0/不存在于任何名单；1/在白名单中
	 */
	public int getPersonPermission(String name,long num)
	{
		ArrayList<Long> black=getList(name, ListAimSuffix.PERSON, ListTypeSuffix.BLACK);
		if(black!=null)
			if(black.contains(num))
				return -1;
		ArrayList<Long> white=getList(name, ListAimSuffix.PERSON, ListTypeSuffix.WHITE);
		if(white!=null)
			if(white.contains(num))
				return 1;
		return 0;
	}
	/**
	 * 返回名单情况
	 * @param name 权限名称
	 * @param num 查询号码
	 * @return 返回数字：-1/在黑名单中；0/不存在于任何名单；1/在白名单中
	 */
	public int getGroupPermission(String name,long num)
	{
		ArrayList<Long> black=getList(name, ListAimSuffix.GROUP, ListTypeSuffix.BLACK);
		if(black!=null)
			if(black.contains(num))
				return -1;
		ArrayList<Long> white=getList(name, ListAimSuffix.GROUP, ListTypeSuffix.WHITE);
		if(white!=null)
			if(white.contains(num))
				return 1;
		return 0;
	}
	/**
	 * 返回名单情况
	 * @param name 权限名称
	 * @param groupNum 群号码
	 * @param num 查询号码
	 * @return 返回数字：-1/在黑名单中；0/不存在于任何名单；1/在白名单中
	 */
	public int getDiscussPermission(String name,long num)
	{
		ArrayList<Long> black=getList(name, ListAimSuffix.DISCUSS, ListTypeSuffix.BLACK);
		if(black!=null)
			if(black.contains(num))
				return -1;
		ArrayList<Long> white=getList(name, ListAimSuffix.DISCUSS, ListTypeSuffix.WHITE);
		if(white!=null)
			if(white.contains(num))
				return 1;
		return 0;
	}
	/**
	 * 返回名单情况
	 * @param name 权限名称
	 * @param groupNum 群号码
	 * @param num 查询号码
	 * @return 返回数字：-1/在黑名单中；0/不存在于任何名单；1/在白名单中
	 */
	public int getGroupDetialedPermission(String name,long groupNum,long num)
	{
		ArrayList<Long> black=getDoubleList(name, ListAimSuffix.PERSONINGROUP, ListTypeSuffix.BLACK,groupNum);
		if(black!=null)
			if(black.contains(num))
				return -1;
		ArrayList<Long> white=getDoubleList(name, ListAimSuffix.PERSONINGROUP, ListTypeSuffix.WHITE,groupNum);
		if(white!=null)
			if(white.contains(num))
				return 1;
		return 0;
	}
	/**
	 * 返回名单情况
	 * @param name 权限名称
	 * @param num 查询号码
	 * @return 返回数字：-1/在黑名单中；0/不存在于任何名单；1/在白名单中
	 */
	public int getDiscussDetialedPermission(String name,long groupNum,long num)
	{
		ArrayList<Long> black=getDoubleList(name, ListAimSuffix.PERSONINDISCUSS, ListTypeSuffix.BLACK,groupNum);
		if(black!=null)
			if(black.contains(num))
				return -1;
		ArrayList<Long> white=getDoubleList(name, ListAimSuffix.PERSONINDISCUSS, ListTypeSuffix.WHITE,groupNum);
		if(white!=null)
			if(white.contains(num))
				return 1;
		return 0;
	}

	public boolean hasPersonPermission(String name,long number)
	{
		ListeningStatus status=getListeningStatus(name, ListAimSuffix.PERSON, number);
		switch(status)
		{
		case ALL:
			return true;
		case NONE:
			return false;
		case ONLY_WHITE:
		case EXCEPT_BLACK:
			int clientStatus=getPersonPermission(name, number);
			if(clientStatus>=1||(status==ListeningStatus.EXCEPT_BLACK&&clientStatus>=0))
				return true;
		}
		return false;
	}
	public boolean hasGroupPermission(String name,long number)
	{
		ListeningStatus status=getListeningStatus(name, ListAimSuffix.GROUP, number);
		switch(status)
		{
		case ALL:
			return true;
		case NONE:
			return false;
		case ONLY_WHITE:
		case EXCEPT_BLACK:
			int clientStatus=getGroupPermission(name, number);
			if(clientStatus>=1||(status==ListeningStatus.EXCEPT_BLACK&&clientStatus>=0))
				return true;
		}
		return false;
	}
	public boolean hasDiscussPermission(String name,long number)
	{
		ListeningStatus status=getListeningStatus(name, ListAimSuffix.DISCUSS, number);
		switch(status)
		{
		case ALL:
			return true;
		case NONE:
			return false;
		case ONLY_WHITE:
		case EXCEPT_BLACK:
			int clientStatus=getDiscussPermission(name, number);
			if(clientStatus>=1||(status==ListeningStatus.EXCEPT_BLACK&&clientStatus>=0))
				return true;
		}
		return false;
	}
	public boolean hasGroupDetailedPermission(String name,long groupNum,long number)
	{
		ListeningStatus status=getListeningStatus(name, ListAimSuffix.PERSONINGROUP, groupNum);
		switch(status)
		{
		case ALL:
			return true;
		case NONE:
			return false;
		case ONLY_WHITE:
		case EXCEPT_BLACK:
			int clientStatus=getGroupDetialedPermission(name, groupNum, number);
			if(clientStatus>=1||(status==ListeningStatus.EXCEPT_BLACK&&clientStatus>=0))
				return true;
		}
		return false;
	}
	public boolean hasDiscussDetailedPermission(String name,long groupNum,long number)
	{
		ListeningStatus status=getListeningStatus(name, ListAimSuffix.PERSONINDISCUSS, groupNum);
		switch(status)
		{
		case ALL:
			return true;
		case NONE:
			return false;
		case ONLY_WHITE:
		case EXCEPT_BLACK:
			int clientStatus=getDiscussDetialedPermission(name, groupNum, number);
			if(clientStatus>=1||(status==ListeningStatus.EXCEPT_BLACK&&clientStatus>=0))
				return true;
		}
		return false;
	}
}
