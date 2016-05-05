package HW3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private final ArrayList<Table> tables;
    private final HashMap indices;
    private final long curPosition;
    private final DBIO io;

    
    public DBMS() {
        this.tables = new ArrayList<>();
        this.indices = new HashMap();
        this.curPosition = 0l;
        this.io = new DBIO();
    }    
    
    public void delete() {
        try {
            Files.delete(Paths.get("test.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            tables.add(t);
            indices.put(key, t);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
    }
    
    public int getHash(String key, String record) {
        return (key + record).hashCode();
    }
    
    public <E extends Comparable<? super E>> void insertRecord(String key, 
            E[] members) {
        // TODO: Check to see if members matches schema
        Table t = findTable(key);
        Record r = new Record(members);
        t.add(new Record(members));
        indices.put(getHash(key,r.toString()), curPosition);
    }
    
    public void findRecord(String key, E member) {
        Table t = findTable(key);
        if (t != null) {
            // The complexity of this is terribad.
            for (Object r : t.getRecords()) {
                Record rec = (Record) r;
                for (Object o : rec.getMembers()) {
                    if (o.hashCode() == member.hashCode()) {
                        Logger.getLogger(Record.class.getName()).log(Level.INFO, r.toString());
                    }
                }
            }
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.INFO, "Record not found.");
        }
    }
    
    public void readDB() {
        io.readDB(tables, indices);
    }
    
    public void writeDB() {
        io.writeDB(tables);
    }

    private Table findTable(String key) {
        return (Table) indices.get(key);
    }
    
}
