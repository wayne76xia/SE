import java.io.*;
import java.util.ArrayList;

public class Teacher 
{
	
	//variables: name and trainList
	private String name;
	private ArrayList<Train> trainList = new ArrayList<Train>();
	
	public  Teacher (String inputName, ArrayList<Train> inputTrainList)
	{
		this.name = inputName;
		this.trainList = inputTrainList;
	}
	
	//get the trains list
	public ArrayList<Train>  getList()
	{
		return trainList;
	}
	
	//add the trains to teacher's list
	public void Train(Train inputTrain)
	{
		trainList.add(inputTrain);
	}
	
	//delete the train to teacher's list
	public void deleteTrain(Train inputTrain)
	{
		trainList.remove(inputTrain);
	}
		
	//set method for the variable name 
	public void setName(String inputName)
	{
		this.name = inputName;
	}
	
	//get method for the variable name 
	public String getName()
	{
		return name;
	}

	public void print(PrintStream ps) 
	{
		ps.print(name+": "+trainList.toString());
	}
	
	/*
	//toString method for the object teacher
	public String toString()
	{
		return name+" who has these trains: "+trainList.toString();   //show the techaer's name and train list
	}
	*/
	
	
	//here need to add some IO methods
	
	
	
	
	

}
