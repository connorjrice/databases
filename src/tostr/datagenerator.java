package tostr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
        createDictionary();
        createFirst();
        createLast();
    }
    
    private void createFirst() {
        
    }
    
    private void createLast() {
        
    }
    
    private void createDictionary() {
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
        
    }
    
    private String getNewUserName() {
        
    }
    
    private String getBDay() {
        
    }
    
    private String getFirstName() {
        
    }
    
    private String getLastName() {
        
    }
    
    public void createPictures(int n) {
        
    }
    
    public void createCreators(int n) {
        
    }
    
    public void createLikes(int n) {
        
    }
    


    
    private void writeFile(ArrayList a, String name, String columns) {
        try (PrintWriter print = new PrintWriter(new File(name))) {
            print.append(columns);
            a.stream().forEach((o) -> {
                print.append(o.toString());
            });
            print.close();
        } catch (FileNotFoundException e) {
           
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
