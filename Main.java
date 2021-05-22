import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author HaoHao
 * Created on 2021/5/19.
 */
public class Main {

    /* 读写常量 */
    static final String DATA_FILE_NAME = "data.txt";
    static final String TABLE_SEPARATOR = "&&&&";
    static final String LINE_SEPARATOR = "@@@@";
    static final String TABLE_TITLE = "####";
    static final String FIELD_SEPARATOR = "!!!!";
    static final String VALUE_SEPARATOR = "----";
    static final String ARRAY_SEPARATOR = ",,,,";
    /* 类型常量 */
    static final String CLASS_TYPE_TRAIN = "Train";// Train.class.getName()
    static final String CLASS_TYPE_TEACHER = "Teacher"; // Teacher.class.getName()
    static final String CLASS_TYPE_CLASSREQUIREMENT = "ClassRequirement";// ClassRequirement.class.getName()
    static final String CLASS_TYPE_SCHOOLCLASS = "SchoolClass";// SchoolClass.class.getName()
    /* 打印常量 */
    static final String PRINT_LINE_SEPARATOR = "————————————————————————————————————————";
    static final String PRINT_FIELD_SEPARATOR = "  |  ";
    static final String PRINT_TABLE_SEPARATOR = "— — — — — — — — — — — — — — — — — — — — — —";

    /* 数据存储区 */
    public static Map<String, Map<Integer, DataItem>> tablesMap = new HashMap<>();
    public static Map<Integer, Set<Integer>> trainTeacherMap = new HashMap<>();
    /* 类型与类名的映射 */
    public static Map<String, Class> classTypeMap = new HashMap<>();
    /* 工厂 */
    private static DataItemFactory factory = DataItemFactory.init();

    static {
        classTypeMap.put(CLASS_TYPE_TRAIN, Train.class);
        classTypeMap.put(CLASS_TYPE_TEACHER, Teacher.class);
        classTypeMap.put(CLASS_TYPE_CLASSREQUIREMENT, ClassRequirement.class);
        classTypeMap.put(CLASS_TYPE_SCHOOLCLASS, SchoolClass.class);
    }

    /**
     * Run Core
     *
     * @param args
     */
    public static void main(String[] args) {
        // init
        initRead();
        // print before modifying
        print();
        // process teacher for search
        processForTeacher();
        // match lab
        match();
        // print after modifying
        print();
        // write new data into file
        write();
    }

    /**
     * 初始化读取
     */
    public static void initRead() {
        File file = new File(DATA_FILE_NAME);
        if (file.exists()) { // 启动时检测数据文件是否存在
            BufferedReader fr = null;
            try {
                fr = new BufferedReader(new FileReader(file));
                String data = fr.readLine();
                // 按格式分隔
                // 1. 分割表
                String[] tables = data.split(TABLE_SEPARATOR);
                // 2. 遍历表
                for (String table : tables) {
                    // 3. 分割行
                    String[] lines = table.split(LINE_SEPARATOR);
                    Map<Integer, String> fieldMap = new HashMap<>();
                    // table_name is not null
                    String tableName = lines[0].split(TABLE_TITLE)[1];
                    // field_name is not null
                    String[] fieldNames = lines[1].split(FIELD_SEPARATOR);
                    for (int j = 1; j < fieldNames.length; j++) {
                        fieldMap.put(j, fieldNames[j]);
                    }
                    // 4. 遍历行 & 创建对象
                    Map<Integer, DataItem> objMap = new HashMap<>();
                    // 遍历对象
                    for (int i = 2; i < lines.length; i++) {
                        ArrayList<Object> objs = new ArrayList<>();
                        String[] values = lines[i].split(VALUE_SEPARATOR);
                        Integer id = null;
                        // test value for array_value
                        for (int j = 1; j < values.length; j++) {
                            if (values[j].contains(ARRAY_SEPARATOR)) {
                                String[] vArr = values[j].substring(0, values[j].length() - ARRAY_SEPARATOR.length())
                                        .split(ARRAY_SEPARATOR);
                                objs.add(vArr);
                            } else {
                                objs.add(values[j]);
                            }
                            if ("id".equals(fieldMap.get(j))) {
                                id = Integer.parseInt(values[j]);
                            }
                        }
                        objMap.put(id, createDataItem(tableName, objs.toArray()));
                    }
                    // 5. 添加到表集合中
                    tablesMap.put(tableName, objMap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数据持久化（写入文件）
     * <p>
     * 因为存在表依赖，因此存放顺序需要按照 Train -> Teacher -> ClassRequirement -> SchoolClass 的顺序进行存储
     */
    public static void write() {
        String[] order = {CLASS_TYPE_TRAIN, CLASS_TYPE_TEACHER, CLASS_TYPE_CLASSREQUIREMENT, CLASS_TYPE_SCHOOLCLASS};
        StringBuilder target = new StringBuilder();
        Arrays.stream(order).forEach(type -> {
            StringBuilder stringBuilder = new StringBuilder();
            // 1. 拼接表头
            stringBuilder.append(TABLE_TITLE).append(type).append(LINE_SEPARATOR);
            // 2. 拼接属性
            Class clazz = classTypeMap.get(type);
            for (Field field : clazz.getDeclaredFields()) {
                stringBuilder.append(FIELD_SEPARATOR).append(field.getName());
            }
            stringBuilder.append(LINE_SEPARATOR);
            // 3. 拼接值
            Map<Integer, DataItem> table = tablesMap.get(type);
            for (DataItem dataItem : table.values()) {
                switch (type) {
                    case CLASS_TYPE_TRAIN:
                        Train train = (Train) dataItem;
                        stringBuilder.append(VALUE_SEPARATOR).append(train.getId());
                        stringBuilder.append(VALUE_SEPARATOR).append(train.getName());
                        stringBuilder.append(LINE_SEPARATOR);
                        break;
                    case CLASS_TYPE_TEACHER:
                        Teacher teacher = (Teacher) dataItem;
                        stringBuilder.append(VALUE_SEPARATOR).append(teacher.getId());
                        stringBuilder.append(VALUE_SEPARATOR).append(teacher.getName());
                        StringBuilder tempTea = new StringBuilder();
                        for (Train t : teacher.getList()) {
                            tempTea.append(t.getId()).append(ARRAY_SEPARATOR);
                        }
                        stringBuilder.append(VALUE_SEPARATOR)
                                .append(tempTea.toString())
                                .append(LINE_SEPARATOR);
                        break;
                    case CLASS_TYPE_CLASSREQUIREMENT:
                        ClassRequirement classRequirement = (ClassRequirement) dataItem;
                        stringBuilder.append(VALUE_SEPARATOR).append(classRequirement.getId());
                        stringBuilder.append(VALUE_SEPARATOR).append(classRequirement.getTeacherNumber());
                        StringBuilder tempCr = new StringBuilder();
                        for (Train t : classRequirement.getList()) {
                            tempCr.append(t.getId()).append(ARRAY_SEPARATOR);
                        }
                        stringBuilder.append(VALUE_SEPARATOR)
                                .append(tempCr.toString())
                                .append(LINE_SEPARATOR);
                        break;
                    case CLASS_TYPE_SCHOOLCLASS:
                        SchoolClass schoolClass = (SchoolClass) dataItem;
                        stringBuilder.append(VALUE_SEPARATOR).append(schoolClass.getId());
                        stringBuilder.append(VALUE_SEPARATOR).append(schoolClass.getName());
                        StringBuilder tempSc = new StringBuilder();
                        for (ClassRequirement c : schoolClass.getList()) {
                            tempSc.append(c.getId()).append(ARRAY_SEPARATOR);
                        }
                        stringBuilder.append(VALUE_SEPARATOR)
                                .append(tempSc.toString())
                                .append(LINE_SEPARATOR);
                        break;
                    default:
                }
            }
            target.append(stringBuilder.substring(0, stringBuilder.length() - LINE_SEPARATOR.length()));
            target.append(TABLE_SEPARATOR);
        });
        File file = new File(DATA_FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file));
            bfw.write(target.substring(0, target.length() - TABLE_SEPARATOR.length()));
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示数据
     */
    public static void print() {
        PrintStream pw = new PrintStream(System.out);
        for (Map.Entry<String, Map<Integer, DataItem>> entry : tablesMap.entrySet()) {
            // 1. 打印表名
            String tableName = entry.getKey();
            pw.println(tableName);
            pw.println(PRINT_LINE_SEPARATOR);
            // 2. 拼接属性
            Class clazz = classTypeMap.get(tableName);
            StringBuilder fieldStr = new StringBuilder();
            for (Field field : clazz.getDeclaredFields()) {
                fieldStr.append(field.getName()).append(PRINT_FIELD_SEPARATOR);
            }
            pw.println(fieldStr.substring(0, fieldStr.length() - PRINT_FIELD_SEPARATOR.length()));
            pw.println(PRINT_LINE_SEPARATOR);
            // 3. 打印值
            for (DataItem dataItem : entry.getValue().values()) {
                dataItem.print(pw);
                pw.println(); // 换行
            }
            pw.println(PRINT_TABLE_SEPARATOR);
        }
    }

    /**
     * 处理待匹配数据
     */
    private static void processForTeacher() {
        for (DataItem dataItem : tablesMap.get(CLASS_TYPE_TRAIN).values()) {
            Train train = (Train) dataItem;
            trainTeacherMap.put(train.getId(), new HashSet<>());
        }
        for (DataItem dataItem : tablesMap.get(CLASS_TYPE_TEACHER).values()) {
            Teacher teacher = (Teacher) dataItem;
            for (Train t : teacher.getList()) {
                trainTeacherMap.get(t.getId()).add(teacher.getId());
            }
        }
    }

    /**
     * lab 匹配
     */
    public static void match() {
        PrintStream pw = new PrintStream(System.out);
        pw.println("Start Matching!");
        for (DataItem dataItem : tablesMap.get(CLASS_TYPE_SCHOOLCLASS).values()) {
            SchoolClass schoolClass = (SchoolClass) dataItem;
            ArrayList<ClassRequirement> classRequirements = schoolClass.getList();
            for (int i = 0; i < classRequirements.size(); i++) {
                ClassRequirement classRequirement = classRequirements.get(i);
                pw.println("开始对" + schoolClass.getName() + "课程的" + classRequirement.getId() + "号Lab进行匹配");
                // 1. 满足条件
                int targetNumber = classRequirement.getTeacherNumber();
                ArrayList<Train> needList = classRequirement.getList();
                Set<Integer> res = null;
                for (Train train : needList) { // 求交集
                    if (res == null) {
                        res = trainTeacherMap.get(train.getId());
                    } else {
                        res.retainAll(trainTeacherMap.get(train.getId()));
                    }
                }
                if (res != null && res.size() >= targetNumber) {
                    pw.println(schoolClass.getName() + "课程的" + classRequirement.getId() + "号Lab完成匹配，不需进行训练");
                    pw.println("人员为: ");
                    for (Integer tId : res) {
                        tablesMap.get(CLASS_TYPE_TEACHER).get(tId).print(pw);
                        pw.println();
                    }
                } else { // 2. 未满足条件 -> 需要 Train
                    pw.println(schoolClass.getName() + "课程的" + classRequirement.getId() + "号Lab未完成匹配，需要进行训练");
                    // train
                    targetNumber -= res.size();
                    // 可用的老师集合
                    Set<Integer> availableSet = new HashSet(tablesMap.get(CLASS_TYPE_TEACHER).keySet());
                    // 与已经存在的求差集
                    availableSet.removeAll(res);
                    for (Integer tId : availableSet) {
                        Teacher teacher = (Teacher) tablesMap.get(CLASS_TYPE_TEACHER).get(tId);
                        HashSet<Train> needTrains = new HashSet<>(needList);
                        // 需要的训练列表
                        needTrains.removeAll(teacher.getList());
                        for (Train train : needTrains) {
                            pw.println("为老师" + teacher.getName() + "训练了" + train.getName());
                            teacher.Train(train);
                        }
                        res.add(teacher.getId());
                        targetNumber -= 1;
                        if (targetNumber == 0) {
                            break;
                        }
                    }
                    pw.println("人员为: ");
                    for (Integer tId : res) {
                        tablesMap.get(CLASS_TYPE_TEACHER).get(tId).print(pw);
                        pw.println();
                    }
                }
                pw.println(PRINT_LINE_SEPARATOR);
            }
            pw.println(PRINT_TABLE_SEPARATOR);
        }
        pw.println("Match Ending!");
        pw.println(PRINT_TABLE_SEPARATOR);
    }

    /**
     * 通过表名和工厂创建对应类型的对象
     *
     * @param type
     * @param arr
     * @return
     */
    public static DataItem createDataItem(String type, Object... arr) {
        switch (type) {
            case CLASS_TYPE_TEACHER:
                return factory.createTeacher(arr[0], arr[1], (String[]) arr[2]);
            case CLASS_TYPE_CLASSREQUIREMENT:
                return factory.createClassRequirement(arr[0], arr[1], (String[]) arr[2]);
            case CLASS_TYPE_SCHOOLCLASS:
                return factory.createSchoolClass(arr[0], arr[1], (String[]) arr[2]);
            case CLASS_TYPE_TRAIN:
                return factory.createTrain(arr[0], arr[1]);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
