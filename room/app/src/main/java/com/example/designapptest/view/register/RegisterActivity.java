package com.example.designapptest.view.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.designapptest.R;
import com.example.designapptest.controller.UserController;
import com.example.designapptest.model.UserModel;
import com.example.designapptest.view.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signup;
    private ProgressDialog progressDialog;
    private RadioButton rad_gender_female_signUp, rad_gender_male_signUp, rad_user_type_user_signUp, rad_user_type_owner_signUp;
    private EditText edt_email_signUp, edt_password_signUp, edt_retype_password_signUp, edt_name_signUp, edt_phone_signUp;

    private FirebaseAuth firebaseAuth;
    private UserController userController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
    }

    private void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void initView() {
        btn_signup = (Button) findViewById(R.id.btn_signUp);
        edt_email_signUp = (EditText) findViewById(R.id.edt_email_signUp);
        edt_password_signUp = (EditText) findViewById(R.id.edt_password_signUp);
        edt_retype_password_signUp = (EditText) findViewById(R.id.edt_retype_password_signUp);
        edt_name_signUp = (EditText) findViewById(R.id.edt_name_signUp);
        edt_phone_signUp = (EditText) findViewById(R.id.edt_phone_signUp);
        rad_gender_female_signUp = (RadioButton) findViewById(R.id.rad_gender_female_signUp);
        rad_gender_male_signUp = (RadioButton) findViewById(R.id.rad_gender_male_signUp);
        rad_user_type_user_signUp = findViewById(R.id.rad_user_type_user_signUp);
        rad_user_type_owner_signUp = findViewById(R.id.rad_user_type_owner_signUp);

        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.MyProgessDialogStyle);
        userController = new UserController(this);

        btn_signup.setOnClickListener(this);
    }

    private void signUp() {
        String avatar = "user.png";
        String name = edt_name_signUp.getText().toString();
        String phone = edt_phone_signUp.getText().toString();
        String email = edt_email_signUp.getText().toString();
        String password = edt_password_signUp.getText().toString();
        String passwordRetype = edt_retype_password_signUp.getText().toString();

        boolean gender = true;
        if (rad_gender_female_signUp.isChecked()) {
            gender = false;
        }

        final Boolean genderUser = gender;
        String error = "Vui lòng nhập";
        if (email.trim().length() == 0) {
            error += " email";
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
        } else if (password.trim().length() == 0) {
            error += " mật khẩu";
            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
        } else if (!passwordRetype.equals(password)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp. Vui lòng nhập lại!", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Đang tạo tài khoản...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            int owner = 1;//1: user, 2: owner, 3: admin
            if (rad_user_type_user_signUp.isChecked()) {
                owner = 1;  //user
            } else if (rad_user_type_owner_signUp.isChecked()) {
                owner = 2;  //owner
            }

            int finalOwner = owner;

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserModel userModel = new UserModel();
                        userModel.setName(name);
                        userModel.setEmail(email);
                        userModel.setOwner(finalOwner);
                        userModel.setAvatar(avatar);
                        userModel.setGender(genderUser);
                        userModel.setPhoneNumber(phone);

                        String uid = Objects.requireNonNull(task.getResult()).getUser().getUid();

                        userController.addUser(userModel, uid);

                        progressDialog.dismiss();

                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
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
                signUp();
                break;
        }
    }
}
