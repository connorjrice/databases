package HW3;

/**
 *
 * @author Connor
 */
public class RecordTest {
    
    public static void main(String[] args) {
        DBMS d = new DBMS();
        String table = "name,sex,age";
        d.createTable(table, "info");
        d.insertRecord("connor,m,24");
        d.findRecord("connor,m,24", "info");
        /*d.insertRecord("yuliya,f,23");
        d.insertRecord("boris,m,3");
        d.insertRecord("clara,f,3"); 
        d.findRecord("connor,m,24", "info");
        //r.modifyRecord("yuliya,f,23", "yuliya,f,24");
        //r.deleteRecord("connor,m,24");
        d.printTableBytes();*/
    }
    
}
