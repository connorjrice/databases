package HW3;

/**
 * TKEY_BEG*keyvalue*TKEY_END*TAB_BEG*REL_BEG*...*REL_END*TAB_END*
 * @author Connor
 */
public class Record {
    
    private Object[] members;    
    
    public Record(Object[] members) {
        this.members = members;
    }
    
    public Object get(int index) {
        return members[index];
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : members) {
            sb.append(o.toString());
        }
        return sb.toString();
    }

    
 
    
}
