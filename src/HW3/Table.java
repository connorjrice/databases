package HW3;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Connor
 */
public class Table<E> {
    
    private final HashMap<E, Class<E>> attributes;
    private final String tablekey;
    private final String primary;
    
    public Table(HashMap<E, Class<E>> attributes, String tablekey, String primary) {
        this.attributes = attributes;
        this.tablekey = tablekey;
        this.primary = primary;
    }
    
    public String getTableKey() {
        return tablekey;
    }    
    
    public HashMap<E, Class<E>> getAttributes() {
        return attributes;
    }
    
    public String getPrimary() {
        return primary;
    }
    
    public String toStringPretty() {
        StringBuilder s = new StringBuilder();
        s.append("Table name: ").append(tablekey);
        return s.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(DBMS.TKEY_BEG);
        s.append(tablekey);        
        s.append(DBMS.TKEY_END);
        s.append(DBMS.PRIME_BEG);
        s.append(primary);
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
