package HW3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Spliterator;
import java.util.function.Consumer;

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
    
    public String getTableKey() {
        return tablekey;
        
    }
    
    public String toStringPretty(HashMap<E, Class<E>> attributes) {
        StringBuilder picky = new StringBuilder();
        attributes.values().forEach((Class<E> v) -> {
            picky.append(v.getSimpleName()).append(" ");
        });
        String types = picky.toString();
        StringBuilder sb = new StringBuilder();
        attributes.keySet().forEach(sb::append); // it's like java is C++
        String labels = sb.toString();
        sb = new StringBuilder();
        System.out.println(Arrays.toString(members));
        Arrays.stream(members).forEach(sb::append);
        String record = sb.toString();

        
	sb.append("*");
        for (int i = 0; i < types.length(); i++) {
            sb.append("-");
        }

	sb.append("*\n");
	String border = sb.toString();
	sb = new StringBuilder();
	sb.append(border);
	sb.append("| ").append(labels).append(" |\n");
	sb.append("| ").append(types).append(" |\n");
	sb.append("| ").append(record).append(" |\n");
	sb.append(border);
	
	
        return sb.toString();
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
