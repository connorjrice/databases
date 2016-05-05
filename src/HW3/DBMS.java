package HW3;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Connor
 */
public class DBMS<E> {
    // These are all used for enclosing data
    public static final byte TKEY_BEG = 0; // Table key
    public static final byte TKEY_END = 1;
    public static final byte TAB_BEG = 2;  
    public static final byte TAB_END = 3;
    public static final byte REL_BEG = 4;
    public static final byte REL_END = 5;
    public static final byte TYPE_SEP = 6;
    public static final byte IND_BEG = 7;
    public static final byte IND_END = 8;
    public static final byte PNT_ORG = 9; // Pointer origin
    public static final byte PNT_DST = 10;// Pointer dest

    private final DBIO io;
    
    private final String db,index;
    
    public DBMS(String db, String index) {
        this.db = db;
        this.index = index;
        this.io = new DBIO(db, index);   
    }
    
    public void delete() {
        io.delete();
    }
    
    
     /**
     * Create a new table.
     * @param attributes
     * @param key: key to find table
     */
    public void createTable(E[] values, Class<E>[] types, String key) {
        if (values.length == types.length) {
            HashMap<E, Class<E>> attributes = new HashMap();
            for (int i = 0; i < values.length; i++) {
                attributes.put(values[i], types[i]);
            }
            Table t = new Table(attributes, key);
            io.createTable(t);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
    }
    
    public int getHash(String key, String record) {
        return (key + record).hashCode();
    }
    
    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.insertRecord(r);
    }

    
    private long getBytes(Object o) {
        return (o.toString().toCharArray().length) + 2;
    }
    
    private long getBytes(Object o1, Object o2) {
        return (o1.toString().toCharArray().length + o2.toString().toCharArray().length) + 2;
    }
    
    public void findRecord(String key, E member) {

    }
    
    public void readDB() {
        io.readDB();
    }
    
    public void write() {
        
    }
    
    public void writeDB() {
        io.writeDB(tableIndices);
        io.writeIndex(index, tableIndices);
    }


    
}
