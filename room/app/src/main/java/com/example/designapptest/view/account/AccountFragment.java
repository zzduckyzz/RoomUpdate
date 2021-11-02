package com.example.designapptest.view.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.designapptest.R;
import com.example.designapptest.view.main.FindRoomMine;
import com.example.designapptest.view.room.FavoriteRoomsActivity;
import com.example.designapptest.view.room.RoomManageActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private Button btnEditAccount;
    private Button btnMyRoom;
    private Button btnMyFavoriteRoom;
    private Button btnLogout;
    private Button btnSupport;
    private Button btnMyFindRoom;

    FirebaseAuth firebaseAuth;

    //View để liên kết
    View layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_account_view, container, false);
        initControl();
        return layout;
    }

    //Khởi tạo control
    private void initControl() {
        btnEditAccount = (Button) layout.findViewById(R.id.btn_edit_account);
        btnMyRoom = layout.findViewById(R.id.btn_my_Room);
        btnMyFavoriteRoom = layout.findViewById(R.id.btn_my_favorite_room);
        btnLogout = layout.findViewById(R.id.btn_logout);
        btnSupport = layout.findViewById(R.id.btn_contact_support);
        btnMyFindRoom = layout.findViewById(R.id.btn_my_find_room);
        btnMyFindRoom.setOnClickListener(this);

        btnMyRoom.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnSupport.setOnClickListener(this);
        btnEditAccount.setOnClickListener(this);
        btnMyFavoriteRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_edit_account:
                Intent intent = new Intent(getContext(), PersonalActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_my_Room:
                Intent intent1 = new Intent(getContext(), RoomManageActivity.class);
                startActivity(intent1);
                break;

            case R.id.btn_my_favorite_room:
                Intent intentFavoriteRooms = new Intent(getContext(), FavoriteRoomsActivity.class);
                startActivity(intentFavoriteRooms);
                break;
            case R.id.btn_my_find_room:
                Intent intentMyFindRooms = new Intent(getContext(), FindRoomMine.class);
                startActivity(intentMyFindRooms);
                break;

            case R.id.btn_contact_support: {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://zalo.me/0966419464"));
                startActivity(browserIntent);
                break;
            }
            case R.id.btn_logout:
                //Khởi tạo firebaseAuth
                firebaseAuth = FirebaseAuth.getInstance();
                //Text Đăng xuất
                firebaseAuth.signOut();
                getActivity().finish();
        }
    }
}
