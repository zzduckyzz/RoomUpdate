package com.example.designapptest.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.adapter.AdapterRecyclerMainRoom;
import com.example.designapptest.adapter.AdapterRecyclerMyRoom;
import com.example.designapptest.adapter.AdapterRecyclerRoomWaitForApproval;
import com.example.designapptest.controller.Interfaces.ILocationModel;
import com.example.designapptest.controller.Interfaces.IMainRoomModel;
import com.example.designapptest.model.LocationModel;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.R;
import com.example.designapptest.adapter.locationAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivityController {
    Context context;
    RoomModel roomModel;
    LocationModel locationModel;
    String UID;

    // khai báo các biến liên quan tới load more.
    int quantityGridMainRoomLoaded = 0;
    int quantityGridMainRoomEachTime = 20;
    int quantityVerifiedRoomLoaded = 0;
    int quantityVerifiedRoomEachTime = 20;

    public MainActivityController(Context context, String UID) {
        this.context = context;
        this.roomModel = new RoomModel();
        this.locationModel = new LocationModel();
        this.UID = UID;
    }

    public void ListMainRoom(RecyclerView recyclerMainRoom, RecyclerView recyclerViewGridMainRoom, ProgressBar progressBarMain,
                             NestedScrollView nestedScrollMainView, Button btnLoadMoreVerifiedRooms,
                             ProgressBar progressBarLoadMoreGridMainRoom) {
        final List<RoomModel> roomModelList = new ArrayList<>();
        final List<RoomModel> roomModelListAuthentication = new ArrayList<>();

        //Tạo layout cho danh sách trọ tìm kiếm nhiều nhất
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerMainRoom.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerMainRoom adapterRecyclerMainRoom = new AdapterRecyclerMainRoom(context, roomModelListAuthentication, R.layout.room_element_list_view, UID);
        //Cài adapter cho recycle
        recyclerMainRoom.setAdapter(adapterRecyclerMainRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerMainRoom, false);
        //End tạo layout cho danh sách trọ tìm kiếm nhiều nhất

        //Tạo layout cho danh sách trọ được hiển thị theo dạng grid phía dưới
        RecyclerView.LayoutManager layoutManagerGrid = new GridLayoutManager(context, 2);
        recyclerViewGridMainRoom.setLayoutManager(layoutManagerGrid);

        //Tạo adapter cho recycle view
        final AdapterRecyclerMainRoom adapterRecyclerGridMainRoom = new AdapterRecyclerMainRoom(context, roomModelList, R.layout.room_element_grid_view, UID);
        //Cài adapter cho recycle
        recyclerViewGridMainRoom.setAdapter(adapterRecyclerGridMainRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerViewGridMainRoom, false);
        //End Tạo layout cho danh sách trọ được hiển thị theo dạng grid phía dưới

        //Tạo interface để truyền dữ liệu lên từ model
        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                roomModelList.add(valueRoom);

                adapterRecyclerGridMainRoom.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarMain.setVisibility(View.GONE);
                // Ẩn progress bar load more
                progressBarLoadMoreGridMainRoom.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {

            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }
        };

        IMainRoomModel iMainRoomModelAuthentication = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                roomModelListAuthentication.add(valueRoom);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerMainRoom.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {
                progressBarMain.setVisibility(View.GONE);
                // Hiển thị nút Xem thêm phòng đã xác nhận
                btnLoadMoreVerifiedRooms.setVisibility(View.VISIBLE);
            }

            @Override
            public void setProgressBarLoadMore() {

            }

            @Override
            public void setQuantityTop(int quantity) {

            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }

        };

        // Gọi hàm lấy dữ liệu khi scroll xuống đáy.
        nestedScrollMainView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreGridMainRoom.setVisibility(View.VISIBLE);

                                quantityGridMainRoomLoaded += quantityGridMainRoomEachTime;
                                roomModel.ListRoom(iMainRoomModel, quantityGridMainRoomLoaded + quantityGridMainRoomEachTime,
                                        quantityGridMainRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model.
        roomModel.ListRoom(iMainRoomModel, quantityGridMainRoomLoaded + quantityGridMainRoomEachTime,
                quantityGridMainRoomLoaded);

        roomModel.getListAuthenticationRoomsAtMainView(iMainRoomModelAuthentication, 5);
    }

    // Hàm kiểm tra danh sách tiện nghi
    private boolean checkTwoConvenients(List<String> lst1, List<String> lst2) {
        if (lst1 == null || lst2 == null)
            return false;

        // Biến đếm số lượng tiện nghi trùng lặp.
        int count = 0;
        int i, j;

        for (i = 0; i < lst1.size(); i++) {
            for (j = 0; j < lst2.size(); j++) {
                if (lst1.get(i).equals(lst2.get(j))) {
                    count++;
                }
            }
        }

        // Số lượng tiện nghi phải lớn hơn ba.
        if (count >= 3)
            return true;

        return false;
    }


    public void ListRoomUser(String userID, RecyclerView recyclerMainRoom, TextView txtQuantity, ProgressBar progressBarMyRooms,
                             LinearLayout lnLtQuantityTopMyRooms, NestedScrollView nestedScrollMyRoomsView,
                             ProgressBar progressBarLoadMoreMyRooms,boolean isAdminCall) {
        final List<RoomModel> roomModelList = new ArrayList<>();

        //Tạo layout cho danh sách trọ tìm kiếm nhiều nhất
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerMainRoom.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
//        final AdapterRecyclerMainRoom adapterRecyclerMainRoom = new AdapterRecyclerMainRoom(context, roomModelList, R.layout.room_element_list_view, UID);
        AdapterRecyclerMyRoom adapterRecyclerMyRoom;
        if(isAdminCall){
            adapterRecyclerMyRoom = new AdapterRecyclerMyRoom(context, roomModelList, R.layout.item_room_admin_user);
        }else {
            adapterRecyclerMyRoom = new AdapterRecyclerMyRoom(context, roomModelList, R.layout.item_room_user);
        }
        //Cài adapter cho recycle
        recyclerMainRoom.setAdapter(adapterRecyclerMyRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerMainRoom, false);
        //End tạo layout cho danh sách trọ tìm kiếm nhiều nhất

        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                roomModelList.add(valueRoom);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerMyRoom.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarMyRooms.setVisibility(View.GONE);
                progressBarLoadMoreMyRooms.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopMyRooms.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }
        };

        nestedScrollMyRoomsView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreMyRooms.setVisibility(View.VISIBLE);

                                quantityVerifiedRoomLoaded += quantityVerifiedRoomEachTime;
                                roomModel.ListRoomUser(iMainRoomModel, userID,
                                        quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime,
                                        quantityVerifiedRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model
        roomModel.ListRoomUser(iMainRoomModel, userID,
                quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime,
                quantityVerifiedRoomLoaded);
    }

    public void loadTopLocation(GridView grVLocation) {


        //Tạo mới mảng cho gridview
        List<LocationModel> datalocation = new ArrayList<LocationModel>();

        //Tạo adapter cho gridview
        locationAdapter adapter = new locationAdapter(context, R.layout.row_element_grid_view_location, datalocation);
        grVLocation.setAdapter(adapter);

        //Tạo mới interface đề truyền dữ liệu lên từ model
        ILocationModel iLocationModel = new ILocationModel() {
            @Override
            public void getListTopRoom(List<LocationModel> topLocation) {
                datalocation.addAll(topLocation);
                adapter.notifyDataSetChanged();
            }
        };

        //Gọi hàm lấy dữ liệu và truyền vào interface
        locationModel.topLocation(iLocationModel);
    }

    public void getListVerifiedRooms(RecyclerView recyclerVerifiedRoom, TextView txtQuantity, ProgressBar progressBarVerifiedRooms,
                                     LinearLayout lnLtQuantityTopVerifiedRooms, NestedScrollView nestedScrollVerifiedRoomsView,
                                     ProgressBar progressBarLoadMoreVerifiedRooms) {
        final List<RoomModel> verifiedRoomsList = new ArrayList<>();

        //Tạo layout cho danh sách trọ đã xác nhận
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerVerifiedRoom.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerMainRoom adapterRecyclerVerifiedRoom = new AdapterRecyclerMainRoom(context, verifiedRoomsList, R.layout.room_element_list_view, UID);
        //Cài adapter cho recycle
        recyclerVerifiedRoom.setAdapter(adapterRecyclerVerifiedRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerVerifiedRoom, false);
        //End tạo layout cho danh sách trọ đã xác nhận

        //Tạo interface để truyền dữ liệu lên từ model
        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                verifiedRoomsList.add(valueRoom);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerVerifiedRoom.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarVerifiedRooms.setVisibility(View.GONE);
                // Ẩn progress bar load more
                progressBarLoadMoreVerifiedRooms.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopVerifiedRooms.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }
        };

        nestedScrollVerifiedRoomsView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreVerifiedRooms.setVisibility(View.VISIBLE);

                                quantityVerifiedRoomLoaded += quantityVerifiedRoomEachTime;
                                roomModel.getListAuthenticationRoomsAtVerifiedRoomsView(iMainRoomModel,
                                        quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime, quantityVerifiedRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model
        roomModel.getListAuthenticationRoomsAtVerifiedRoomsView(iMainRoomModel,
                quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime, quantityVerifiedRoomLoaded);
    }

    public void getListFavoriteRooms(RecyclerView recyclerFavoriteRoom, TextView txtQuantity, ProgressBar progressBarFavoriteRooms,
                                     LinearLayout lnLtQuantityTopFavoriteRooms, NestedScrollView nestedScrollFavoriteRoomsView,
                                     ProgressBar progressBarLoadMoreFavoriteRooms) {
        final List<RoomModel> favoriteRoomsList = new ArrayList<>();

        //Tạo layout cho danh sách trọ yêu thích
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerFavoriteRoom.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerMainRoom adapterRecyclerFavoriteRoom = new AdapterRecyclerMainRoom(context, favoriteRoomsList,
                R.layout.room_element_list_view, UID);
        //Cài adapter cho recycle
        recyclerFavoriteRoom.setAdapter(adapterRecyclerFavoriteRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerFavoriteRoom, false);
        //End tạo layout cho danh sách trọ yêu thích

        //Tạo interface để truyền dữ liệu lên từ model
        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                favoriteRoomsList.add(valueRoom);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerFavoriteRoom.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarFavoriteRooms.setVisibility(View.GONE);
                // Ẩn progress bar load more.
                progressBarLoadMoreFavoriteRooms.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopFavoriteRooms.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {
                quantityVerifiedRoomLoaded = quantityLoaded;
            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

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
                adapterRecyclerFavoriteRoom.removeItem(viewHolder, recyclerFavoriteRoom, adapterRecyclerFavoriteRoom, txtQuantity,
                        iMainRoomModel, roomModel, quantityVerifiedRoomLoaded, quantityVerifiedRoomEachTime);
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
        itemTouchHelper.attachToRecyclerView(recyclerFavoriteRoom);
        //

        nestedScrollFavoriteRoomsView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreFavoriteRooms.setVisibility(View.VISIBLE);

                                quantityVerifiedRoomLoaded += quantityVerifiedRoomEachTime;
                                roomModel.getListFavoriteRooms(iMainRoomModel, UID,
                                        quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime, quantityVerifiedRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model
        roomModel.getListFavoriteRooms(iMainRoomModel, UID,
                quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime, quantityVerifiedRoomLoaded);
    }

    public void addToFavoriteRooms(String roomId, Context context, MenuItem item) {
        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {

            }

            @Override
            public void makeToast(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void setIconFavorite(int iconRes) {
                item.setIcon(iconRes);
            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {

            }

            @Override
            public void setQuantityTop(int quantity) {

            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }
        };

        roomModel.addToFavoriteRooms(roomId, iMainRoomModel, UID);
    }

    public void removeFromFavoriteRooms(String roomId, Context context, MenuItem item) {
        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {

            }

            @Override
            public void makeToast(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void setIconFavorite(int iconRes) {
                item.setIcon(iconRes);
            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {

            }

            @Override
            public void setQuantityTop(int quantity) {

            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }
        };

        roomModel.removeFromFavoriteRooms(roomId, iMainRoomModel, UID);
    }

    public void ListRoomsAdminView(RecyclerView recyclerAdminRoomsView, TextView txtQuantity, ProgressBar progressBarAdminRooms,
                                   LinearLayout lnLtQuantityTopAdminRooms, NestedScrollView nestedScrollAdminRoomsView,
                                   ProgressBar progressBarLoadMoreAdminRooms) {
        final List<RoomModel> roomModelList = new ArrayList<>();

        //Tạo layout cho danh sách trọ tìm kiếm nhiều nhất
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAdminRoomsView.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerMainRoom adapterRecyclerAdminRooms = new AdapterRecyclerMainRoom(context, roomModelList, R.layout.room_element_list_view, UID);
        //Cài adapter cho recycle
        recyclerAdminRoomsView.setAdapter(adapterRecyclerAdminRooms);
        ViewCompat.setNestedScrollingEnabled(recyclerAdminRoomsView, false);
        //End tạo layout cho danh sách trọ tìm kiếm nhiều nhất

        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                roomModelList.add(valueRoom);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerAdminRooms.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarAdminRooms.setVisibility(View.GONE);
                progressBarLoadMoreAdminRooms.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopAdminRooms.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }

        };

        // Gọi hàm lấy dữ liệu khi scroll xuống đáy.
        nestedScrollAdminRoomsView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreAdminRooms.setVisibility(View.VISIBLE);

                                quantityVerifiedRoomLoaded += quantityVerifiedRoomEachTime;
                                roomModel.ListRoom(iMainRoomModel, quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime,
                                        quantityVerifiedRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model.
        roomModel.ListRoom(iMainRoomModel, quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime,
                quantityVerifiedRoomLoaded);
    }

    public void ListRoomsWaitForApprovalAdminView(RecyclerView recyclerAdminRoomsWaitForApprovalView,
                                                  TextView txtQuantity,
                                                  ProgressBar progressBarAdminRoomsWaitForApproval,
                                                  LinearLayout lnLtQuantityTopAdminRoomsWaitForApproval,
                                                  NestedScrollView nestedScrollAdminRoomsWaitForApprovalView,
                                                  ProgressBar progressBarLoadMoreAdminRoomsWaitForApproval) {
        final List<RoomModel> roomModelList = new ArrayList<>();

        //Tạo layout cho danh sách trọ tìm kiếm nhiều nhất
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAdminRoomsWaitForApprovalView.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerRoomWaitForApproval adapterRecyclerRoomWaitForApproval =
                new AdapterRecyclerRoomWaitForApproval(context, roomModelList, R.layout.element_room_wait_for_approval_list_view);
        //Cài adapter cho recycle
        recyclerAdminRoomsWaitForApprovalView.setAdapter(adapterRecyclerRoomWaitForApproval);
        ViewCompat.setNestedScrollingEnabled(recyclerAdminRoomsWaitForApprovalView, false);
        //End tạo layout cho danh sách trọ tìm kiếm nhiều nhất

        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());

                //Thêm vào trong danh sách trọ
                roomModelList.add(valueRoom);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerRoomWaitForApproval.notifyDataSetChanged();
            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarAdminRoomsWaitForApproval.setVisibility(View.GONE);
                progressBarLoadMoreAdminRoomsWaitForApproval.setVisibility(View.GONE);
            }

            @Override
            public void setQuantityTop(int quantity) {
                lnLtQuantityTopAdminRoomsWaitForApproval.setVisibility(View.VISIBLE);
                // Hiển thị kết quả trả về
                txtQuantity.setText(quantity + "");
            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {

            }

        };

        // Gọi hàm lấy dữ liệu khi scroll xuống đáy.
        nestedScrollAdminRoomsWaitForApprovalView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBarLoadMoreAdminRoomsWaitForApproval.setVisibility(View.VISIBLE);

                                quantityVerifiedRoomLoaded += quantityVerifiedRoomEachTime;
                                roomModel.ListRoomWaitForApproval(iMainRoomModel, quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime,
                                        quantityVerifiedRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model.
        roomModel.ListRoomWaitForApproval(iMainRoomModel, quantityVerifiedRoomLoaded + quantityVerifiedRoomEachTime,
                quantityVerifiedRoomLoaded);
    }

    public void getSumRooms(TextView txtSumRoomsAdminView) {
        IMainRoomModel iMainRoomModel = new IMainRoomModel() {
            @Override
            public void getListMainRoom(RoomModel valueRoom) {

            }

            @Override
            public void makeToast(String message) {

            }

            @Override
            public void setIconFavorite(int iconRes) {

            }

            @Override
            public void setButtonLoadMoreVerifiedRooms() {

            }

            @Override
            public void setProgressBarLoadMore() {

            }

            @Override
            public void setQuantityTop(int quantity) {

            }

            @Override
            public void setQuantityLoadMore(int quantityLoaded) {

            }

            @Override
            public void setSumRoomsAdminView(long quantity) {
                txtSumRoomsAdminView.setText(quantity + "");
            }
        };

        roomModel.SumRooms(iMainRoomModel);
    }
}
