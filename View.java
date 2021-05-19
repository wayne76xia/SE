import java.io.PrintStream;
import java.util.ArrayList;

public class View implements TextIO,ShowDataItem 
{
	//用来显示搜索出来的教师
	private ArrayList<DataItem> DataItemList = new ArrayList<DataItem>();

	public  View ( ArrayList<DataItem> inputDataItemList)
	{
		this.DataItemList = inputDataItemList;
	}
		
	public	void	addDataItem(DataItem inputDataItem)	
	{ 
		DataItemList.add(inputDataItem);
	}

	public	void	deleteDataItem(DataItem inputDataItem)
	{
		DataItemList.remove(inputDataItem);
	}

	public void print(PrintStream ps) 
	{
		for (int i= 0; i < DataItemList.size(); i++)
		{
			DataItemList.get(i).print(ps);
			ps.println();
		}		
	}


	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

}
