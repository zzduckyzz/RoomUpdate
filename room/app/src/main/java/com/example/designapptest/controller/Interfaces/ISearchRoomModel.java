package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.RoomModel;

public interface ISearchRoomModel {
    public void sendDataRoom(RoomModel roomModel,boolean ishadFound);
    public void setProgressBarLoadMore();
    public void setQuantityTop(int quantity);
}
