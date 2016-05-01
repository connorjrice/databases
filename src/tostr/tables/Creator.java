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
public class Creator {
    
    public int pid,uid;
    
    public Creator(int pid, int uid) {
        this.pid = pid;
        this.uid = uid;
    }
    
    @Override
    public String toString() {
        return uid + "," + pid;
    }
    
}
