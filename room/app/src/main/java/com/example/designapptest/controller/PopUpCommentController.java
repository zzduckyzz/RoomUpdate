package com.example.designapptest.controller;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterRecyclerComment;
import com.example.designapptest.controller.Interfaces.IRoomCommentModel;
import com.example.designapptest.model.CommentModel;
import com.example.designapptest.model.RoomModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PopUpCommentController {
    CommentModel commentModel;
    Context context;
    String UID;

    // khai báo các biến liên quan tới load more.
    int quantityCommentsLoaded = 0;
    int quantityCommentsEachTime = 20;

    public PopUpCommentController(Context context, String UID) {
        this.context = context;
        this.commentModel = new CommentModel();
        this.UID = UID;
    }

    public void ListRoomComments(RecyclerView recyclerRoomComments, String roomID, TextView txtRoomGreatReview,
                                 TextView txtRoomPrettyGoodReview, TextView txtRoomMediumReview, TextView txtRoomBadReview,
                                 LinearLayout lnLtTopAllComments, ProgressBar progressBarAllComments,
                                 LinearLayout lnLyQuantityTopAllComments, TextView txtQuantityAllComments,
                                 NestedScrollView nestedScrollAllComments, ProgressBar progressBarLoadMoreAllComments) {

        RoomModel roomModel = new RoomModel();
        roomModel.setRoomID(roomID);

        final List<CommentModel> commentModelList = new ArrayList<CommentModel>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerRoomComments.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerComment adapterRecyclerComment = new AdapterRecyclerComment(context,
                R.layout.comment_element_grid_room_detail_view, commentModelList, roomID,
                UID, true);
        //Cài adapter cho recycle
        recyclerRoomComments.setAdapter(adapterRecyclerComment);
        ViewCompat.setNestedScrollingEnabled(recyclerRoomComments, false);

        //Tạo interface để truyền dữ liệu lên từ model
        IRoomCommentModel iRoomCommentsModel = new IRoomCommentModel() {
            @Override
            public void getListRoomComments(CommentModel valueComment) {
                // Load ảnh nén
                valueComment.setCompressionImageFit(Picasso.get().load(valueComment.getUserComment().getAvatar()).fit());

                //Thêm vào trong danh sách bình luận
                commentModelList.add(valueComment);

                //sortListComments(commentModelList);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerComment.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setView() {

            }

            @Override
            public void setLinearLayoutTopAllComments(List<CommentModel> listCommentsModel) {
                lnLtTopAllComments.setVisibility(View.VISIBLE);

                // Load số lượng bình luận thuộc từng loại điểm
                long great, prettyGood, medium, bad;
                great = prettyGood = medium = bad = 0;
                for (CommentModel commentModel : listCommentsModel) {
                    long stars = commentModel.getStars();

                    if (stars < 4) {
                        bad += 1;
                    } else if (stars < 7) {
                        medium += 1;
                    } else if (stars < 9) {
                        prettyGood += 1;
                    } else {
                        great += 1;
                    }
                }

                txtRoomBadReview.setText(bad + "");
                txtRoomMediumReview.setText(medium + "");
                txtRoomPrettyGoodReview.setText(prettyGood + "");
                txtRoomGreatReview.setText(great + "");
                // End load số lượng bình luận thuộc từng loại điểm
            }

            @Override
            public void setProgressBarLoadMore() {
                // set view
                progressBarAllComments.setVisibility(View.GONE);
                progressBarLoadMoreAllComments.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityComments(int quantity) {
                lnLyQuantityTopAllComments.setVisibility(View.VISIBLE);
                // Set số lượng hiển thị
                txtQuantityAllComments.setText(quantity + "");
            }
        };

        nestedScrollAllComments.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                // check xem có scroll đc ko
                View child = nestedScrollView.getChildAt(0);
                if (child != null) {
                    int childHeight = child.getHeight();
                    // Nếu scroll đc
                    if (nestedScrollView.getHeight() < childHeight + nestedScrollView.getPaddingTop() + nestedScrollView.getPaddingBottom()) {
                        View lastItemView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                        if (lastItemView != null) {
                            if (i1 >= lastItemView.getMeasuredHeight() - nestedScrollView.getMeasuredHeight()) {
                                // Hiển thị progress bar
                                progressBarLoadMoreAllComments.setVisibility(View.VISIBLE);

                                quantityCommentsLoaded += quantityCommentsEachTime;

                                commentModel.getListRoomComments(iRoomCommentsModel, roomModel,
                                        quantityCommentsLoaded + quantityCommentsEachTime, quantityCommentsLoaded);
                            }
                        }
                    }
                }
            }

        });

        //Gọi hàm lấy dữ liệu trong model
        commentModel.getListRoomComments(iRoomCommentsModel, roomModel,
                quantityCommentsLoaded + quantityCommentsEachTime, quantityCommentsLoaded);
    }

}
