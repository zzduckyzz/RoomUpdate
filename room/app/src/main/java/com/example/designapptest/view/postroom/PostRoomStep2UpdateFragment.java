package com.example.designapptest.view.postroom;

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

import com.example.designapptest.controller.Interfaces.IUpdateRoomModel;
import com.example.designapptest.controller.PostRoomUpdateController;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.R;

public class PostRoomStep2UpdateFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //Biến filnal lưu tên fragment
    public static final String FRAG_NAME = "POST_ROOM_STEP_2";
    //End biến filnal lưu tên fragment

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
    float priceRoom, electricBill, warterBill, InternetBill, parkingBill;
    //End biến global

    //Activity chứa viewpager
    PostRoomAdapterUpdateActivity postRoom;

    RoomModel roomModel;

    PostRoomUpdateController postRoomUpdateController;

    public PostRoomStep2UpdateFragment() {

    }

    //Hàm lấy dữ liệu từ control
    private void getDataFromControl() {

        typeID = IDTypeOfRoom();

        genderRoom = rBtnMale.isChecked();

        currentNumber = 0;
        maxNumber = Long.parseLong(edtNumberPeoplePushRoom.getText().toString());

        width = Float.parseFloat(edtWidthPushRoom.getText().toString());
        length = Float.parseFloat(edtLengthPushRoom.getText().toString());

        priceRoom = Float.parseFloat(edtPriceRoomPushRoom.getText().toString());
        electricBill = Float.parseFloat(edtElectricBillPushRoom.getText().toString());
        warterBill = Float.parseFloat(edtWaterBillPushRoom.getText().toString());
        InternetBill = Float.parseFloat(edtInternetPushRoom.getText().toString());
        parkingBill = Float.parseFloat(edtParkingPushRoom.getText().toString());
    }

    // Hàm kiểm tra xem người dùng có thay đổi thông tin không
    private boolean compareInfo() {

        if (typeID.equals(roomModel.getTypeID()) && genderRoom == roomModel.isGender()
                && maxNumber == roomModel.getMaxNumber() && width == roomModel.getWidth()
                && length == roomModel.getLength() && priceRoom == roomModel.getRentalCosts()
                && electricBill == roomModel.getListRoomPrice().get(0).getPrice()
                && warterBill == roomModel.getListRoomPrice().get(1).getPrice()
                && InternetBill == roomModel.getListRoomPrice().get(2).getPrice()
                && parkingBill == roomModel.getListRoomPrice().get(3).getPrice())
            return false;

        return true;
    }

    //Hàm kiểm tra dữ liệu đúng truyền vào từ control
    private boolean checkTrueDataFromControl() {
        if (edtNumberPeoplePushRoom.getText().toString().matches("")
                || edtWidthPushRoom.getText().toString().matches("")
                || edtLengthPushRoom.getText().toString().matches("")
                || edtPriceRoomPushRoom.getText().toString().matches("")
                || edtElectricBillPushRoom.getText().toString().matches("")
                || edtWaterBillPushRoom.getText().toString().matches("")
                || edtInternetPushRoom.getText().toString().matches("")
                || edtParkingPushRoom.getText().toString().matches("")) {
            return false;
        }
        return true;
    }

    //Hàm trả về loại id loại phòng
    private String IDTypeOfRoom() {
        if (rBtnType1PushRoom.isChecked() == true) {
            //Trọ
            return "RTID3";
        }
        if (rBtnType2PushRoom.isChecked() == true) {
            //Ký túc xá
            return "RTID0";
        }
        if (rBtnType3PushRoom.isChecked() == true) {
            //Chung cư
            return "RTID2";
        }
        if (rBtnType4PushRoom.isChecked() == true) {
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
        postRoom = (PostRoomAdapterUpdateActivity) getContext();

        roomModel = postRoom.returnRoomModel();

        // Gán giá trị cho các control
        initData();

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

    //Hàm gán giá trị từ room model cho post room step 2
    private void initData() {
        switch (roomModel.getRoomType()) {
            case "Trọ":
                rBtnType1PushRoom.setChecked(true);
                break;
            case "Ký túc xá":
                rBtnType2PushRoom.setChecked(true);
                break;
            case "Chung cư":
                rBtnType3PushRoom.setChecked(true);
                break;
            case "Nhà nguyên căn":
                rBtnType4PushRoom.setChecked(true);
                break;
        }

        if (roomModel.isGender() == true) {
            rBtnMale.setChecked(true);
        } else rBtnFemale.setChecked(true);

        edtNumberPeoplePushRoom.setText(String.valueOf(roomModel.getMaxNumber()));
        edtLengthPushRoom.setText(String.valueOf(roomModel.getLength()));
        edtWidthPushRoom.setText(String.valueOf(roomModel.getWidth()));


        // Giá phòng
        edtPriceRoomPushRoom.setText(String.valueOf(roomModel.getRentalCosts()));

        // Tiền điện
        if (roomModel.getListRoomPrice().get(0).getPrice() == 0) {
            chBoxFreeElectricPushRoom.setChecked(true);
        } else {
            edtElectricBillPushRoom.setText(String.valueOf(roomModel.getListRoomPrice().get(0).getPrice()));
        }

        // Tiền nước
        if (roomModel.getListRoomPrice().get(1).getPrice() == 0) {
            chBoxFreeWaterPushRoom.setChecked(true);
        } else {
            edtWaterBillPushRoom.setText(String.valueOf(roomModel.getListRoomPrice().get(1).getPrice()));
        }

        // Tiền internet
        if (roomModel.getListRoomPrice().get(2).getPrice() == 0) {
            chBoxFreeInternetPushRoom.setChecked(true);
        } else {
            edtInternetPushRoom.setText(String.valueOf(roomModel.getListRoomPrice().get(2).getPrice()));
        }

        // Tiền giữ xe
        if (roomModel.getListRoomPrice().get(3).getPrice() == 0) {
            chBoxFreeParkingPushRoom.setChecked(true);
        } else {
            edtParkingPushRoom.setText(String.valueOf(roomModel.getListRoomPrice().get(3).getPrice()));
        }
    }

//    public void setOnPostRoomStep2Update() {
//        roomModel.setTypeID(typeID);
//        roomModel.setGender(genderRoom);
//        roomModel.setCurrentNumber(currentNumber);
//        roomModel.setMaxNumber(maxNumber);
//        roomModel.setWidth(width);
//        roomModel.setLength(length);
//
//        roomModel.setRentalCosts(priceRoom);
//        roomModel.getListRoomPrice().get(0).setPrice(electricBill);
//        roomModel.getListRoomPrice().get(1).setPrice(warterBill);
//        roomModel.getListRoomPrice().get(2).setPrice(InternetBill);
//        roomModel.getListRoomPrice().get(3).setPrice(parkingBill);
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_nextStep2_post_room:

                if (checkTrueDataFromControl() == true) {
                    //Lấy dữ liệu từ control
                    getDataFromControl();

                    if (compareInfo() == true) {

                        postRoomUpdateController = new PostRoomUpdateController(getContext());

                        IUpdateRoomModel iUpdateRoomModel = new IUpdateRoomModel() {
                            @Override
                            public void getSuccessNotifyRoomMode1(RoomModel roomModel1) {
                                roomModel = roomModel1;

                                initData();
                            }
                        };

                        postRoomUpdateController.postRoomStep2Update(roomModel.getRoomID(), typeID, genderRoom, currentNumber, maxNumber, width, length,
                                priceRoom, electricBill, warterBill, InternetBill, parkingBill, iUpdateRoomModel);

                        //setOnPostRoomStep2Update();
                    } else {
                        //Thông báo lỗi
                        Toast.makeText(getContext(), "Bạn chưa thay đổi thông tin nào cả", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Thông báo lỗi
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    //Dổi màu thành màu xám ở activity
                    changeColorInActivity(false);
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
