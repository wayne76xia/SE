import java.io.*;
import java.util.ArrayList;

public class SchoolClass 
{
	private String name;
	
	//�����γ�������
	private ArrayList<classRequirement> schoolClassRequirementList = new ArrayList<classRequirement>();
	
	//constructor
	public SchoolClass(String inputName, ArrayList<classRequirement> inputSchoolClassRequirementList)
	{
		this.schoolClassRequirementList=inputSchoolClassRequirementList;
		this.name=inputName;
	}
	
	//�޸����ƣ������ñ������ܱ��û�ֱ�ӵ���
	public void setName (String inputName)
	{
		this.name = inputName;
	}
	
	//��ȡ���ƣ������ñ������ܱ��û�ֱ�ӵ���
	public String getName ()
	{
		return this.name;
	}
	
	//��������б��������ñ������ܱ��û�ֱ�ӵ���
	public void addSchoolClassRequirementList(classRequirement inputSchoolClassRequirementList)
	{
		schoolClassRequirementList.add(inputSchoolClassRequirementList);
	}
	
	//ɾ�������б��������ñ������ܱ��û�ֱ�ӵ���
	public void deleteSchoolClassRequirementList(classRequirement inputSchoolClassRequirementList)
	{
		schoolClassRequirementList.remove(inputSchoolClassRequirementList);
	}
	
	//��ӡ�γ̣�����д�ɽӿ�
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
