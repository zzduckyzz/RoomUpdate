package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FindRoomFilterModel implements Parcelable {

    double minPrice, maxPrice;
    int gender;

    List<String> lstConvenients;
    List<String> lstLocationSearchs;

    protected FindRoomFilterModel(Parcel in) {
        minPrice = in.readDouble();
        maxPrice = in.readDouble();
        gender = in.readInt();
        lstConvenients = in.createStringArrayList();
        lstLocationSearchs = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(minPrice);
        dest.writeDouble(maxPrice);
        dest.writeInt(gender);
        dest.writeStringList(lstConvenients);
        dest.writeStringList(lstLocationSearchs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FindRoomFilterModel> CREATOR = new Creator<FindRoomFilterModel>() {
        @Override
        public FindRoomFilterModel createFromParcel(Parcel in) {
            return new FindRoomFilterModel(in);
        }

        @Override
        public FindRoomFilterModel[] newArray(int size) {
            return new FindRoomFilterModel[size];
        }
    };

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<String> getLstConvenients() {
        return lstConvenients;
    }

    public void setLstConvenients(List<String> lstConvenients) {
        this.lstConvenients = lstConvenients;
    }

    public List<String> getLstLocationSearchs() {
        return lstLocationSearchs;
    }

    public void setLstLocationSearchs(List<String> lstLocationSearchs) {
        this.lstLocationSearchs = lstLocationSearchs;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public FindRoomFilterModel() {
    }

    public FindRoomFilterModel(List<String> lstConvenients, List<String> lstLocationSearchs, int gender, double minPrice, double maxPrice) {
        this.lstConvenients = lstConvenients;
        this.lstLocationSearchs = lstLocationSearchs;
        this.gender = gender;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

    }
}
