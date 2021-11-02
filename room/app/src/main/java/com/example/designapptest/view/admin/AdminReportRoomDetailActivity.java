package com.example.designapptest.view.admin;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.designapptest.model.ReportedRoomModel;
import com.example.designapptest.R;

public class AdminReportRoomDetailActivity extends AppCompatActivity {
    Toolbar toolbar;

    ReportedRoomModel reportedRoomModel;

    TextView txtRoomType, txtRoomName, txtRoomAddress, txtRoomPhoneNumber, txtTimeReport, txtUserReport,
    txtReasonReport, txtDetailReport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_room_detail_view);

        reportedRoomModel = getIntent().getParcelableExtra("reportRoom");

        initControl();
    }

    private void initControl() {
        toolbar = findViewById(R.id.toolbar);

        txtRoomType = (TextView) findViewById(R.id.txt_roomType);
        txtRoomName = (TextView) findViewById(R.id.txt_roomName);
        txtRoomAddress = (TextView) findViewById(R.id.txt_roomAddress);
        txtRoomPhoneNumber = (TextView) findViewById(R.id.txt_roomPhoneNumber);
        txtTimeReport = (TextView) findViewById(R.id.txt_time_report);
        txtUserReport = (TextView) findViewById(R.id.txt_user_report);
        txtReasonReport = (TextView) findViewById(R.id.txt_reason_report);
        txtDetailReport = (TextView) findViewById(R.id.txt_detail_report);
    }


    @Override
    protected void onStart() {
        super.onStart();

        initData();
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Chi tiết báo cáo phòng trọ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        txtRoomType.setText(reportedRoomModel.getReportedRoom().getRoomType());
        txtRoomName.setText(reportedRoomModel.getReportedRoom().getName());

        //Set address for room
        String longAddress = reportedRoomModel.getReportedRoom().getApartmentNumber() + " "
                + reportedRoomModel.getReportedRoom().getStreet() + ", "
                + reportedRoomModel.getReportedRoom().getWard() + ", "
                + reportedRoomModel.getReportedRoom().getCounty() + ", "
                + reportedRoomModel.getReportedRoom().getCity();
        txtRoomAddress.setText(longAddress);
        //End Set address for room

        txtRoomPhoneNumber.setText(reportedRoomModel.getReportedRoom().getRoomOwner().getPhoneNumber());
        txtUserReport.setText(reportedRoomModel.getUserReport().getEmail());
        txtTimeReport.setText(reportedRoomModel.getTime());
        txtReasonReport.setText(reportedRoomModel.getReason());
        txtDetailReport.setText(reportedRoomModel.getDetail());
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
