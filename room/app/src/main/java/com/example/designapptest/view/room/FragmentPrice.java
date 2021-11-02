package com.example.designapptest.view.room;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.designapptest.domain.PriceFilter;
import com.example.designapptest.R;
import com.example.designapptest.view.searchandmap.SearchViewActivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;


public class FragmentPrice extends Fragment {

    RangeSeekBar RangePrice;
    View view;
    float minPrice,maxPrice;
    TextView txtMinPrice,txtMaxPrice;

    PriceFilter priceFilter = null;

    public FragmentPrice() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_price, container, false);
        initControl();
        return view;
    }

    private void initControl(){
        txtMinPrice = view.findViewById(R.id.txt_min_price);
        txtMaxPrice = view.findViewById(R.id.txt_max_price);

        minPrice = (float) 0.5;
        maxPrice = 10;

        txtMinPrice.setText(minPrice +" triệu");
        txtMaxPrice.setText(maxPrice+" triệu");

        RangePrice = view.findViewById(R.id.range_price);
        RangePrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value = bar.getSelectedMinValue();
                Number max_value = bar.getSelectedMaxValue();

                //Lưu lại giá trị trên biến float
                minPrice = (float) (21 - (int) min_value) / 2;
                maxPrice = (float) (21 - (int) max_value) / 2;
                //End lưu lại giá trị trên biến float

                //Hiển trị ra cho user
                txtMinPrice.setText(minPrice +" triệu");
                txtMaxPrice.setText(maxPrice+" triệu");
                //End hiển thị ra cho user

                //Gửi dữ liệu qua searchview
                sendData();
            }
        });

        RangePrice.setSelectedMinValue(20);
        RangePrice.setSelectedMaxValue(1);
    }

    private void sendData() {
        if (priceFilter != null) {
            ((SearchViewActivity) getContext()).removeFilter(priceFilter);
        }
        priceFilter = new PriceFilter(minPrice, maxPrice);
        ((SearchViewActivity) getContext()).addFilter(priceFilter);
    }
}
