package com.example.designapptest.view.main;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.example.designapptest.adapter.AdapterPhoto;
import com.example.designapptest.model.PhotoModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.designapptest.R;
import com.example.designapptest.controller.MainActivityController;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.view.login.LoginActivity;
import com.example.designapptest.view.room.VerifiedRoomsViewActivity;
import com.example.designapptest.view.searchandmap.LocationSearchActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainFragment extends Fragment {

    //Qui thêm vào
    RecyclerView recyclerMainRoom;
    RecyclerView recyclerGridMainRoom;
    MainActivityController mainActivityController;
    ProgressBar progressBarMain;
    //End Qui thêm vào

    // Linh thêm
    NestedScrollView nestedScrollMainView;
    Button btnLoadMoreVerifiedRooms;
    ProgressBar progressBarLoadMoreGridMainRoom;
    // End Linh thêm

    // GridView grVRoom;
    GridView grVLocation;

    EditText edTSearch;

    //Them vao de test
    FusedLocationProviderClient client;

    SharedPreferences sharedPreferences;
    String UID;

    CollapsingToolbarLayout collapsingToolbarLayout;

    FloatingActionButton btnFabSearch;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    AdapterPhoto photoAdapter;
    List<PhotoModel> mListPhoto;
    Timer mTimer;

    //Layout
    View layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.activity_main, container, false);
        initControl();
        clickSearchRoom();
        clickLoadMoreVerifiedRooms();
        autoSlideImages();

        return layout;
    }

    private void initControl() {
        viewPager = layout.findViewById(R.id.viewpager);
        circleIndicator=layout.findViewById(R.id.circle_indicator);
        mListPhoto = getListPhoto();
        photoAdapter = new AdapterPhoto(this, mListPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        grVLocation = layout.findViewById(R.id.grV_location);

        edTSearch = layout.findViewById(R.id.edT_search);

        nestedScrollMainView = (NestedScrollView) layout.findViewById(R.id.nested_scroll_main_view);
        btnLoadMoreVerifiedRooms = (Button) layout.findViewById(R.id.btn_load_more_verified_rooms);
        progressBarLoadMoreGridMainRoom = (ProgressBar) layout.findViewById(R.id.progress_bar_grid_main_rooms);
        progressBarLoadMoreGridMainRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerMainRoom = (RecyclerView) layout.findViewById(R.id.recycler_Main_Room);
        recyclerGridMainRoom = (RecyclerView) layout.findViewById(R.id.recycler_Grid_Main_Room);
        progressBarMain = (ProgressBar) layout.findViewById(R.id.Progress_Main);
        progressBarMain.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        btnFabSearch = layout.findViewById(R.id.btn_fab_search);
        btnFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkclick", "onClick: ");
                Intent intentSearchLocation = new Intent(getContext(), LocationSearchActivity.class);
                startActivity(intentSearchLocation);
            }
        });
    }
    private List<PhotoModel> getListPhoto(){
        List<PhotoModel> list = new ArrayList<>();
        list.add(new PhotoModel(R.drawable.pink1));
        list.add(new PhotoModel(R.drawable.pink2));
        list.add(new PhotoModel(R.drawable.pink3));
        list.add(new PhotoModel(R.drawable.pink4));
        return list;
    }

    private void autoSlideImages(){
        if(mListPhoto == null || mListPhoto.isEmpty() || viewPager == null){
            return;
        }
        if(mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size() -1;
                        if(currentItem < totalItem){
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },500,3000);
    }

    private void setView() {
        // Hiển thị progress bar main
        progressBarMain.setVisibility(View.VISIBLE);
        // Ẩn nút Xem thêm phòng đã xác nhận
        btnLoadMoreVerifiedRooms.setVisibility(View.GONE);
        // Ẩn progress bar load more grid main rooms
        progressBarLoadMoreGridMainRoom.setVisibility(View.GONE);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    private void clickSearchRoom() {
        edTSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkclick", "onClick: ");
                Intent intentSearchLocation = new Intent(getContext(), LocationSearchActivity.class);
                startActivity(intentSearchLocation);
            }
        });
    }

    private void clickLoadMoreVerifiedRooms() {
        btnLoadMoreVerifiedRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVerifiedRooms = new Intent(getContext(), VerifiedRoomsViewActivity.class);
                startActivity(intentVerifiedRooms);
            }
        });
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    public void onStart() {
        super.onStart();

        setView();

        // detail room dùng
        RoomModel.getListFavoriteRoomsId(UID);

        mainActivityController = new MainActivityController(getContext(), UID);
        mainActivityController.ListMainRoom(recyclerMainRoom, recyclerGridMainRoom, progressBarMain,
                nestedScrollMainView, btnLoadMoreVerifiedRooms, progressBarLoadMoreGridMainRoom);

        //Load top địa điểm nhiều phòng
        mainActivityController.loadTopLocation(grVLocation);
    }
    //End load dữ liệu vào danh sách trong lần đầu chạy
}
