package com.example.designapptest.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.example.designapptest.view.searchandmap.SearchViewActivity;

import java.util.List;

public class AdapterRecyclerSuggestions extends RecyclerView.Adapter<AdapterRecyclerSuggestions.ViewHolder> {

    public final static String INTENT_DISTRICT = "DICTRICT";
    Context context;
    int resource;
    List<String> stringListDistrictLocation;
    boolean isSearchRoomCall;

    public AdapterRecyclerSuggestions(Context context, int resource, List<String> stringListDistrictLocation, boolean isSearchRoomCall) {
        this.context = context;
        this.resource = resource;
        this.stringListDistrictLocation = stringListDistrictLocation;
        this.isSearchRoomCall = isSearchRoomCall;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLocation;
        LinearLayout LinearContainElement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocation = itemView.findViewById(R.id.txt_location);
            LinearContainElement = itemView.findViewById(R.id.Linear_contain_element);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerSuggestions.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerSuggestions.ViewHolder viewHolder, int i) {
        //Gán giá trị địa chỉ
        viewHolder.txtLocation.setText(stringListDistrictLocation.get(i));

        viewHolder.LinearContainElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Trường hợp gọi từ màn hình chính
                if (isSearchRoomCall == false) {
                    Intent intent = new Intent(context, SearchViewActivity.class);
                    intent.putExtra(INTENT_DISTRICT, stringListDistrictLocation.get(i));
                    context.startActivity(intent);
                } else {
                    //Trường hợp gọi từ search room
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(INTENT_DISTRICT, stringListDistrictLocation.get(i));
                    ((AppCompatActivity) context).setResult(Activity.RESULT_OK, returnIntent);
                    ((AppCompatActivity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringListDistrictLocation.size();
    }


}
