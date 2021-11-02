package com.example.designapptest.view.postroom;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.designapptest.R;
import com.example.designapptest.controller.Interfaces.IUpdateRoomModel;
import com.example.designapptest.controller.PostRoomUpdateController;
import com.example.designapptest.model.RoomModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class PostRoomStep1UpdateFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //Biến filnal lưu tên fragment
    public static final String FRAG_NAME = "POST_ROOM_STEP_1";
    //End biến filnal lưu tên fragment

    //Biến final share dữ liệu
    public final static String SHARE_CITY = "CITY";
    public final static String SHARE_WARD = "WARD";
    public final static String SHARE_DISTRICT = "DISTRICT";
    public final static String SHARE_STREET = "STREET";
    public final static String SHARE_NO = "NO";

    private final static int RQ_LOCATION = 14;

    //Biến sharedPreferences để truyền dữ liệu giữa các fragment trong viewpager
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    //End Biến sharedPreferences để truyền dữ liệu giữa các fragment trong viewpager

    private Spinner spnDistrictPushRoom, spnCityPushRoom, spnWardPushRoom;
    private EditText edtStreetPushRoom, edtNoPushRoom;
    private Button btnNextStep1PostRoom;
    private TextView txtChooseLocation;

    //Biến lưu thông tin mới
    String District, City, Ward, Street, No;
    //End Biến lưu thông tin


    //Activity chứa viewpager
    PostRoomAdapterUpdateActivity postRoom;

    // Dữ liệu room model phòng cần cập nhật
    RoomModel roomModel;

    //RoomModel roomModel2;

    // controller cập nhật
    PostRoomUpdateController postRoomUpdateController;

    FusedLocationProviderClient client;
    boolean isChooseMap = false;

    ArrayAdapter<String> adapterSpnspnDistrictPushRoom;

    public PostRoomStep1UpdateFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.post_room_step_1_view, container, false);

        initControl(layout);
        loadDataForSpinner();
        spnDistrictPushRoom.setOnItemSelectedListener(this);

        //Lấy ra activity hiện tại
        postRoom = (PostRoomAdapterUpdateActivity) getContext();

        roomModel = postRoom.returnRoomModel();

        initData();

        return layout;
    }

    //Đổ dữ liệu cho phường mỗi khi quận thay đổi giá trị
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String District = parent.getItemAtPosition(position).toString();
        loadDataForWardSpn(District);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Load data cho spinner
    private void loadDataForSpinner() {

        //Đặt thuộc tính cho spinner CityPushRoom
        String[] CityList = {"Hồ Chí Minh"};
        ArrayAdapter<String> adapterSpnCityPushRoom = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, CityList);
        adapterSpnCityPushRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCityPushRoom.setAdapter(adapterSpnCityPushRoom);
        //End Đặt thuộc tính cho spinner CityPushRoom

        //Đặt thuộc tính cho spinner spnDistrictPushRoom
        String[] DistricList = {"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5",
                "Quận 6", "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12",
                "Quận Thủ Đức", "Quận Gò Vấp", "Quận Bình Thạnh", "Quận Tân Bình",
                "Quận Tân Phú", "Quận Phú Nhuận", "Quận Bình Tân"};
        adapterSpnspnDistrictPushRoom = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, DistricList);
        adapterSpnspnDistrictPushRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDistrictPushRoom.setAdapter(adapterSpnspnDistrictPushRoom);
        //End đặt thuộc tính cho spinner spnDistricPushRoom
    }

    //Load Phường cho quận tương ứng
    private void loadDataForWardSpn(String District) {
        ArrayAdapter<String> adapterSpnWardPushRoom = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, WardOfDisTrict(District));
        adapterSpnWardPushRoom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWardPushRoom.setAdapter(adapterSpnWardPushRoom);
    }

    //Khởi tạo giá trị cho các control trên fragment
    private void initControl(View view) {
        spnCityPushRoom = (Spinner) view.findViewById(R.id.spn_city_push_room);
        spnDistrictPushRoom = (Spinner) view.findViewById(R.id.spn_district_push_room);
        spnWardPushRoom = (Spinner) view.findViewById(R.id.spn_ward_push_room);
        edtStreetPushRoom = (EditText) view.findViewById(R.id.edt_street_push_room);
        edtNoPushRoom = (EditText) view.findViewById(R.id.edt_no_push_room);

        btnNextStep1PostRoom = (Button) view.findViewById(R.id.btn_nextStep1_post_room);
        btnNextStep1PostRoom.setOnClickListener(this);
    }

    // Hàm gán data vào các control
    private void initData() {

        // Lấy quận đang đuoc chọn
        for (int i = 0; i < spnDistrictPushRoom.getCount(); i++) {
            if (spnDistrictPushRoom.getItemAtPosition(i).equals(roomModel.getCounty())) {
                spnDistrictPushRoom.setSelection(i);

                break;
            }
        }

        loadDataForWardSpn(roomModel.getCounty());

        // Lấy phường đang được chọn
        for (int i = 0; i < spnWardPushRoom.getCount(); i++) {
            if (spnWardPushRoom.getItemAtPosition(i).equals(roomModel.getWard()) == true) {

                spnWardPushRoom.setSelection(i);

                break;
            }
        }

        // Hiển thị đường
        edtStreetPushRoom.setText(roomModel.getStreet());
        // Hiển thị số nhà
        edtNoPushRoom.setText(roomModel.getApartmentNumber());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_nextStep1_post_room:
                // Nhân kết quả từ người dùng
                getDataFromControl();
                if (checkTrueDataFromControl() == true) {
                    if (compareInfo() == true) {
                        postRoomUpdateController = new PostRoomUpdateController(getContext());
                        IUpdateRoomModel iUpdateRoomModel = new IUpdateRoomModel() {
                            @Override
                            public void getSuccessNotifyRoomMode1(RoomModel roomModel1) {
                                roomModel = roomModel1;
                                initData();
                            }
                        };

                        postRoomUpdateController.postRoomStep1Update(roomModel.getRoomID(), City, District, Ward, Street, No,
                                roomModel.getCity(), roomModel.getCounty(), roomModel.getWard(), roomModel.getStreet(),
                                roomModel.getApartmentNumber(), iUpdateRoomModel);

                        //setOnPostRoomStep1Update();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQ_LOCATION) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                //Lấy ra thông tin địa chỉ vật lý
                String tempDistrict = data.getStringExtra(SHARE_DISTRICT);
                String tempStreet = data.getStringExtra(SHARE_STREET);
                String tempNo = data.getStringExtra(SHARE_NO);

                //Hiển thị các thông tin vừa lấy được lên UI
                spnDistrictPushRoom.setSelection(adapterSpnspnDistrictPushRoom.getPosition(tempDistrict));
                edtStreetPushRoom.setText(tempStreet);
                edtNoPushRoom.setText(tempNo);
            }
        }
    }

    //Hàm chuyển màu ở activity
    private void changeColorInActivity(boolean isComplete) {
        //Gọi hàm trong posrRoom Thông qua Activity
        postRoom.onMsgFromFragToPostRoom(FRAG_NAME, isComplete);
    }

    //Hàm lấy các giá trị từ người dùng từ các control
    private void getDataFromControl() {
        City = spnCityPushRoom.getSelectedItem().toString();
        District = spnDistrictPushRoom.getSelectedItem().toString();
        Ward = spnWardPushRoom.getSelectedItem().toString();
        Street = edtStreetPushRoom.getText().toString();
        No = edtNoPushRoom.getText().toString();
        //getLocation();
    }

    // Hàm kiểm tra xem người dùng có thay đổi thông tin không
    private boolean compareInfo() {
        // Có thay đổi thành phố không
        if (City.equals(roomModel.getCity()) == true && District.equals(roomModel.getCounty()) == true
                && Ward.equals(roomModel.getWard()) == true && Street.equals(roomModel.getStreet()) == true
                && No.equals(roomModel.getApartmentNumber()) == true)
            return false;

        return true;
    }

    //Hàm kiểm tra giá trị nhập từ người dùng có chính xác hay không
    private boolean checkTrueDataFromControl() {
        return !Street.matches("") && !No.matches("");
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //Hàm trả về list phường tương ứng với quận
    private String[] WardOfDisTrict(String DisTrict) {

        String[] List_W_Q_1 = {"P.Tân Định", "P.Đa Kao", "P.Bến Nghé", "P.Bến Thành",
                "P.Nguyễn Thái Bình", "P.Phạm Ngũ Lão", "P.Cầu Ông Lãnh",
                "P.Cô Giang", "P.Nguyễn Cư Trinh", "P.Cầu Kho"};

        String[] List_W_Q_2 = {"P.Thảo Điền", "P.An Phú", "P.Bình An", "P.Bình Trưng Đông",
                "P.Bình Trưng Tây", "P.Bình Khánh", "P.An Khánh", "P.Cát Lái",
                "P.Thạnh Mỹ Lợi", "P.An Lợi Đông", "P.Thủ Thiêm"};

        String[] List_W_Q_12 = {"P.Thạnh Xuân", "P.Thạnh Lộc", "P.Hiệp Thành", "P.Thới An," +
                "P.P.Tân Chánh Hiệp", "P.An Phú Đông", "P.Tân Thới Hiệp",
                "P.Trung Mỹ Tây", "P.Tân Hưng Thuận", "P.Đông Hưng Thuận",
                "P.Tân Thới Nhất"};

        String[] List_W_ThuDuc = {"P.Linh Xuân", "P.Bình Chiểu", "P.Linh Trung", "P.Tam Bình",
                "P.Tam Phú", "P.Hiệp Bình Phước", "P.Hiệp Bình Chánh", "P.Linh Chiểu",
                "P.Linh Tây", "P.Linh Đông", "P.Bình Thọ", "P.Trường Thọ"};

        String[] List_W_Q_9 = {"P.Long Bình", "P.Long Thạch Mỹ", "P.Tân Phú", "P.Hiệp Phú",
                "P.Tăng Nhơn Phú A", "P.Tăng Nhơn Phú B", "P.Phước Long B",
                "P.Phước Long A", "P.Trường Thạch", "P.Long Phước",
                "P.Long Trường", "P.Phước Bình", "P.Phú Hữu"};

        String[] List_W_TanPhu = {"P.Tân Sơn Nhì", "P.Tây Thạnh", "P.Sơn Kỳ", "P.Tân Quý",
                "P.Tân Thành", "P.Phú Thọ Hòa", "P.Phú Thạnh", "P.Phú Trung",
                "P.Hòa Thạnh", "P.Hiệp Tân", "P.Tân Thới Hòa"};

        String[] List_W_BinhTan = {"P.Bình Hưng Hòa", "P.Bình Hưng Hòa A", "P.Bình Hưng Hòa B",
                "P.Bình Trị Đông", "P.Bình Trị Đông A", "P.Bình Trị Đông B",
                "P.Tân Tạo", "P.Tân Tạo A", "P.An Lạc", "P.An Lạc A"};

        String[] List_W_Q_7 = {"P.Tân Thuận Đông", "P.Tân Thuận Tây", "P.Tân Kiểng", "P.Tân Hưng",
                "P.Bình Thuận", "P.Tân Quy", "P.Phú Thuận", "P.Tân Phú", "P.Tân Phong",
                "P.Phú Mỹ"};

        String[] List_W_BinhThanh = {"P.1", "P.2", "P.3", "P.5", "P.7", "P.11", "P.12", "P.14",
                "P.15", "P.17", "P.19", "P.21", "P.22", "P.24", "P.25", "P.26",
                "P.27", "P.28"};

        String[] List_W_PhuNhuan = {"P.1", "P.2", "P.5", "P.7", "P.8", "P.9", "P.10", "P.11",
                "P.12", "P.13", "P.14", "P.15", "P.17"};

        String[] List_W_Q_3 = new String[14];
        String[] List_W_TanBinh = new String[15];
        String[] List_W_GoVap = new String[17];
        String[] List_W_Q_10 = new String[15];
        String[] List_W_Q_11 = new String[16];
        String[] List_W_Q_4 = new String[15];
        String[] List_W_Q_5 = new String[15];
        String[] List_W_Q_6 = new String[14];
        String[] List_W_Q_8 = new String[16];

        for (int i = 0; i < 14; i++) {
            List_W_Q_6[i] = "P." + (i + 1);
            List_W_Q_3[i] = "P." + (i + 1);
        }
        for (int i = 0; i < 15; i++) {
            List_W_TanBinh[i] = "P." + (i + 1);
            List_W_Q_10[i] = "P." + (i + 1);
            List_W_Q_4[i] = "P." + (i + 1);
            List_W_Q_5[i] = "P." + (i + 1);
        }
        for (int i = 0; i < 16; i++) {
            List_W_Q_11[i] = "P." + (i + 1);
            List_W_Q_8[i] = "P." + (i + 1);
        }
        for (int i = 0; i < 17; i++) {
            List_W_GoVap[i] = "P." + (i + 1);
        }

        switch (DisTrict) {
            case "Quận 1":
                return List_W_Q_1;
            case "Quận 2":
                return List_W_Q_2;
            case "Quận 3":
                return List_W_Q_3;
            case "Quận 4":
                return List_W_Q_4;
            case "Quận 5":
                return List_W_Q_5;
            case "Quận 6":
                return List_W_Q_6;
            case "Quận 7":
                return List_W_Q_7;
            case "Quận 8":
                return List_W_Q_8;
            case "Quận 9":
                return List_W_Q_9;
            case "Quận 10":
                return List_W_Q_10;
            case "Quận 11":
                return List_W_Q_11;
            case "Quận 12":
                return List_W_Q_12;
            case "Quận Thủ Đức":
                return List_W_ThuDuc;
            case "Quận Gò Vấp":
                return List_W_GoVap;
            case "Quận Bình Thạnh":
                return List_W_BinhThanh;
            case "Quận Tân Bình":
                return List_W_TanBinh;
            case "Quận Tân Phú":
                return List_W_TanPhu;
            case "Quận Phú Nhuận":
                return List_W_PhuNhuan;
            case "Quận Bình Tân":
                return List_W_BinhTan;
        }
        return List_W_BinhTan;
    }


}
