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
import com.example.designapptest.model.UserModel;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class AdminHostRoomsActivity extends AppCompatActivity {
    RecyclerView recyclerAdminHostRoomsView;
    MainActivityController mainActivityController;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarAdminHostRooms;
    LinearLayout lnLtQuantityTopAdminHostRooms;

    // Số lượng trả về.
    TextView txtQuantity;

    NestedScrollView nestedScrollAdminHostRoomsView;
    ProgressBar progressBarLoadMoreAdminHostRooms;

    UserModel userModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_rooms_view);

        userModel = getIntent().getParcelableExtra("chutro");

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerAdminHostRoomsView = (RecyclerView) findViewById(R.id.recycler_favorite_rooms);

        toolbar = findViewById(R.id.toolbar);

        progressBarAdminHostRooms = (ProgressBar) findViewById(R.id.progress_bar_favorite_rooms);
        progressBarAdminHostRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopAdminHostRooms = (LinearLayout) findViewById(R.id.lnLt_quantity_top_favorite_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_favorite_rooms);

        nestedScrollAdminHostRoomsView = (NestedScrollView) findViewById(R.id.nested_scroll_favorite_rooms_view);
        progressBarLoadMoreAdminHostRooms = (ProgressBar) findViewById(R.id.progress_bar_load_more_favorite_rooms);
        progressBarLoadMoreAdminHostRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar.
        progressBarAdminHostRooms.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreAdminHostRooms.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopAdminHostRooms.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.ListRoomUser(userModel.getUserID(), recyclerAdminHostRoomsView, txtQuantity, progressBarAdminHostRooms,
                lnLtQuantityTopAdminHostRooms, nestedScrollAdminHostRoomsView, progressBarLoadMoreAdminHostRooms,true);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Phòng trọ của " + userModel.getName());
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
