import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Teacher extends DataItem implements TextIO, ShowDataItem {

    //variables: name and trainList
    private Integer id;
    private String name;
    private ArrayList<Train> trainList = new ArrayList<Train>();

    public Teacher(String inputName) {
        this.name = inputName;
    }

    public Teacher(Integer id, String inputName) {
        this.id = id;
        this.name = inputName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //get method for the variable name
    public String getName() {
        return name;
    }

    //set method for the variable name
    public void setName(String inputName) {
        this.name = inputName;
    }

    //get the trains list
    public ArrayList<Train> getList() {
        return trainList;
    }

    //add the trains to teacher's list
    public void Train(Train inputTrain) {
        trainList.add(inputTrain);
    }

    //delete the train to teacher's list
    public void deleteTrain(Train inputTrain) {
        Iterator<Train> iterator = trainList.iterator();
        while (iterator.hasNext()) {
            Train train = iterator.next();
            if (train.getId().equals(inputTrain.getId())) {
                iterator.remove();
                return;
            }
        }
    }

    @Override
    public void print(PrintStream ps) {
        StringBuilder stringBuilder = new StringBuilder(id + "\t\t" + name + "\t\t");
        trainList.stream().forEach(e -> stringBuilder.append(e.getName()).append(","));
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
