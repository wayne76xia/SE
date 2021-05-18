import java.io.*;
import java.util.ArrayList;


public class TeacherView 
{
	
	//用来显示搜索出来的教师
	private ArrayList<Teacher> teacherList = new ArrayList<Teacher>();

	public  TeacherView ( ArrayList<Teacher> inputTeacherList)
	{
		this.teacherList = inputTeacherList;
	}
	
	public	void	addTeacher(Teacher inputTeacher)	
	{ 
		teacherList.add(inputTeacher);
	}

	public	void	deleteTeacher(Teacher inputTeacher)
	{
		teacherList.remove(inputTeacher);
	}
	
	/*
	public Teacher	find(String n)
	{
		for (int j = 0; j < i; j++)
			if (list[j].getName().equals(n))
				return list[j];
		return null;
	}
	*/
	
	public	void	print(PrintStream ps)
	{
		for (int i= 0; i < teacherList.size(); i++)
		{
			teacherList.get(i).print(ps);
			ps.println();
		}
	}


}
