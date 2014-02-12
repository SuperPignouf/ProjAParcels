/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Pantoufle
 */
public class ParcelStatus {
    
    private ParcelID parcelID;
    private String status, comment;
    private Date toDate;

    public ParcelStatus() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.parcelID != null ? this.parcelID.hashCode() : 0);
        hash = 73 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 73 * hash + (this.comment != null ? this.comment.hashCode() : 0);
        hash = 73 * hash + (this.toDate != null ? this.toDate.hashCode() : 0);
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
        final ParcelStatus other = (ParcelStatus) obj;
        if (this.parcelID != other.parcelID && (this.parcelID == null || !this.parcelID.equals(other.parcelID))) {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        if ((this.comment == null) ? (other.comment != null) : !this.comment.equals(other.comment)) {
            return false;
        }
        if (this.toDate != other.toDate && (this.toDate == null || !this.toDate.equals(other.toDate))) {
            return false;
        }
        return true;
    }

    public ParcelID getParcelID() {
        return parcelID;
    }

    public void setParcelID(ParcelID parcelID) {
        this.parcelID = parcelID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    
    
}
