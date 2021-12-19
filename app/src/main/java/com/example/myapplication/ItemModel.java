package com.example.myapplication;

public class ItemModel implements Comparable<ItemModel>{
    private  boolean isSectionHeader;
    private String deptPos;
    private String arrivalPos;
    private String deptTime;
    private String arrivalTime;
    private String date;

    public ItemModel(String deptPos, String arrivalPos, String deptTime, String date) {
        this.deptPos = "탑승 정류장> "+deptPos;
        this.arrivalPos = "하차 정류장> "+arrivalPos;
        this.deptTime = "탑승 시간> "+deptTime;
        this.arrivalTime = "expected";
        this.date = date;
        isSectionHeader = false;
    }

    public String getArrivalPos() {
        return arrivalPos;
    }

    public void setArrivalPos(String arrivalPos) {
        this.arrivalPos = arrivalPos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDeptTime() {
        return deptTime;
    }

    public void setDeptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    public String getDeptPos() {
        return deptPos;
    }

    public void setDeptPos(String deptPos) {
        this.deptPos = deptPos;
    }

    public boolean isSectionHeader() {
        return isSectionHeader;
    }

    @Override
    public int compareTo(ItemModel itemModel) {
        return this.date.compareTo(itemModel.date);
    }

    public void setToSectionHeader() {
        isSectionHeader = true;
    }
}