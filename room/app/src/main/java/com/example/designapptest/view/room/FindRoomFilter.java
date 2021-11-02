package com.example.designapptest.view.room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.designapptest.R;
import com.example.designapptest.model.FindRoomFilterModel;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

public class FindRoomFilter extends AppCompatActivity {
    RangeSeekBar RangePrice;
    double minPrice, maxPrice;
    TextView txtMinPrice, txtMaxPrice;

    RadioButton rBtnMale, rBtnFemale, rBtnAll;
    int gender;

    CheckBox chBoxD1, chBoxD2, chBoxD3, chBoxD4, chBoxD5, chBoxD6, chBoxD7, chBoxD8, chBoxD9,
            chBoxD10, chBoxD11, chBoxD12, chBoxDThuDuc, chBoxDTanBinh, chBoxDBinhTan, chBoxDGoVap,
            chBoxDBinhThanh, chBoxDTanPhu, chBoxDPhuNhuan;
    List<CheckBox> lstCheckboxLocationSearch = new ArrayList<>();
    List<String> lstLocationSearchs;

    CheckBox chBoxWc,chBoxWifi,chBoxClock,chBoxBed,chBoxFridge,chBoxWardrobe,
            chBoxPet,chBoxWindow,chBoxParking,chBoxWashmachine,chBoxWaterheater,chBoxSecurity,chBoxArcondition;
    List<String> lstConvenients;

    Button btnApplyFindRoomFilter;

    Context context;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room_filter_view);

        context = getParent();

        initControl();
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setAboutPrice();

        clickApplyFilter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Khởi tạo các giá trị cho control
    private void initControl() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Khoảng giá
        txtMinPrice = findViewById(R.id.txt_min_price_find_room_filter);
        txtMaxPrice = findViewById(R.id.txt_max_price_find_room_filter);
        RangePrice = findViewById(R.id.range_price_find_room_filter);

        // Giới tính
        rBtnMale = (RadioButton) findViewById(R.id.rBtn_male_find_room_filter);
        rBtnFemale = (RadioButton) findViewById(R.id.rBtn_female_find_room_filter);
        rBtnAll = (RadioButton) findViewById(R.id.rBtn_all_find_room_filter);

        // Khởi tạo control của vị trí tìm kiếm
        chBoxD1 = findViewById(R.id.chBox_D1);
        chBoxD2 = findViewById(R.id.chBox_D2);
        chBoxD3 = findViewById(R.id.chBox_D3);
        chBoxD4 = findViewById(R.id.chBox_D4);
        chBoxD5 = findViewById(R.id.chBox_D5);
        chBoxD6 = findViewById(R.id.chBox_D6);
        chBoxD7 = findViewById(R.id.chBox_D7);
        chBoxD8 = findViewById(R.id.chBox_D8);
        chBoxD9 = findViewById(R.id.chBox_D9);
        chBoxD10 = findViewById(R.id.chBox_D10);
        chBoxD11 = findViewById(R.id.chBox_D11);
        chBoxD12 = findViewById(R.id.chBox_D12);
        chBoxDThuDuc = findViewById(R.id.chBox_DThuDuc);
        chBoxDTanBinh = findViewById(R.id.chBox_DTanBinh);
        chBoxDBinhTan = findViewById(R.id.chBox_DBinhTan);
        chBoxDGoVap = findViewById(R.id.chBox_DGoVap);
        chBoxDBinhThanh = findViewById(R.id.chBox_DBinhThanh);
        chBoxDTanPhu = findViewById(R.id.chBox_DTanPhu);
        chBoxDPhuNhuan = findViewById(R.id.chBox_DPhuNhuan);

        lstCheckboxLocationSearch.add(chBoxD1);
        lstCheckboxLocationSearch.add(chBoxD2);
        lstCheckboxLocationSearch.add(chBoxD3);
        lstCheckboxLocationSearch.add(chBoxD4);
        lstCheckboxLocationSearch.add(chBoxD5);
        lstCheckboxLocationSearch.add(chBoxD6);
        lstCheckboxLocationSearch.add(chBoxD7);
        lstCheckboxLocationSearch.add(chBoxD8);
        lstCheckboxLocationSearch.add(chBoxD9);
        lstCheckboxLocationSearch.add(chBoxD10);
        lstCheckboxLocationSearch.add(chBoxD11);
        lstCheckboxLocationSearch.add(chBoxD12);
        lstCheckboxLocationSearch.add(chBoxDThuDuc);
        lstCheckboxLocationSearch.add(chBoxDTanBinh);
        lstCheckboxLocationSearch.add(chBoxDBinhTan);
        lstCheckboxLocationSearch.add(chBoxDGoVap);
        lstCheckboxLocationSearch.add(chBoxDBinhThanh);
        lstCheckboxLocationSearch.add(chBoxDTanPhu);
        lstCheckboxLocationSearch.add(chBoxDPhuNhuan);

        // Khởi tạo các control của tiện nghi
        chBoxWc = findViewById(R.id.chBox_wc);
        chBoxWifi = findViewById(R.id.chBox_wifi);
        chBoxClock = findViewById(R.id.chBox_clock);
        chBoxBed = findViewById(R.id.chBox_bed);
        chBoxFridge = findViewById(R.id.chBox_fridge);
        chBoxWardrobe =findViewById(R.id.chBox_wardrobe);
        chBoxPet = findViewById(R.id.chBox_pet);
        chBoxWindow = findViewById(R.id.chBox_window);
        chBoxParking = findViewById(R.id.chBox_parking);
        chBoxWashmachine = findViewById(R.id.chBox_washmachine);
        chBoxWaterheater = findViewById(R.id.chBox_waterheater);
        chBoxSecurity = findViewById(R.id.chBox_security);
        chBoxArcondition = findViewById(R.id.chBox_arcondition);

        btnApplyFindRoomFilter = (Button) findViewById(R.id.btn_apply_find_room_filter);
    }

    // Khởi tạo giá trị cho các control
    private void initData() {
        // Gán các giá trị toolbar.
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Bộ lọc tìm ở ghép");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        minPrice = (float) 0.5;
        maxPrice = 10;

        txtMinPrice.setText(minPrice + " triệu");
        txtMaxPrice.setText(maxPrice + " triệu");
        RangePrice.setSelectedMinValue(20);
        RangePrice.setSelectedMaxValue(1);
    }

    // Hàm sự kiện người dùng cài đặt khoản giá
    private void setAboutPrice() {
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
                txtMinPrice.setText(minPrice + " triệu");
                txtMaxPrice.setText(maxPrice + " triệu");
                //End hiển thị ra cho user
            }
        });
    }

    // Hàm ấy các giá trị khi người dùng chọn.
    private FindRoomFilterModel getValueFilter() {
        //Tao moi list các tiện nghi
        lstConvenients = new ArrayList<String>();
        //Them vao theo ma neu nhu check dung
        if(chBoxWc.isChecked()==true){
            //Wc rieng
            lstConvenients.add("IDC1");
        }
        if(chBoxWifi.isChecked()==true){
            //co wifi
            lstConvenients.add("IDC3");
        }
        if(chBoxClock.isChecked()==true){
            //tu do
            lstConvenients.add("IDC4");
        }
        if(chBoxBed.isChecked()==true){
            //giuong ngu
            lstConvenients.add("IDC9");
        }
        if(chBoxFridge.isChecked()==true){
            //Tu lanh
            lstConvenients.add("IDC6");
        }
        if(chBoxWardrobe.isChecked()==true){
            //Tu quan cao
            lstConvenients.add("IDC10");
        }
        if(chBoxPet.isChecked()==true){
            //cho phep co thu cung
            lstConvenients.add("IDC5");
        }
        if(chBoxWindow.isChecked()==true){
            //co cua so
            lstConvenients.add("IDC11");
        }
        if(chBoxParking.isChecked()==true){
            //Cho de xe
            lstConvenients.add("IDC2");
        }
        if(chBoxWashmachine.isChecked()==true){
            //co may giat
            lstConvenients.add("IDC7");
        }
        if(chBoxWaterheater.isChecked()==true){
            //co may nuoc nong
            lstConvenients.add("IDC12");
        }
        if(chBoxSecurity.isChecked()==true){
            //an ninh
            lstConvenients.add("IDC8");
        }
        if(chBoxArcondition.isChecked()==true){
            //may lanh
            lstConvenients.add("IDC0");
        }

        //Tao moi list các vị trí tìm kiếm.
        lstLocationSearchs = new ArrayList<String>();
        for (int index = 0; index < lstCheckboxLocationSearch.size(); index++) {
            if (lstCheckboxLocationSearch.get(index).isChecked()) {
                lstLocationSearchs.add(lstCheckboxLocationSearch.get(index).getText().toString());
            }
        }

        // gán giới tính.
        if (rBtnMale.isChecked()== true) {
            gender = 0;
        }
        else if (rBtnFemale.isChecked() == true) {
            gender = 1;
        }
        else {
            gender = 2;
        }

        FindRoomFilterModel findRoomFilterModel = new FindRoomFilterModel(lstConvenients, lstLocationSearchs, gender, minPrice, maxPrice);

        return  findRoomFilterModel;
    }

    // Hàm áp dụng bộ lọc.
    private void  clickApplyFilter() {
        btnApplyFindRoomFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindRoomFilterModel findRoomFilterModel = getValueFilter();

                final Intent data = new Intent();

                // Truyền data vào intent
                data.putExtra(FindRoom.SHARE_FINDROOM, findRoomFilterModel);

                // Đặt resultCode là Activity.RESULT_OK to
                // thể hiện đã thành công và có chứa kết quả trả về
                setResult(AppCompatActivity.RESULT_OK, data);

                // gọi hàm finish() để đóng Activity hiện tại và trở về MainActivity.
                finish();;
            }
        });
    }
}
