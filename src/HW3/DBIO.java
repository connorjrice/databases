package HW3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 * Maybe extend data too?
 * @author Connor
 */
public class DBIO<E> extends Data {
    private final String db, ind;

    // Integer = Hash, String = Long[].toString()
    private final HashMap<Integer, String> index;
    private final HashMap<String, RandomAccessFile> rafs;
    private final HashMap<String, Long> curPositions;
    
    private static final String TABLE_SPLIT = "(ΒΜ)|(ΝΓ)|(Η)|(,)";
    private static final String INDEX_SPLIT = "(Η)";
    private static final String RELATION_SPLIT = "(ΛΜ)|(ΝΕ)|(,)";
    
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
     * Parses file given my path and decides what to do with input.
     */
    private void read(String path) {
        try {
            RandomAccessFile file = rafs.get(path);
            if (file.length() > 0) {
                String s = new String(fromHex(file.readLine()));
                for (String parsed : s.split("\\R")) {
                    int type = parse(parsed);
                    decide(parsed, type);
                }

            } else {
                Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE,
                        "{0} was empty!", path);
            }            
       } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    
    /*
     * Return a record from the db file.
     * @param primary
     * @param tablekey
     * @return 
     */
    public Record hashLookup(E primary, String tablekey) {
        return getRecordFromHash(parseIndexString(getHash(primary,tablekey)));
    }    
    
    /**
     * De-hex, lookup, return.
     * @param bounds[0] = startPos, bounds[1] = endPos
     * @return 
     */
    private Record getRecordFromHash(Long[] bounds) {
        Record r = null;
        try {
            RandomAccessFile file = rafs.get(db);
            file.seek(bounds[0]);
            byte[] hexbytes = new byte[Math.toIntExact(bounds[1]-bounds[0])];
            int i = 0;
            while (file.getFilePointer() < bounds[1]) {
                hexbytes[i] = file.readByte();
                i++;
            }
            String hex = new String(hexbytes);
            String converted = new String(fromHex(hex));
            System.out.println(converted);
            
            String s = ""         ;

            // Trim special chars and split
            //String s = sb.toString().substring(1,sb.length()-1);
            String[] split = s.split(RELATION_SPLIT);
            int primaryindex = -1;
            boolean found = false;
            for (int j = 2; j < split.length; j++) {
                if (split[1].compareTo(split[j]) == 0 & !found) {
                    primaryindex = j-2;
                    found = true;
                }
            }
            r = new Record(Arrays.copyOfRange(split, 2, 5), split[0],
                    primaryindex);
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArithmeticException ae) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, 
                    "HOLY COW THAT WAS A BIG ENTRY!", ae);            

        }
       
        return r;        
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
    public void write(String input, E primary, String tablekey, boolean update) {
        try {
            RandomAccessFile dbfile = rafs.get(db);
            long dbstart = curPositions.get(db);
            dbfile.seek(dbstart);
            dbfile.writeBytes(toHex(input));
            long dbdiff = dbfile.getFilePointer() - dbstart;
            index.put(getHash(primary, tablekey), (Long.toString(dbstart) + "," +
                Long.toString(dbdiff+dbstart)));

            
            RandomAccessFile indfile = rafs.get(ind);
            long indstart = curPositions.get(ind);
            indfile.seek(indstart);
            indfile.writeBytes(getInd(primary, tablekey));

            long inddiff = indfile.getFilePointer() - indstart;
            
            if (update) {
                updatePos(dbdiff, inddiff);
            }

        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addTable(Table t) {
        tables.put(getHash((E) t.getPrimary(), t.getTableKey()), t);
        write(t.toString(), (E) t.getPrimary(), t.getTableKey(), true);
    }
    
    private String getInd(E primary, String tablekey) {
        StringBuilder sb = new StringBuilder();
        sb.append(DBMS.IND_BEG);
        sb.append(getHash(primary, tablekey));
        sb.append(DBMS.SEP);
        sb.append(index.get(getHash(primary, tablekey)));
        sb.append(DBMS.IND_END).append('\n');
        return toHex(sb.toString());
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

    
    protected String deleteRecord(E primary, String tablekey) {
        Long[] bounds = parseIndexString(getHash(primary, tablekey));
        
        RandomAccessFile raf = rafs.get(db);
        try {
            raf.seek(bounds[0]);
            while (raf.getFilePointer() < bounds[1]) {
                raf.writeChar(DBMS.BAD_BAD_BAD);
            }
        } catch (IOException ex) {
            Logger.getLogger(DBIO.class.getName()).log(Level.SEVERE, null, ex);
            return "Failure";
        }
        return "Success.";

    }
    

    private void updatePos(long dbOffset, long indOffset) {
        curPositions.put(db, curPositions.get(db) + dbOffset);
        curPositions.put(ind, curPositions.get(ind) + indOffset);
    }
    


    
    public HashMap<E, Class<E>> getAttributes(String tablekey) {
        try {
            HashMap<E, Class<E>> attributes = tables.get(tablekey.hashCode()).getAttributes();            
            return attributes;
        } catch (java.lang.NullPointerException e) {
            return new HashMap();
        }


    }    
    
    private void readTable(String s) {
        s = s.substring(1, s.length()-1);
        String[] pairs = s.split(TABLE_SPLIT);
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
        tables.put(getHash((E) t.getPrimary(), t.getTableKey()), t);
    }
    

    
    private void readIndex(String s) {
        s = s.substring(1, s.length()-1); // trim special chars     
        String[] pair = s.split(INDEX_SPLIT);
        index.put(Integer.parseInt(pair[0]), pair[1]);
    }
    
    
    public Collection<Table> getTables() {
        return tables.values();
    }
    
    
}
