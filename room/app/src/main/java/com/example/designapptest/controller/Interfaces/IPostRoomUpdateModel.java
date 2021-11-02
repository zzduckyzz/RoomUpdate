package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.RoomModel;

public interface IPostRoomUpdateModel {
    public void getSuccessNotifyPostRoomStep1();

    public void getSuccessNotifyPostRoomStep2();

    public void getSuccessNotifyPostRoomStep3();

    public void getSuccessNotifyPostRoomStep4();

    public void getRoomFollowId(RoomModel roomModel);
}
