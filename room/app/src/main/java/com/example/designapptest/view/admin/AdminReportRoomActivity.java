package com.example.designapptest.view.admin;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.controller.ReportedRoomController;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class AdminReportRoomActivity extends AppCompatActivity {
    RecyclerView recyclerAdminReportRoomView;
    ReportedRoomController reportedRoomController;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarAdminReportRoom;
    LinearLayout lnLtQuantityTopAdminReportRoom;

    // Số lượng trả về.
    TextView txtQuantity;

    NestedScrollView nestedScrollAdminReportRoomView;
    ProgressBar progressBarLoadMoreAdminReportRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_rooms_view);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerAdminReportRoomView = (RecyclerView) findViewById(R.id.recycler_favorite_rooms);

        toolbar = findViewById(R.id.toolbar);

        progressBarAdminReportRoom = (ProgressBar) findViewById(R.id.progress_bar_favorite_rooms);
        progressBarAdminReportRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopAdminReportRoom = (LinearLayout) findViewById(R.id.lnLt_quantity_top_favorite_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_favorite_rooms);

        nestedScrollAdminReportRoomView = (NestedScrollView) findViewById(R.id.nested_scroll_favorite_rooms_view);
        progressBarLoadMoreAdminReportRoom = (ProgressBar) findViewById(R.id.progress_bar_load_more_favorite_rooms);
        progressBarLoadMoreAdminReportRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar.
        progressBarAdminReportRoom.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreAdminReportRoom.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopAdminReportRoom.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

       reportedRoomController = new ReportedRoomController(this);
       reportedRoomController.ListReports(recyclerAdminReportRoomView, txtQuantity, progressBarAdminReportRoom,
               lnLtQuantityTopAdminReportRoom, nestedScrollAdminReportRoomView, progressBarLoadMoreAdminReportRoom);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Báo cáo phòng trọ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
