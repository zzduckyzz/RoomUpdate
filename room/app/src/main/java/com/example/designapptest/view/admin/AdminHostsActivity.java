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

import com.example.designapptest.controller.UserController;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class AdminHostsActivity extends AppCompatActivity {
    RecyclerView recyclerAdminHostsView;
    UserController userController;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarAdminHosts;
    LinearLayout lnLtQuantityTopAdminHosts;

    // Số lượng trả về.
    TextView txtQuantity;

    NestedScrollView nestedScrollAdminHostsView;
    ProgressBar progressBarLoadMoreAdminHosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified_rooms_view);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
    }

    private void initControl() {
        recyclerAdminHostsView = (RecyclerView) findViewById(R.id.recycler_verified_rooms);

        toolbar = findViewById(R.id.toolbar);

        progressBarAdminHosts = (ProgressBar) findViewById(R.id.progress_bar_verified_rooms);
        progressBarAdminHosts.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopAdminHosts = (LinearLayout) findViewById(R.id.lnLt_quantity_top_verified_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_verified_rooms);

        nestedScrollAdminHostsView = (NestedScrollView) findViewById(R.id.nested_scroll_verified_rooms_view);
        progressBarLoadMoreAdminHosts = (ProgressBar) findViewById(R.id.progress_bar_load_more_verified_rooms);
        progressBarLoadMoreAdminHosts.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar.
        progressBarAdminHosts.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreAdminHosts.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopAdminHosts.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

        userController = new UserController(this);
        userController.ListHosts(recyclerAdminHostsView, txtQuantity, progressBarAdminHosts,
                lnLtQuantityTopAdminHosts, nestedScrollAdminHostsView, progressBarLoadMoreAdminHosts);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Tất cả chủ phòng");
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