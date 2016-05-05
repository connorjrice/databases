package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Connor
 */
public class DBMS<E> {
    // These are all used for enclosing data
    public static final char TKEY_BEG = 'Α'; // Table key
    public static final char TKEY_END = 'Β';
    public static final char TAB_BEG = 'Γ';  
    public static final char TAB_END = 'Δ';
    public static final char REL_BEG = 'Ε';
    public static final char REL_END = 'Ζ';
    public static final char SEP = 'Η';
    public static final char IND_BEG = 'Θ';
    public static final char IND_END = 'Ι';
    public static final char RELT_BEG = 'Κ'; // Relation table key beg
    public static final char RELT_END = 'Λ';
    public static final char PRIME_BEG = 'Μ';
    public static final char PRIME_END = 'Ν';
    public static final String TABLE = "TABLE";
    public static final char BAD_BAD_BAD = 'ѯ';
    public static final char SNEAKY_KEY = 'ϻ'; // Stuck into hashmaps for tables
    private final DBIO io;                     // Represents table key
    
    private final String db,index;

    
    public DBMS(String db, String index) {
        this.db = db;
        this.index = index;
        this.io = new DBIO(db, index);
    }
    
    public DBMS() {
        this.db = "test.db";
        this.index = "index.db";
        this.io = new DBIO(db, index);
    }
 
    /**
     * Create a new table.
     * @param attributes
     * @param key: key to find table
     */
    public <E extends Comparable<? super E>> void createTable(E[] values, 
            Class<E>[] types, String key, int primaryindex) {
        if (values.length == types.length) {
            HashMap<E, Class<E>> attributes = new HashMap();
            for (int i = 0; i < values.length; i++) {
                attributes.put(values[i], types[i]);
            }
            Table t = new Table(attributes, key, values[primaryindex].toString());
            io.addTable(t);
            io.write(t.toString(), t.getTableKey(), key, true);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
    }
    
    public void showTables() {
        for (Table t : ((Collection<Table>) io.getTables())) {
            System.out.println(t.toStringPretty(t.getAttributes(), null, t.getTableKey()));
        }
    }
    
    
    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.write(r.toString(), r.getPrimary(), tablekey, true);
    }
    
    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex, boolean db) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.write(r.toString(), r.getPrimary(), tablekey, db);
    }    
    
    public <E extends Comparable<? super E>> void findRecord(E primarykey, String tablekey) {
        Record r = io.hashLookup(primarykey, tablekey);
        HashMap<E, Class<E>> attributes = io.getAttributes(r.getTableKey());
        System.out.println(r.toStringPretty(attributes, r.getMembers()));
    }
    
    public <E extends Comparable<? super E>> void modifyRecord(E primarykey, String tablekey, E[] members) {
        Record r = io.hashLookup(primarykey, tablekey);
        // Check bytes
        if (Arrays.toString(r.getMembers()).getBytes().length == 
                Arrays.toString(members).getBytes().length) {
            io.write(r.toString(), r.getPrimary(), tablekey, false);
        } else {
            deleteRecord(primarykey, tablekey);
            insertRecord(tablekey, members, r.getPrimaryIndex(), false);
        }
    }
    

    
    public <E extends Comparable<? super E>> String deleteRecord(E primarykey, String tablekey) {
        return io.deleteRecord(primarykey, tablekey);
    }
    
}
