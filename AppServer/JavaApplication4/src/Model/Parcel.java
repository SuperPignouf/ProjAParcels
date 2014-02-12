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
public class Parcel {
    
    private ParcelID parcelID;
    private long orderID, scanCode;
    private String scanCodeType, content, description, declaredValueCurrency;
    private float massKg, lenghtCm, heightCm, widthCm, declaredValue;
    private Date timeStp, toDate;

    public Parcel() {
    }

    public ParcelID getParcelID() {
        return parcelID;
    }

    public void setParcelID(ParcelID parcelID) {
        this.parcelID = parcelID;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getScanCode() {
        return scanCode;
    }

    public void setScanCode(long scanCode) {
        this.scanCode = scanCode;
    }

    public String getScanCodeType() {
        return scanCodeType;
    }

    public void setScanCodeType(String scanCodeType) {
        this.scanCodeType = scanCodeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeclaredValueCurrency() {
        return declaredValueCurrency;
    }

    public void setDeclaredValueCurrency(String declaredValueCurrency) {
        this.declaredValueCurrency = declaredValueCurrency;
    }

    public float getMassKg() {
        return massKg;
    }

    public void setMassKg(float massKg) {
        this.massKg = massKg;
    }

    public float getLenghtCm() {
        return lenghtCm;
    }

    public void setLenghtCm(float lenghtCm) {
        this.lenghtCm = lenghtCm;
    }

    public float getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(float heightCm) {
        this.heightCm = heightCm;
    }

    public float getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(float widthCm) {
        this.widthCm = widthCm;
    }

    public float getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(float declaredValue) {
        this.declaredValue = declaredValue;
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
