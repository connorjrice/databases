/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public static final byte TAB_BEG = 5;  
    public static final byte TAB_END = 6;
    public static final byte TKEY_BEG = 2; // Table key
    public static final byte TKEY_END = 3;
    public static final byte REL_BEG = 4;
    public static final byte REL_END = 5;
    public static final byte TYPE_SEP = 8;

    private final ArrayList<Table> tables;
    private final HashMap index;
    
    private String tempKey = "";
    
    public DBMS() {
       try {
            Files.delete(Paths.get("test.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.tables = new ArrayList<>();
        this.index = new HashMap();
    }    
    
     /**
     * Create a new table.
     * @param attributes
     * @param key: key to find table
     */
    public void createTable(String[] attributes, String key) {
        StringBuilder sb = new StringBuilder();
        Table t = new Table(attributes, key);
        tables.add(t);
        index.put(key, t);
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
            while (raf.getFilePointer() < raf.length()) {
                String s = raf.readLine();
                decide(s.substring(1,s.length()-1), parse(s));
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int parse(String s) {
        if (s.startsWith("2") && s.endsWith("3")) {
            return 0; // key
        } else if (s.startsWith("4") && s.endsWith("5")) {
            return 1; // table
        } else if (s.startsWith("6") && s.endsWith("7")) {
            return 2; // relation
        } else {
            return -1;   
        }
    }
    
    private void decide(String s, int i) {
        if (i == 0) { // key
            tempKey = s;
        } else if (i == 1) { // table
            if (tempKey.length() > 0) {
                
            } else {
                Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, "Parsing error: no temp key but found table.");
            }
        } else if (i == 2) { // relation
            
        } else {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, "Parsing error: invalid decision value.");
        }
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
                    raf.writeChar(c);
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
