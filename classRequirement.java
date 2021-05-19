import java.io.*;
import java.util.ArrayList;

public class classRequirement extends DataItem implements TextIO,ShowDataItem
{
	//需求的名称，设置为private让用户不能直接访问
	private String Name;
	private int teacherNumber;
	private ArrayList<Train> needTrainList = new ArrayList<Train>();
	
	//constructor
	public classRequirement(int inputTeacherNumber, ArrayList<Train> inputNeedTrainList)
	{
		this.teacherNumber=inputTeacherNumber;
		this.needTrainList=inputNeedTrainList;
	}
	
	public classRequirement(String inputName)
	{
		this.Name=inputName;
	}
	
	//set and get Name method
	public void setName(String inputName)
	{
		this.Name = inputName;
	}
	
	public String getName()
	{
		return Name;
	}
	
	//set and get TeacherNumber method
	public void setTeacherNumber(int inputTeacherNumber)
	{
		this.teacherNumber = inputTeacherNumber;
	}
	
	public int getTeacherNumber()
	{
		return teacherNumber;
	}
	
	public void addRequiredTrain(Train inputTrain)
	{
		needTrainList.add(inputTrain);
	}
	
	public void deleteRequiredTrain(Train inputTrain)
	{
		needTrainList.remove(inputTrain);
	}
	//判断某个老师是否符合要求，用于实现筛选功能
	public boolean judgeTeacher(Teacher inputTeacher)
	{
		for(int i=0;i<this.needTrainList.size();i++)
		{
			if(inputTeacher.getList().contains(needTrainList.get(i)))
			{
				
			}
			else
			{
				return false;
			}

		}
		return true;
	}
	
	
	public	void	print(PrintStream ps)
	{
		for (int j = 0; j < needTrainList.size(); j++)
		{
			needTrainList.get(j).print(ps);
			ps.print(',');
		}
	}
	
	
	
	
	//here need to add some IO methods
	@Override
	public void read() 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
