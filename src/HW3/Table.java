package HW3;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Table class. Main components are:
 * (HashMap<E, Class<E>>) attributes
 *     where E = id value
 *     and Class<E> is datatype for E
 * (String) tablekey = identifier for table
 * (String) primary = value of primary key
 * @author Connor
 */
public class Table<E extends Comparable<Table>> extends Data {
    
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
    

    public String toStringPretty(HashMap<E, Class<E>> attributes, String primarykey) {
        return super.toStringPretty(attributes, null, primarykey);
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
