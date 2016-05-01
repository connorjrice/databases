/*
 * how to SQL likes
set likes = (
select count(uid)
from likes
where likes.pid = pictures.id
);
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
