package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.UserModel;


public interface IBookRoom {
    public void getListBookRoom(UserModel userModel);
    public void makeToast(String message);
    public void setView();
}
