package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.model.ViewModel;

import java.util.List;

public interface IViewModel {
    public void getViewInfo(ViewModel viewModel);
    public void setView();
    public void setLinearLayoutTopAllView(List<ViewModel> listViewsModel);
    public void setProgressBarLoadMore();
    public void setQuantityViews(int quantity);
}
