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
public class User {
    
    public int id;
    public String uname, bday, urealfirst, ureallast;
    
    public User(int id, String uname, String bday, String urealfirst, String ureallast) {
        this.id = id;
        this.uname = uname;
        this.bday = bday;
        this.urealfirst = urealfirst;
        this.ureallast = ureallast;
    }
    
    @Override
    public String toString() {
        return id + "," + uname + "," + bday + "," + urealfirst + "," + ureallast;
    }
    
}
