package com.example.designapptest.view.room;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.designapptest.controller.FindRoomAddController;
import com.example.designapptest.model.FindRoomModel;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FindRoomAddActivity extends AppCompatActivity {

    FindRoomAddController findRoomAddController;

    // Người dùng
    ImageView imgAvatarUser, imgGenderUser;
    TextView txtNameUser;

    // Giới tính
    RadioButton rBtnMale, rBtnFemale;

    // Khoảng giá
    RangeSeekBar RangePrice;
    double minPrice, maxPrice;
    TextView txtMinPrice, txtMaxPrice;

    CheckBox chBoxD1, chBoxD2, chBoxD3, chBoxD4, chBoxD5, chBoxD6, chBoxD7, chBoxD8, chBoxD9,
            chBoxD10, chBoxD11, chBoxD12, chBoxDThuDuc, chBoxDTanBinh, chBoxDBinhTan, chBoxDGoVap,
            chBoxDBinhThanh, chBoxDTanPhu, chBoxDPhuNhuan;
    List<CheckBox> lstCheckboxLocationSearch = new ArrayList<>();
    List<String> lstLocationSearchs;

    CheckBox chBoxWc, chBoxWifi, chBoxClock, chBoxBed, chBoxFridge, chBoxWardrobe,
            chBoxPet, chBoxWindow, chBoxParking, chBoxWashmachine, chBoxWaterheater, chBoxSecurity, chBoxArcondition;
    List<String> lstConvenients;

    // Áp dụng bộ lọc
    Button btnPostFindRoom;

    ProgressBar progressBarFindRoomAdd;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room_add_view);

        // Lấy id của người dùng hiện tại
        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setAboutPrice();

        findRoomAddController = new FindRoomAddController(this);

        // Show thông tin của chủ bài đăng tìm ở ghép
        findRoomAddController.setUserOwnerFindRoom(UID, imgAvatarUser, imgGenderUser, txtNameUser, progressBarFindRoomAdd);

        clickPostFindRoom(findRoomAddController);

    }
    //End load dữ liệu vào danh sách trong lần đầu chạy

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initControl() {
        toolbar = findViewById(R.id.toolbar);

        progressBarFindRoomAdd = (ProgressBar) findViewById(R.id.progress_find_room_add);
        progressBarFindRoomAdd.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        // Các control của người dùng hiện tại
        imgAvatarUser = (ImageView) findViewById(R.id.img_avatar_user_find_room_add);
        imgGenderUser = (ImageView) findViewById(R.id.img_gender_user_find_room_add);
        txtNameUser = (TextView) findViewById(R.id.txt_name_user_find_room_add);

        // Khoảng giá trong tìm ở ghép
        txtMinPrice = findViewById(R.id.txt_min_price_find_room_add);
        txtMaxPrice = findViewById(R.id.txt_max_price_find_room_add);
        RangePrice = findViewById(R.id.range_price_find_room_add);

        // Giới tính
        rBtnMale = (RadioButton) findViewById(R.id.rBtn_male_find_room_add);
        rBtnFemale = (RadioButton) findViewById(R.id.rBtn_female_find_room_add);

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
        chBoxWardrobe = findViewById(R.id.chBox_wardrobe);
        chBoxPet = findViewById(R.id.chBox_pet);
        chBoxWindow = findViewById(R.id.chBox_window);
        chBoxParking = findViewById(R.id.chBox_parking);
        chBoxWashmachine = findViewById(R.id.chBox_washmachine);
        chBoxWaterheater = findViewById(R.id.chBox_waterheater);
        chBoxSecurity = findViewById(R.id.chBox_security);
        chBoxArcondition = findViewById(R.id.chBox_arcondition);

        btnPostFindRoom = (Button) findViewById(R.id.btn_post_find_room_add);
    }

    // Khởi tạo giá trị cho các control
    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Thêm tìm ở ghép");
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

    private FindRoomModel getNewFindRoom() {
        // Id của người đăng tìm kiếm ỏ ghép.
        String userID = UID;

        // thời gian đăng.
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        //Tao moi list các tiện nghi
        lstConvenients = new ArrayList<String>();
        //Them vao theo ma neu nhu check dung
        if (chBoxWc.isChecked() == true) {
            //Wc rieng
            lstConvenients.add("IDC1");
        }
        if (chBoxWifi.isChecked() == true) {
            //co wifi
            lstConvenients.add("IDC3");
        }
        if (chBoxClock.isChecked() == true) {
            //tu do
            lstConvenients.add("IDC4");
        }
        if (chBoxBed.isChecked() == true) {
            //giuong ngu
            lstConvenients.add("IDC9");
        }
        if (chBoxFridge.isChecked() == true) {
            //Tu lanh
            lstConvenients.add("IDC6");
        }
        if (chBoxWardrobe.isChecked() == true) {
            //Tu quan cao
            lstConvenients.add("IDC10");
        }
        if (chBoxPet.isChecked() == true) {
            //cho phep co thu cung
            lstConvenients.add("IDC5");
        }
        if (chBoxWindow.isChecked() == true) {
            //co cua so
            lstConvenients.add("IDC11");
        }
        if (chBoxParking.isChecked() == true) {
            //Cho de xe
            lstConvenients.add("IDC2");
        }
        if (chBoxWashmachine.isChecked() == true) {
            //co may giat
            lstConvenients.add("IDC7");
        }
        if (chBoxWaterheater.isChecked() == true) {
            //co may nuoc nong
            lstConvenients.add("IDC12");
        }
        if (chBoxSecurity.isChecked() == true) {
            //an ninh
            lstConvenients.add("IDC8");
        }
        if (chBoxArcondition.isChecked() == true) {
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
        boolean gender;
        if (rBtnMale.isSelected() == true) {
            gender = true;
        } else {
            gender = false;
        }

        FindRoomModel findRoomModel = new FindRoomModel(userID, date, minPrice, maxPrice, gender, null, lstConvenients, lstLocationSearchs);

        return findRoomModel;
    }

    private void clickPostFindRoom(FindRoomAddController findRoomAddController) {
        btnPostFindRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindRoomModel findRoomModel = getNewFindRoom();

                findRoomAddController.addNewFindRoom(findRoomModel);
            }
        });
    }
}
