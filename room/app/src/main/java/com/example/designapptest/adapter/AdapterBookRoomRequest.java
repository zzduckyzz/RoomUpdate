package com.example.designapptest.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.designapptest.R;
import com.example.designapptest.model.BookRoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterBookRoomRequest  extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final List<BookRoomModel> bookRoomModelList;
    private final int userRole;
    public AdapterBookRoomRequest(Context context, int layout, List<BookRoomModel> personList, int ok) {
        this.context = context;
        this.layout = layout;
        this.bookRoomModelList = personList;
        this.userRole = ok;
    }

    @Override
    public int getCount() {
        return bookRoomModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        // ANH XA
        TextView txt_CreatedAt = (TextView) convertView.findViewById(R.id.txt_CreateAt);
        TextView txt_NameBookRoom = (TextView) convertView.findViewById(R.id.txt_nameBookRoom);
        TextView txt_RentalCost = (TextView) convertView.findViewById(R.id.txt_RentalCost);
        TextView txt_Phone = (TextView) convertView.findViewById(R.id.txt_phone);
        TextView txt_Description = (TextView) convertView.findViewById(R.id.txt_description);
        TextView txt_City = (TextView) convertView.findViewById(R.id.txt_City);
        TextView txt_Country = (TextView) convertView.findViewById(R.id.txt_Country);
        TextView txt_Ward = (TextView) convertView.findViewById(R.id.txt_Ward);
        TextView txt_Street = (TextView) convertView.findViewById(R.id.txt_Street);
        TextView txt_Status = (TextView) convertView.findViewById(R.id.txt_Status);
        TextView txt_UName = (TextView) convertView.findViewById(R.id.txtUName);
        TextView txt_UPhone = (TextView) convertView.findViewById(R.id.txtUPhone);
        Button btn_CancelBookRoom = (Button) convertView.findViewById(R.id.btn_CancelBookRoom);
        Button btn_ok = (Button) convertView.findViewById(R.id.btn_okBook);
        // gan gia tri
        BookRoomModel data = bookRoomModelList.get(position);
        txt_CreatedAt.setText(data.getOrderAt());
        txt_NameBookRoom.setText(data.getNameRoom());
        txt_RentalCost.setText(data.getRentalCost()+" Triệu  |  ");
        txt_Phone.setText("Phone: "+data.getPhone());
        txt_Description.setText(data.getDescription());
        txt_City.setText(data.getCity()+"  -  ");
        txt_Country.setText(data.getCounty());
        txt_Ward.setText(data.getWard()+"  -  ");
        txt_Street.setText(data.getStreet());
        txt_UName.setText("Khách: "+data.getNameUser());
        txt_UPhone.setText(data.getPhoneUser());

        if(userRole == 2){ // chu phong

            String tmp = "Đã Từ chối";
            btn_CancelBookRoom.setEnabled(true);
            btn_CancelBookRoom.setText("Từ chối");
            btn_CancelBookRoom.setTextColor(Color.GRAY);
            btn_ok.setEnabled(true);
            btn_ok.setText("Duyệt");
            btn_ok.setTextColor(Color.GRAY);

            if(data.getID_Status().equals("1")){
                tmp = "Đợi xét duyệt";
                btn_CancelBookRoom.setEnabled(true);
                btn_CancelBookRoom.setTextColor(Color.BLACK);
                btn_ok.setTextColor(Color.BLACK);
            }
            if(data.getID_Status().equals("2")){
                tmp = "Đã duyệt";
                btn_CancelBookRoom.setTextColor(Color.GRAY);
                btn_ok.setTextColor(Color.GRAY);
                txt_Status.setTextColor(Color.RED);

            }

            btn_CancelBookRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getID_Status().equals("1")){
                        new AsyncTaskNetworkUpdateR(context, "0").execute(data.getID());
                        txt_Status.setText("Đã từ chối");
                        btn_CancelBookRoom.setTextColor(Color.BLACK);
                        btn_CancelBookRoom.setEnabled(false);
                        btn_ok.setEnabled(false);
                        btn_ok.setTextColor(Color.GRAY);
                    }
                }
            });
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getID_Status().equals("1")){
                        new AsyncTaskNetworkUpdateR(context, "2").execute(data.getID());
                        txt_Status.setText("Đã Duyệt");
                        btn_CancelBookRoom.setTextColor(Color.GRAY);
                        btn_CancelBookRoom.setEnabled(false);
                        btn_ok.setEnabled(false);
                        btn_ok.setTextColor(Color.GRAY);
                    }
                }
            });
            txt_Status.setText(tmp);
            System.out.println(tmp);
        }else{
            String tmp = "Từ chối";
            btn_CancelBookRoom.setEnabled(false);
            btn_ok.setEnabled(false);
            btn_ok.setText("");
            btn_ok.setTextColor(Color.GRAY);
            if(data.getID_Status().equals("1")){
                tmp = "Đợi xét duyệt";
                btn_CancelBookRoom.setEnabled(true);
            }
            if(data.getID_Status().equals("2")){
                tmp = "Đã duyệt";
                btn_CancelBookRoom.setTextColor(Color.GRAY);
                txt_Status.setTextColor(Color.RED);
            }

            btn_CancelBookRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getID_Status().equals("1")){
                        new AsyncTaskNetworkR(context).execute(data.getID());
                        txt_Status.setText("Đã hủy đặt phòng");
                        btn_CancelBookRoom.setText("Đã Hủy");
                        btn_CancelBookRoom.setTextColor(Color.BLACK);
                        btn_CancelBookRoom.setEnabled(false);
                    }
                }
            });
            txt_Status.setText(tmp);
            System.out.println(tmp);
        }
        return convertView;
    }
}
class AsyncTaskNetworkR extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Context context;
    public AsyncTaskNetworkR(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("BookRoom").orderByChild("id").equalTo(strings[0]);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error");
            }
        });
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Hủy đặt phòng");
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aString) {
        super.onPostExecute(aString);
        // Hủy dialog đi.
        progressDialog.dismiss();
        // Hiển thị IP lên TextView.

    }
}


class AsyncTaskNetworkUpdateR extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Context context;
    String Status;
    public AsyncTaskNetworkUpdateR(Context context, String s) {
        this.context = context;
        this.Status = s;
    }

    @Override
    protected String doInBackground(String... strings) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("BookRoom").orderByChild("id").equalTo(strings[0]);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().child("id_Status").setValue(Status);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error");
            }
        });
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Hiển thị Dialog khi bắt đầu xử lý.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Hủy đặt phòng");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aString) {
        super.onPostExecute(aString);
        // Hủy dialog đi.
        progressDialog.dismiss();
        // Hiển thị IP lên TextView.

    }
}
