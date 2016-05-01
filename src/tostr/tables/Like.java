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
public class Like {
    
    public int uid, pid;
    
    public Like(int uid, int pid) {
        this.uid = uid;
        this.pid = pid;
    }
    
    @Override
    public String toString() {
        return uid + "," + pid;
    }
    
    
}
