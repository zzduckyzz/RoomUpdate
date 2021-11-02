package com.example.designapptest.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.designapptest.controller.Interfaces.ICallBackFromAddRoom;
import com.example.designapptest.controller.Interfaces.OnPostRoomFinish;
import com.example.designapptest.model.RoomModel;

import java.util.List;

public class PostRoomStep4Controller {
    RoomModel roomModel;
    Context context;

    public PostRoomStep4Controller(Context context) {
        this.roomModel = new RoomModel();
        this.context = context;
    }

    public void callAddRoomFromModel(String UID, RoomModel dataRoom, List<String> listConvenient, List<String> listPathImg,
                                     float electricBill, float warterBill, float InternetBill, float parkingBill, ProgressDialog progressDialog, OnPostRoomFinish onPostRoomFinish) {

        ICallBackFromAddRoom iCallBackFromAddRoom = isSuccess -> {
            if (isSuccess) {
                //Stop progess
                progressDialog.dismiss();
                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                onPostRoomFinish.invoke();
            } else {
                //do nothing
            }
        };

        //Gọi hàm thêm phòng ở model
        roomModel.addRoom(UID, dataRoom, listConvenient, listPathImg, electricBill, warterBill, InternetBill, parkingBill, iCallBackFromAddRoom, context);
    }
}
