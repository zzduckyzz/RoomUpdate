package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.RoomModel;

public interface IMainRoomModel {
    public void getListMainRoom(RoomModel valueRoom);
    public void makeToast(String message);
    public void setIconFavorite(int iconRes);
    public void setButtonLoadMoreVerifiedRooms();
    public void setProgressBarLoadMore();
    public void setQuantityTop(int quantity);
    public void setQuantityLoadMore(int quantityLoaded);
    public void setSumRoomsAdminView(long quantity);
}
