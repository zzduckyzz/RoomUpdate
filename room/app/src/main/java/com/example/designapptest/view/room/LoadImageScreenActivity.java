package com.example.designapptest.view.room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterRecyclerLoadImage;
import com.example.designapptest.model.LoadImageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoadImageScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RQ_PERMISS_READEX = 10;
    public static final String INTENT_LIST_IMAGE = "ListPathImageChoosed";
    List<LoadImageModel> listPathImage;
    List<String> listPathImageChoosed;

    RecyclerView recyclerLoadImage;
    AdapterRecyclerLoadImage adapterRecyclerLoadImage;

    TextView txtDone;
    ImageView imgTakePicture;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image_screen);

        initData();
        initControl();

        //Check quyền truy cập vào thẻ nhớ
        int checkReadExStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (checkReadExStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RQ_PERMISS_READEX);
        } else {
            getAllImage();
        }
    }

    //Khởi tạo control
    private void initControl() {
        txtDone = findViewById(R.id.txt_done);
        txtDone.setOnClickListener(this);

        imgTakePicture = findViewById(R.id.img_take_picture);

        recyclerLoadImage = findViewById(R.id.recycler_load_image);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapterRecyclerLoadImage = new AdapterRecyclerLoadImage(this, R.layout.load_image_element_recycler_view, listPathImage);
        recyclerLoadImage.setLayoutManager(layoutManager);
        recyclerLoadImage.setAdapter(adapterRecyclerLoadImage);
    }

    //Set data mặc định
    private void initData() {
        listPathImage = new ArrayList<LoadImageModel>();
    }

    //Hàm trả về danh sách tất cả các đường dẫn hinh trên máy
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getAllImage() {
        String[] projection = {MediaStore.Images.Media.DATA};
        //Lấy tất cả hình trong thẻ nhớ
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String Path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            LoadImageModel loadImageModel = new LoadImageModel(Path, false);
            listPathImage.add(loadImageModel);

            Log.d("kiem tra", Path);
            adapterRecyclerLoadImage.notifyDataSetChanged();
            cursor.moveToNext();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RQ_PERMISS_READEX) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllImage();
            } else {
                //Đóng activity
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txt_done:
                listPathImageChoosed = SearchChoosedInList();

                //Truyền dữ liệu về màn hình trước
                Intent data = getIntent();
                data.putStringArrayListExtra(INTENT_LIST_IMAGE, (ArrayList<String>) listPathImageChoosed);

                setResult(RESULT_OK, data);
                //End Truyền dữ liệu về màn hình trước

                //Đóng màn hình
                finish();
                break;

            case R.id.img_take_picture:

                break;
        }
    }

    //Hàm lọc ra các hình được chọn dùng lamda expression
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> SearchChoosedInList() {

        List<LoadImageModel> result = listPathImage.stream()
                .filter(item -> item.isChecked() == true)
                .collect(Collectors.toList());
        List<String> listData = new ArrayList<>();
        for (LoadImageModel data : result) {
            listData.add(data.getPath());
        }
        return listData;
    }
}
