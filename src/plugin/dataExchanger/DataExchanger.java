package plugin.dataExchanger;

import java.util.ArrayList;
import java.util.HashMap;

public class DataExchanger extends Exchanger
{

	public DataExchanger(String path)
	{
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setItem(String name, String text)
	{
		// TODO Auto-generated method stub
		super.setItem(name, text);
		writeDocument();
	}

	@Override
	public void setListItem(String listName, String name, String text)
	{
		// TODO Auto-generated method stub
		super.setListItem(listName, name, text);
		writeDocument();
	}

	@Override
	public HashMap<String, String> getAllItem()
	{
		// TODO Auto-generated method stub
		return super.getAllItem();
	}

	@Override
	public boolean deleteItem(String name)
	{
		// TODO Auto-generated method stub
		boolean result = super.deleteItem(name);
		writeDocument();
		return result;
	}

	@Override
	public boolean deleteList(String listName)
	{
		// TODO Auto-generated method stub
		boolean result = super.deleteList(listName);
		writeDocument();
		return result;
	}

	@Override
	public boolean deleteListItem(String listName, int index)
	{
		// TODO Auto-generated method stub
		boolean result = super.deleteListItem(listName, index);
		writeDocument();
		return result;
	}

	@Override
	public boolean deleteListItem(String listName, String name, String text)
	{
		// TODO Auto-generated method stub
		boolean result = super.deleteListItem(listName, name, text);
		writeDocument();
		return result;
	}

	@Override
	public boolean deleteListItem(String listName, String itemName)
	{
		// TODO Auto-generated method stub
		boolean result = super.deleteListItem(listName, itemName);
		writeDocument();
		return result;
	}

	@Override
	public String getItem(String name)
	{
		// TODO Auto-generated method stub
		return super.getItem(name);
	}

	@Override
	public ArrayList<String> getListItem(String listName, String Name)
	{
		// TODO Auto-generated method stub
		return super.getListItem(listName, Name);
	}

	@Override
	public ArrayList<String[]> getList(String listName)
	{
		// TODO Auto-generated method stub
		return super.getList(listName);
	}
}
