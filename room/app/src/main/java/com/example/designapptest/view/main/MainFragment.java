package com.example.designapptest.view.main;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import com.example.designapptest.R;
import com.example.designapptest.controller.MainActivityController;
import com.example.designapptest.model.RoomModel;
import com.example.designapptest.view.login.LoginActivity;
import com.example.designapptest.view.room.VerifiedRoomsViewActivity;
import com.example.designapptest.view.searchandmap.LocationSearchActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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

        return layout;
    }

    private void initControl() {
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
