import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ClassRequirement extends DataItem implements TextIO, ShowDataItem {
    //需求的名称，设置为private让用户不能直接访问
    private Integer id;
    private Integer teacherNumber;
    private ArrayList<Train> needTrainList = new ArrayList<Train>();

    //constructor
    public ClassRequirement(Integer id, Integer inputTeacherNumber) {
        this.id = id;
        this.teacherNumber = inputTeacherNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTeacherNumber() {
        return teacherNumber;
    }

    //set and get TeacherNumber method
    public void setTeacherNumber(int inputTeacherNumber) {
        this.teacherNumber = inputTeacherNumber;
    }

    public ArrayList<Train> getList() {
        return needTrainList;
    }

    public void addRequiredTrain(Train inputTrain) {
        needTrainList.add(inputTrain);
    }

    /**
     * 对象非同一对象的话，无法正常删除
     *
     * @param inputTrain
     */
    public void deleteRequiredTrain(Train inputTrain) {
        Iterator<Train> iterator = needTrainList.iterator();
        while (iterator.hasNext()) {
            Train train = iterator.next();
            if (train.getId().equals(inputTrain.getId())) {
                iterator.remove();
                return;
            }
        }
    }

    //判断某个老师是否符合要求，用于实现筛选功能
    public boolean judgeTeacher(Teacher inputTeacher) {
        for (int i = 0; i < this.needTrainList.size(); i++) {
            if (inputTeacher.getList().contains(needTrainList.get(i))) {

            } else {
                return false;
            }

        }
        return true;
    }


    @Override
    public void print(PrintStream ps) {
//        for (int j = 0; j < needTrainList.size(); j++) {
//            needTrainList.get(j).print(ps);
//            ps.print(',');
//        }
        StringBuilder stringBuilder = new StringBuilder(id + "\t\t" + teacherNumber + "\t\t");
        needTrainList.stream().forEach(e -> stringBuilder.append(e.getName()).append(","));
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
