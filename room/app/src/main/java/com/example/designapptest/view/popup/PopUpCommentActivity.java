package com.example.designapptest.view.popup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.example.designapptest.controller.PopUpCommentController;
import com.example.designapptest.view.login.LoginActivity;

public class PopUpCommentActivity extends AppCompatActivity {

    ImageButton btnFinish;
    String roomID;

    LinearLayout lnLtTopAllComments;
    TextView txtRoomGreatReview, txtRoomPrettyGoodReview, txtRoomMediumReview, txtRoomBadReview;

    TextView txtQuantityAllComments;
    ProgressBar progressBarAllComments;
    LinearLayout lnLyQuantityTopAllComments;

    NestedScrollView nestedScrollAllComments;
    ProgressBar progressBarLoadMoreAllComments;

    RecyclerView recyclerCommentRoomDetailAll;

    PopUpCommentController popUpCommentController;

    SharedPreferences sharedPreferences;
    String UID;

    RelativeLayout rLTTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_comment);
        roomID = getIntent().getStringExtra("phongtro");

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();

        changeDisplay();
    }

    //Hàm thay đổi kích thước của màn hình
    private void changeDisplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .9));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

    private void initControl() {

        recyclerCommentRoomDetailAll = findViewById(R.id.recycler_comment_and_rate_all);

        txtRoomGreatReview = findViewById(R.id.txt_roomGreatReview_all);
        txtRoomPrettyGoodReview = (TextView) findViewById(R.id.txt_roomPrettyGoodReview_all);
        txtRoomMediumReview = (TextView) findViewById(R.id.txt_roomMediumReview_all);
        txtRoomBadReview = (TextView) findViewById(R.id.txt_roomBadReview_all);
        lnLtTopAllComments = (LinearLayout) findViewById(R.id.lnLt_top_all_comments_detail_room);

        txtQuantityAllComments = (TextView) findViewById(R.id.txt_quantity_all_comments_detail_room);
        lnLyQuantityTopAllComments = (LinearLayout) findViewById(R.id.lnLt_quantity_top_all_comments_detail_room);
        progressBarAllComments = (ProgressBar) findViewById(R.id.progress_bar_all_comments_detail_room);
        progressBarAllComments.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        nestedScrollAllComments = (NestedScrollView) findViewById(R.id.nested_scroll_all_comments_detail_room);
        progressBarLoadMoreAllComments = (ProgressBar) findViewById(R.id.progress_bar_load_more_all_comments_detail_room);
        progressBarLoadMoreAllComments.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        rLTTop = findViewById(R.id.rLT_Top);
        rLTTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setView() {
        // Set view
        lnLtTopAllComments.setVisibility(View.GONE);
        progressBarAllComments.setVisibility(View.VISIBLE);
        lnLyQuantityTopAllComments.setVisibility(View.GONE);
        progressBarLoadMoreAllComments.setVisibility(View.GONE);
    }

    public void setAdapter() {
        popUpCommentController = new PopUpCommentController(this, UID);
        popUpCommentController.ListRoomComments(recyclerCommentRoomDetailAll, roomID, txtRoomGreatReview,
                txtRoomPrettyGoodReview, txtRoomMediumReview, txtRoomBadReview, lnLtTopAllComments,
                progressBarAllComments, lnLyQuantityTopAllComments, txtQuantityAllComments,
                nestedScrollAllComments, progressBarLoadMoreAllComments);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setView();
        setAdapter();
    }
}
