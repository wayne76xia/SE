
public class TeacherFactory implements DataFactory
{
	public Teacher makeDataitem(String inputName)
	{
		return new Teacher(inputName);
	}
}
