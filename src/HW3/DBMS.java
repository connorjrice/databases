package HW3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 	feff01c3
	LATIN CAPITAL LETTER DZ WITH CARON (U+01C4)	feff01c4
	LATIN CAPITAL LETTER D WITH SMALL LETTER Z WITH CARON (U+01C5)	feff01c5
	LATIN SMALL LETTER DZ WITH CARON (U+01C6)	feff01c6
	LATIN CAPITAL LETTER LJ (U+01C7)	feff01c7
	LATIN CAPITAL LETTER L WITH SMALL LETTER J (U+01C8)	feff01c8
	LATIN SMALL LETTER LJ (U+01C9)	feff01c9
	LATIN CAPITAL LETTER NJ (U+01CA)	feff01ca
	LATIN CAPITAL LETTER N WITH SMALL LETTER J (U+01CB)	feff01cb
	LATIN SMALL LETTER NJ (U+01CC)	feff01cc
	LATIN CAPITAL LETTER A WITH CARON (U+01CD)	feff01cd
	LATIN SMALL LETTER A WITH CARON (U+01CE)	feff01ce
	LATIN CAPITAL LETTER I WITH CARON (U+01CF)	feff01cf
	LATIN SMALL LETTER I WITH CARON (U+01D0)	feff01d0
	LATIN CAPITAL LETTER O WITH CARON (U+01D1)	feff01d1
ǒ	LATIN SMALL LETTER O WITH CARON (U+01D2)	feff01d2
Ǔ	LATIN CAPITAL LETTER U WITH CARON (U+01D3)	feff01d3
ǔ	LATIN SMALL LETTER U WITH CARON (U+01D4)	feff01d4
Ǖ	LATIN CAPITAL LETTER U WITH DIAERESIS AND MACRON (U+01D5)	feff01d5
ǖ	LATIN SMALL LETTER U WITH DIAERESIS AND MACRON (U+01D6)	feff01d6
Ǘ	LATIN CAPITAL LETTER U WITH DIAERESIS AND ACUTE (U+01D7)	feff01d7
ǘ	LATIN SMALL LETTER U WITH DIAERESIS AND ACUTE (U+01D8)	feff01d8
Ǚ	LATIN CAPITAL LETTER U WITH DIAERESIS AND CARON (U+01D9)	feff01d9
ǚ	LATIN SMALL LETTER U WITH DIAERESIS AND CARON (U+01DA)	feff01da
Ǜ	LATIN CAPITAL LETTER U WITH DIAERESIS AND GRAVE (U+01DB)	feff01db
ǜ	LATIN SMALL LETTER U WITH DIAERESIS AND GRAVE (U+01DC)	feff01dc
ǝ	LATIN SMALL LETTER TURNED E (U+01DD)	feff01dd
Ǟ	LATIN CAPITAL LETTER A WITH DIAERESIS AND MACRON (U+01DE)	feff01de
ǟ	LATIN SMALL LETTER A WITH DIAERESIS AND MACRON (U+01DF)	feff01df
Ǡ	LATIN CAPITAL LETTER A WITH DOT ABOVE AND MACRON (U+01E0)	feff01e0
ǡ	LATIN SMALL LETTER A WITH DOT ABOVE AND MACRON (U+01E1)	feff01e1
Ǣ	LATIN CAPITAL LETTER AE WITH MACRON (U+01E2)	feff01e2
ǣ	LATIN SMALL LETTER AE WITH MACRON (U+01E3)	feff01e3
Ǥ	LATIN CAPITAL LETTER G WITH STROKE (U+01E4)
 * @author Connor
 */
public class DBMS<E> {
    // These are all used for enclosing data
    /*public static final char TKEY_BEG = 'Ǆ'; // Table key
    public static final char TKEY_END = 'ǅ';
    public static final char TAB_BEG = 'ǆ';  
    public static final char TAB_END = 'Ǉ';
    public static final char REL_BEG = 'ǈ';
    public static final char REL_END = 'ǉ';
    public static final char SEP = 'Ǌ';
    public static final char IND_BEG = 'ǋ';
    public static final char IND_END = 'ǌ';
    public static final char RELT_BEG ='Ǎ' ; // Relation table key beg
    public static final char RELT_END = 'ǎ';
    public static final char PRIME_BEG = 'Ǐ';
    public static final char PRIME_END = 'ǐ';
 
    public static final char BAD_BAD_BAD = 'Ǒ';
    public static final char SNEAKY_KEY = 14; */// Stuck into hashmaps for tables
    public static final char TKEY_BEG = 'A'; // Table key
    public static final char TKEY_END = 'B';
    public static final char TAB_BEG = 'C';  
    public static final char TAB_END = 'D';
    public static final char REL_BEG = 'E';
    public static final char REL_END = 'F';
    public static final char SEP = 16;
    public static final char IND_BEG = 17;
    public static final char IND_END = 18;
    public static final char RELT_BEG =19 ; // Relation table key beg
    public static final char RELT_END = 20;
    public static final char PRIME_BEG = 21;
    public static final char PRIME_END = 22;
 
    public static final char BAD_BAD_BAD = 'Ǒ';
    public static final char SNEAKY_KEY = 14; 


    private final DBIO io;                     // Represents table key
    
    private final String db,index;

    
    public DBMS(String db, String index) {
        this.db = db;
        this.index = index;
        this.io = new DBIO(db, index);
    }
    
    public DBMS() {
        this.db = "test.db";
        this.index = "index.db";
        this.io = new DBIO(db, index);
    }
 
    /**
     * Create a new table.
     * @param attributes
     * @param key: key to find table
     */
    public <E extends Comparable<? super E>> void createTable(E[] values, 
            Class<E>[] types, String key, int primaryindex) {
        if (values.length == types.length) {
            HashMap<E, Class<E>> attributes = new HashMap();
            for (int i = 0; i < values.length; i++) {
                attributes.put(values[i], types[i]);
            }
            Table t = new Table(attributes, key, values[primaryindex].toString());
            io.addTable(t);
            io.write(t.toString(), t.getTableKey(), key, true);
        } else {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, "Table "
                    + "creation error: inequal number of values and types.");
        }
    }
    
    public void showTables() {
        for (Table t : ((Collection<Table>) io.getTables())) {
            System.out.println(t.toStringPretty(t.getAttributes(), null, t.getTableKey()));
        }
    }
    
    
    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.write(r.toString(), r.getPrimary(), tablekey, true);
    }
    
    public <E extends Comparable<? super E>> void insertRecord(String tablekey, 
            E[] members, int primaryindex, boolean db) {
        // TODO: Check to see if members matches schema
        Record r = new Record(members, tablekey, primaryindex);
        io.write(r.toString(), r.getPrimary(), tablekey, db);
    }    
    
    /**
     * Flow: findRecord() -> io.hashLookup() -> 
     * @param <E>
     * @param primarykey
     * @param tablekey 
     */
    public <E extends Comparable<? super E>> void findRecord(E primarykey, String tablekey) {
        Record r = io.hashLookup(primarykey, tablekey);
        HashMap<E, Class<E>> attributes = io.getAttributes(r.getTableKey());
        System.out.println(r.toStringPretty(attributes, r.getMembers(), ""));
    }
    
    public <E extends Comparable<? super E>> void modifyRecord(E primarykey, String tablekey, E[] members) {
        Record r = io.hashLookup(primarykey, tablekey);
        // Check bytes
        if (Arrays.toString(r.getMembers()).getBytes().length == 
                Arrays.toString(members).getBytes().length) {
            io.write(r.toString(), r.getPrimary(), tablekey, false);
        } else {
            deleteRecord(primarykey, tablekey);
            insertRecord(tablekey, members, r.getPrimaryIndex(), false);
        }
    }
    
    public <E extends Comparable<? super E>> String deleteRecord(E primarykey, String tablekey) {
        return io.deleteRecord(primarykey, tablekey);
    }
    
}
