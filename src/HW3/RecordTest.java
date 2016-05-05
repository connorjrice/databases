package HW3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Connor
 */
public class RecordTest {
    
    public static void main(String[] args) {
        //DBMS d = new DBMS("test.db", "index.db");
        if (args[0].compareTo("read") == 0) {
            read();
        } else if (args[0].compareTo("write") == 0) {
            write();
        } else if (args[0].compareTo("delete") == 0) {
            delete();
        } else if (args[0].compareTo("full") == 0) {
            delete();
            write();
            read();
        }
        
    }
    
    public static <E extends Comparable> void write() {        
        DBMS d = new DBMS();
        String[] values = {"name","sex","age"};
        Class<?>[] types = {String.class, String.class, Integer.class};
        d.createTable(values, types, "info", 0);
        String[] record = {"connor", "m", "23"};
        d.insertRecord("info", record, 0);
        record = new String[]{"boris", "m", "2"};
        d.insertRecord("info", record, 0);
        record = new String[]{"clara", "f", "2"};
        d.insertRecord("info", record, 0);
        record = new String[]{"yuliya", "f", "23"};        
        d.insertRecord("info", record, 0);        
        
        /*d.createTable(values, types, "secrets", 0);
        record = new String[]{"connor", "m", "23", "cheese"};
        
        d.insertRecord("info", record, 3);
        record = new String[]{"boris", "m", "2", "wires"};
        d.insertRecord("info", record, 0);
        record = new String[]{"clara", "f", "2", "moths"};
        d.insertRecord("info", record, 0);
        record = new String[]{"yuliya", "f", "23", "ramen"};        
        d.insertRecord("info", record, 0);    */
    }
    
    public static void read() {
        DBMS d = new DBMS("test.db", "index.db");
        d.showTables();
        d.findRecord("connor", "info");
        //System.out.println(d.deleteRecord("connor", "info"));
        //d.findRecord("connor", "info");                
        //d.findRecord("boris", "info");

        //d.readDB();
        //d.findRecord("info", "connor");
    }
    
   
    public static void delete() {
        try {
            Files.delete(Paths.get("test.db"));
            Files.delete(Paths.get("index.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }        

}
