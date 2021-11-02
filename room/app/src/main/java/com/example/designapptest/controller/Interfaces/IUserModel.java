package com.example.designapptest.controller.Interfaces;


import com.example.designapptest.model.UserModel;

public interface IUserModel {
    public void getListUsers(UserModel valueUser);
    public void setProgressBarLoadMore();
    public void setQuantityTop(int quantity);
    public void setSumHostsAdminView(long quantity);
}
