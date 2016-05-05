package HW3;

/**
 *
 * @author Connor
 */
public class RecordTest {
    
    public static void main(String[] args) {
        DBMS d = new DBMS("test.db", "index.db");
        write(d);
        read(d);
    }
    
    public static <E extends Comparable> void write(DBMS d) {        
        //d.delete();
        String[] values = {"name","sex","age"};
        Class<?>[] types = {String.class, String.class, String.class};
        d.createTable(values, types, "info");
        String[] record = {"connor", "m", "23"};
        d.insertRecord("info", record, 1);
        //d.findRecord("info", "connor");
        //d.writeDB();
    }
    
    public static void read(DBMS d) {

        d.readDB();
        //d.findRecord("info", "connor");
    }
    
    public static void hw(DBMS d) {

        String[] table = {"name","sex","age"};
//        d.createTable(table, "info");
        String[] record = {"connor", "m", "23"};
        d.insertRecord("info", record, 1);
        //d.findRecord("info", "connor");

        /*d.insertRecord("yuliya,f,23");
        d.insertRecord("boris,m,3");
        d.insertRecord("clara,f,3"); 
        d.findRecord("connor,m,24", "info");
        //r.modifyRecord("yuliya,f,23", "yuliya,f,24");
        //r.deleteRecord("connor,m,24");
        d.printTableBytes();*/
    }
    
}
