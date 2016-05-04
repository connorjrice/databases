/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;

import java.io.IOException;
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
    public final byte TAB_BEG = 5;  
    public final byte TAB_END = 6;
    public final byte TKEY_BEG = 2; // Table key
    public final byte TKEY_END = 3;
    public final byte REL_BEG = 4;
    public final byte REL_END = 5;

    private final ArrayList<Table> tables;
    private final HashMap index;
    
    
    public DBMS() {
        /*try {
            Files.delete(Paths.get("test.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        this.tables = new ArrayList<>();
        this.index = new HashMap();
    }    
    
     /**
     * Create a new table.
     * @param table: schema
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
            t.getRecords().stream().forEach((r) -> {
                for (Object o : r.getMembers()) {
                    if (o.hashCode() == member.hashCode()) {
                        Logger.getLogger(Record.class.getName()).log(Level.INFO, r.toString());
                    }
                }
            });
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.INFO, "Record not found.");
        }
        
    }
    
    private Table findTable(String key) {
        System.out.println(index.keySet());
        return (Table) index.get(key);
    }
    
}
