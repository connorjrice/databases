package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
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
    /**
     * Returns a string with pretty formatting.
     * Thanks OCD!
     * @param attributes
     * @return 
     */
    public String toStringPretty(HashMap<E, Class<E>> attributes) {
        StringBuilder sb = new StringBuilder();
        String labels = "";
        String record = "";
        Object[] t = getTypes(attributes);
        if (!((ArrayList<Integer>) t[1]).isEmpty()) {
            labels = "| " + getLabels(t) + "|\n";
            record = "| " + getRecord(t) + "|\n";
        }
	String border = getBorder((String) t[0]);
        
	sb.append(border).append("\n");
	sb.append("| ").append((String) t[0]).append("|\n");        
	sb.append(labels);
	sb.append(record);
	sb.append(border);
        return sb.toString();
    }
    
    private Object[] getTypes(HashMap<E, Class<E>> attributes) {
        StringBuilder picky = new StringBuilder();
        ArrayList<Integer> positions = new ArrayList<>();
        attributes.values().forEach((Class<E> v) -> {
            picky.append(v.getSimpleName()).append(" ");
            positions.add(picky.length());
        });
        
        if (positions.isEmpty()) {
            picky.append("--*i got nothin'*--");
        }
        return new Object[]{picky.toString(),positions, attributes};
    }
    
    private String getLabels(Object[] t) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : ((Set<String>) ((HashMap<E, Class<E>>) t[2]).keySet())) {
            sb.append(s);            
            for (int j = sb.length(); j < ((ArrayList<Integer>) t[1]).get(i); j++) {
                sb.append(" ");
            }
            i++;
        }
        return sb.toString();
    }
    
    private String getRecord(Object[] t) {
        StringBuilder sb = new StringBuilder();        
        int i = 0;
        // Add a class cast exception to check for tom foolery (ArrayList<Integer>t[2]).
        // positions.size() = members.length
        if (members.length > 0 && ((ArrayList<Integer>) t[1]).size() == members.length) {
            for (Object o : Arrays.stream(members).toArray()) {
                sb.append(o.toString());
                // Spacing
                for (int j = sb.length(); j < ((ArrayList<Integer>) t[1]).get(i); j++) {
                    sb.append(" ");
                }
                i++;            
            }
        }
        return sb.toString();
    }
    
    private String getBorder(String types) {
        StringBuilder sb = new StringBuilder();                
        sb.append("*-");
        for (int j = 0; j < types.length(); j++) {
            sb.append("-");
        }
	sb.append("*");
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
