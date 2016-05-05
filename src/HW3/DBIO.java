/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;

import java.io.File;
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
    private static final Set<Character> VALUES = new HashSet<>(Arrays.
            asList(DBMS.IND_END, DBMS.REL_END, DBMS.TAB_END, DBMS.TKEY_END));
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
        initialize();
    }
    
    public DBIO(String db, String ind, boolean delete) {
            this.index = new HashMap<>();
            this.db = db;
            this.ind = ind;
            this.rafs = new HashMap<>();
            this.positions = new HashMap<>();
            if (delete) {
                try {
                    Files.delete(Paths.get("test.db"));
                    Files.delete(Paths.get("index.db"));
                } catch (IOException ex) {
                    Logger.getLogger(Record.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }            
            initialize();
    }
        
    private void initialize() {
        try {

            rafs.put(db, new RandomAccessFile(db, "rw"));
            positions.put(db, rafs.get(db).length());
            rafs.put(ind, new RandomAccessFile(ind, "rw"));
            positions.put(ind, rafs.get(ind).length());
            if (new File(db).exists()) {
                parseDatabase();
            }
            if(new File(ind).exists()) {
                 parseIndices();                
            }            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Parses the index file and builds the HashMap.
     */
    private void parseIndices() {
        try {
            RandomAccessFile indfile = rafs.get(ind);
            if (indfile.length() > 0) {
                StringBuilder sb = new StringBuilder();
                char c;
                while (indfile.getFilePointer() < indfile.length()) {
                    while ((c=indfile.readChar()) != '\n') {
                        sb.append(c);
                    }
                    int type = parse(sb.toString());
                    decide(sb.toString(), type);
                    sb = new StringBuilder();
                }
            } else {
                Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE,
                        "Index file was empty!");
            }
            
           
       } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void parseDatabase() {
        try {
            RandomAccessFile dbfile = rafs.get(db);
            if (dbfile.length() > 0) {
                StringBuilder sb = new StringBuilder();
                char c;
                while (dbfile.getFilePointer() < dbfile.length()) {
                    while ((c=dbfile.readChar()) != '\n') {
                        sb.append(c);
                    }
                    int type = parse(sb.toString());
                    decide(sb.toString(), type);
                    sb = new StringBuilder();
                }
            } else {
                Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE,
                        "Database file was empty!");
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
    public void write(String input, E primary, String tablekey) {
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
    
    private String getIndUTF(E primary, String tablekey) {
        StringBuilder sb = new StringBuilder();
        sb.append(DBMS.IND_BEG);
        sb.append(getHash(primary, tablekey));
        sb.append(DBMS.SEP);
        sb.append(index.get(getHash(primary, tablekey)).toString());
        sb.append(DBMS.IND_END).append('\n');
        return sb.toString();
    }
    
    private int getHash(E primary, String tablekey) {
        return (primary.toString() + tablekey).hashCode();
    }
    
    
    private void updatePos(long dbOffset, long indOffset) {
        positions.put(db, positions.get(db) + dbOffset);
        positions.put(ind, positions.get(ind) + indOffset);
    }
            
    
    
    
    public void hashLookup(E primary, String tablekey) {
        System.out.println(index.get(getHash(primary, tablekey)));
    }
    
    private int getHash(String key, E member) {
        return (key + member.toString()).hashCode();
    }
    
    private int parse(String s) {
        if (s.charAt(0) == DBMS.TKEY_BEG && s.indexOf(DBMS.TAB_END) > 0) {
            return 0; // Table
        } else if (s.charAt(0) == DBMS.REL_BEG && s.indexOf(DBMS.REL_END) > 0) {
            return 1; // relation
        } else if (s.charAt(0) == DBMS.IND_BEG && s.indexOf(DBMS.IND_END) > 0) {
            return 2; // index
        } else {
            return -1;
        }
    }

    private void decide(String s, int i) {
        if (i == 0) {
            readTable(s);            
        } else if (i == 1) { 
            readRelation(s);
        } else if (i == 2) {
            readIndex(s);
        } else {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, 
                    "Parsing error: invalid decision value.");     
        }
    }
    
    private void readTable(String s) {
        s = s.substring(1, s.length()-1);
        String[] pairs = s.split(",");
        HashMap<E, Class<E>> attributes = new HashMap();
        /*for (String d : pairs) {
            String[] pair = d.split("6");
            pair[1] = pair[1].trim();
            try {
                Class<E> type = (Class<E>) Class.forName(pair[1]);
                attributes.put((E)pair[0], type);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBMS.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }*/
    }
    
    private void readRelation(String s) {
        s = s.substring(1, s.length()-1);
    }
    
    private void readIndex(String s) {
        s = s.substring(1, s.length()-1); // trim special chars     
        String sep = " " + DBMS.SEP;
        sep = sep.substring(1);
        String[] pair = s.split(sep);
        index.put(Integer.parseInt(pair[0]), Long.parseLong(pair[1]));
    }
    
    
}
