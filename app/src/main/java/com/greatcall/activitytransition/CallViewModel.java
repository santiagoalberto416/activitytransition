package com.greatcall.activitytransition;

import android.graphics.Bitmap;

import java.io.Serializable;

public final class CallViewModel implements Serializable{

    public static final int NORMAL_ROW = 0;
    public static final int EMPTY_ROW = 1;

    private String name;
    private String phoneNumber;
    private Boolean isFavorite;
    private Bitmap icon;
    private Boolean hasHeader;
    private String sectionName;
    private int rowType;

    public CallViewModel(String name, String phoneNumber, Boolean isFavorite, Bitmap icon, Boolean hasHeader, String sectionName, int rowType) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isFavorite = isFavorite;
        this.icon = icon;
        this.hasHeader = hasHeader;
        this.sectionName = sectionName;
        this.rowType = rowType;
    }

    public CallViewModel() {
        this.rowType = NORMAL_ROW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Boolean getHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(Boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }
}
