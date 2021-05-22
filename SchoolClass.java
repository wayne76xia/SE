import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class SchoolClass extends DataItem implements TextIO, ShowDataItem {

    private Integer id;
    private String name;

    //�����γ�������
    private ArrayList<ClassRequirement> schoolClassRequirementList = new ArrayList<ClassRequirement>();

    //constructor
    public SchoolClass(Integer id, String inputName) {
        this.id = id;
        this.name = inputName;
    }

    public SchoolClass(String inputName) {
        this.name = inputName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //��ȡ���ƣ������ñ������ܱ��û�ֱ�ӵ���
    public String getName() {
        return this.name;
    }

    //�޸����ƣ������ñ������ܱ��û�ֱ�ӵ���
    public void setName(String inputName) {
        this.name = inputName;
    }

    public ArrayList<ClassRequirement> getList() {
        return schoolClassRequirementList;
    }

    //��������б��������ñ������ܱ��û�ֱ�ӵ���
    public void addSchoolClassRequirementList(ClassRequirement inputSchoolClassRequirementList) {
        schoolClassRequirementList.add(inputSchoolClassRequirementList);
    }

    //ɾ�������б��������ñ������ܱ��û�ֱ�ӵ���
    public void deleteSchoolClassRequirementList(ClassRequirement inputSchoolClassRequirementList) {
        Iterator<ClassRequirement> iterator = schoolClassRequirementList.iterator();
        while (iterator.hasNext()) {
            ClassRequirement classRequirement = iterator.next();
            if (classRequirement.getId().equals(inputSchoolClassRequirementList.getId())) {
                iterator.remove();
                return;
            }
        }
    }



    /**
     * ��ӡ�γ�
     *
     * @param ps
     */
    @Override
    public void print(PrintStream ps) {
//        ps.println(name);
//        for (int j = 0; j < schoolClassRequirementList.size(); j++) {
//            schoolClassRequirementList.get(j).print(ps);
//            ps.println();
//        }
        StringBuilder stringBuilder = new StringBuilder(id + "\t\t" + name + "\t\t");
        schoolClassRequirementList.stream().forEach(e -> stringBuilder.append(e.getId()).append(","));
        ps.print(stringBuilder.substring(0, stringBuilder.length() - ",".length()));
    }

    //here need to add some IO methods
    @Override
    public void read() {
        // TODO Auto-generated method stub

    }

    @Override
    public void write() {
        // TODO Auto-generated method stub

    }

}
