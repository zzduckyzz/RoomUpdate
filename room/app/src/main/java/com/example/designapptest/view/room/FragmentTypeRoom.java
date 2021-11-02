package com.example.designapptest.view.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.example.designapptest.domain.TypeFilter;
import com.example.designapptest.R;
import com.example.designapptest.view.searchandmap.SearchViewActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentTypeRoom extends Fragment implements CompoundButton.OnCheckedChangeListener {

    RadioButton rBtnType1,rBtnType2,rBtnType3,rBtnType4,rBtnTypeAll;
    View view;

    List<TypeFilter> typeFilterList;
    int currentPosition;

    public FragmentTypeRoom(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_type_room, container, false);
        initData();
        initControl();
        return view;
    }

    private void initControl(){
        rBtnType1 = view.findViewById(R.id.rBtn_type1_push_room);
        rBtnType2 = view.findViewById(R.id.rBtn_type2_push_room);
        rBtnType3 = view.findViewById(R.id.rBtn_type3_push_room);
        rBtnType4 = view.findViewById(R.id.rBtn_type4_push_room);
        rBtnTypeAll = view.findViewById(R.id.rBtn_type_all);

        rBtnType1.setOnCheckedChangeListener(this);
        rBtnType2.setOnCheckedChangeListener(this);
        rBtnType3.setOnCheckedChangeListener(this);
        rBtnType4.setOnCheckedChangeListener(this);
        rBtnTypeAll.setOnCheckedChangeListener(this);
    }

    private void initData(){
        typeFilterList = new ArrayList<TypeFilter>();
        typeFilterList.add(new TypeFilter("Trọ","RTID3"));
        typeFilterList.add(new TypeFilter("Ký túc xá","RTID0"));
        typeFilterList.add(new TypeFilter("Chung cư","RTID2"));
        typeFilterList.add(new TypeFilter("Nhà nguyên căn","RTID1"));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if(isChecked){
            ((SearchViewActivity)getContext()).removeFilter(typeFilterList.get(currentPosition));
            switch (id){
                case R.id.rBtn_type1_push_room:
                    currentPosition = 0;
                    break;
                case R.id.rBtn_type2_push_room:
                    currentPosition = 1;
                    break;
                case R.id.rBtn_type3_push_room:
                    currentPosition = 2;
                    break;
                case R.id.rBtn_type4_push_room:
                    currentPosition = 3;
                    break;
            }
            if (id != R.id.rBtn_type_all) {
                ((SearchViewActivity) getContext()).addFilter(typeFilterList.get(currentPosition));
            }
        }

    }
}
