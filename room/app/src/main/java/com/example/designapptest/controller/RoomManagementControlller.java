package com.example.designapptest.controller;

import android.content.Context;
import android.widget.TextView;

import com.example.designapptest.controller.Interfaces.IInfoOfAllRoomUser;
import com.example.designapptest.model.RoomModel;

public class RoomManagementControlller {
    RoomModel roomModel;
    Context context;

    public RoomManagementControlller(Context context) {
        this.context = context;
        roomModel = new RoomModel();
    }

    public void loadQuantiryInfo(String UID, TextView txtQuantityRoom, TextView txtQuantityView, TextView txtQuantityCommand) {
        IInfoOfAllRoomUser iInfoOfAllRoomUser = new IInfoOfAllRoomUser() {
            @Override
            public void sendQuantity(int value, int type) {
                if (type == 0) {
                    txtQuantityRoom.setText(String.valueOf(value));
                } else if (type == 1) {
                    txtQuantityView.setText(String.valueOf(value));
                } else {
                    txtQuantityCommand.setText(String.valueOf(value));
                }
            }
        };

        roomModel.infoOfAllRoomOfUser(UID, iInfoOfAllRoomUser);
    }
}
