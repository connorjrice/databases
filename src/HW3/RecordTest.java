package HW3;

/**
 *
 * @author Connor
 */
public class RecordTest {
    
    public static void main(String[] args) {
        Record r = new Record();
        String table = "name,sex,age";
        r.createTable(table);
        r.insertRecord("connor,m,24");
        r.insertRecord("yuliya,f,23");
        r.insertRecord("boris,m,3");
        r.insertRecord("clara,f,3");        
        r.modifyRecord("yuliya,f,23", "yuliya,f,24");
        r.deleteRecord("connor,m,24");
        r.printTableBytes();
    }
    
}
