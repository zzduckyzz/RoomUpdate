package com.example.designapptest.view.postroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.designapptest.R;
import com.example.designapptest.controller.Interfaces.OnPostRoomFinish;
import com.example.designapptest.controller.PostRoomStep4Controller;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.view.login.LoginActivity;
import com.example.designapptest.view.room.PostRoomActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostRoomStep4Fragment extends Fragment implements View.OnClickListener {

    public static final String FRAG_NAME = "POST_ROOM_STEP_4";
    SharedPreferences sharedpreferences;
    String District, City, Ward, Street, No;

    //Biến truyền thông tin từ fragment 2
    boolean genderRoom;
    String typeID;
    long currentNumber, maxNumber;
    float width, length;
    float priceRomm, electricBill, warterBill, InternetBill, parkingBill;
    //End biến truyền thông tin từ fragment 2

    //Biến truyền thông tin từ fragmet 3
    List<String> listConvenient;
    List<String> listPathImageChoosed;
    //End biến truyền thông tin từ frament 3

    //Biến lưu thông tin từ frament này
    String name, describe;
    //End biến lưu thông tin từ fragment này

    //Biến lưu user id
    String UID;

    //Control
    Button btnNextStep4PostRoom;
    EditText edtNamePushRoom, edtDescribePushRoom;
    //End Control

    PostRoomActivity postRoom;

    //Controller thao tác với database
    PostRoomStep4Controller controller;

    ProgressDialog progressDialog;

    public PostRoomStep4Fragment() {

    }


    //Lấy dữ liệu từ sharePreference
    private void getDataFromPreference() {
        sharedpreferences = this.getActivity().getSharedPreferences(PostRoomActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);

        //Lấy dữ liệu từ fragment 1
        Street = sharedpreferences.getString(PostRoomStep1Fragment.SHARE_STREET, "");
        City = sharedpreferences.getString(PostRoomStep1Fragment.SHARE_CITY, "");
        District = sharedpreferences.getString(PostRoomStep1Fragment.SHARE_DISTRICT, "");
        Ward = sharedpreferences.getString(PostRoomStep1Fragment.SHARE_WARD, "");
        No = sharedpreferences.getString(PostRoomStep1Fragment.SHARE_NO, "");

        //Lấy dữ liệu từ fragment2
        genderRoom = sharedpreferences.getBoolean(PostRoomStep2Fragment.SHARE_GENDER_ROOM, true);
        typeID = sharedpreferences.getString(PostRoomStep2Fragment.SHARE_TYPEID, "");

        currentNumber = sharedpreferences.getLong(PostRoomStep2Fragment.SHARE_CURRENTNUMBER, 0);
        maxNumber = sharedpreferences.getLong(PostRoomStep2Fragment.SHARE_MAXNUMBER, 0);

        width = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_WIDTH, 0);
        length = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_LENGTH, 0);

        priceRomm = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_RPICEROOM, 0);
        electricBill = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_ELECTRICB, 0);
        warterBill = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_WARTERB, 0);
        InternetBill = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_INTERNETB, 0);
        parkingBill = sharedpreferences.getFloat(PostRoomStep2Fragment.SHARE_PARKINGB, 0);
        //End lấy dữ liệu từ fragment2

        //Lấy dữ liệu từ frament 3
        Set<String> setTemp = new HashSet<String>();
        Set<String> setConvenient = sharedpreferences.getStringSet(PostRoomStep3Fragment.SHARE_LIST_CONVENIENT, setTemp);
        listConvenient = new ArrayList<String>();
        listConvenient.addAll(setConvenient);

        Set<String> setPathImage = sharedpreferences.getStringSet(PostRoomStep3Fragment.SHARE_LISTPATHIMAGE, setTemp);
        listPathImageChoosed = new ArrayList<String>();
        listPathImageChoosed.addAll(setPathImage);
        //End lấy dữ liệu từ fragment 3

        //Lấy UID
        SharedPreferences sharedPreferences2 = this.getActivity().getSharedPreferences(LoginActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        UID = sharedPreferences2.getString(LoginActivity.SHARE_UID, "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.post_room_step_4_view, container, false);
        initControl(layout);

        //Lấy context của fragment
        postRoom = (PostRoomActivity) getContext();

        return layout;
    }

    private void initControl(View view) {
        edtNamePushRoom = view.findViewById(R.id.edt_name_push_room);
        edtDescribePushRoom = view.findViewById(R.id.edt_describe_push_room);

        btnNextStep4PostRoom = view.findViewById(R.id.btn_nextStep4_post_room);
        btnNextStep4PostRoom.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
    }

    //Hàm lấy giá trị người dùng nhập từ control
    private void getDataFromControl() {
        name = edtNamePushRoom.getText().toString();
        describe = edtDescribePushRoom.getText().toString();
    }

    //Hàm kiểm tra dữ liệu đúng từ control
    private boolean checkTrueDataFromControl() {
        return !name.matches("") && !describe.matches("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_nextStep4_post_room:
                //Lấy dữ liệu từ các fragment
                getDataFromPreference();
                //Lấy dữ liệu từ control
                getDataFromControl();
                //Kiểm tra dữ liệu nếu đúng thì thực hiện
                if (checkTrueDataFromControl()) {

                    changeColorInActivity();
                    //Kiểm tra xem những step trước đã hoàn thành chưa
                    if (postRoom.isStepOneComplete() && postRoom.isStepTwoComplete() && postRoom.isStepTreeComplete()) {
                        //Show dialog
                        progressDialog.setMessage("Đang đăng phòng....");
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();

                        //Gọi hàm đăng phòng ở controller
                        callAddRoomControlller();
                    }
                } else {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //Truyền tham số và gọi hàm thêm phòng ở controller
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void callAddRoomControlller() {
        PostRoomStep4Controller controller = new PostRoomStep4Controller(getContext());

        //Tạo mới một room
        RoomModel dataRoom = new RoomModel();

        // phòng mới nên chưa đc duyệt
        dataRoom.setApprove(false);

        //Set giá trị
        dataRoom.setDescribe(describe);
        dataRoom.setName(name);
        dataRoom.setOwner(UID);

        //Lấy ra thời gian hiện tại
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        //Set thời gian tạo
        dataRoom.setTimeCreated(date);

        dataRoom.setCurrentNumber(currentNumber);
        dataRoom.setMaxNumber(maxNumber);
        dataRoom.setLength(length);
        dataRoom.setWidth(width);
        dataRoom.setRentalCosts(priceRomm);
        dataRoom.setAuthentication(false);
        dataRoom.setGender(genderRoom);

        dataRoom.setApartmentNumber(No);
        dataRoom.setCounty(District);
        dataRoom.setStreet(Street);
        dataRoom.setCity(City);
        dataRoom.setWard(Ward);

        dataRoom.setTypeID(typeID);

        dataRoom.setMedium(0);
        dataRoom.setGreat(0);
        dataRoom.setPrettyGood(0);
        dataRoom.setBad(0);
        //End set giá trị

        //Gọi hàm
        controller.callAddRoomFromModel(UID, dataRoom, listConvenient, listPathImageChoosed, electricBill, warterBill, InternetBill, parkingBill, progressDialog, new OnPostRoomFinish() {
            @Override
            public void invoke() {
                requireActivity().finish();
            }
        });
    }

    //Hàm chuyển màu ở activity
    private void changeColorInActivity() {
        //Gọi hàm trong posrRoom Thông qua Activity
        postRoom.onMsgFromFragToPostRoom(FRAG_NAME, true);
    }
}
