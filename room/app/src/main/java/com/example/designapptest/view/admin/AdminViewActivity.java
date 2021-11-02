package com.example.designapptest.view.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.designapptest.R;
import com.example.designapptest.controller.DetailRoomController;
import com.example.designapptest.controller.MainActivityController;
import com.example.designapptest.controller.UserController;
import com.example.designapptest.domain.myFilter;
import com.example.designapptest.view.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminViewActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    String UID;

    TextView txtSumViewsAdminView, txtSumHostsAdminView, txtSumRoomsAdminView;

    LinearLayout lnLtRoomsAdminView, lnLtHostsAdminView, lnLtReportsAdminView, lnLtRoomsWaitForApprovalAdminView, lnLtLogoutAdminView;

    UserController userController;
    MainActivityController mainActivityController;
    DetailRoomController detailRoomController;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();

        setClick();
    }

    private void initControl() {
        txtSumViewsAdminView = (TextView) findViewById(R.id.txt_sum_views_admin_view);
        txtSumHostsAdminView = (TextView) findViewById(R.id.txt_sum_hosts_admin_view);
        txtSumRoomsAdminView = (TextView) findViewById(R.id.txt_sum_rooms_admin_view);

        lnLtRoomsAdminView = (LinearLayout) findViewById(R.id.lnLt_rooms_admin_view);
        lnLtHostsAdminView = (LinearLayout) findViewById(R.id.lnLt_hosts_admin_view);
        lnLtReportsAdminView = (LinearLayout) findViewById(R.id.lnLt_reports_admin_view);
        lnLtRoomsWaitForApprovalAdminView = (LinearLayout) findViewById(R.id.lnLt_rooms_wait_for_approval_admin_view);
        lnLtLogoutAdminView = (LinearLayout) findViewById(R.id.lnLt_logout_admin_view);
    }

    private void setClick() {
        lnLtRoomsAdminView.setOnClickListener(this);
        lnLtHostsAdminView.setOnClickListener(this);
        lnLtReportsAdminView.setOnClickListener(this);
        lnLtLogoutAdminView.setOnClickListener(this);
        lnLtRoomsWaitForApprovalAdminView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userController = new UserController(this);
        userController.getSumHosts(txtSumHostsAdminView);

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.getSumRooms(txtSumRoomsAdminView);

        List<myFilter> filterList = new ArrayList<>();
        detailRoomController = new DetailRoomController(this, "", filterList, UID);
        detailRoomController.getSumViews(txtSumViewsAdminView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.lnLt_rooms_admin_view:
                Intent iRooms = new Intent(AdminViewActivity.this, AdminRoomsActivity.class);
                startActivity(iRooms);
                break;
            case R.id.lnLt_hosts_admin_view:
                Intent iHosts = new Intent(AdminViewActivity.this, AdminHostsActivity.class);
                startActivity(iHosts);
                break;
            case R.id.lnLt_reports_admin_view:
                Intent iReports = new Intent(AdminViewActivity.this, AdminReportRoomActivity.class);
                startActivity(iReports);
                break;
            case R.id.lnLt_rooms_wait_for_approval_admin_view:
                Intent iRoomsWaitForApproval = new Intent(AdminViewActivity.this, AdminRoomsWaitForApprovalActivity.class);
                startActivity(iRoomsWaitForApproval);
                break;
            case R.id.lnLt_logout_admin_view: {
                //Khởi tạo firebaseAuth
                firebaseAuth = FirebaseAuth.getInstance();
                //Text Đăng xuất
                firebaseAuth.signOut();

                Intent intent = new Intent(AdminViewActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}