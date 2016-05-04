/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.StringUtils;



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
    private final HashMap index;
    
    private String tempKey = "";
    
    public DBMS() {
        this.tables = new ArrayList<>();
        this.index = new HashMap();
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
            index.put(key, t);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
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
        index.clear();
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                String s = raf.readLine().trim();
                decide(s, parse(s));
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int parse(String s) {
/*        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            System.out.println(b);
        }*/if (s.startsWith("0") && s.endsWith("1")) {
            return 0; // key
        } else if (s.startsWith("2") && s.endsWith("3")) {
            return 1; // table
        } else if (s.startsWith("4") && s.endsWith("5")) {
            return 2; // relation
        } else {
            return -1;   
        }
    }
    
    /**
     * Remember: we are being passed the string without special chars.
     * @param s
     * @param i 
     */
    private void decide(String s, int i) {
        if (i == 0) { 
            // s is a key, store it
            tempKey = s;
        } else if (i == 1) { 
            // Put a table together if we have a tempKey
            if (tempKey.length() > 0) { 
                // Fix our input so that Java doesn't throw ClassNotFound
                String[] pairs = fixString(s);
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
        } else if (i == 2) { // relation
            
        } else {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, "Parsing error: invalid decision value.");
        }
    }
    
    private String[] fixString(String s) {
        String fixed = s.trim();                
        fixed = fixed.substring(1,fixed.length()-1);
        byte[] parsed = StringUtils.getBytesUtf8(fixed);
        return StringUtils.newStringUtf8(parsed).split(","); 
    }
    
    
    private String readKey(String s) {
        if (s.startsWith("2") && s.endsWith("3")) {
            return s.substring(1,s.length()-1);
        }
        else {
            return "nokey";
        }
    }
    
    
    public void writeDB() {
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            for (Table t : tables) {
                String s = t.toString();
                for (char c : s.toCharArray()) {
                    raf.write(c);
                }
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Table findTable(String key) {
        return (Table) index.get(key);
    }
    
}
