import java.io.*;

public class Train 
{
	private String name;
	
	
	public Train (String inputName)
	{
		this.name = inputName;
	}
	
	public void setName (String inputName)
	{
		this.name = inputName;
	}
	
	public String getName ()
	{
		return this.name;
	}

	public void print(PrintStream ps) 
	{
		ps.print(name+" ");
	}
	
	
	
	
	//here need to add some IO methods

	
}
