package com.example.designapptest.view.searchandmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterRecyclerFilter;
import com.example.designapptest.adapter.AdapterRecyclerSuggestions;
import com.example.designapptest.controller.Interfaces.ICallBackSearchView;
import com.example.designapptest.controller.SearchViewController;
import com.example.designapptest.domain.myFilter;
import com.example.designapptest.view.login.LoginActivity;
import com.example.designapptest.view.room.FragmentConvenient;
import com.example.designapptest.view.room.FragmentNumberPeople;
import com.example.designapptest.view.room.FragmentPrice;
import com.example.designapptest.view.room.FragmentTypeRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ICallBackSearchView {

    public final static int REQUEST_DISTRICT = 99;
    public final static String REQUEST = "requestcode";

    //Lưu lại trạng thái của 4 fragment thay vì tạo mới
    private HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    CheckBox chBoxPrice, chBoxConvenient, chBoxType, chBoxNumber;
    RecyclerView recyclerFilter, recyclerSearchRoom;
    Button btnsSubmit;
    EditText edTSearch;
    ImageButton btnDeleteAllFilter;
    TextView txtCancel;

    ProgressBar progessBarLoad;
    LinearLayout lnLtResultReturnSearchView;
    TextView txtNumberRoom;

    NestedScrollView nestedScrollSearchView;
    ProgressBar progressBarLoadMoreSearchView;

    FrameLayout fragmentContainer;

    Drawable blueUp;
    Drawable grayDown;

    int blueColor;
    int grayColor;

    List<myFilter> filterList;
    AdapterRecyclerFilter adapterRecyclerFilter;

    String district;

    SharedPreferences sharedPreferences;
    String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initData();
        initControl();
        getDistrict();
        getColor();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Gọi hàm tìm kiếm
        callSearchRoomController();
    }

    private void getDistrict() {
        Intent intent = getIntent();
        district = intent.getStringExtra(AdapterRecyclerSuggestions.INTENT_DISTRICT);

        //Set text cho district
        edTSearch.setText(district);
    }

    //Lấy màu từ resource
    private void getColor() {
        blueUp = getResources().getDrawable(R.drawable.ic_svg_up_24);
        grayDown = getResources().getDrawable(R.drawable.ic_svg_down_2_15);
        blueColor = getResources().getColor(R.color.success);
        grayColor = getResources().getColor(R.color.unsuccess);
    }

    private void initData() {
        filterList = new ArrayList<myFilter>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DISTRICT) {
            if (resultCode == RESULT_OK) {
                //Lấy thông tin truyền về
                district = data.getStringExtra(AdapterRecyclerSuggestions.INTENT_DISTRICT);

                //Set text cho edit text
                edTSearch.setText(district);
            }
        }
    }

    private void initControl() {
        edTSearch = findViewById(R.id.edT_search);
        edTSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchViewActivity.this, LocationSearchActivity.class);
                intent.putExtra(REQUEST, REQUEST_DISTRICT);
                startActivityForResult(intent, REQUEST_DISTRICT);
            }
        });

        txtNumberRoom = findViewById(R.id.txt_number_room);
        //Ẩn text

        progessBarLoad = findViewById(R.id.progress_bar_search_view);
        //Đổi màu cho progessbar
        progessBarLoad.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        //Ẩn lần đầu
        progessBarLoad.setVisibility(View.GONE);

        lnLtResultReturnSearchView = (LinearLayout) findViewById(R.id.lnLt_resultReturn_search_view);

        nestedScrollSearchView = (NestedScrollView) findViewById(R.id.nested_scroll_search_view);
        progressBarLoadMoreSearchView = (ProgressBar) findViewById(R.id.progress_bar_load_more_search_view);
        progressBarLoadMoreSearchView.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        chBoxPrice = findViewById(R.id.chBox_price);
        chBoxConvenient = findViewById(R.id.chBox_convenient);
        chBoxType = findViewById(R.id.chBox_type);
        chBoxNumber = findViewById(R.id.chBox_number);

        chBoxPrice.setOnClickListener(this);
        chBoxConvenient.setOnClickListener(this);
        chBoxType.setOnClickListener(this);
        chBoxNumber.setOnClickListener(this);

        chBoxPrice.setOnCheckedChangeListener(this);
        chBoxConvenient.setOnCheckedChangeListener(this);
        chBoxType.setOnCheckedChangeListener(this);
        chBoxNumber.setOnCheckedChangeListener(this);

        fragmentContainer = findViewById(R.id.fragment_container);

        recyclerFilter = findViewById(R.id.recycler_filter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFilter.setLayoutManager(staggeredGridLayoutManager);

        adapterRecyclerFilter = new AdapterRecyclerFilter(this, R.layout.search_filter_element_recyclerview, filterList);
        recyclerFilter.setAdapter(adapterRecyclerFilter);

        recyclerSearchRoom = findViewById(R.id.recycler_search_room);

        btnsSubmit = findViewById(R.id.btn_submit);
        btnsSubmit.setOnClickListener(this);
        //Ẩn nút bấm lần đầu khởi tạo
        btnsSubmit.setVisibility(View.GONE);

        btnDeleteAllFilter = findViewById(R.id.btn_delete_all_filter);
        btnDeleteAllFilter.setOnClickListener(this);

        txtCancel = findViewById(R.id.txt_cancel);
        txtCancel.setOnClickListener(this);

    }

    private void getDataFromControl() {
        district = edTSearch.getText().toString();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            getDataFromControl();
            callSearchRoomController();
        } else if (id == R.id.btn_delete_all_filter) {
            removeAllFilter();
        } else if (id == R.id.txt_cancel) {
            //Hủy màn hình
            finish();
        } else {
            boolean isChecked = ((CheckBox) v).isChecked();
            if (isChecked) {
                //Hiện fragment
                fragmentContainer.setVisibility(View.VISIBLE);

                //Hiện nút bấm
                btnsSubmit.setVisibility(View.VISIBLE);

                //Replace fragment
                switch (id) {
                    case R.id.chBox_price:
                        GroupCheckbox(0);
                        changeFragment(0);
                        break;
                    case R.id.chBox_convenient:
                        GroupCheckbox(1);
                        changeFragment(1);
                        break;
                    case R.id.chBox_type:
                        GroupCheckbox(2);
                        changeFragment(2);
                        break;
                    case R.id.chBox_number:
                        GroupCheckbox(3);
                        changeFragment(3);
                        break;
                }
            } else {
                //Ẩn fragment
                fragmentContainer.setVisibility(View.GONE);
                //Ẩn nút bấm
                btnsSubmit.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.chBox_price:
                setColorForCheckBox(chBoxPrice, isChecked);
                break;
            case R.id.chBox_convenient:
                setColorForCheckBox(chBoxConvenient, isChecked);
                break;
            case R.id.chBox_type:
                setColorForCheckBox(chBoxType, isChecked);
                break;
            case R.id.chBox_number:
                setColorForCheckBox(chBoxNumber, isChecked);
                break;
        }
    }

    private void setColorForCheckBox(CheckBox chBox, boolean isChecked) {
        if (isChecked) {
            chBox.setTextColor(blueColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                chBox.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, blueUp, null);
            }
        } else {
            chBox.setTextColor(grayColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                chBox.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, grayDown, null);
            }
        }
    }

    //Group checkbox
    private void GroupCheckbox(int i) {
        switch (i) {
            case 0:
                chBoxConvenient.setChecked(false);
                chBoxType.setChecked(false);
                chBoxNumber.setChecked(false);
                break;
            case 1:
                chBoxPrice.setChecked(false);
                chBoxType.setChecked(false);
                chBoxNumber.setChecked(false);
                break;
            case 2:
                chBoxPrice.setChecked(false);
                chBoxConvenient.setChecked(false);
                chBoxNumber.setChecked(false);
                break;
            case 3:
                chBoxPrice.setChecked(false);
                chBoxConvenient.setChecked(false);
                chBoxType.setChecked(false);
                break;
        }
    }

    //Hàm thay đổi fragment
    private void changeFragment(int i) {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Lấy fragment từ hashmap nếu đã tồn tại ngược lại tạo mới và push vào hash map
        if (fragmentHashMap.get(i) != null) {
            fragmentTransaction.replace(R.id.fragment_container, fragmentHashMap.get(i));
        } else {
            switch (i) {
                case 0:
                    fragment = new FragmentPrice();
                    fragmentHashMap.put(0, fragment);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    break;
                case 1:
                    fragment = new FragmentConvenient();
                    fragmentHashMap.put(1, fragment);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    break;
                case 2:
                    fragment = new FragmentTypeRoom();
                    fragmentHashMap.put(2, fragment);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    break;
                case 3:
                    fragment = new FragmentNumberPeople();
                    fragmentHashMap.put(3, fragment);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    break;
            }
        }

        fragmentTransaction.commit();
    }


    @Override
    public void addFilter(myFilter filter) {
        filterList.add(filter);
        adapterRecyclerFilter.notifyDataSetChanged();
    }

    @Override
    public void replaceFilter(myFilter filter) {

    }

    @Override
    public void removeFilter(myFilter filter) {
        filterList.remove(filter);
        adapterRecyclerFilter.notifyDataSetChanged();
    }

    //Hàm gọi hàm tìm kiếm trong controller
    private void callSearchRoomController() {
        //Hiện progess bar
        progessBarLoad.setVisibility(View.VISIBLE);
        //Ẩn số lượng kết quả
        lnLtResultReturnSearchView.setVisibility(View.GONE);
        // Ẩn progress bar load more
        progressBarLoadMoreSearchView.setVisibility(View.GONE);

        SearchViewController controller = new SearchViewController(this, district, filterList, UID);
        controller.loadSearchRoom(recyclerSearchRoom, txtNumberRoom, progessBarLoad, lnLtResultReturnSearchView,
                nestedScrollSearchView, progressBarLoadMoreSearchView);
    }

    private void removeAllFilter() {
        filterList.removeAll(filterList);
        adapterRecyclerFilter.notifyDataSetChanged();
    }
}
