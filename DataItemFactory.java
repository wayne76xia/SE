import java.util.Arrays;

public class DataItemFactory {

    Integer teacherId;
    Integer classRequirementId;
    Integer schoolClassId;
    Integer trainId;

    private DataItemFactory() {

    }

    public DataItemFactory(Integer teacherId, Integer classRequirementId, Integer schoolClassId, Integer trainId) {
        this.teacherId = teacherId;
        this.classRequirementId = classRequirementId;
        this.schoolClassId = schoolClassId;
        this.trainId = trainId;
    }

    public static DataItemFactory init() {
        return new DataItemFactory();
    }

    public Teacher createTeacher(Object id, Object name, String[] trains) {
        Teacher teacher = new Teacher(Integer.parseInt(id.toString()), name.toString());
        Arrays.stream(trains).forEach(no -> {
            teacher.Train((Train) Main.tablesMap.get(Main.CLASS_TYPE_TRAIN).get(Integer.parseInt(no)));
        });
        return teacher;
    }

    public ClassRequirement createClassRequirement(Object id, Object number, String[] trains) {
        ClassRequirement classRequirement =
                new ClassRequirement(Integer.parseInt(id.toString()), Integer.parseInt(number.toString()));
        Arrays.stream(trains).forEach(no -> {
            classRequirement.addRequiredTrain((Train) Main.tablesMap.get(Main.CLASS_TYPE_TRAIN).get(Integer.parseInt(no)));
        });
        return classRequirement;
    }

    public SchoolClass createSchoolClass(Object id, Object name, String[] classRequirements) {
        SchoolClass schoolClass = new SchoolClass(Integer.parseInt(id.toString()), name.toString());
        Arrays.stream(classRequirements).forEach(no -> {
            schoolClass.addSchoolClassRequirementList((ClassRequirement) Main.tablesMap.get(Main.CLASS_TYPE_CLASSREQUIREMENT).get(Integer.parseInt(no)));
        });
        return schoolClass;
    }

    public Train createTrain(Object id, Object name) {
        return new Train(Integer.parseInt(id.toString()), name.toString());
    }
}
