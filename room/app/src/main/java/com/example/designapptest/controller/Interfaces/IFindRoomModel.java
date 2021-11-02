package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.FindRoomModel;

public interface IFindRoomModel {
    public void getListFindRoom(FindRoomModel valueRoom);

    public void getSuccessNotify(int quantity);

    public void setProgressBarLoadMore();

    public void addSuccessNotify();
}
