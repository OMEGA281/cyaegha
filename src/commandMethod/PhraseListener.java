package commandMethod;

import java.util.ArrayList;

import commandMethod.register.OnMessageReceiveListener;
import connection.ReceiveMessageType;

public class PhraseListener extends Father 
{
	private static ArrayList<WordRefect> WordsMap=new ArrayList<>();
	private static final int ONLY_EXISTS=0;
	private static final int CONTAIN_EXISTS=1;
	private class WordRefect
	{
		ArrayList<String> words=new ArrayList<>();
		String returnString;
		boolean existStatus;
	}
	@Override
	public void initialize() 
	{
		// TODO Auto-generated method stub
		addMessageReceiveListener(new OnMessageReceiveListener() {
			@Override
			public int run(ReceiveMessageType messageType) {
				// TODO Auto-generated method stub
				
				return RETURN_PASS;
			}
		});
	}

}
