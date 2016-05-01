package HW3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * I'm going to try variable-length.
 * @author Connor
 */
public class Record {
    
    private RandomAccessFile raf;
    
    public Record() throws FileNotFoundException {
        raf = new RandomAccessFile("test.db", "rw");        
    }
    
    public void createTable(String table) {
        byte[] data = table.getBytes();
        try {
            raf.write(data);
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertRecord(String record) {
        byte[] data = record.getBytes();
        try {
            raf.write(data);
        }  catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    public void findRecord(String query) {
        try {
            byte[] data = query.getBytes();
            long position = raf.length();
            while (position > 0) {
                position -= 1;
                raf.seek(position);
                byte b = raf.readByte();
            } 
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
            
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
