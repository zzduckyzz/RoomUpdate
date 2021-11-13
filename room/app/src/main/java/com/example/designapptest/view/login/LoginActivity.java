package com.example.designapptest.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.designapptest.R;
import com.example.designapptest.model.UserModel;
import com.example.designapptest.view.account.ResetPasswordByEmailActivity;
import com.example.designapptest.view.admin.AdminViewActivity;
import com.example.designapptest.view.main.MainMenuActivity;
import com.example.designapptest.view.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener {
    public static final String SHARE_UID = "currentUserId";
    public static final String USER_ROLE = "userRole";
    public static final String PREFS_DATA_NAME = "currentUserId";
    public static final String NameUser = "NameUser";
    public static final String PhoneUser = "PhoneUser";
    private Button btn_signUp;
    private Button btn_login;
    private EditText edt_username_login;
    private EditText edt_password_login;
    private ProgressDialog progressDialog;

    private DatabaseReference nodeRoot;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_login = findViewById(R.id.btn_login);
        edt_username_login = findViewById(R.id.edt_username_login);
        edt_password_login = findViewById(R.id.edt_password_login);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.MyProgessDialogStyle);

        btn_signUp.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    private void initData() {
        //Khởi tạo firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Text Đăng xuất
        firebaseAuth.signOut();
        // Lưu mã user đăng nhập vào app
        sharedPreferences = getSharedPreferences(PREFS_DATA_NAME, MODE_PRIVATE);
        /*ClickForgotPassword();*/
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Thêm sự kiện listenerStateChange
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Xóa sự kiện ListenerStateChange
        firebaseAuth.removeAuthStateListener(this);
    }

    private void login() {
        String username = edt_username_login.getText().toString();
        String password = edt_password_login.getText().toString();

        if (username.trim().length() == 0 || password.trim().length() == 0) {
            Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Đang đăng nhập...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    System.out.println(task);
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_signUp:
                Intent iSignup = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(iSignup);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            checkUserLogin(user.getUid());
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    private void checkUserLogin(String UID) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lưu lại mã user đăng nhập vào app
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SHARE_UID, UID);

                UserModel userModel = null;

                for (DataSnapshot userValue : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(userValue.getKey()).equals(UID)) {
                        userModel = userValue.getValue(UserModel.class);
                        break;
                    }
                }
                if (userModel != null) {
                    int owner = userModel.getOwner();
                    editor.putString(NameUser, userModel.getName());
                    editor.putString(PhoneUser, userModel.getPhoneNumber());
                    if (1 == owner) {
                        editor.putInt(USER_ROLE, 1);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                    } else if (2 == owner) {
                        editor.putInt(USER_ROLE, 2);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                    } else {
                        editor.putInt(USER_ROLE, 3);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), AdminViewActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        };

        nodeRoot.child("Users").addListenerForSingleValueEvent(valueEventListener);
    }

    public void ClickForgotPassword(View v) {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordByEmailActivity.class);
        startActivity(intent);
    }



}