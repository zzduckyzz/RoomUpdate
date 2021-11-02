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

import com.example.designapptest.controller.FindRoomController;
import com.example.designapptest.model.FindRoomFilterModel;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class FindRoomMineActivity extends AppCompatActivity {
    // ID object truyen qua find room

    RecyclerView recyclerFindRoomMine;

    FindRoomController findRoomController;

    FindRoomFilterModel findRoomFilterModel;

    ProgressBar progressBarFindRoomMine;
    LinearLayout lnLtTopHavResultReturnFindRoomMine;

    // Số lượng trả về.
    TextView txtResultReturn;

    NestedScrollView nestedScrollFindRoomMineView;
    ProgressBar progressBarLoadMoreFindRoom;

    Toolbar toolbar;

    // Biến báo load lại find room.
    boolean flagFindRoom = true;

    SharedPreferences sharedPreferences;
    String UID;

    //Layout
    View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room_mine_view);

        // Lấy id của người dùng hiện tại
        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void setView() {
        // Hiện progress bar.
        progressBarFindRoomMine.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreFindRoom.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtTopHavResultReturnFindRoomMine.setVisibility(View.GONE);
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    public void onStart() {
        super.onStart();

        initData();

        setView();

        findRoomController = new FindRoomController(this);
        findRoomController.ListFindRoomMine(UID, recyclerFindRoomMine, txtResultReturn, progressBarFindRoomMine,
                lnLtTopHavResultReturnFindRoomMine, nestedScrollFindRoomMineView, progressBarLoadMoreFindRoom);
    }
    //End load dữ liệu vào danh sách trong lần đầu chạy

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initControl() {
       toolbar = (Toolbar) findViewById(R.id.toolbar);


        recyclerFindRoomMine = (RecyclerView) findViewById(R.id.recycler_find_room_mine);

        progressBarFindRoomMine = (ProgressBar) findViewById(R.id.progress_bar_find_room_mine);
        progressBarFindRoomMine.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtTopHavResultReturnFindRoomMine = (LinearLayout) findViewById(R.id.lnLt_top_haveResultReturn_find_room_mine);
        txtResultReturn = (TextView) findViewById(R.id.txt_resultReturn_find_room_mine);

        nestedScrollFindRoomMineView = (NestedScrollView) findViewById(R.id.nested_scroll_find_room_mine_view);
        progressBarLoadMoreFindRoom = (ProgressBar) findViewById(R.id.progress_bar_load_more_find_room_mine);
        progressBarLoadMoreFindRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void initData() {
        // Gán các giá trị toolbar.
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Tìm phòng ở ghép của tôi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


}
