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
import com.example.designapptest.adapter.AdapterRecyclerView;
import com.example.designapptest.controller.Interfaces.IViewModel;
import com.example.designapptest.model.ViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PopUpViewsController {

    ViewModel viewModel;
    Context context;

    // khai báo các biến liên quan tới load more.
    int quantityViewLoaded = 0;
    int quantityViewEachTime = 20;

    public PopUpViewsController(Context context) {
        this.context = context;
        viewModel = new ViewModel();
    }

    public void ListRoomView(RecyclerView recyclerRoomView, String roomID,
                             LinearLayout lnLtTopAllView, ProgressBar progressBarAllView, TextView txtQuantityAllView,
                             NestedScrollView nestedScrollAllView, ProgressBar progressBarLoadMoreAllViews) {

        final List<ViewModel> viewModelList = new ArrayList<ViewModel>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerRoomView.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerView adapterRecyclerView = new AdapterRecyclerView(context, R.layout.element_view_recycler_view, viewModelList);
        recyclerRoomView.setAdapter(adapterRecyclerView);
        ViewCompat.setNestedScrollingEnabled(recyclerRoomView, false);

        IViewModel iViewModel = new IViewModel() {
            @Override
            public void getViewInfo(ViewModel viewModel) {
                viewModel.setCompressionImageFit(Picasso.get().load(viewModel.getUserView().getAvatar()).fit());
                //Log.d("check3", viewModel.getUserView().getName());
                //Thêm vào trong danh sách bình luận
                viewModelList.add(viewModel);

                //sortListComments(commentModelList);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerView.notifyDataSetChanged();
            }

            @Override
            public void setView() {

            }

            @Override
            public void setLinearLayoutTopAllView(List<ViewModel> listViewsModel) {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarAllView.setVisibility(View.GONE);
                progressBarLoadMoreAllViews.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityViews(int quantity) {
                lnLtTopAllView.setVisibility(View.VISIBLE);
                txtQuantityAllView.setText(quantity + "");
            }
        };

        nestedScrollAllView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreAllViews.setVisibility(View.VISIBLE);

                                quantityViewLoaded += quantityViewEachTime;

                                viewModel.getListRoomComments(iViewModel, roomID,
                                        quantityViewLoaded + quantityViewEachTime, quantityViewLoaded);
                            }
                        }
                    }
                }
            }

        });


        viewModel.getListRoomComments(iViewModel, roomID, quantityViewLoaded + quantityViewEachTime, quantityViewLoaded);
    }
}
