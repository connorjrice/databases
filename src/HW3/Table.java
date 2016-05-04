package HW3;

import java.util.ArrayList;

/**
 *
 * @author Connor
 */
public class Table {

    private final byte TAB_BEG = 5;  
    private final byte TAB_END = 6;
    private final byte TKEY_BEG = 2; // Table key
    private final byte TKEY_END = 3;
    private final byte REL_BEG = 4;
    private final byte REL_END = 5;
    
    private final ArrayList<Record> values;
    private final String[] attributes;
    private final String key;
    
    public Table(String[] attributes, String key) {
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
    
    public String[] getAttributes() {
        return attributes;
    }
    
    @Override
    public String toString() {
        String s = "";
        s += TKEY_BEG;
        s += key;        
        s += TKEY_END;
        
        s += TAB_BEG;
        s += attributes;
        s += TAB_END;
        
        for (Record r : values) {
            s += REL_BEG;
            s += r.toString();
            s += REL_END;
        }
        
        return s;
    }
    
}
