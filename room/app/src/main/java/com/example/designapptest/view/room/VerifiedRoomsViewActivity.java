package com.example.designapptest.view.room;

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

import com.example.designapptest.controller.MainActivityController;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class VerifiedRoomsViewActivity extends AppCompatActivity {
    RecyclerView recyclerMyVerifiedRoom;
    MainActivityController mainActivityController;
    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarVerifiedRooms;
    LinearLayout lnLtQuantityTopVerifiedRooms;

    // Số lượng trả về.
    TextView txtQuantity;

    NestedScrollView nestedScrollVerifiedRoomsView;
    ProgressBar progressBarLoadMoreVerifiedRooms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified_rooms_view);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerMyVerifiedRoom = (RecyclerView) findViewById(R.id.recycler_verified_rooms);

        toolbar = findViewById(R.id.toolbar);

        progressBarVerifiedRooms = (ProgressBar) findViewById(R.id.progress_bar_verified_rooms);
        progressBarVerifiedRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopVerifiedRooms = (LinearLayout) findViewById(R.id.lnLt_quantity_top_verified_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_verified_rooms);

        nestedScrollVerifiedRoomsView = (NestedScrollView) findViewById(R.id.nested_scroll_verified_rooms_view);
        progressBarLoadMoreVerifiedRooms = (ProgressBar) findViewById(R.id.progress_bar_load_more_verified_rooms);
        progressBarLoadMoreVerifiedRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar.
        progressBarVerifiedRooms.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreVerifiedRooms.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopVerifiedRooms.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.getListVerifiedRooms(recyclerMyVerifiedRoom, txtQuantity, progressBarVerifiedRooms,
                lnLtQuantityTopVerifiedRooms, nestedScrollVerifiedRoomsView, progressBarLoadMoreVerifiedRooms);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Phòng trọ đã xác nhận");
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
