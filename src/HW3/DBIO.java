package HW3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Connor
 */
public class DBIO<E> {
    private final String db, ind;

    // Integer = Hash, String = Long[].toString()
    private final HashMap<Integer, String> index;
    private final HashMap<String, RandomAccessFile> rafs;
    private final HashMap<String, Long> curPositions;
    
    private static final String TABLE_INNER = "(ΒΜ)|(ΝΓ)|(Η)|(,)";
    private static final String INDEX_INNER = "(Η)";
    private static final String RELATION_INNER = "(ΛΜ)|(ΝΕ)|(,)";
    
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final HashMap<Integer, Table> tables;    
    
    public DBIO(String db, String ind) {
        //delete();
        this.index = new HashMap<>();
        this.db = db;
        this.ind = ind;
        this.rafs = new HashMap<>();
        this.curPositions = new HashMap<>();
        this.tables = new HashMap<>();        
        initialize();
    }
    
    public DBIO(String db, String ind, boolean delete) {
            this.index = new HashMap<>();
            this.db = db;
            this.ind = ind;
            this.rafs = new HashMap<>();
            this.curPositions = new HashMap<>();
            this.tables = new HashMap<>();            
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
            curPositions.put(db, rafs.get(db).length());
            rafs.put(ind, new RandomAccessFile(ind, "rw"));
            curPositions.put(ind, rafs.get(ind).length());
            if (new File(db).exists()) {
                read(db);
            }
            if(new File(ind).exists()) {
                 read(ind);                
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
    private void read(String path) {
        try {
            RandomAccessFile file = rafs.get(path);
            if (file.length() > 0) {
                StringBuilder sb = new StringBuilder();
                char c;
                while (file.getFilePointer() < file.length()) {
                    while ((c=file.readChar()) != '\n') {
                        sb.append(c);
                    }
                    int type = parse(sb.toString());
                    decide(sb.toString(), type);
                    sb = new StringBuilder();
                }
            } else {
                Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE,
                        "{0} was empty!", path);
            }
       } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private Long[] parseIndexString(int hashCode) {
        String[] split = index.get(hashCode).split(",");
        return new Long[]{Long.parseLong(split[0]),Long.parseLong(split[1])};
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
            long dbstart = curPositions.get(db);
            dbfile.seek(dbstart);
            //String s = bytesToHex(input.getBytes());
            dbfile.writeChars(input);
            long dbdiff = dbfile.getFilePointer() - dbstart;
            index.put(getHash(primary, tablekey), (Long.toString(dbstart) + "," +
                Long.toString(dbdiff+dbstart)));

            
            RandomAccessFile indfile = rafs.get(ind);
            long indstart = curPositions.get(ind);
            indfile.seek(indstart);
            String inds = getIndUTF(primary,tablekey);
            indfile.writeChars(inds);
            long inddiff = indfile.getFilePointer() - indstart;
            
            updatePos(dbdiff, inddiff);
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addTable(Table t) {
        tables.put(getHash((E) t.getPrimary(), t.getTableKey()), t);
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
        sb.append(index.get(getHash(primary, tablekey)));
        sb.append(DBMS.IND_END).append('\n');
        return sb.toString();
    }
    
    /**
     * Returns the hash of the primary key and tablekey.
     * @param primary
     * @param tablekey
     * @return 
     */
    protected int getHash(E primary, String tablekey) {
        return (primary.toString() + tablekey).hashCode();
    }
    
    
    
    private int getHash(Table t) {
        return (t.getTableKey().hashCode());
    }
    
    private void updatePos(long dbOffset, long indOffset) {
        curPositions.put(db, curPositions.get(db) + dbOffset);
        curPositions.put(ind, curPositions.get(ind) + indOffset);
    }
    
    /**
     * Return a record from the db file.
     * @param primary
     * @param tablekey
     * @return 
     */
    public Record hashLookup(E primary, String tablekey) {
        return parseRelation(parseIndexString(getHash(primary,tablekey)));
    }
    
    private Record parseRelation(Long[] bounds) {
        Record r = null;
        try {
            RandomAccessFile file = rafs.get(db);
            file.seek(bounds[0]);
            StringBuilder sb = new StringBuilder();            
            char c;            
            while (file.getFilePointer() < bounds[1]) {
                while ((c=file.readChar()) != '\n') {
                    sb.append(c);                
                }
            }
            // Trim special chars and split
            String s = sb.toString().substring(1,sb.length()-1);
            String[] split = s.split(RELATION_INNER);
            int primaryindex = -1;
            boolean found = false;
            for (int i = 2; i < split.length; i++) {
                if (split[1].compareTo(split[i]) == 0 & !found) {
                    primaryindex = i-2;
                    found = true;
                }
            }
            r = new Record(Arrays.copyOfRange(split, 2, 5), split[0],primaryindex);
       } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return r;
    }
    
    private int parse(String s) {
        if (s.charAt(0) == DBMS.TKEY_BEG && s.indexOf(DBMS.TAB_END) > 0) {
            return 0; // Table
        } else if (s.charAt(0) == DBMS.RELT_BEG && s.indexOf(DBMS.REL_END) > 0) {
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
            // Relations are only parsed when they've been looked up through
            // the hash.
            //readRelation(s);
        } else if (i == 2) {
            readIndex(s);
        } else {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, 
                    "Parsing error: invalid decision value.");     
        }
    }
    
    public HashMap<E, Class<E>> getAttributes(String tablekey) {
        return tables.get(tablekey.hashCode()).getAttributes();
    }    
    
    private void readTable(String s) {
        s = s.substring(1, s.length()-1);
        String[] pairs = s.split(TABLE_INNER);
        HashMap<E, Class<E>> attributes = new HashMap();
        for (int i = 2; i < pairs.length-1; i+=2) {
            try {
                Class<E> type = (Class<E>) Class.forName(pairs[i+1]);
                attributes.put((E)pairs[i], type);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBMS.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        Table t = new Table(attributes, pairs[0], pairs[1]);
        tables.put(getHash(t), t);
    }
    
    private void readRelation(String s) {
        s = s.substring(1, s.length()-1);
    }
    
    private void readIndex(String s) {
        s = s.substring(1, s.length()-1); // trim special chars     
        String[] pair = s.split(INDEX_INNER);
        index.put(Integer.parseInt(pair[0]), pair[1]);
    }
    
    
    public Collection<Table> getTables() {
        return tables.values();
    }
    
    
}
