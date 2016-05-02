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
    
    public Record() {
        //
    }
    
    public void createTable(Object table) {
        byte[] data = table.toString().getBytes();

        try {
            raf = new RandomAccessFile("test.db", "rw");
            raf.seek(raf.length());
            for (byte b : data) {
                raf.writeByte(b);
            }
            raf.close();
        } catch (IOException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertRecord(Object record) {
        byte[] data = record.toString().getBytes();
        try {
            raf = new RandomAccessFile("test.db", "rw");
            raf.seek(raf.length());
            for (byte b : data) {
                raf.writeByte(b);                
            }

            raf.close();
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
