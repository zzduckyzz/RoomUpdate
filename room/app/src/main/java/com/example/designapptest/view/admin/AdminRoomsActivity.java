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

import com.example.designapptest.controller.MainActivityController;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class AdminRoomsActivity extends AppCompatActivity {
    RecyclerView recyclerAdminRoomsView;
    MainActivityController mainActivityController;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarAdminRooms;
    LinearLayout lnLtQuantityTopAdminRooms;

    // Số lượng trả về.
    TextView txtQuantity;

    NestedScrollView nestedScrollAdminRoomsView;
    ProgressBar progressBarLoadMoreAdminRooms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified_rooms_view);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerAdminRoomsView = (RecyclerView) findViewById(R.id.recycler_verified_rooms);

        toolbar = findViewById(R.id.toolbar);

        progressBarAdminRooms = (ProgressBar) findViewById(R.id.progress_bar_verified_rooms);
        progressBarAdminRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopAdminRooms = (LinearLayout) findViewById(R.id.lnLt_quantity_top_verified_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_verified_rooms);

        nestedScrollAdminRoomsView = (NestedScrollView) findViewById(R.id.nested_scroll_verified_rooms_view);
        progressBarLoadMoreAdminRooms = (ProgressBar) findViewById(R.id.progress_bar_load_more_verified_rooms);
        progressBarLoadMoreAdminRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar.
        progressBarAdminRooms.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreAdminRooms.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopAdminRooms.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.ListRoomsAdminView(recyclerAdminRoomsView, txtQuantity, progressBarAdminRooms,
                lnLtQuantityTopAdminRooms, nestedScrollAdminRoomsView, progressBarLoadMoreAdminRooms);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Tất cả phòng trọ");
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