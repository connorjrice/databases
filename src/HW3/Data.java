package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import javax.xml.bind.DatatypeConverter;

/**
 * Parent class for Record and Table.
 * @author Connor
 */
public class Data<E>  {
    private static final String NODICE = "--*i got nothin'*--";
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();    
    
    public Data() {

    }
    
    /**
     * Returns a string with pretty formatting.
     * Thanks OCD!
     * @param attributes
     * @param members
     * @return 
     */
    public <E extends Comparable>
        String toStringPretty(HashMap<E, Class<E>> attributes, E[] members, String tablekey) {
        StringBuilder sb = new StringBuilder();
        String labels = "";
        String record = "";
        Object[] t = getTypes(attributes);
        String border = getBorder((String) t[0]);        
        sb.append(border).append("\n");                    

        labels = "| " + getLabels(t) + "|\n";
        if (tablekey.compareTo("") == 0) {
            // We're not dealing with a table
            if (((String)t[0]).compareTo(NODICE) == 0)  {
                // We didn't find a record
                //record = "| " + getRecord(t, members) + "|\n";
                sb.append("| ").append((String) t[0]).append("|\n");                    
            } else {
                // We did find a record, so print no results.
                record = "| " + getRecord(t, members) + "|\n";
                sb.append("| ").append((String) t[0]).append("|\n");                    
                sb.append(labels);            
                sb.append(record);                    
            }
             
        } else {
            labels = "| " + getLabels(t) + "|\n";
            record = "| " + getRecord(t, tablekey) + "|\n";
            sb.append(record);                            
            sb.append("| ").append((String) t[0]).append("|\n");
            sb.append(labels);
        }
       

	sb.append(border);
        return sb.toString();
    }

    /**
     * Returns a... struct? 
     * t[0] = (String) types
     * t[1] = (ArrayList<Integer> positions
     * t[2] = (HashMap<E, Class<E>> attributes
     * @param attributes
     * @return 
     */
    private <E extends Comparable<? super E>>
         Object[] getTypes(HashMap<E, Class<E>> attributes) {
        StringBuilder picky = new StringBuilder();
        ArrayList<Integer> positions = new ArrayList<>();
        attributes.values().forEach((Class<E> v) -> {
            picky.append(v.getSimpleName()).append(" ");
            positions.add(picky.length());
        });
        
        if (positions.isEmpty()) {
            picky.append(NODICE);
        }
        return new Object[]{picky.toString(),positions, attributes};
    }
    
    /**
     * Returns a string containing the id labels.
     * @param t
     * @return 
     */
    private <E extends Comparable<? super E>> String getLabels(Object[] t) {
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
    
    /**
     * Returns the id of the record queried for.
     * TODO: Select * From
     * @param t
     * @param members
     * @return 
     */
    private <E extends Comparable<? super E>> String getRecord(Object[] t,
            E[] members) {
        StringBuilder sb = new StringBuilder();        
        int i = 0;
        // Add a class cast exception to check for tom foolery (ArrayList<Integer>t[2]).
        // positions.size() = members.length
        if (members != null) {
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
        } else { // if members is null we have a table
                 // TODO: or an array
        }
        return sb.toString();
    }
    
    /**
     * Returns the id of the record queried for.
     * TODO: Select * From
     * @param t
     * @param members
     * @return 
     */
    private <E extends Comparable<? super E>> String getRecord(Object[] t,
            String primarykey) {
        StringBuilder sb = new StringBuilder();        
        int horizontalspaces = ((String) t[0]).length();
        for (int i = 0; i < horizontalspaces/8; i++) {
            sb.append(" ");
        }
        // Add a class cast exception to check for tom foolery (ArrayList<Integer>t[2]).
        // positions.size() = members.length
        for (int i = 0; i < horizontalspaces/8; i++) {
            sb.append(" ");
        }
        
        sb.append("Table: ").append(primarykey);
        while (sb.length() < horizontalspaces) {
            sb.append(" ");            
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
    
    public static String toHexString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
        
    }
    
    /**
     * I got this from StackOverflow
     * @param bytes
     * @return 
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
}            
 
    
    public String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];
        
        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);
        
        return result;
    }        
}
