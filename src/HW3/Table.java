package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Connor
 */
public class Table<E> {
    
    private final HashMap<E, Class<E>> attributes;
    private final String key;
    
    public Table(HashMap<E, Class<E>> attributes, String key) {
        this.attributes = attributes;
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }    
    
    public HashMap<E, Class<E>> getAttributes() {
        return attributes;
    }
    
    public String toStringPretty() {
        StringBuilder s = new StringBuilder();
        s.append("Table name: ").append(key);
        return s.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(DBMS.TKEY_BEG);
        s.append(key);        
        s.append(DBMS.TKEY_END);
        s.append(DBMS.PRIME_BEG);
        s.append("TABLE");
        s.append(DBMS.PRIME_END);
        s.append(DBMS.TAB_BEG);
        for (Entry<E, Class<E>> e : attributes.entrySet()) {
            s.append(e.getKey().toString()).append(DBMS.SEP);
            String class_ = e.getValue().toString().split(" ")[1];
            s.append(class_).append(',').append(' ');
        }
        s = new StringBuilder().append(s.substring(0, s.length()-2));
        
        s.append(DBMS.TAB_END).append('\n');
        
        return s.toString();
    }
    
}
