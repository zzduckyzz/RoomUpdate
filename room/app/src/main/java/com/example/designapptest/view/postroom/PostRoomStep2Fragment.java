package com.example.designapptest.view.postroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.designapptest.R;
import com.example.designapptest.view.room.PostRoomActivity;

public class PostRoomStep2Fragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //Biến filnal lưu tên fragment
    public static final String FRAG_NAME = "POST_ROOM_STEP_2";
    //End biến filnal lưu tên fragment

    //Biến final share dữ liệu
    public final static String SHARE_GENDER_ROOM = "GENDER_ROOM";
    public final static String SHARE_TYPEID = "TYPEID";
    public final static String SHARE_CURRENTNUMBER = "CURRENTNUMBER";
    public final static String SHARE_MAXNUMBER = "MAXNUMBER";
    public final static String SHARE_WIDTH = "WITDTH";
    public final static String SHARE_LENGTH = "LENGTH";
    public final static String SHARE_RPICEROOM = "PRICEROOM";
    public final static String SHARE_ELECTRICB = "ELECTRICB";
    public final static String SHARE_WARTERB = "WARTERB";
    public final static String SHARE_INTERNETB = "INTERNETB";
    public final static String SHARE_PARKINGB = "PARKINGB";
    //End Biến final share dữ liệu

    //Biến sharedPreferences để truyền dữ liệu giữa các fragment trong viewpager
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    //End Biến sharedPreferences để truyền dữ liệu giữa các fragment trong viewpager

    RadioButton rBtnType1PushRoom, rBtnType2PushRoom, rBtnType3PushRoom, rBtnType4PushRoom, rBtnMale, rBtnFemale;
    EditText edtNumberPeoplePushRoom, edtLengthPushRoom, edtWidthPushRoom;
    EditText edtPriceRoomPushRoom, edtElectricBillPushRoom, edtWaterBillPushRoom, edtInternetPushRoom, edtParkingPushRoom;
    CheckBox chBoxFreeElectricPushRoom, chBoxFreeWaterPushRoom, chBoxFreeInternetPushRoom, chBoxFreeParkingPushRoom;
    Button btnNextStep2PostRoom;

    //Biến global
    boolean genderRoom;
    String typeID;
    long currentNumber, maxNumber;
    float width, length;
    float priceRomm, electricBill, warterBill, InternetBill, parkingBill;
    //End biến global

    //Activity chứa viewpager
    PostRoomActivity postRoom;

    public PostRoomStep2Fragment() {

    }


    //Hàm lấy dữ liệu từ control
    private void getDataFromControl() {

        typeID = IDTypeOfRoom();

        genderRoom = rBtnMale.isChecked();

        currentNumber = 0;
        maxNumber = Long.parseLong(edtNumberPeoplePushRoom.getText().toString());
        width = Float.parseFloat(edtWidthPushRoom.getText().toString());
        length = Float.parseFloat(edtLengthPushRoom.getText().toString());

        priceRomm = Float.parseFloat(edtPriceRoomPushRoom.getText().toString());
        electricBill = Float.parseFloat(edtElectricBillPushRoom.getText().toString());
        warterBill = Float.parseFloat(edtWaterBillPushRoom.getText().toString());
        InternetBill = Float.parseFloat(edtInternetPushRoom.getText().toString());
        parkingBill = Float.parseFloat(edtParkingPushRoom.getText().toString());
    }

    //Hàm kiểm tra dữ liệu đúng truyền vào từ control
    private boolean checkTrueDataFromControl() {
        return !edtNumberPeoplePushRoom.getText().toString().matches("")
                && !edtWidthPushRoom.getText().toString().matches("")
                && !edtLengthPushRoom.getText().toString().matches("")
                && !edtPriceRoomPushRoom.getText().toString().matches("")
                && !edtElectricBillPushRoom.getText().toString().matches("")
                && !edtWaterBillPushRoom.getText().toString().matches("")
                && !edtInternetPushRoom.getText().toString().matches("")
                && !edtParkingPushRoom.getText().toString().matches("");
    }

    //Lưu dữ liệu để truyền giữa các fragment
    private void saveDataToPreference() {
        sharedpreferences = this.getActivity().getSharedPreferences(PostRoomActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        editor.putBoolean(SHARE_GENDER_ROOM, genderRoom);
        editor.putString(SHARE_TYPEID, typeID);

        editor.putLong(SHARE_CURRENTNUMBER, currentNumber);
        editor.putLong(SHARE_MAXNUMBER, maxNumber);

        editor.putFloat(SHARE_LENGTH, length);
        editor.putFloat(SHARE_WIDTH, width);

        editor.putFloat(SHARE_RPICEROOM, priceRomm);
        editor.putFloat(SHARE_ELECTRICB, electricBill);
        editor.putFloat(SHARE_INTERNETB, InternetBill);
        editor.putFloat(SHARE_WARTERB, warterBill);
        editor.putFloat(SHARE_PARKINGB, parkingBill);

        editor.commit();
    }

    //Hàm trả về loại id loại phòng
    private String IDTypeOfRoom() {
        if (rBtnType1PushRoom.isChecked()) {
            //Trọ
            return "RTID3";
        }
        if (rBtnType2PushRoom.isChecked()) {
            //Ký túc xá
            return "RTID0";
        }
        if (rBtnType3PushRoom.isChecked()) {
            //Chung cư
            return "RTID2";
        }
        if (rBtnType4PushRoom.isChecked()) {
            //Nhà nguyên căn
            return "RTID1";
        }

        return "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.post_room_step_2_view, container, false);
        //Liên kết các giá trị trên control
        initControl(view);

        //Lấy ra activity hiện tại
        postRoom = (PostRoomActivity) getContext();
        return view;
    }


    //Hàm khởi tạo các control
    private void initControl(View view) {
        rBtnType1PushRoom = view.findViewById(R.id.rBtn_type1_push_room);
        rBtnType2PushRoom = view.findViewById(R.id.rBtn_type2_push_room);
        rBtnType3PushRoom = view.findViewById(R.id.rBtn_type3_push_room);
        rBtnType4PushRoom = view.findViewById(R.id.rBtn_type4_push_room);
        rBtnMale = view.findViewById(R.id.rBtn_male);
        rBtnFemale = view.findViewById(R.id.rBtn_female);

        edtNumberPeoplePushRoom = view.findViewById(R.id.edt_number_people_push_room);
        edtLengthPushRoom = view.findViewById(R.id.edt_length_push_room);
        edtWidthPushRoom = view.findViewById(R.id.edt_width_push_room);

        edtPriceRoomPushRoom = view.findViewById(R.id.edt_priceRoom_push_room);
        edtElectricBillPushRoom = view.findViewById(R.id.edt_electricBill_push_room);
        edtWaterBillPushRoom = view.findViewById(R.id.edt_waterBill_push_room);
        edtInternetPushRoom = view.findViewById(R.id.edt_internet_push_room);
        edtParkingPushRoom = view.findViewById(R.id.edt_parking_push_room);

        chBoxFreeElectricPushRoom = view.findViewById(R.id.chBox_freeElectric_push_room);
        chBoxFreeWaterPushRoom = view.findViewById(R.id.chBox_freeWater_push_room);
        chBoxFreeInternetPushRoom = view.findViewById(R.id.chBox_freeInternet_push_room);
        chBoxFreeParkingPushRoom = view.findViewById(R.id.chBox_freeParking_push_room);

        chBoxFreeElectricPushRoom.setOnCheckedChangeListener(this);
        chBoxFreeWaterPushRoom.setOnCheckedChangeListener(this);
        chBoxFreeInternetPushRoom.setOnCheckedChangeListener(this);
        chBoxFreeParkingPushRoom.setOnCheckedChangeListener(this);

        btnNextStep2PostRoom = view.findViewById(R.id.btn_nextStep2_post_room);

        btnNextStep2PostRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_nextStep2_post_room:

                if (checkTrueDataFromControl()) {
                    //Lấy dữ liệu từ control
                    getDataFromControl();

                    //Lưu dữ liệu xuống data
                    saveDataToPreference();

                    //Làm đổi màu xanh
                    changeColorInActivity(true);

                    //Chuyển sang page kế tiếp
                    postRoom.setCurrentPage(2);
                } else {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.chBox_freeElectric_push_room:
                if (isChecked) {
                    edtElectricBillPushRoom.setText("0");
                    edtElectricBillPushRoom.setEnabled(false);
                } else {
                    edtElectricBillPushRoom.setEnabled(true);
                }
                break;

            case R.id.chBox_freeInternet_push_room:
                if (isChecked) {
                    edtInternetPushRoom.setText("0");
                    edtInternetPushRoom.setEnabled(false);
                } else {
                    edtInternetPushRoom.setEnabled(true);
                }
                break;

            case R.id.chBox_freeWater_push_room:
                if (isChecked) {
                    edtWaterBillPushRoom.setText("0");
                    edtWaterBillPushRoom.setEnabled(false);
                } else {
                    edtWaterBillPushRoom.setEnabled(true);
                }
                break;
            case R.id.chBox_freeParking_push_room:
                if (isChecked) {
                    edtParkingPushRoom.setText("0");
                    edtParkingPushRoom.setEnabled(false);
                } else {
                    edtParkingPushRoom.setEnabled(true);
                }
                break;
        }
    }

    //Hàm chuyển màu ở activity
    private void changeColorInActivity(boolean isComplete) {

        //Gọi hàm trong posrRoom Thông qua Activity
        postRoom.onMsgFromFragToPostRoom(FRAG_NAME, isComplete);
    }
}
