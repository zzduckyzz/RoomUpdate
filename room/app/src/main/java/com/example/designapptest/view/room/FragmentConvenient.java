package com.example.designapptest.view.room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterRecyclerChBoxConvenient;
import com.example.designapptest.domain.ConvenientFilter;

import java.util.ArrayList;
import java.util.List;


public class FragmentConvenient extends Fragment {

    View view;
    RecyclerView recyclerConvenient;
    List<ConvenientFilter> convenientFilterList;

    public FragmentConvenient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_convenient, container, false);

        initData();
        initControl();
        return view;
    }

    private void initControl() {
        recyclerConvenient = view.findViewById(R.id.recycler_convenient);
        Log.d("mycheck", convenientFilterList.size() + "");

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerConvenient.setLayoutManager(staggeredGridLayoutManager);

        AdapterRecyclerChBoxConvenient adapterRecyclerChBoxConvenient = new AdapterRecyclerChBoxConvenient(getContext(), R.layout.convenient_chbox_element_recyclerview, convenientFilterList);
        recyclerConvenient.setAdapter(adapterRecyclerChBoxConvenient);


    }

    private void initData() {
        convenientFilterList = new ArrayList<ConvenientFilter>();

        String[] name = {"Máy lạnh", "WC riêng", "Tủ quần áo", "Cửa sổ", "Nước nóng", "Chỗ để xe", "Wifi", "Tự do",
                "Thú cưng", "Tủ lạnh", "Máy giặt", "An ninh", "Giường"};
        String[] id = {"IDC0", "IDC1", "IDC10", "IDC11", "IDC12", "IDC2", "IDC3", "IDC4",
                "IDC5", "IDC6", "IDC7", "IDC8", "IDC9"};
        int[] resrouce = {R.drawable.ic_svg_aircondition_24, R.drawable.ic_svg_wc_24, R.drawable.ic_svg_wardrobe_24, R.drawable.ic_svg_window_24,
                R.drawable.ic_svg_waterheater_24, R.drawable.ic_svg_motobike_24, R.drawable.ic_svg_wifi_24, R.drawable.ic_svg_clock_24,
                R.drawable.ic_svg_pet_24, R.drawable.ic_svg_fridge_24, R.drawable.ic_svg_washmachine_24, R.drawable.ic_svg_security_24,
                R.drawable.ic_svg_bed_24};

        for (int i = 0; i < 13; i++) {
            ConvenientFilter convenientFilter = new ConvenientFilter(name[i], id[i], resrouce[i]);
            convenientFilterList.add(convenientFilter);
        }
    }
}
