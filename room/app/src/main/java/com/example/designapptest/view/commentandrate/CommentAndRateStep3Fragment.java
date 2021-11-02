package com.example.designapptest.view.commentandrate;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.controller.CommentController;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.R;
import com.example.designapptest.view.login.LoginActivity;

public class CommentAndRateStep3Fragment extends Fragment {
    TextView txtQuantityMyComments;
    ProgressBar progressBarMyComments;
    LinearLayout lnLyQuantityTopMyComments;

    NestedScrollView nestedScrollMyComments;
    ProgressBar progressBarLoadMoreMyComments;

    View viewCommentAndRateStep3;

    RecyclerView recyclerMyComments;

    RoomModel roomModel;

    SharedPreferences sharedPreferences;
    String UID;

    CommentController commentController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        viewCommentAndRateStep3 = inflater.inflate(R.layout.comment_and_rate_step_3_room_detail_view, container, false);

        // Lấy đối tượng roomModel truyền từ room_detail
        roomModel = ((CommentAndRateMainActivity)this.getActivity()).getRoomModelInfo();
        sharedPreferences = ((CommentAndRateMainActivity)this.getActivity()).getSharedPreferences(LoginActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID,"n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();

        setAdapter();

        return viewCommentAndRateStep3;
    }

    @Override
    public void onStart() {
        super.onStart();

        setView();
    }

    public CommentAndRateStep3Fragment() {

    }

    private void initControl() {
        recyclerMyComments = (RecyclerView) viewCommentAndRateStep3.findViewById(R.id.recycler_my_comment_and_rate);

        txtQuantityMyComments = (TextView) viewCommentAndRateStep3.findViewById(R.id.txt_quantity_my_comments_detail_room);
        lnLyQuantityTopMyComments = (LinearLayout) viewCommentAndRateStep3.findViewById(R.id.lnLt_quantity_top_my_comments_detail_room);
        progressBarMyComments = (ProgressBar) viewCommentAndRateStep3.findViewById(R.id.progress_bar_my_comments_detail_room);
        progressBarMyComments.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        nestedScrollMyComments = (NestedScrollView) viewCommentAndRateStep3.findViewById(R.id.nested_scroll_my_comments_detail_room);
        progressBarLoadMoreMyComments = (ProgressBar) viewCommentAndRateStep3.findViewById(R.id.progress_bar_load_more_my_comments_detail_room);
        progressBarLoadMoreMyComments.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void setView() {
        // set view
        progressBarMyComments.setVisibility(View.VISIBLE);
        lnLyQuantityTopMyComments.setVisibility(View.GONE);
        progressBarLoadMoreMyComments.setVisibility(View.GONE);
    }

    public void setAdapter() {
        commentController = new CommentController((CommentAndRateMainActivity)this.getActivity(), UID);
        commentController.ListMyRoomComments(recyclerMyComments, roomModel, progressBarMyComments,
                lnLyQuantityTopMyComments, txtQuantityMyComments,
                nestedScrollMyComments, progressBarLoadMoreMyComments);
    }
}
