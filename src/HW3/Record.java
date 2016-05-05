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
    
    public int getPrimaryIndex() {
     return primary;   
    }
    
    public String getPrimary() {
        return members[primary].toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DBMS.RELT_BEG);
        sb.append(tablekey);
        sb.append(DBMS.RELT_END);
        sb.append(DBMS.PRIME_BEG);
        sb.append(getPrimary());
        sb.append(DBMS.PRIME_END);
        sb.append(DBMS.REL_BEG);
        for (int i = 0; i < members.length; i++) {

            if (i == members.length-1) {
                sb.append(members[i].toString());
            } else {
                sb.append(members[i].toString()).append(", ");                
            }

        }
        sb.append(DBMS.REL_END).append('\n');
        return sb.toString();
    }

    
 
    
}
