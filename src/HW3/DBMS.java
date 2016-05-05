package HW3;

import java.util.ArrayList;
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
    
    public  final HashMap<Character, Integer> symbols;

    private final DBIO io;
    
    
    private final String db,index;
    private ArrayList<Table> tables;
    
    public DBMS(String db, String index) {
        this.db = db;
        this.index = index;
        this.io = new DBIO(db, index);
        this.tables = new ArrayList<>();
        this.symbols = new HashMap<>();
        buildSymbols();
    }
    
    public DBMS() {
        this.db = "test.db";
        this.index = "index.db";
        this.io = new DBIO(db, index);
        this.tables = new ArrayList<>();
        this.symbols = new HashMap<>();
        buildSymbols();
    }
    
    private void buildSymbols() {
        symbols.put(TKEY_BEG, 0);
        symbols.put(TKEY_END, 1);
        symbols.put(TAB_BEG, 2);
        symbols.put(TAB_END, 3);
        symbols.put(REL_BEG, 4);
        symbols.put(REL_END, 5);
        symbols.put(SEP, 6);
        symbols.put(IND_BEG, 7);
        symbols.put(IND_END, 8);
        symbols.put(RELT_BEG, 9);
        symbols.put(RELT_END, 10);
        symbols.put(PRIME_BEG, 11);        
        symbols.put(PRIME_END, 12);                
    }
    
    public void buildIndices() {
     io.buildIndices();   
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
            tables.add(t);
            io.write(t.toString(), "TABLE", key);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
    }
    
    public void getTable(String key) {
        
    }
    

    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.write(r.toString(), r.getPrimary(), tablekey);
    }
    
    public <E extends Comparable<? super E>> void findRecord(E primarykey, String tablekey) {
        io.hashLookup(primarykey, tablekey);
    }
    
    public <E extends Comparable<? super E>> void modifyRecord() {
        
    }
    
    public <E extends Comparable<? super E>> void deleteRecord() {
        
    }
    
}
