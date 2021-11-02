package com.example.designapptest.view.account;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.designapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordByEmailActivity extends AppCompatActivity {

     private EditText edtEmailResetPass;
     private Button btnResetPass;
     FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_by_email);

        edtEmailResetPass = findViewById(R.id.edt_email_reset_pass);
        btnResetPass = findViewById(R.id.btn_reset_pass);
        firebaseAuth = FirebaseAuth.getInstance();

        ResetPass();
    }

    public void ResetPass(){

        btnResetPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email= edtEmailResetPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Nhập email đã đăng ký", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordByEmailActivity.this, "Chúng tôi đã gửi hướng dẫn đặt lại mật khẩu tới email của bạn", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordByEmailActivity.this, "Lỗi gửi email!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

    }


}
