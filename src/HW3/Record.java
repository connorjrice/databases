package HW3;

/**
 * TKEY_BEG*keyvalue*TKEY_END*TAB_BEG*REL_BEG*...*REL_END*TAB_END*
 * @author Connor
 * @param <E>
 */
public class Record<E> {
    
    private final E[] members;
    private final String tablekey;    
    private final int primary;
    
    public Record(E[] members, String tablekey, int primary) {
        this.members = members;
        this.tablekey = tablekey;
        this.primary = primary;
    }
    
    public E get(int index) {
        return members[index];
    }
    
    public E[] getMembers() {
        return members;
    }
    
    public int getPrimary() {
     return primary;   
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DBMS.RELT_BEG);
        sb.append(tablekey);
        sb.append(DBMS.RELT_END);
        for (int i = 0; i < members.length; i++) {
            sb.append(DBMS.REL_BEG);
            if (i == members.length-1) {
                sb.append(members[i].toString());
            } else {
                sb.append(members[i].toString()).append(", ");                
            }
            sb.append(DBMS.REL_END);
        }
        return sb.toString().substring(0, sb.length()-2);
    }

    
 
    
}
