package HW3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TKEY_BEG*keyvalue*TKEY_END*TAB_BEG*REL_BEG*...*REL_END*TAB_END*
 * @author Connor
 */
public class Record {
    
    // These are all used for enclosing data
    public final byte TAB_BEG = 33;  
    public final byte TAB_END = 35;
    public final byte TKEY_BEG = 40; // Table key
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
        StringBuilder sb = new StringBuilder();
        addKey(sb, key.getBytes());
        addSchema(sb, table.toString().getBytes());
        writeTable(sb);
    }
    
    private void addKey(StringBuilder sb, byte[] keydata) {
        sb.append(TKEY_BEG);
        for (byte b : keydata) {
            sb.append(String.format("%02X", b));
        }
        sb.append(TKEY_END);
    }
    
    private void addSchema(StringBuilder sb, byte[] tabledata) {
        sb.append(TAB_BEG);
        for (byte b : tabledata) {
            sb.append(String.format("%02X", b));
        }
        sb.append(TAB_END);
    }
    
    private void writeTable(StringBuilder sb) {
        try {
            try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
                raf.seek(raf.length());
                for (byte b : sb.toString().getBytes()) {
                    raf.writeByte(b);
                }
                raf.close();
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
                raf.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    public void findRecord(String query, String key) {
        StringBuilder record = new StringBuilder();
        byte[] keydata = key.getBytes();
        byte[] querydata = query.getBytes();
        try {
            RandomAccessFile raf = new RandomAccessFile("test.db", "rw");
            long length = raf.length()-1;
            long position = 0;
            boolean queryfound = false;
            while (position < length && !queryfound) {
                position = findFirstTuple(raf, keydata);
                raf.seek(position);
                byte[] relation = getRelation(raf);
                
                // At this point we are looking at the first relation
                if (byteCompare(raf, query.getBytes())) { // If we find our query
                    byte b = raf.readByte();
                    ArrayList<Byte> a = new ArrayList<>();
                    while (b != REL_END) {
                        a.add(b);
                        b = raf.readByte();
                    }
                } else { // Move on to next
                    
                }
                raf.close();
            } 
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(Record.class.getName()).log(Level.INFO, record.toString());
    }
    
    private boolean contains(byte[] a, byte[] b) {
        boolean contains = false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[0]) {
                
            }
        }
        return contains;
    }
    
    
    private byte[] getRelation(RandomAccessFile raf) throws IOException {
        long position = raf.getFilePointer();
        int relationLength = getRelationLength(raf);
        byte[] relation = new byte[relationLength];
        if (relation.length > 0) {
            for (int i = 0; i < relationLength; i++) {
                relation[i] = raf.readByte();
            }
        }
        return relation;
        
    }
    
    private int getRelationLength(RandomAccessFile raf) throws IOException {
        long position = raf.getFilePointer();
        int count = 0;
        raf.seek(position-1);
        byte b = raf.readByte();
        if (b == REL_BEG) {
            while (b != REL_END) {
                b = raf.readByte();
                count++;
            }
        }
        return count;
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
    private long findFirstTuple(RandomAccessFile raf, byte[] keydata) throws IOException {
        try {
            //position = raf.getFilePointer();
            byte b = raf.readByte();
            if (b == TKEY_BEG) {
                if (byteCompare(raf, keydata)) {
                    while (b != TKEY_END) {
                        b = raf.readByte();
                    }
                    if (b == TAB_BEG) {
                        while (b != TAB_END) {
                            b = raf.readByte();
                        }
                    }
                    if (b == REL_BEG) {
                        b = raf.readByte();
                        return raf.getFilePointer();
                    }
                }
            } else { // Find TKEY_BEG
                while (b != TKEY_BEG && raf.getFilePointer() < raf.length()-1){
                    b = raf.readByte();                    
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return raf.getFilePointer();
    }
    
    /**
     * Compares the current place of raf to the byte[] data
     * @param raf
     * @param data
     * @return 
     */
    private boolean byteCompare(RandomAccessFile raf, byte[] data) {
        boolean isEqual = false;
        try {
            long startingposition = raf.getFilePointer();
            isEqual = true;
            byte curbyte = raf.readByte();
            for (int i = 0; i < data.length; i++) {
                if (curbyte != data[i]) {
                    isEqual = false;
                    break;
                }
            }
            raf.seek(startingposition);
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
