package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import com.example.designapptest.controller.Interfaces.IBookRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookRoomModel implements Parcelable {
    String ID;
    String ID_User;
    String ID_Room;
    String ID_Status;
    String OrderAt;
    String NameRoom;
    String City;
    String County;
    String Description;
    String Street;
    String Ward;
    String RentalCost;
    String Phone;
    String AuthID;
    String NameUser;
    String PhoneUser;

    public BookRoomModel(String ID, String ID_User, String ID_Room, String ID_Status,
                         String orderAt, String NameRoom, String City, String County, String Description,
                         String Street, String Ward, String RentalCost, String Phone, String AuthID, String NameUser, String PhoneUser) {
        this.ID = ID;
        this.ID_User = ID_User;
        this.ID_Room = ID_Room;
        this.ID_Status = ID_Status;
        this.OrderAt = orderAt;
        this.NameRoom = NameRoom;
        this.City = City;
        this.County = County;
        this.Description = Description;
        this.Street = Street;
        this.Ward = Ward;
        this.RentalCost = RentalCost;
        this.Phone = Phone;
        this.AuthID = AuthID;
        this.NameUser = NameUser;
        this.PhoneUser = PhoneUser;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID_User() {
        return ID_User;
    }

    public void setID_User(String ID_User) {
        this.ID_User = ID_User;
    }

    public String getID_Room() {
        return ID_Room;
    }

    public void setID_Room(String ID_Room) {
        this.ID_Room = ID_Room;
    }

    public String getID_Status() {
        return ID_Status;
    }

    public void setID_Status(String ID_Status) {
        this.ID_Status = ID_Status;
    }

    public String getOrderAt() {
        return OrderAt;
    }

    public void setOrderAt(String orderAt) {
        OrderAt = orderAt;
    }

    public String getNameRoom() {
        return NameRoom;
    }

    public void setNameRoom(String nameRoom) {
        NameRoom = nameRoom;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public String getRentalCost() {
        return RentalCost;
    }

    public void setRentalCost(String rentalCost) {
        RentalCost = rentalCost;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAuthID() {
        return AuthID;
    }

    public void setAuthID(String authID) {
        AuthID = authID;
    }

    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getPhoneUser() {
        return PhoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        PhoneUser = phoneUser;
    }

    protected BookRoomModel(Parcel in) {
        ID = in.readString();
        ID_User = in.readString();
        ID_Room = in.readString();
        ID_Status = in.readString();
        OrderAt = in.readString();
        NameRoom = in.readString();
        City = in.readString();
        County = in.readString();
        Street = in.readString();
        Ward = in.readString();
        RentalCost = in.readString();
        Description = in.readString();
        Phone = in.readString();
        AuthID = in.readString();
        NameUser = in.readString();
        PhoneUser = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(ID_Room);
        dest.writeString(ID_User);
        dest.writeString(ID_Status);
        dest.writeString(OrderAt);
        dest.writeString(NameRoom);
        dest.writeString(City);
        dest.writeString(County);
        dest.writeString(Street);
        dest.writeString(Ward);
        dest.writeString(RentalCost);
        dest.writeString(Description);
        dest.writeString(Phone);
        dest.writeString(AuthID);
        dest.writeString(NameUser);
        dest.writeString(PhoneUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookRoomModel> CREATOR = new Creator<BookRoomModel>() {
        @Override
        public BookRoomModel createFromParcel(Parcel in) {
            return new BookRoomModel(in);
        }

        @Override
        public BookRoomModel[] newArray(int size) {
            return new BookRoomModel[size];
        }
    };


    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    public BookRoomModel() {
        //Trả về node gốc của database
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public void addBookRoom(BookRoomModel bookRoomModel, IBookRoom iBookRoom) {
        DatabaseReference nodeBookRoom = FirebaseDatabase.getInstance().getReference().child("BookRoom");
        String bookID = nodeBookRoom.child(ID).push().getKey();

        nodeBookRoom.child(bookRoomModel.ID).child(bookRoomModel.ID_Room).setValue(bookRoomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iBookRoom.makeToast("Đặt phòng thành công");
                    iBookRoom.setView();
                }
            }
        });
    }

}
