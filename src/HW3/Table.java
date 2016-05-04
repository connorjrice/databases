package HW3;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Connor
 */
public class Table<E> {
    
    private final ArrayList<Record> values;
    private final HashMap<E, Class<E>> attributes;
    private final String key;
    
    public Table(HashMap<E, Class<E>> attributes, String key) {
        this.attributes = attributes;
        this.key = key;
        this.values = new ArrayList<>();
    }
    
    public void add(Record r) {
        values.add(r);   
    }
    
    public String getKey() {
        return key;
    }
    
    public ArrayList<Record> getRecords() {
        return values;
    }
    
    public HashMap<E, Class<E>> getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        String s = "";
        s += DBMS.TKEY_BEG;
        s += key;        
        s += DBMS.TKEY_END;
        s += "\n";
        
        s += DBMS.TAB_BEG;
        for (E e : attributes.keySet()) {
            s += e.toString() + ", ";
        }
        
        s = s.substring(0,s.length()-2);
        s += DBMS.TAB_END;
        s += "\n";        
        
        for (Record r : values) {
            s += DBMS.REL_BEG;
            s += r.toString();
            s += DBMS.REL_END;
            s += "\n";            
        }
        
        return s;
    }
    
}
