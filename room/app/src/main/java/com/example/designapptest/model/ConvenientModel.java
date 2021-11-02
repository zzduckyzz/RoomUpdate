package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ConvenientModel implements Parcelable {
    String name;
    String convenientID;
    String imageName;

    protected ConvenientModel(Parcel in) {
        name = in.readString();
        convenientID = in.readString();
        imageName = in.readString();
    }

    public static final Creator<ConvenientModel> CREATOR = new Creator<ConvenientModel>() {
        @Override
        public ConvenientModel createFromParcel(Parcel in) {
            return new ConvenientModel(in);
        }

        @Override
        public ConvenientModel[] newArray(int size) {
            return new ConvenientModel[size];
        }
    };

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConvenientID() {
        return convenientID;
    }

    public void setConvenientID(String convenientID) {
        this.convenientID = convenientID;
    }

    public ConvenientModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(convenientID);
        dest.writeString(imageName);
    }
}
