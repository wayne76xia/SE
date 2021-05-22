import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author HaoHao
 * Created on 2021/5/19.
 */
public class Main {

    /* ��д���� */
    static final String DATA_FILE_NAME = "data.txt";
    static final String TABLE_SEPARATOR = "&&&&";
    static final String LINE_SEPARATOR = "@@@@";
    static final String TABLE_TITLE = "####";
    static final String FIELD_SEPARATOR = "!!!!";
    static final String VALUE_SEPARATOR = "----";
    static final String ARRAY_SEPARATOR = ",,,,";
    /* ���ͳ��� */
    static final String CLASS_TYPE_TRAIN = "Train";// Train.class.getName()
    static final String CLASS_TYPE_TEACHER = "Teacher"; // Teacher.class.getName()
    static final String CLASS_TYPE_CLASSREQUIREMENT = "ClassRequirement";// ClassRequirement.class.getName()
    static final String CLASS_TYPE_SCHOOLCLASS = "SchoolClass";// SchoolClass.class.getName()
    /* ��ӡ���� */
    static final String PRINT_LINE_SEPARATOR = "��������������������������������������������������������������������������������";
    static final String PRINT_FIELD_SEPARATOR = "  |  ";
    static final String PRINT_TABLE_SEPARATOR = "�� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� �� ��";

    /* ���ݴ洢�� */
    public static Map<String, Map<Integer, DataItem>> tablesMap = new HashMap<>();
    public static Map<Integer, Set<Integer>> trainTeacherMap = new HashMap<>();
    /* ������������ӳ�� */
    public static Map<String, Class> classTypeMap = new HashMap<>();
    /* ���� */
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
     * ��ʼ����ȡ
     */
    public static void initRead() {
        File file = new File(DATA_FILE_NAME);
        if (file.exists()) { // ����ʱ��������ļ��Ƿ����
            BufferedReader fr = null;
            try {
                fr = new BufferedReader(new FileReader(file));
                String data = fr.readLine();
                // ����ʽ�ָ�
                // 1. �ָ��
                String[] tables = data.split(TABLE_SEPARATOR);
                // 2. ������
                for (String table : tables) {
                    // 3. �ָ���
                    String[] lines = table.split(LINE_SEPARATOR);
                    Map<Integer, String> fieldMap = new HashMap<>();
                    // table_name is not null
                    String tableName = lines[0].split(TABLE_TITLE)[1];
                    // field_name is not null
                    String[] fieldNames = lines[1].split(FIELD_SEPARATOR);
                    for (int j = 1; j < fieldNames.length; j++) {
                        fieldMap.put(j, fieldNames[j]);
                    }
                    // 4. ������ & ��������
                    Map<Integer, DataItem> objMap = new HashMap<>();
                    // ��������
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
                    // 5. ��ӵ�������
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
     * ���ݳ־û���д���ļ���
     * <p>
     * ��Ϊ���ڱ���������˴��˳����Ҫ���� Train -> Teacher -> ClassRequirement -> SchoolClass ��˳����д洢
     */
    public static void write() {
        String[] order = {CLASS_TYPE_TRAIN, CLASS_TYPE_TEACHER, CLASS_TYPE_CLASSREQUIREMENT, CLASS_TYPE_SCHOOLCLASS};
        StringBuilder target = new StringBuilder();
        Arrays.stream(order).forEach(type -> {
            StringBuilder stringBuilder = new StringBuilder();
            // 1. ƴ�ӱ�ͷ
            stringBuilder.append(TABLE_TITLE).append(type).append(LINE_SEPARATOR);
            // 2. ƴ������
            Class clazz = classTypeMap.get(type);
            for (Field field : clazz.getDeclaredFields()) {
                stringBuilder.append(FIELD_SEPARATOR).append(field.getName());
            }
            stringBuilder.append(LINE_SEPARATOR);
            // 3. ƴ��ֵ
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
     * ��ʾ����
     */
    public static void print() {
        PrintStream pw = new PrintStream(System.out);
        for (Map.Entry<String, Map<Integer, DataItem>> entry : tablesMap.entrySet()) {
            // 1. ��ӡ����
            String tableName = entry.getKey();
            pw.println(tableName);
            pw.println(PRINT_LINE_SEPARATOR);
            // 2. ƴ������
            Class clazz = classTypeMap.get(tableName);
            StringBuilder fieldStr = new StringBuilder();
            for (Field field : clazz.getDeclaredFields()) {
                fieldStr.append(field.getName()).append(PRINT_FIELD_SEPARATOR);
            }
            pw.println(fieldStr.substring(0, fieldStr.length() - PRINT_FIELD_SEPARATOR.length()));
            pw.println(PRINT_LINE_SEPARATOR);
            // 3. ��ӡֵ
            for (DataItem dataItem : entry.getValue().values()) {
                dataItem.print(pw);
                pw.println(); // ����
            }
            pw.println(PRINT_TABLE_SEPARATOR);
        }
    }

    /**
     * �����ƥ������
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
     * lab ƥ��
     */
    public static void match() {
        PrintStream pw = new PrintStream(System.out);
        pw.println("Start Matching!");
        for (DataItem dataItem : tablesMap.get(CLASS_TYPE_SCHOOLCLASS).values()) {
            SchoolClass schoolClass = (SchoolClass) dataItem;
            ArrayList<ClassRequirement> classRequirements = schoolClass.getList();
            for (int i = 0; i < classRequirements.size(); i++) {
                ClassRequirement classRequirement = classRequirements.get(i);
                pw.println("��ʼ��" + schoolClass.getName() + "�γ̵�" + classRequirement.getId() + "��Lab����ƥ��");
                // 1. ��������
                int targetNumber = classRequirement.getTeacherNumber();
                ArrayList<Train> needList = classRequirement.getList();
                Set<Integer> res = null;
                for (Train train : needList) { // �󽻼�
                    if (res == null) {
                        res = trainTeacherMap.get(train.getId());
                    } else {
                        res.retainAll(trainTeacherMap.get(train.getId()));
                    }
                }
                if (res != null && res.size() >= targetNumber) {
                    pw.println(schoolClass.getName() + "�γ̵�" + classRequirement.getId() + "��Lab���ƥ�䣬�������ѵ��");
                    pw.println("��ԱΪ: ");
                    for (Integer tId : res) {
                        tablesMap.get(CLASS_TYPE_TEACHER).get(tId).print(pw);
                        pw.println();
                    }
                } else { // 2. δ�������� -> ��Ҫ Train
                    pw.println(schoolClass.getName() + "�γ̵�" + classRequirement.getId() + "��Labδ���ƥ�䣬��Ҫ����ѵ��");
                    // train
                    targetNumber -= res.size();
                    // ���õ���ʦ����
                    Set<Integer> availableSet = new HashSet(tablesMap.get(CLASS_TYPE_TEACHER).keySet());
                    // ���Ѿ����ڵ���
                    availableSet.removeAll(res);
                    for (Integer tId : availableSet) {
                        Teacher teacher = (Teacher) tablesMap.get(CLASS_TYPE_TEACHER).get(tId);
                        HashSet<Train> needTrains = new HashSet<>(needList);
                        // ��Ҫ��ѵ���б�
                        needTrains.removeAll(teacher.getList());
                        for (Train train : needTrains) {
                            pw.println("Ϊ��ʦ" + teacher.getName() + "ѵ����" + train.getName());
                            teacher.Train(train);
                        }
                        res.add(teacher.getId());
                        targetNumber -= 1;
                        if (targetNumber == 0) {
                            break;
                        }
                    }
                    pw.println("��ԱΪ: ");
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
     * ͨ�������͹���������Ӧ���͵Ķ���
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
