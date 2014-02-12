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
public class Order {
    
    private OrderID orderID;
    private long senderID;
    private long receiverID;
    private String billingCurrency;
    private float billingValue;
    private Date collectionDate;
    private Date timeStp;
    private Date toDate;

    public Order() {
    }

    public OrderID getOrderID() {
        return orderID;
    }

    public void setOrderID(OrderID orderID) {
        this.orderID = orderID;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(long receiverID) {
        this.receiverID = receiverID;
    }

    public String getBillingCurrency() {
        return billingCurrency;
    }

    public void setBillingCurrency(String billingCurrency) {
        this.billingCurrency = billingCurrency;
    }

    public float getBillingValue() {
        return billingValue;
    }

    public void setBillingValue(float billingValue) {
        this.billingValue = billingValue;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public Date getTimeStp() {
        return timeStp;
    }

    public void setTimeStp(Date timeStp) {
        this.timeStp = timeStp;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }  
    
}
