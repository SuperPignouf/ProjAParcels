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
public class ClientID implements Serializable {
    private long clientID;
    private Date fromDate;

    public ClientID(long clientID, Date fromDate) {
        this.clientID = clientID;
        this.fromDate = fromDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.clientID ^ (this.clientID >>> 32));
        hash = 41 * hash + (this.fromDate != null ? this.fromDate.hashCode() : 0);
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
        final ClientID other = (ClientID) obj;
        if (this.clientID != other.clientID) {
            return false;
        }
        if (this.fromDate != other.fromDate && (this.fromDate == null || !this.fromDate.equals(other.fromDate))) {
            return false;
        }
        return true;
    }

    public long getClientID() {
        return clientID;
    }

    public void setClientID(long clientID) {
        this.clientID = clientID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    
    
}
