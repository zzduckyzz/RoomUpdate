package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.ReportedRoomModel;

public interface IReportedRoomModel {
    public void makeToast(String message);
    public void getListReportRooms(ReportedRoomModel reportedRoomModel);
    public void setProgressBarLoadMore();
    public void setQuantityTop(int quantity);
    public void setQuantityLoadMore(int quantityLoaded);
}
