package com.example.designapptest.view.room;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.controller.MainActivityController;
import com.example.designapptest.controller.RoomManagementControlller;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;


public class RoomManageActivity extends AppCompatActivity {
    RecyclerView recyclerMainRoom;
    MainActivityController mainActivityController;
    RoomManagementControlller roomManagementControlller;
    List<RoomModel> roomModelList = new ArrayList<>();

    ProgressBar progressBarMyRooms;
    LinearLayout lnLtQuantityTopMyRooms;
    // Số lượng trả về.
    TextView txtQuantityMyRooms;
    TextView txtQuantityRoom,txtQuantityComment,txtQuantityView;

    NestedScrollView nestedScrollMyRoomsView;
    ProgressBar progressBarLoadMoreMyRooms;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_management_user_view);

        sharedPreferences = this.getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID,"n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerMainRoom = (RecyclerView)findViewById(R.id.recycler_Main_Room);

        txtQuantityRoom = findViewById(R.id.txt_quantity_room);
        txtQuantityComment = findViewById(R.id.txt_quantity_comment);
        txtQuantityView = findViewById(R.id.txt_quantity_view);

        progressBarMyRooms = (ProgressBar) findViewById(R.id.progress_bar_my_rooms);
        progressBarMyRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopMyRooms = (LinearLayout) findViewById(R.id.lnLt_quantity_top_my_rooms);
        txtQuantityMyRooms = (TextView) findViewById(R.id.txt_quantity_my_rooms);

        nestedScrollMyRoomsView = (NestedScrollView) findViewById(R.id.nested_scroll_my_rooms);
        progressBarLoadMoreMyRooms = (ProgressBar) findViewById(R.id.progress_bar_load_more_my_rooms);
        progressBarLoadMoreMyRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Phòng của bạn");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setView() {
        // Hiện progress bar.
        progressBarMyRooms.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreMyRooms.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopMyRooms.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setView();
        getData();

    }

    private void getData(){
        roomManagementControlller = new RoomManagementControlller(this);
        roomManagementControlller.loadQuantiryInfo(UID,txtQuantityRoom,txtQuantityView,txtQuantityComment);

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.ListRoomUser(UID, recyclerMainRoom, txtQuantityMyRooms, progressBarMyRooms,
                lnLtQuantityTopMyRooms, nestedScrollMyRoomsView, progressBarLoadMoreMyRooms,false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
