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

    private final ArrayList<Table> tables;
    private final HashMap indices;
    
    private String tempKey = "";
    
    public DBMS() {
        this.tables = new ArrayList<>();
        this.indices = new HashMap();
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
        t.add(new Record(members));

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
        tables.clear();
        indices.clear();
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            raf.seek(0);
                StringBuilder sb = new StringBuilder();
                char c;
                while (raf.getFilePointer() < raf.length()) {
                    while ((c = raf.readChar()) != '\n'){
                        sb.append(c);
                    }
                    decide(sb.toString().trim(), parse(sb.toString().trim()));
                    sb = new StringBuilder();
                }
                raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int parse(String s) {
        if (s.startsWith("0") && s.endsWith("1")) {
            return 0; // key
        } else if (s.startsWith("2") && s.endsWith("3")) {
            return 1; // table
        } else if (s.startsWith("4") && s.endsWith("5")) {
            return 2; // relation
        } else {
            return -1;   
        }
    }

    private void decide(String s, int i) {
        if (i == 0) { 
            // s is a key, store it
            readKey(s);
        } else if (i == 1) { 
            readTable(s);
        } else if (i == 2) { // relation
            readRelation(s);
        } else {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, "Parsing error: invalid decision value.");     
        }
    }
    
    private void readKey(String s) {
        tempKey = s;
    }
    
    private void readTable(String s) {
        // Put a table together if we have a tempKey
            if (tempKey.length() > 0) { 
                // Fix our input so that Java doesn't throw ClassNotFound
                //String[] pairs = fixString(s);
                s = s.substring(1, s.length()-1);
                String[] pairs = s.split(",");
                HashMap<E, Class<E>> attributes = new HashMap();
                for (String d : pairs) {
                    String[] pair = d.split("6");
                    pair[1] = pair[1].trim();
                    try {
                        Class<E> type = (Class<E>) Class.forName(pair[1]);
                        attributes.put((E)pair[0], type);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(DBMS.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
                tempKey = "";
            } else {
                Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, "Parsing error: no temp key but found table.");
            }
    }
    
    private void readRelation(String s) {
        s = s.substring(1, s.length()-1);
    }
    
    
    public void writeDB() {
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            for (Table t : tables) {
                raf.writeChars(t.toString());
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Table findTable(String key) {
        return (Table) indices.get(key);
    }
    
}
