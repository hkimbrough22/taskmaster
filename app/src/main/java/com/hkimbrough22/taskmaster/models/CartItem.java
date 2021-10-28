package com.hkimbrough22.taskmaster.models;

import java.util.Date;

public class CartItem {
    protected String itemName;
    protected Date addedOn;

    public CartItem(String itemName, Date addedOn) {
        this.itemName = itemName;
        this.addedOn = addedOn;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    @Override
    public String toString() {
        return "itemName= " + itemName +
                "\naddedOn= " + addedOn;
    }
}
