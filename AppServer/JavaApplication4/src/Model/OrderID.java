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
public class OrderID implements Serializable {
    private Long orderID;
    private Date fromDate;
    
    public OrderID(Long orderID, Date fromDate){
        this.orderID = orderID;
        this.fromDate = fromDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.orderID != null ? this.orderID.hashCode() : 0);
        hash = 89 * hash + (this.fromDate != null ? this.fromDate.hashCode() : 0);
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
        final OrderID other = (OrderID) obj;
        if (this.orderID != other.orderID && (this.orderID == null || !this.orderID.equals(other.orderID))) {
            return false;
        }
        if (this.fromDate != other.fromDate && (this.fromDate == null || !this.fromDate.equals(other.fromDate))) {
            return false;
        }
        return true;
    }

    public Long getOrderID() {
        return orderID;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
   
}
