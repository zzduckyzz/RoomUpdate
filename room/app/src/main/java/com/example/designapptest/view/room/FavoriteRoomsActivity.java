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

public class FavoriteRoomsActivity extends AppCompatActivity {
    RecyclerView recyclerMyFavoriteRoom;
    MainActivityController mainActivityController;
    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarFavoriteRooms;
    LinearLayout lnLtQuantityTopFavoriteRooms;

    // Số lượng trả về.
    TextView txtQuantity;

    NestedScrollView nestedScrollFavoriteRoomsView;
    ProgressBar progressBarLoadMoreFavoriteRooms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_rooms_view);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerMyFavoriteRoom = (RecyclerView) findViewById(R.id.recycler_favorite_rooms);

        toolbar = findViewById(R.id.toolbar);

        progressBarFavoriteRooms = (ProgressBar) findViewById(R.id.progress_bar_favorite_rooms);
        progressBarFavoriteRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopFavoriteRooms = (LinearLayout) findViewById(R.id.lnLt_quantity_top_favorite_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_favorite_rooms);

        nestedScrollFavoriteRoomsView = (NestedScrollView) findViewById(R.id.nested_scroll_favorite_rooms_view);
        progressBarLoadMoreFavoriteRooms = (ProgressBar) findViewById(R.id.progress_bar_load_more_favorite_rooms);
        progressBarLoadMoreFavoriteRooms.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar
        progressBarFavoriteRooms.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreFavoriteRooms.setVisibility(View.GONE);
        // Ẩn thi kết quả trả vể.
        lnLtQuantityTopFavoriteRooms.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.getListFavoriteRooms(recyclerMyFavoriteRoom, txtQuantity, progressBarFavoriteRooms,
                lnLtQuantityTopFavoriteRooms, nestedScrollFavoriteRoomsView, progressBarLoadMoreFavoriteRooms);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Phòng trọ yêu thích");
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
