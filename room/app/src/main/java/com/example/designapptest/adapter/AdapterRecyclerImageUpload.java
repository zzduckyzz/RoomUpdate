package com.example.designapptest.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterRecyclerImageUpload extends RecyclerView.Adapter<AdapterRecyclerImageUpload.ViewHolder> {

    Context context;
    int resource;
    List<String> listImagePath;

    public AdapterRecyclerImageUpload(Context context, int resource, List<String> listImagePath) {
        this.context = context;
        this.resource = resource;
        this.listImagePath = listImagePath;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUpload, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUpload = itemView.findViewById(R.id.img_upload);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerImageUpload.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerImageUpload.ViewHolder viewHolder, int i) {
        Uri uri = Uri.parse(listImagePath.get(i));
        Picasso.get().load(uri).centerCrop().fit().into(viewHolder.imgUpload);

        //Set tag để tránh việc tái sử dụng của recycler view
        viewHolder.imgDelete.setTag(i);
        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                listImagePath.remove(index);
                //Thông báo có thay đổi dữ liệu
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImagePath.size();
    }


}
