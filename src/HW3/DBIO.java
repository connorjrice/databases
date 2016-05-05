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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Connor
 */
public class DBIO<E> {
    private String tempKey = "";
    private static final Set<Character> VALUES = new HashSet<>(Arrays.asList(DBMS.IND_END, DBMS.REL_END, DBMS.TAB_END, DBMS.TKEY_END));
    private final String db, ind;

    private final HashMap<Integer, Long> index;
    private final HashMap<String, RandomAccessFile> rafs;
    private final HashMap<String, Long> positions;
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();     
    
    public DBIO(String db, String ind) {
        //delete();
        this.index = new HashMap<>();
        this.db = db;
        this.ind = ind;
        this.rafs = new HashMap<>();
        this.positions = new HashMap<>();

        try {
            rafs.put(db, new RandomAccessFile(db, "rw"));
            positions.put(db, rafs.get(db).length());
            rafs.put(ind, new RandomAccessFile(ind, "rw"));
            positions.put(ind, rafs.get(ind).length());
            buildIndices();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public DBIO() {
        //delete();
        this.index = new HashMap<>();
        this.db = "test.db";
        this.ind = "index.db";
        this.rafs = new HashMap<>();
        this.positions = new HashMap<>();

        try {
            rafs.put(db, new RandomAccessFile(db, "rw"));
            positions.put(db, rafs.get(db).length());
            rafs.put(ind, new RandomAccessFile(ind, "rw"));
            positions.put(ind, rafs.get(ind).length());
            buildIndices();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void buildIndices() {
        try {
            RandomAccessFile indfile = rafs.get(ind);
            if (indfile.length()> 0) {
                StringBuilder sb = new StringBuilder();
                char c;
                while (indfile.getFilePointer() < indfile.length()) {
                    while ((c=indfile.readChar()) != '\n') {
                        sb.append(c).append(" ");
                    }
                    System.out.println(sb.toString());
                    System.out.println(Arrays.toString(sb.toString().getBytes()));
                }
            } else {
                Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, "Index file was empty!");
            }
            
           
       } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * 
     * @param input the data to be written
     * @param primary primary key, -1 for tables
     * @param tablekey key of the table
     */
    public void write(String input, String primary, String tablekey) {
        try {
            RandomAccessFile dbfile = rafs.get(db);
            long dbstart = positions.get(db);
            dbfile.seek(dbstart);
            //String s = bytesToHex(input.getBytes());
            dbfile.writeChars(input);
            index.put(getHash(primary, tablekey), dbstart);
            long dbdiff = dbfile.getFilePointer() - dbstart;
            
            
            RandomAccessFile indfile = rafs.get(ind);
            long indstart = positions.get(ind);
            indfile.seek(indstart);
            String inds = getIndUTF(primary,tablekey);
            indfile.writeChars(inds);
            long inddiff = indfile.getFilePointer() - indstart;
            
            updatePos(dbdiff, inddiff);
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static String toHexString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
        
    }
    
    /**
     * I got this from StackOverflow
     * @param bytes
     * @return 
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
}        
    
    private String getIndUTF(String primary, String tablekey) {
        StringBuilder sb = new StringBuilder();
        sb.append(DBMS.IND_BEG);
        sb.append(getHash(primary, tablekey));
        sb.append(DBMS.SEP);
        sb.append(index.get(getHash(primary, tablekey)).toString());
        sb.append(DBMS.IND_END).append('\n');
        return sb.toString();
    }
    
    private int getHash(String primary, String tablekey) {
        return (primary + tablekey).hashCode();
    }
    
    
    private void updatePos(long dbOffset, long indOffset) {
        positions.put(db, positions.get(db) + dbOffset);
        positions.put(ind, positions.get(ind) + indOffset);
    }
            
    
    public void delete() {
        try {
            Files.delete(Paths.get("test.db"));
            Files.delete(Paths.get("index.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
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
