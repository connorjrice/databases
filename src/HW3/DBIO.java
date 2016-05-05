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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Connor
 */
public class DBIO<E> {
    private String tempKey = "";
    private static final Set<Byte> VALUES = new HashSet<>(Arrays.asList(DBMS.IND_END, DBMS.REL_END, DBMS.TAB_END, DBMS.TKEY_END));
    private RandomAccessFile dbfile, indfile; 

    private final HashMap<Integer, Long> index;
    private final HashMap<String, Integer> tableIndices; // Key, Index in indices
    private final ArrayList<Table> tables;
    private final HashMap<String, RandomAccessFile> rafs;
    private final HashMap<RandomAccessFile, Long> positions;
    
    public DBIO(String db, String ind)     {
        this.index = new HashMap<>();
        
        this.tableIndices = new HashMap<>();
        this.rafs = new HashMap<>();
        this.positions = new HashMap<>();
        tables = new ArrayList<>();
        try {
            this.dbfile = new RandomAccessFile(db, "rw");
            this.indfile = new RandomAccessFile(ind, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void write(String input, String file) {
        try {
            RandomAccessFile raf = rafs.get(file);
            long startpos = positions.get(raf);
            raf.seek(startpos);
            raf.writeUTF(input);
            updatePos(raf, getBytes(input));

        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private int getBytes(String input) {
        return input.toCharArray().length;
    }
    
    private void updatePos(RandomAccessFile raf, long offset) {
        positions.put(raf, positions.get(raf) + offset);
    }
            
    
    public void delete() {
        try {
            Files.delete(Paths.get("test.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void readDB() {

        try {
            dbfile.seek(0);
       
            StringBuilder sb = new StringBuilder();
            char c;
            while (dbfile.getFilePointer() < dbfile.length()) {
                byte[] bytes = dbfile.readLine().getBytes();
                int i = 0;
                while (!VALUES.contains(bytes[i]) && i < bytes.length-1){
                    sb.append(bytes[i]);
                    i++;
                }
                decide(sb.toString().trim(), parse(sb.toString().trim()));
                sb = new StringBuilder();
            }
            dbfile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void hashLookup(String key, E member) {
        System.out.println(index.get(getHash(key, member)));
    }
    
    private int getHash(String key, E member) {
        return (key + member.toString()).hashCode();
    }
    
    private int parse(String s) {
        if (s.charAt(0) == DBMS.TKEY_BEG && s.charAt(s.length()) == DBMS.TKEY_END) {
            return 0; // key
        } else if (s.charAt(0) == DBMS.TAB_BEG && s.charAt(s.length()) == DBMS.TAB_END) {
            return 1; // table
        } else if (s.charAt(0) == DBMS.REL_BEG && s.charAt(s.length()) == DBMS.REL_END) {
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
    
    public void writeIndex(HashMap indices) {
        try (RandomAccessFile raf = new RandomAccessFile("index.db", "rw")) {
            for(Object e : indices.entrySet()) {
                raf.writeChars(e.toString());
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
