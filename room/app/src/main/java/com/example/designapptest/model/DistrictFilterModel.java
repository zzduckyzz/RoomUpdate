package com.example.designapptest.model;

import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.IDistrictFilterModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DistrictFilterModel {

    DatabaseReference nodeRoot;

    public DistrictFilterModel() {
        nodeRoot = FirebaseDatabase.getInstance().getReference().child("LocationRoom");
    }

    //Hàm trả về danh sách quận có trong firebase
    public void listDistrictLocation(String filterString, IDistrictFilterModel iDistrictFilterModel) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //Lọc bằng vòng for
                    if (data.getKey().toLowerCase().contains(filterString.toLowerCase())) {
                        //Kích hoạt interface và gửi dữ liệu
                        iDistrictFilterModel.sendDistrict(data.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
}
