package com.example.designapptest.view.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.designapptest.R;
import com.example.designapptest.model.UserModel;
import com.example.designapptest.view.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PersonalActivity extends AppCompatActivity {
    private EditText edtNamePersonalPage;
    private Spinner spnGenderPersonalPage;
    private EditText edtPhonePersonalPage;
    private ImageView imgAvtPersonalPage;
    private ImageButton btnImgCancelPersonalPage;
    private CheckBox chBoxEditPersonalPage;
    private TextView txtChangeAvtPersonalPage;
    private TextView txtPasswordPersonalPage;
    private EditText edtOldPasswordPersonalPage, edtNewPasswordPersonalPage, edtRetypeNewPasswordPersonalPage;
    private Button btnChangePasswordPersonalPage;

    private String mImageUrl;
    private Uri uri;

    private boolean isChangeAvatar = false;
    private boolean isMale;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private UserModel userModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page_view);
        edtNamePersonalPage = findViewById(R.id.edt_name_personal_page);
        spnGenderPersonalPage = findViewById(R.id.spn_gender_personal_page);
        edtPhonePersonalPage = findViewById(R.id.edt_phone_personal_page);
        imgAvtPersonalPage = findViewById(R.id.img_avt_personal_page);
        chBoxEditPersonalPage = findViewById(R.id.chBox_edit_personal_page);
        btnImgCancelPersonalPage = findViewById(R.id.btnImg_cancel_personal_page);
        txtChangeAvtPersonalPage = findViewById(R.id.txt_change_avt_personal_page);
        txtPasswordPersonalPage = findViewById(R.id.tv_password_personal_page);
        edtOldPasswordPersonalPage = findViewById(R.id.edt_old_password_personal_page);
        edtNewPasswordPersonalPage = findViewById(R.id.edt_new_password_personal_page);
        edtRetypeNewPasswordPersonalPage = findViewById(R.id.edt_retype_new_password_personal_page);
        btnChangePasswordPersonalPage = findViewById(R.id.btn_change_password_personal_page);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        initControl();
        EditPersonal();
        cancel();
    }

    private void getDataFromControl() {
        String name = edtNamePersonalPage.getText().toString();
        String phoneNumber = edtPhonePersonalPage.getText().toString();

        userModel.setName(name);
        userModel.setPhoneNumber(phoneNumber);
    }

    private void initControl() {
        List<String> list = new ArrayList<>();
        list.add("Nam");
        list.add("Nữ");
        list.add("Cả nam và nữ");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenderPersonalPage.setAdapter(adapter);
        spnGenderPersonalPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isMale = i == 0;
                spnGenderPersonalPage.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences2 = this.getSharedPreferences(LoginActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        String UID = sharedPreferences2.getString(LoginActivity.SHARE_UID, "");

        DatabaseReference nodeUser = databaseReference.child("Users").child(UID);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);

                edtNamePersonalPage.setText(userModel.getName());
                edtPhonePersonalPage.setText(userModel.getPhoneNumber());
                if (userModel.isGender()) {
                    spnGenderPersonalPage.setSelection(0);
                } else {
                    spnGenderPersonalPage.setSelection(1);
                }
                Picasso.get().load(userModel.getAvatar()).into(imgAvtPersonalPage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeUser.addListenerForSingleValueEvent(valueEventListener);
    }

    private void EditPersonal() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences2 = this.getSharedPreferences(LoginActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        String UID = sharedPreferences2.getString(LoginActivity.SHARE_UID, "");

        DatabaseReference nodeUser = databaseReference.child("Users").child(UID);

        chBoxEditPersonalPage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                chBoxEditPersonalPage.setBackground(getResources().getDrawable(R.drawable.ic_svg_check_24));
                btnImgCancelPersonalPage.setImageDrawable(getResources().getDrawable(R.drawable.ic_svg_cancel_24));
                edtNamePersonalPage.setEnabled(true);
                spnGenderPersonalPage.setEnabled(true);
                edtPhonePersonalPage.setEnabled(true);
                txtChangeAvtPersonalPage.setText("Đổi ảnh đại diện");
                txtPasswordPersonalPage.setText("Đổi mật khẩu");

                changeImage();
            } else {
                progressDialog.setMessage("Đang chỉnh sửa ...");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                chBoxEditPersonalPage.setBackground(getResources().getDrawable(R.drawable.ic_svg_edit_24));
                btnImgCancelPersonalPage.setImageDrawable(null);
                edtNamePersonalPage.setEnabled(false);
                spnGenderPersonalPage.setEnabled(false);
                edtPhonePersonalPage.setEnabled(false);
                txtChangeAvtPersonalPage.setText("");
                txtPasswordPersonalPage.setText("");
                edtOldPasswordPersonalPage.setWidth(0);
                edtNewPasswordPersonalPage.setWidth(0);
                edtRetypeNewPasswordPersonalPage.setWidth(0);

                if (isChangeAvatar) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Users");
                    StorageReference ref = storageReference.child(uri.getLastPathSegment());

                    ref.putFile(uri).continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return ref.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getDataFromControl();
                            Uri downloadUri = task.getResult();
                            mImageUrl = downloadUri.toString();

                            userModel.setAvatar(mImageUrl);
                            userModel.setGender(isMale);
                            nodeUser.setValue(userModel);
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    getDataFromControl();

                    nodeUser.child("gender").setValue(isMale);
                    nodeUser.child("name").setValue(userModel.getName());
                    nodeUser.child("phoneNumber").setValue(userModel.getPhoneNumber());
                    progressDialog.dismiss();
                }
            }

        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences2 = this.getSharedPreferences(LoginActivity.PREFS_DATA_NAME, Context.MODE_PRIVATE);
        String UID = sharedPreferences2.getString(LoginActivity.SHARE_UID, "");

        DatabaseReference nodeUser = databaseReference.child("Users").child(UID);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Log.d("check", uri.getLastPathSegment());

            Picasso.get().load(uri).into(imgAvtPersonalPage);
            isChangeAvatar = true;
        }
    }

    private void changeImage() {
        imgAvtPersonalPage.setOnClickListener(v -> chooseImage());
    }

    private void cancel() {
        btnImgCancelPersonalPage.setOnClickListener(v -> {
            chBoxEditPersonalPage.setBackground(getResources().getDrawable(R.drawable.ic_svg_edit_24));
            btnImgCancelPersonalPage.setImageDrawable(null);
            edtNamePersonalPage.setEnabled(false);
            spnGenderPersonalPage.setEnabled(false);
            edtPhonePersonalPage.setEnabled(false);
            txtChangeAvtPersonalPage.setText("");
        });
    }

    public void ChangePass(View v) {
        edtOldPasswordPersonalPage.setVisibility(View.VISIBLE);
        edtNewPasswordPersonalPage.setVisibility(View.VISIBLE);
        edtRetypeNewPasswordPersonalPage.setVisibility(View.VISIBLE);


        btnChangePasswordPersonalPage.setVisibility(View.VISIBLE);

        btnChangePasswordPersonalPage.setOnClickListener(v1 -> {
            final String email = firebaseUser.getEmail();
            final String newPass = edtNewPasswordPersonalPage.getText().toString();
            final String retypeNewPass = edtRetypeNewPasswordPersonalPage.getText().toString();
            final String oldPass = edtOldPasswordPersonalPage.getText().toString();

            if (oldPass.equals("") || newPass.equals("") || retypeNewPass.equals("")) {
                Toast.makeText(PersonalActivity.this, "Hãy điền đủ thông tin yêu cầu", Toast.LENGTH_SHORT).show();
            } else {

                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);

                //kiểm tra mật khẩu cũ có giống vs mk đăng ký không, nếu giống thì updatepassword
                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (newPass.equals(retypeNewPass)) {
                                firebaseUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(PersonalActivity.this, "Lỗi!!!Xin thực hiện lại", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PersonalActivity.this, "Thay đổi mật khẩu thành  công", Toast.LENGTH_SHORT).show();
                                            edtOldPasswordPersonalPage.setVisibility(View.INVISIBLE);
                                            edtNewPasswordPersonalPage.setVisibility(View.INVISIBLE);
                                            edtRetypeNewPasswordPersonalPage.setVisibility(View.INVISIBLE);
                                            btnChangePasswordPersonalPage.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(PersonalActivity.this, "Xác nhận mật khẩu mới không đúng!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PersonalActivity.this, "Xác thực người dùng thất bại do nhập mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
