package com.example.designapptest.view.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.designapptest.R;
import com.example.designapptest.view.account.AccountFragment;
import com.example.designapptest.view.login.LoginActivity;
import com.example.designapptest.view.room.FindRoom;
import com.example.designapptest.view.room.PostRoomActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenuActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    FrameLayout fragmentContainer;

    MainFragment HomeView;
    AccountFragment AccountFragment;
    FindRoom FindRoomView;
    int userRole;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        initControl();
        HomeView = new MainFragment();
        setFragment(HomeView);

        initUserLogin();
    }

    private void initUserLogin() {
        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        userRole = sharedPreferences.getInt(LoginActivity.USER_ROLE, 1);
    }

    //Khởi tạo control
    private void initControl() {
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            switch (id) {
                case R.id.nav_home:
                    //Chuyển sang màn hình home
                    setFragment(HomeView);
                    return true;
                case R.id.nav_find_room:
                    //Chuyển sang màn hình tìm kiếm ở ghép
                    FindRoomView = new FindRoom();
                    setFragment(FindRoomView);
                    return true;
                case R.id.nav_post_room:
                    if (2 == userRole) {    //role chu phong tro
                        //Hiển thị màn hình đăng phòng mới
                        Intent intent = new Intent(MainMenuActivity.this, PostRoomActivity.class);
                        startActivity(intent);
                        return true;
                    } else {
                        Toast.makeText(MainMenuActivity.this, "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                case R.id.nav_acount:
                    //Chuyển sang màn hình quản lý tài khoản
                    AccountFragment = new AccountFragment();
                    setFragment(AccountFragment);
                    return true;

                default:
                    return false;
            }
        });
    }

    //Hàm replace fragment tương ứng khi chọn vào menu
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
