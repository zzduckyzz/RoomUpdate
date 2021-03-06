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
        txt_RentalCost.setText(data.getRentalCost()+" Tri???u  |  ");
        txt_Phone.setText("Phone: "+data.getPhone());
        txt_Description.setText(data.getDescription());
        txt_City.setText(data.getCity()+"  -  ");
        txt_Country.setText(data.getCounty());
        txt_Ward.setText(data.getWard()+"  -  ");
        txt_Street.setText(data.getStreet());
        txt_UName.setText("Kh??ch: "+data.getNameUser());
        txt_UPhone.setText(data.getPhoneUser());

        if(userRole == 2){ // chu phong

            String tmp = "???? T??? ch???i";
            btn_CancelBookRoom.setEnabled(true);
            btn_CancelBookRoom.setText("T??? ch???i");
            btn_CancelBookRoom.setTextColor(Color.GRAY);
            btn_ok.setEnabled(true);
            btn_ok.setText("Duy???t");
            btn_ok.setTextColor(Color.GRAY);

            if(data.getID_Status().equals("1")){
                tmp = "?????i x??t duy???t";
                btn_CancelBookRoom.setEnabled(true);
                btn_CancelBookRoom.setTextColor(Color.BLACK);
                btn_ok.setTextColor(Color.BLACK);
            }
            if(data.getID_Status().equals("2")){
                tmp = "???? duy???t";
                btn_CancelBookRoom.setTextColor(Color.GRAY);
                btn_ok.setTextColor(Color.GRAY);
                txt_Status.setTextColor(Color.RED);

            }

            btn_CancelBookRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getID_Status().equals("1")){
                        new AsyncTaskNetworkUpdateR(context, "0").execute(data.getID());
                        txt_Status.setText("???? t??? ch???i");
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
                        txt_Status.setText("???? Duy???t");
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
            String tmp = "T??? ch???i";
            btn_CancelBookRoom.setEnabled(false);
            btn_ok.setEnabled(false);
            btn_ok.setText("");
            btn_ok.setTextColor(Color.GRAY);
            if(data.getID_Status().equals("1")){
                tmp = "?????i x??t duy???t";
                btn_CancelBookRoom.setEnabled(true);
            }
            if(data.getID_Status().equals("2")){
                tmp = "???? duy???t";
                btn_CancelBookRoom.setTextColor(Color.GRAY);
                txt_Status.setTextColor(Color.RED);
            }

            btn_CancelBookRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getID_Status().equals("1")){
                        new AsyncTaskNetworkR(context).execute(data.getID());
                        txt_Status.setText("???? h???y ?????t ph??ng");
                        btn_CancelBookRoom.setText("???? H???y");
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

        // Hi???n th??? Dialog khi b???t ?????u x??? l??.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("H???y ?????t ph??ng");
        progressDialog.setMessage("??ang x??? l??...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aString) {
        super.onPostExecute(aString);
        // H???y dialog ??i.
        progressDialog.dismiss();
        // Hi???n th??? IP l??n TextView.

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

        // Hi???n th??? Dialog khi b???t ?????u x??? l??.
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("H???y ?????t ph??ng");
        progressDialog.setMessage("Dang xu ly...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String aString) {
        super.onPostExecute(aString);
        // H???y dialog ??i.
        progressDialog.dismiss();
        // Hi???n th??? IP l??n TextView.

    }
}
