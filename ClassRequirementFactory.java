public class ClassRequirementFactory implements DataFactory
{

		public classRequirement makeDataitem(String inputName)
		{
			return new classRequirement(inputName);
		}
}

