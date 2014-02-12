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
class ParcelID implements Serializable{
    
    private long parcelID;
    private Date fromDate;

    public ParcelID(long parcelID, Date fromDate) {
        this.parcelID = parcelID;
        this.fromDate = fromDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (int) (this.parcelID ^ (this.parcelID >>> 32));
        hash = 19 * hash + (this.fromDate != null ? this.fromDate.hashCode() : 0);
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
        final ParcelID other = (ParcelID) obj;
        if (this.parcelID != other.parcelID) {
            return false;
        }
        if (this.fromDate != other.fromDate && (this.fromDate == null || !this.fromDate.equals(other.fromDate))) {
            return false;
        }
        return true;
    }

    public long getParcelID() {
        return parcelID;
    }

    public void setParcelID(long parcelID) {
        this.parcelID = parcelID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    
    
}
