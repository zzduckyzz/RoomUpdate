package com.example.designapptest.controller;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.adapter.AdapterRecyclerSuggestions;
import com.example.designapptest.controller.Interfaces.IDistrictFilterModel;
import com.example.designapptest.model.DistrictFilterModel;
import com.example.designapptest.R;

import java.util.ArrayList;
import java.util.List;

public class locationSearchController {
    Context context;
    DistrictFilterModel districtFilterModel;

    public locationSearchController(Context context){
        this.context = context;
        this.districtFilterModel = new DistrictFilterModel();
    }

    public void loadDistrictInData(RecyclerView recyclerSuggestion, String FilterString, boolean isSearchRoomCall){

        //Khởi tạo list lưu quận
        List<String> stringListDistrict = new ArrayList<String>();

        //Khởi tạo Adapter
        AdapterRecyclerSuggestions adapterRecyclerSuggestions = new AdapterRecyclerSuggestions(context, R.layout.element_location_recycler_view,stringListDistrict,isSearchRoomCall);
        //Set adapter
        recyclerSuggestion.setAdapter(adapterRecyclerSuggestions);

        //Tạo layout cho recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerSuggestion.setLayoutManager(layoutManager);


        IDistrictFilterModel iDistrictFilterModel = new IDistrictFilterModel() {
            @Override
            public void sendDistrict(String District) {
                stringListDistrict.add(District);
                adapterRecyclerSuggestions.notifyDataSetChanged();
            }
        };
        districtFilterModel.listDistrictLocation(FilterString,iDistrictFilterModel);


    }


}
