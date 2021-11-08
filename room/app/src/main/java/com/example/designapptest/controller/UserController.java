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
import com.example.designapptest.adapter.AdapterRecyclerHost;
import com.example.designapptest.controller.Interfaces.IUserModel;
import com.example.designapptest.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    Context context;
    UserModel userModel;

    int quantityHostLoaded = 0;
    int quantityHostEachTime = 20;

    public UserController(Context context) {
        this.context = context;
        this.userModel = new UserModel();
    }

    public void ListHosts(RecyclerView recyclerAdminHostsView, TextView txtQuantity, ProgressBar progressBarAdminHosts,
                          LinearLayout lnLtQuantityTopAdminHosts, NestedScrollView nestedScrollAdminHostsView,
                          ProgressBar progressBarLoadMoreAdminHosts) {
        final List<UserModel> userModelList = new ArrayList<>();

        //Tạo layout cho danh sách trọ tìm kiếm nhiều nhất
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAdminHostsView.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerHost adapterRecyclerAdminHosts = new AdapterRecyclerHost(context, userModelList, R.layout.element_host_list_view);
        //Cài adapter cho recycle
        recyclerAdminHostsView.setAdapter(adapterRecyclerAdminHosts);
        ViewCompat.setNestedScrollingEnabled(recyclerAdminHostsView, false);
        //End tạo layout cho danh sách trọ tìm kiếm nhiều nhất

        IUserModel iHostModel = new IUserModel() {
            @Override
            public void getListUsers(UserModel valueUser) {
                // Load ảnh nén
                valueUser.setCompressionImageFit(Picasso.get().load(valueUser.getAvatar()).fit());

                //Thêm vào trong danh sách chủ trọ
                userModelList.add(valueUser);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerAdminHosts.notifyDataSetChanged();
            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarAdminHosts.setVisibility(View.GONE);
                progressBarLoadMoreAdminHosts.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopAdminHosts.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setSumHostsAdminView(long quantity) {

            }

        };

        // Gọi hàm lấy dữ liệu khi scroll xuống đáy.
        nestedScrollAdminHostsView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreAdminHosts.setVisibility(View.VISIBLE);

                                quantityHostLoaded += quantityHostEachTime;
                                userModel.ListHosts(iHostModel, quantityHostLoaded + quantityHostEachTime,
                                        quantityHostLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model.
        userModel.ListHosts(iHostModel, quantityHostLoaded + quantityHostEachTime,
                quantityHostLoaded);
    }

    public void getSumHosts(TextView txtSumHostsAdminView) {
        IUserModel iHostModel = new IUserModel() {
            @Override
            public void getListUsers(UserModel valueUser) {
            }

            @Override
            public void setProgressBarLoadMore() {
            }

            @Override
            public void setQuantityTop(int quantity) {
            }

            @Override
            public void setSumHostsAdminView(long quantity) {
                txtSumHostsAdminView.setText(quantity + "");
            }
        };

        userModel.SumHosts(iHostModel);
    }

    public void addUser(UserModel newUserModel, String uid) {
        userModel.addUser(newUserModel, uid);
    }
}
