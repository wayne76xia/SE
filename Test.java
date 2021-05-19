import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HaoHao
 * Created on 2021/5/19.
 */
public class Test {


    static final String TABLE_SEPARATOR = "&&&&";
    static final String LINE_SEPARATOR = "||||";
    static final String TABLE_TITLE = "####";
    static final String FIELD_SEPARATOR = "$$$$";
    static final String VALUE_SEPARATOR = "----";
    static final String ARRAY_SEPARATOR = ",,,,";

    /**
     * 按格式读写
     *
     * @param args
     */
    public static void main(String[] args) {
        String fileName = "data.txt";
        File file = new File(fileName);
        if (file.exists()) { // 启动时检测数据文件是否存在
            BufferedReader fr = null;
            try {
                fr = new BufferedReader(new FileReader(file));
                String data = fr.readLine();
                // 按格式分隔
                // 1. 分割表
                String[] tables = data.split(TABLE_SEPARATOR);
                // tables
                Map<String, ArrayList<DataItem>> tablesMap = new HashMap<>();
                // 2. 遍历表
                for (String table : tables) {
                    // 3. 分割行
                    String[] lines = table.split(LINE_SEPARATOR);
                    // 1 : id ...
                    Map<Integer, String> fieldMap = new HashMap<>();
                    // 1 : value_1
                    Map<Integer, ArrayList<String>> valuesMap = new HashMap<>();
                    // 4. 遍历行
                    for (int i = 0; i < lines.length; i++) {
                        // table_name
                        if (i == 0) {
                            // table_name is not null
                            String tableName = lines[i].split(TABLE_TITLE)[1];
                        }
                        // field_name
                        else if (i == 1) {
                            // field_name is not null
                            String[] fieldNames = lines[i].split(FIELD_SEPARATOR);
                            for (int j = 1; j < fieldNames.length; j++) {
                                fieldMap.put(j, fieldNames[j]);
                                valuesMap.put(j, new ArrayList<>());
                            }
                        }
                        // value
                        else {
                            String[] values = lines[i].split(VALUE_SEPARATOR);
                            // test value for array_value
                            for (int j = 1; j < values.length; j++) {
                                valuesMap.get(j).add(values[j]);
                            }
                        }
                    }
                    // 5. 创对象
                    // factory -> builder
                    //
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
