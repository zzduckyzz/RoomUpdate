package com.example.designapptest.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterRecyclerReportRoom;
import com.example.designapptest.controller.Interfaces.IReportedRoomModel;
import com.example.designapptest.model.ReportedRoomModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReportedRoomController {
    ReportedRoomModel reportedRoomModel;
    Context context;

    int quantityReportRoomLoaded = 0;
    int quantityReportRoomEachTime = 20;

    public ReportedRoomController(Context context) {
        this.context = context;
        this.reportedRoomModel = new ReportedRoomModel();
    }

    public void addReport(ReportedRoomModel reportedRoomModel, String roomId) {
        IReportedRoomModel iReportedRoomModel = new IReportedRoomModel() {
            @Override
            public void makeToast(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getListReportRooms(ReportedRoomModel reportedRoomModel) {

            }

            @Override
            public void setProgressBarLoadMore() {

            }

            @Override
            public void setQuantityTop(int quantity) {

            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {
                quantityReportRoomLoaded = quantityLoaded;
            }
        };

        reportedRoomModel.addReport(reportedRoomModel, roomId, iReportedRoomModel);
    }

    public void ListReports(RecyclerView recyclerAdminReportRoomView, TextView txtQuantity, ProgressBar progressBarAdminReportRoom,
                            LinearLayout lnLtQuantityTopAdminReportRoom, NestedScrollView nestedScrollAdminReportRoomView,
                            ProgressBar progressBarLoadMoreAdminReportRoom) {
        final List<ReportedRoomModel> reportedRoomModelList = new ArrayList<>();

        //Tạo layout cho danh sách trọ tìm kiếm nhiều nhất
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAdminReportRoomView.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerReportRoom adapterRecyclerReportRoom = new AdapterRecyclerReportRoom(context, reportedRoomModelList, R.layout.element_report_list_view);
        //Cài adapter cho recycle
        recyclerAdminReportRoomView.setAdapter(adapterRecyclerReportRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerAdminReportRoomView, false);
        //End tạo layout cho danh sách trọ tìm kiếm nhiều nhất

        IReportedRoomModel iReportedRoomModel = new IReportedRoomModel() {
            @Override
            public void makeToast(String message) {

            }

            @Override
            public void getListReportRooms(ReportedRoomModel reportedRoomModel) {
                // Load ảnh nén
                reportedRoomModel.getReportedRoom()
                        .setCompressionImageFit(Picasso.get().load(reportedRoomModel.getReportedRoom().getCompressionImage())
                                .fit());

                //Thêm vào trong danh sách chủ trọ
                reportedRoomModelList.add(reportedRoomModel);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerReportRoom.notifyDataSetChanged();
            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarAdminReportRoom.setVisibility(View.GONE);
                progressBarLoadMoreAdminReportRoom.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopAdminReportRoom.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {
            }
        };

        //
        ColorDrawable swipeBackground = new ColorDrawable(Color.parseColor("#C03A2B"));
        Drawable deleteIcon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_garbage, null);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapterRecyclerReportRoom.removeItem(viewHolder, recyclerAdminReportRoomView, adapterRecyclerReportRoom,
                        txtQuantity, iReportedRoomModel, reportedRoomModel, quantityReportRoomLoaded,
                        quantityReportRoomEachTime);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                int iconMarginTopBottom = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;

                if (dX > 0) {
                    swipeBackground.setBounds(itemView.getLeft(), itemView.getTop(), ((int) dX), itemView.getBottom());
                    deleteIcon.setBounds(
                            itemView.getLeft() + deleteIcon.getIntrinsicWidth(),
                            itemView.getTop() + iconMarginTopBottom,
                            itemView.getLeft() + 2 * deleteIcon.getIntrinsicWidth(),
                            itemView.getBottom() - iconMarginTopBottom);
                } else {
                    swipeBackground.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(),
                            itemView.getBottom());
                    deleteIcon.setBounds(
                            itemView.getRight() - 2 * deleteIcon.getIntrinsicWidth(),
                            itemView.getTop() + iconMarginTopBottom,
                            itemView.getRight() - deleteIcon.getIntrinsicWidth(),
                            itemView.getBottom() - iconMarginTopBottom);
                }

                swipeBackground.draw(c);

                c.save();

                if (dX > 0) {
                    c.clipRect(itemView.getLeft(), itemView.getTop(), ((int) dX), itemView.getBottom());
                } else {
                    c.clipRect(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                }
                deleteIcon.draw(c);

                c.restore();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerAdminReportRoomView);
        //

        // Gọi hàm lấy dữ liệu khi scroll xuống đáy.
        nestedScrollAdminReportRoomView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreAdminReportRoom.setVisibility(View.VISIBLE);

                                quantityReportRoomLoaded += quantityReportRoomEachTime;
                                reportedRoomModel.ListReportedRooms(iReportedRoomModel,
                                        quantityReportRoomLoaded + quantityReportRoomEachTime,
                                        quantityReportRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model.
        reportedRoomModel.ListReportedRooms(iReportedRoomModel, quantityReportRoomLoaded + quantityReportRoomEachTime,
                quantityReportRoomLoaded);
    }
}
