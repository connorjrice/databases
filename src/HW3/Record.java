package HW3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TKEY_BEG*keyvalue*TKEY_END*TAB_BEG*REL_BEG*...*REL_END*TAB_END*
 * @author Connor
 */
public class Record {
    
    
    public final byte TAB_BEG = 33;
    public final byte TAB_END = 35;
    public final byte TKEY_BEG = 40;
    public final byte TKEY_END = 41;
    public final byte REL_BEG = 2;
    public final byte REL_END = 3;
    
    public Record() {
        
        try {
            Files.delete(Paths.get("test.db"));
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Create a new table.
     * @param table: schema
     * @param key: key to find table
     */
    public void createTable(Object table, String key) {
        byte[] tabledata = table.toString().getBytes();
        byte[] keydata = key.getBytes();
        
        StringBuilder sb = new StringBuilder();
        // Format is TKEY_BEG|key|TKEY_END
        sb.append(TKEY_BEG);
        for (byte b : keydata) {
            sb.append(String.format("%02X", b));
        }
        sb.append(TKEY_END);
        sb.append(TAB_BEG);
        for (byte b : tabledata) {
            sb.append(String.format("%02X", b));
        }
        sb.append(TAB_END);
        byte[] converted = sb.toString().getBytes();
        try {
            try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
                raf.seek(raf.length());
                for (byte b : converted) {
                    raf.writeByte(b);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertRecord(Object record) {
        byte[] data = record.toString().getBytes();
        
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        byte[] converted = sb.toString().getBytes();
        try {
            try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
                raf.seek(raf.length());
                for (byte b : converted) {
                    raf.writeByte(b);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    public void findRecord(String query, String key) {
        StringBuilder record = new StringBuilder();
        byte[] keydata = key.getBytes();
        try {
            RandomAccessFile raf = new RandomAccessFile("test.db", "rw");
            byte[] querydata = query.getBytes();
            long length = raf.length()-1;
            long position = 0;
            boolean queryfound = false;
            while (position < length && !queryfound) {
                position = findFirstTuple(raf, keydata);
                raf.seek(position);
                // At this point we are looking at the first relation in the table
                
                raf.seek(position);
                byte b = raf.readByte();
                if (b == TKEY_BEG) { // We have found
                    // TODO: Check if key is correct
                    while (b != TKEY_END) {
                        b = raf.readByte();
                        position++;
                    }
                    if (b == TAB_BEG) { // Now we're into the table that we want
                        b = raf.readByte();
                        position += 1;
                        while (b != TAB_END) {
                            if (b == REL_BEG) {
                                long start = raf.getFilePointer();
                                if (checkRelation(raf, querydata, position, length)) {
                                    queryfound = true;
                                    raf.seek(start);
                                    position = start;
                                    while (b != TAB_END) {
                                        b = raf.readByte();
                                        position++;
                                        record.append(Integer.toHexString(b));
                                    }
                                } else {
                                    
                                }
                            }
                        }
                    }
                }
            } 
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Record.class.getName()).log(Level.INFO, record.toString());
            
    }
    
    /**
     * Provide a long that is the offset indicating the location of the 
     * first actual tuple of data contained within a table that is specified
     * by the byte[] keydata.
     * Or, if there is no such relation, return -1;
     * @param raf
     * @param keydata
     * @return 
     */
    private long findFirstTuple(RandomAccessFile raf, byte[] keydata) {
        long position = -1l;
        try {
            //position = raf.getFilePointer();
            byte b = raf.readByte();
            if (b == TKEY_BEG) { // If we are at the 
                
            } else {
                b = raf.readByte();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return position;
    }
    
    private boolean checkRelation(RandomAccessFile raf, byte[] query, long position, long length) {
        boolean isEqual = false;
        try {
            isEqual = true;
            byte curbyte = raf.readByte();
            for (int i = 0; i < query.length; i++) {
                if (curbyte != query[i]) {
                    isEqual = false;
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return isEqual;
    }
    
    public void modifyRecord(String query, String modified) {
        //find record
        // change it
        // rewrite it
    }
    
    public void deleteRecord(String query) {
        
    }
    
    public void printTableBytes() {
        
    }
    
}
