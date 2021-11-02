package com.example.designapptest.view.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.designapptest.domain.GenderFilter;
import com.example.designapptest.R;
import com.example.designapptest.view.searchandmap.SearchViewActivity;


public class FragmentNumberPeople extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    View view;
    TextView txtNumber;
    ImageButton btnDecrease,btnPlus;
    RadioButton radMale,radFemale;
    int currentNumber = 2;
    GenderFilter currentFilter = null;

    public FragmentNumberPeople() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_number_people, container, false);
        initControl();
        return view;
    }

    private void initControl(){
        txtNumber = view.findViewById(R.id.txt_number);
        txtNumber.setText(currentNumber+"");

        btnPlus = view.findViewById(R.id.btn_plus);
        btnDecrease=view.findViewById(R.id.btn_decrease);

        btnPlus.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);

        radMale = view.findViewById(R.id.rad_male);
        radFemale = view.findViewById(R.id.rad_female);

        radMale.setOnCheckedChangeListener(this);
        radFemale.setOnCheckedChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_plus:
                currentNumber++;
                txtNumber.setText(currentNumber+"");
                senData();
                if(currentNumber >= 2){
                    btnDecrease.setEnabled(true);
                }
                break;
            case R.id.btn_decrease:
                currentNumber--;
                txtNumber.setText(currentNumber+"");
                senData();
                if(currentNumber < 2){
                    btnDecrease.setEnabled(false);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        senData();
    }


    private void senData() {
        if (currentFilter != null) {
            ((SearchViewActivity) getContext()).removeFilter(currentFilter);
        }
        currentFilter = new GenderFilter(currentNumber, radMale.isChecked());
        ((SearchViewActivity) getContext()).addFilter(currentFilter);
    }
}
