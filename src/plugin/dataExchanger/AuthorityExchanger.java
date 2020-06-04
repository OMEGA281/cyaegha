package commandMethod.dataExchanger;

import java.util.ArrayList;

public class AuthorityExchanger extends Exchanger
{

	public AuthorityExchanger(String path) 
	{
		super(path);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void addItem(String name, String text) {
		// TODO Auto-generated method stub
		super.addItem(name, text);
		writeDocument();
	}
	@Override
	public void addListItem(String listName, String name, String text) {
		// TODO Auto-generated method stub
		super.addListItem(listName, name, text);
		writeDocument();
	}
	@Override
	public boolean deleteItem(String name) {
		// TODO Auto-generated method stub
		boolean result=super.deleteItem(name);
		writeDocument();
		return result;
	}
	@Override
	public boolean deleteList(String listName) {
		// TODO Auto-generated method stub
		boolean result=super.deleteList(listName);
		writeDocument();
		return result;
	}
	@Override
	public boolean deleteListItem(String listName, int index) {
		// TODO Auto-generated method stub
		boolean result=super.deleteListItem(listName, index);
		writeDocument();
		return result;
	}
	@Override
	public boolean deleteListItem(String listName, String name, String text) {
		// TODO Auto-generated method stub
		boolean result=super.deleteListItem(listName, name, text);
		writeDocument();
		return result;
	}
	@Override
	public boolean deleteListItem(String listName, String itemName) {
		// TODO Auto-generated method stub
		boolean result=super.deleteListItem(listName, itemName);
		writeDocument();
		return result;
	}
	@Override
	public String getItem(String name) {
		// TODO Auto-generated method stub
		return super.getItem(name);
	}
	@Override
	public ArrayList<String> getListItem(String listName, String Name) {
		// TODO Auto-generated method stub
		return super.getListItem(listName, Name);
	}
	@Override
	public ArrayList<String[]> getList(String listName) {
		// TODO Auto-generated method stub
		return super.getList(listName);
	}
}
