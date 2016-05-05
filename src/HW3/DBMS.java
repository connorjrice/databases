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
    public static final byte SEP = 6;
    public static final byte IND_BEG = 7;
    public static final byte IND_END = 8;
    public static final byte RELT_BEG = 9; // Relation table key beg
    public static final byte RELT_END = 10;
    public static final byte PRIME_BEG = 11;
    public static final byte PRIME_END = 12;
    

    private final DBIO io;
    
    private final String db,index;
    
    public DBMS(String db, String index) {
        this.db = db;
        this.index = index;
        this.io = new DBIO(db, index);   
    }
    /**
     * Create a new table.
     * @param attributes
     * @param key: key to find table
     */
    public <E extends Comparable<? super E>> void createTable(E[] values, 
            Class<E>[] types, String key) {
        if (values.length == types.length) {
            HashMap<E, Class<E>> attributes = new HashMap();
            for (int i = 0; i < values.length; i++) {
                attributes.put(values[i], types[i]);
            }
            Table t = new Table(attributes, key);
            io.write(t.toString(), "-1", key);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
    }
    
    public void getTable(String key) {
        
    }
    
    public int getHash(String key, String record) {
        return (key + record).hashCode();
    }
    
    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.write(r.toString(), r.getPrimary(), tablekey);
    }
    
    public <E extends Comparable<? super E>> void findRecord(String key) {
        //io.hashLookup(key);
    }
    
    public <E extends Comparable<? super E>> void modifyRecord() {
        
    }
    
    public <E extends Comparable<? super E>> void deleteRecord() {
        
    }
    
    public void readDB() {
        io.readIndices();
    }
    
}
