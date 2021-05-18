import java.io.*;
import java.util.ArrayList;

public class SchoolClass 
{
	private String name;
	
	//建立课程需求组
	private ArrayList<classRequirement> schoolClassRequirementList = new ArrayList<classRequirement>();
	
	//constructor
	public SchoolClass(String inputName, ArrayList<classRequirement> inputSchoolClassRequirementList)
	{
		this.schoolClassRequirementList=inputSchoolClassRequirementList;
		this.name=inputName;
	}
	
	//修改名称，用于让变量不能被用户直接调用
	public void setName (String inputName)
	{
		this.name = inputName;
	}
	
	//获取名称，用于让变量不能被用户直接调用
	public String getName ()
	{
		return this.name;
	}
	
	//添加需求列表，，用于让变量不能被用户直接调用
	public void addSchoolClassRequirementList(classRequirement inputSchoolClassRequirementList)
	{
		schoolClassRequirementList.add(inputSchoolClassRequirementList);
	}
	
	//删除需求列表，，用于让变量不能被用户直接调用
	public void deleteSchoolClassRequirementList(classRequirement inputSchoolClassRequirementList)
	{
		schoolClassRequirementList.remove(inputSchoolClassRequirementList);
	}
	
	//打印课程，可以写成接口
	public	void	print(PrintStream ps)
	{
		ps.println(name);
		for (int j = 0; j < schoolClassRequirementList.size(); j++)
		{
			schoolClassRequirementList.get(j).print(ps);
			ps.println();
		}
	}

	
	
	//here need to add some IO methods

}
