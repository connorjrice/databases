package HW3;

/**
 * TKEY_BEG*keyvalue*TKEY_END*TAB_BEG*REL_BEG*...*REL_END*TAB_END*
 * @author Connor
 * @param <E>
 */
public class Record<E> {
    
    private final E[] members;    
    
    public Record(E[] members) {
        this.members = members;
    }
    
    public E get(int index) {
        return members[index];
    }
    
    public E[] getMembers() {
        return members;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E e : members) {
            sb.append(e.toString()).append(", ");
        }
        return sb.toString().substring(0, sb.length()-2);
    }

    
 
    
}
