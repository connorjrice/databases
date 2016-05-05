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
    
    public void readDB(ArrayList tables, HashMap indices) {
        tables.clear();
        indices.clear();
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            raf.seek(0);
                StringBuilder sb = new StringBuilder();
                char c;
                while (raf.getFilePointer() < raf.length()) {
                    while ((c = raf.readChar()) != '\n'){
                        sb.append(c);
                    }
                    decide(sb.toString().trim(), parse(sb.toString().trim()));
                    sb = new StringBuilder();
                }
                raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int parse(String s) {
        if (s.startsWith("0") && s.endsWith("1")) {
            return 0; // key
        } else if (s.startsWith("2") && s.endsWith("3")) {
            return 1; // table
        } else if (s.startsWith("4") && s.endsWith("5")) {
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
    
    
    public void writeDB(ArrayList<Table> tables) {
        try (RandomAccessFile raf = new RandomAccessFile("test.db", "rw")) {
            for (Table t : tables) {
                raf.writeChars(t.toString());
            }
            raf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
