/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tostr.java;

/**
 *
 * @author Connor
 */
public class datarun {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        datagenerator dg = new datagenerator();
        dg.createUsers(5000);
        dg.createPictures(10000);
        dg.createCreators();
        dg.createLikes(20000);
    }
    
}
