/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tostr.tables;

/**
 *
 * @author Connor
 */
public class Picture {
    public int id, creatorid, likes;
    public String date;
    public String name;
    
    public Picture(int id, int creatorid, int likes, String date, String name) {
        this.id = id;
        this.creatorid = creatorid;
        this.likes = likes;
        this.date = date;
        this.name = name;
    }
    
    public void incLikes() {
        likes += 1;
    }
    
    public void decLikes() {
        if (likes > 0) {
            likes -= 1;
        }
    }
    
   @Override
    public String toString() {
        return id + "," + creatorid + "," + likes + "," + date + "," + name;
    }
    
    
}
