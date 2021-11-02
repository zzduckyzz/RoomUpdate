package com.example.designapptest.domain;

public class GenderFilter extends myFilter {
    int maxNumber;
    boolean gender;

    public GenderFilter(){

    }

    public GenderFilter(int maxNumber, boolean gender) {
        if(gender){
            this.name = maxNumber+" Nam";
        }else {
            this.name = maxNumber+" Nữ";
        }
        this.maxNumber = maxNumber;
        this.gender = gender;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public void replace(myFilter filter) {

    }
}
