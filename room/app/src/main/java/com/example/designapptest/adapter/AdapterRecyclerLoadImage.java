package com.example.designapptest.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.model.LoadImageModel;
import com.example.designapptest.R;

import java.util.List;

public class AdapterRecyclerLoadImage extends RecyclerView.Adapter<AdapterRecyclerLoadImage.ViewHolder> {

    List<LoadImageModel> listPathImage;
    Context context;
    int resource;

    public AdapterRecyclerLoadImage(Context context, int resource, List<LoadImageModel> listPathImage) {
        this.listPathImage = listPathImage;
        this.context = context;
        this.resource=resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgLoad;
        CheckBox chBoxChooseImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLoad = itemView.findViewById(R.id.img_load);
            chBoxChooseImage = itemView.findViewById(R.id.chBox_choose_image);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerLoadImage.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource,viewGroup,false);
        ViewHolder viewHolder  = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerLoadImage.ViewHolder viewHolder, final int i) {
        final LoadImageModel loadImageModel = listPathImage.get(i);
        String Path = loadImageModel.getPath();
        Uri uri = Uri.parse(Path);
        viewHolder.imgLoad.setImageURI(uri);
        viewHolder.chBoxChooseImage.setChecked(loadImageModel.isChecked());

        viewHolder.chBoxChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox =(CheckBox)v;
                listPathImage.get(i).setChecked(checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPathImage.size();
    }
}
