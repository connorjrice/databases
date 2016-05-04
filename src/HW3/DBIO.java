/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HW3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.StringUtils;

/**
 *
 * @author Connor
 */
public class DBIO<E> {
    private String tempKey = "";
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();    
    
    public void readDB(ArrayList tables, HashMap index) {
        tables.clear();
        index.clear();
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            raf.seek(0);
//            while (raf.getFilePointer() < raf.length()) {
            String input = raf.readUTF();
            input = bytesToHex(input.getBytes());
            String[] entries = input.split("\n");
            for (String s : entries){
                decide(s, parse(s));                
            }

 //           }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int parse(String s) {
/*        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            System.out.println(b);
        }*/if (s.startsWith("0") && s.endsWith("1")) {
            return 0; // key
        } else if (s.startsWith("2") && s.endsWith("3")) {
            return 1; // table
        } else if (s.startsWith("4") && s.endsWith("5")) {
            return 2; // relation
        } else {
            return -1;   
        }
    }
    
    /**
     * Remember: we are being passed the string without special chars.
     * @param s
     * @param i 
     */
    private void decide(String s, int i) {
        if (i == 0) { 
            // s is a key, store it
            tempKey = s;
        } else if (i == 1) { 
            // Put a table together if we have a tempKey
            if (tempKey.length() > 0) { 
                // Fix our input so that Java doesn't throw ClassNotFound
                String[] pairs = fixString(s);
//                System.out.println(StringUtils.equals(pairs[1].toCharArray(), "java.lang.String".toCharArray()));
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
        } else if (i == 2) { // relation
            
        } else {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, "Parsing error: invalid decision value.");
        }
    }
    
    private String[] fixString(String s) {
        String fixed = s.trim();                
        fixed = fixed.substring(1,fixed.length()-1);
        byte[] parsed = StringUtils.getBytesUtf8(fixed);
        return StringUtils.newStringUtf8(parsed).split(","); 
    }
    
    
    private String readKey(String s) {
        if (s.startsWith("2") && s.endsWith("3")) {
            return s.substring(1,s.length()-1);
        }
        else {
            return "nokey";
        }
    }
    
    
    public void writeDB(ArrayList<Table> tables) {
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            for (Table t : tables) {
                String s = bytesToHex(t.toString().getBytes());
                raf.writeUTF(s);
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
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
}
