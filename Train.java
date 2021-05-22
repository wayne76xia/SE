import java.io.PrintStream;
import java.util.Objects;

public class Train extends DataItem {
    private Integer id;
    private String name;

    public Train() {
    }

    public Train(Integer id, String inputName) {
        this.id = id;
        this.name = inputName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String inputName) {
        this.name = inputName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Train)) {
            return false;
        }
        Train train = (Train) o;
        return id.equals(train.id) && name.equals(train.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public void print(PrintStream ps) {
        ps.print(id + "\t\t" + name);
    }
}
