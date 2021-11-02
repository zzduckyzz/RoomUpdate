package com.example.designapptest.model;

import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.IMapRoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapRoomModel {

    private DatabaseReference nodeRoot;

    public MapRoomModel() {
        //Khởi tạo node root đến database
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public void listLocationRoom(IMapRoomModel iMapRoomModel) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot nodeRoom = dataSnapshot.child("Room");

                for (DataSnapshot valueRoom : nodeRoom.getChildren()) {
                    RoomModel roomModel = valueRoom.getValue(RoomModel.class);
                    //Set ID cho roomModel
                    roomModel.setRoomID(valueRoom.getKey());

                    //Gửi dữ liệu
                    iMapRoomModel.getListLocationRoom(roomModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
}
