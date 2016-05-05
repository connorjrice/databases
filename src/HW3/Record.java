package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

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
        ArrayList<Integer> positions = new ArrayList<>();
        attributes.values().forEach((Class<E> v) -> {
            picky.append(v.getSimpleName()).append(" ");
            positions.add(picky.length());
        });
        
        String types = picky.toString();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : (Set<String>) attributes.keySet()) {
            sb.append(s);            
            for (int j = sb.length(); j < positions.get(i)-1; j++) {
                sb.append(" ");
            }
            i++;
        }
        
        String labels = sb.toString();
        sb = new StringBuilder();
        i = 0;
        if (members.length > 0 && positions.size() == members.length) {
            for (Object o : Arrays.stream(members).toArray()) {
                sb.append(o.toString());
                for (int j = sb.length(); j < positions.get(i)-1; j++) {
                    sb.append(" ");
                }
                i++;            
            }
        }
        String record = sb.toString();
        sb = new StringBuilder();
        
	sb.append("*-");
        for (int j = 0; j < types.length(); j++) {
            sb.append("-");
        }

	sb.append("*");
	String border = sb.toString();
	sb = new StringBuilder();
	sb.append(border).append("\n");
	sb.append("| ").append(types).append("|\n");        
	sb.append("| ").append(labels).append(" |\n");
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
