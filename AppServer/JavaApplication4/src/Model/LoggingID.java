/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Pantoufle
 */
class LoggingID implements Serializable {
    
    private Date timeStp;
    private long userID;

    public LoggingID(Date timeStp, long userID) {
        this.timeStp = timeStp;
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.timeStp != null ? this.timeStp.hashCode() : 0);
        hash = 47 * hash + (int) (this.userID ^ (this.userID >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoggingID other = (LoggingID) obj;
        if (this.timeStp != other.timeStp && (this.timeStp == null || !this.timeStp.equals(other.timeStp))) {
            return false;
        }
        if (this.userID != other.userID) {
            return false;
        }
        return true;
    }

    public Date getTimeStp() {
        return timeStp;
    }

    public void setTimeStp(Date timeStp) {
        this.timeStp = timeStp;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
    
    
    
}
