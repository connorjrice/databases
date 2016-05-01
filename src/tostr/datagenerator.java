package tostr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import tostr.tables.Creator;
import tostr.tables.Like;
import tostr.tables.Picture;
import tostr.tables.User;

/**
 *
 * @author Connor
 */
public class datagenerator {
    
    private ArrayList<Creator> creators;
    private ArrayList<Picture> pictures;
    private ArrayList<Like> likes;
    private ArrayList<User> users;   
    private Random randy;
    private ArrayList<String> dictionary;
    private ArrayList<String> first;
    private ArrayList<String> last;
    
    
    public datagenerator() {
        creators = new ArrayList<>();
        pictures = new ArrayList<>();
        likes = new ArrayList<>();
        users = new ArrayList<>();
        dictionary = new ArrayList<>();
        first = new ArrayList<>();
        last = new ArrayList<>();
        randy = new Random();
        createDictionaries();
    }
    
    private void createDictionaries() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("dict.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("first.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                first.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("last.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                last.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void createUsers(int n) {
        for (int i = 0; i < n; i++) {
            users.add(createNewUser());
        }
        writeFile(users, "users.csv", "id,uname,bday,urealfirst,ureallast");
    }
    
    private User createNewUser() {
        return new User(getNextUserID(), getNewUserName(), getBDay(), getFirstName(), getLastName());
    }
    
    private int getNextUserID() {
        return users.size();
    }
    
    /**
     * Remember to write a check in for if a username exists when feeding data
     * into sql!
     * @return 
     */
    private String getNewUserName() {
        int length = randy.nextInt(3) + 1;
        String uname = "";
        for (int i = 0; i < length; i++) {
            uname += dictionary.get(randy.nextInt(dictionary.size()));
        }
        if (uname.length() > 63) {
            return uname.substring(0, 63);
        } else {
            return uname;
        }
    }
    
    
    
    private String getBDay() {
        int y1 = randy.nextInt(2) + 1;
        String bday = "";
        if (y1 == 1) { // 19XX or 20XX
            bday += "19";
            bday += randy.nextInt(8) + 2; // 19$X (2-9)
            bday += randy.nextInt(9) + 1; // 19X$ (1-9)
        } else {
            bday += "20";
            int y2 = randy.nextInt(2); // 201X or 200X
            bday += y2;
            if (y2 == 0) { // 200X (0-9)
                bday += randy.nextInt(10);
            } else { // 201X (0-3)
                bday += randy.nextInt(4);
            }
        } // bday == $$$$
        bday += "-"; // bday == $$$$-
        int month  = (randy.nextInt(12) + 1);
        if (month < 10) {
            bday += "0";
        }
        bday += month;
        // bday == $$$$-$$
        bday += "-"; // bday == $$$$-$$-
        int day = randy.nextInt(28)+1; // 1-28 (for simplicity)
        if (day < 10) {
            bday += "0";
        }
        bday += day;
        return bday;
    }
    
    private String getFirstName() {
        return first.get(randy.nextInt(first.size()));
    }
    
    private String getLastName() {
        return last.get(randy.nextInt(last.size()));
    }
    
    public void createPictures(int n) {
        for (int i = 0; i < n; i++) {
            pictures.add(createNewPicture());
        }
        writeFile(pictures, "pictures.csv", "id,creatorid,likes,date,name");
    }
    
    private Picture createNewPicture() {
        return new Picture(getNextPID(), getCreatorID(), 0, getPicDate(), getNewPicName());
    }
    
    private int getNextPID() {
        return pictures.size();
    }
    
    private int getCreatorID() {
        return randy.nextInt(users.size());
    }
    
    private String getPicDate() {
        String picdate = "2016-04-";
        int day  = (randy.nextInt(28) + 1);
        if (day < 10) {
            picdate += "0";
        }
        picdate += day;
        return picdate;
    }
    
    private String getNewPicName() {
        String picname = "";
        int index = randy.nextInt(dictionary.size());
        picname = dictionary.get(index).substring(0,1).toUpperCase() + dictionary.get(index).substring(1);
        index = randy.nextInt(dictionary.size());
        picname += " " + dictionary.get(index).substring(0,1).toUpperCase() + dictionary.get(index).substring(1);
        return picname;
    }
    
    /**
     * There had better be a pictures array.
     */
    public void createCreators() {
        pictures.stream().forEach((p) -> {
            creators.add(new Creator(p.creatorid,p.id));
        });
        writeFile(creators, "creators.csv", "uid,pid");
    }
    
    /**
     * There had better be a pictures and users array.
     * @param n 
     */
    public void createLikes(int n) {
        for (int i = 0; i < n; i++) {
            likes.add(new Like(getLikeUID(), getLikePID()));
        }
        writeFile(likes, "likes.csv", "uid,pid");
    }
    
    private int getLikeUID() {
        return randy.nextInt(users.size());
    }
    
    private int getLikePID() {
        return randy.nextInt(pictures.size());
    }
    
    


    
    private void writeFile(ArrayList a, String name, String columns) {
        try (PrintWriter print = new PrintWriter(new File(name))) {
            print.append(columns + "\n");
            a.stream().forEach((o) -> {
                print.append(o.toString() + "\n");
            });
            print.close();
        } catch (FileNotFoundException e) {
            try {
                Files.createFile(Paths.get(name));
            } catch (IOException ex) {
                Logger.getLogger(datagenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
        
    private void addNewCreation() {
        int creatorid, likes, pictureid;
        creatorid = pickCreator();
        
    }
    
    private void addLikes(int pictureid) {
        
    }
    
    
    
    private void addUser() {
        
    }
    
    private int pickCreator() {
        return randy.nextInt(creators.size());
    }
    

    
    public void makePictures(int n){
        Random randy = new Random();

        try (PrintWriter print = new PrintWriter(new File(n+"diradj.csv"))) {
            //print.append(randy.nextInt(10000));

            print.close();
        } catch (FileNotFoundException e) {
           
        }        
    }
}
