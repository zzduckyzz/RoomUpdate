package com.example.designapptest.controller.Interfaces;

import com.example.designapptest.domain.myFilter;


public interface ICallBackSearchView {
    public void addFilter(myFilter filter);
    public void replaceFilter(myFilter filter);
    public void removeFilter(myFilter filter);
}
