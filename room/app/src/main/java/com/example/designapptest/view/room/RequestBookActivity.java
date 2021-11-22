package com.example.designapptest.view.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterBookRoom;
import com.example.designapptest.adapter.AdapterBookRoomRequest;
import com.example.designapptest.model.BookRoomModel;
import com.example.designapptest.view.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestBookActivity extends AppCompatActivity {
    ListView lstViewBookRoom;
    SharedPreferences sharedPreferences;
    String UID;
    Toolbar toolbar;
    int userRole;

    @Override
    protected void onRestart() {
        super.onRestart();
        new  AsyncTaskNetworkRequest(RequestBookActivity.this,lstViewBookRoom, UID, userRole).execute(UID);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Yêu cầu đặt phòng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sharedPreferences = getSharedPreferences(LoginActivity.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginActivity.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");
        userRole = sharedPreferences.getInt(LoginActivity.USER_ROLE, 1);
        System.out.println(UID);
        lstViewBookRoom = (ListView) findViewById(R.id.listRequsetBookRoom);
        new  AsyncTaskNetworkRequest(RequestBookActivity.this,lstViewBookRoom, UID, userRole).execute(UID);
    }
}

class AsyncTaskNetworkRequest extends AsyncTask<String, Void, String> {
    List<BookRoomModel> bookRoomModelList = new ArrayList<>();
    AdapterBookRoomRequest adapterBookRoom;
    ProgressDialog progressDialog;
    Context context;
    ListView listView;
    String UID;
    final Handler handler = new Handler();
    int userRole;
    public AsyncTaskNetworkRequest(Context context, ListView li, String UID, int ok) {
        this.context = context;
        this.listView = li;
        this.UID = UID;
        this.userRole = ok;
    }

    @Override
    protected String doInBackground(String... lists) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("BookRoom");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookRoomModel roomModel = ds.getValue(BookRoomModel.class);
                    if(roomModel.getAuthID().equals(lists[0])){
                        bookRoomModelList.add(roomModel);
                        System.out.println(bookRoomModelList.size());
                    }
                }
                System.out.println(bookRoomModelList.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Can not get data book room");
            }
        };

        usersRef.addListenerForSingleValueEvent(valueEventListener);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("loading...");
        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Gửi yêu cầu");
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aString) {
        super.onPostExecute(aString);
        // Hủy dialog đi.
        System.out.println("ok loading");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                // Hiển thị IP lên TextView.
                adapterBookRoom = new AdapterBookRoomRequest(context, R.layout.element_book_room, bookRoomModelList, userRole);
                listView.setAdapter(adapterBookRoom);
            }
        }, 3000);

    }
}