import java.io.PrintStream;

public class DataItem implements ShowDataItem {

    private String name;

    @Override
    public void print(PrintStream ps) {
        ps.print(name);
    }
}
