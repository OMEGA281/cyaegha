package plugin.dataExchanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataExchanger extends Exchanger
{

	public DataExchanger(String path)
	{
		super(path);
	}
	@Override
	public boolean addList(String listName, String text)
	{
		boolean b= super.addList(listName, text);
		writeDocument();
		return b;
	}
	@Override
	public boolean addList(String listName, String text, boolean allowRepetition)
	{
		boolean b=super.addList(listName, text, allowRepetition);
		writeDocument();
		return b;
	}
	@Override
	public boolean containInList(String listName, String text)
	{
		return super.containInList(listName, text);
	}
	@Override
	public boolean creatList(String name, boolean allowRepetition)
	{
		boolean b=super.creatList(name, allowRepetition);
		writeDocument();
		return b;
	}
	@Override
	public boolean creatMap(String name)
	{
		boolean b=super.creatMap(name);
		writeDocument();
		return b;
	}
	@Override
	public boolean deleteItem(String name)
	{
		boolean b=super.deleteItem(name);
		writeDocument();
		return b;
	}
	@Override
	public boolean deleteList(String listName)
	{
		boolean b=super.deleteList(listName);
		writeDocument();
		return b;
	}
	@Override
	public boolean deleteList(String listName, String text)
	{
		boolean b=super.deleteList(listName, text);
		writeDocument();
		return b;
	}
	@Override
	public boolean deleteMap(String name)
	{
		boolean b=super.deleteMap(name);
		writeDocument();
		return b;
	}
	@Override
	public boolean deleteMapData(String name, String key)
	{
		boolean b=super.deleteMapData(name, key);
		writeDocument();
		return b;
	}
	@Override
	public HashMap<String, String> getAllItem()
	{
		return super.getAllItem();
	}
	@Override
	public String getItem(String name)
	{
		return super.getItem(name);
	}
	@Override
	public ArrayList<String> getList(String listName)
	{
		return super.getList(listName);
	}
	@Override
	public Map<String, String> getMap(String name)
	{
		return super.getMap(name);
	}
	@Override
	public String getMapData(String name, String key)
	{
		return super.getMapData(name, key);
	}
	@Override
	public boolean hasItem(String name)
	{
		return super.hasItem(name);
	}
	@Override
	public boolean hasList(String name)
	{
		return super.hasList(name);
	}
	@Override
	public boolean hasMap(String name)
	{
		return super.hasMap(name);
	}
	@Override
	public void setItem(String name, String text)
	{
		super.setItem(name, text);
		writeDocument();
	}
	@Override
	public void setMapData(String name, String key, String text)
	{
		super.setMapData(name, key, text);
		writeDocument();
	}
}
