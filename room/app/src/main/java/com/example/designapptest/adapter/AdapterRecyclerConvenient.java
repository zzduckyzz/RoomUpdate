package com.example.designapptest.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.model.ConvenientModel;
import com.example.designapptest.R;

import java.util.List;

public class AdapterRecyclerConvenient extends RecyclerView.Adapter<AdapterRecyclerConvenient.ViewHolder> {

    Context context;
    Context contextMain;
    int layout;
    List<ConvenientModel> ConvenientModelList;

    public AdapterRecyclerConvenient(Context context, Context contextMain, int layout, List<ConvenientModel> ConvenientModelList) {
        this.context = context;
        this.contextMain = contextMain;
        this.layout = layout;
        this.ConvenientModelList = ConvenientModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUtilityRoomDetail;
        TextView txtNameUtilityRoomDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUtilityRoomDetail = (ImageView) itemView.findViewById(R.id.img_utility_room_detail);
            txtNameUtilityRoomDetail = (TextView) itemView.findViewById(R.id.txt_nameUtility_utility_room_detail);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerConvenient.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerConvenient.ViewHolder viewHolder, int i) {
        final ConvenientModel convenientModel = ConvenientModelList.get(i);

        //Gán các giá trị vào giao diện
        viewHolder.txtNameUtilityRoomDetail.setText(convenientModel.getName());

        int resourceId = context.getResources().getIdentifier(convenientModel.getImageName(), "drawable", contextMain.getPackageName());
        viewHolder.imgUtilityRoomDetail.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        int convenients = ConvenientModelList.size();
//        if (convenients > 6) {
//            return 6;
//        } else {
//            return convenients;
//        }
        return convenients;
    }

    public void removeAt(int position) {
        ConvenientModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, ConvenientModelList.size());
    }

    public void insertAt(int position, ConvenientModel convenientModel) {
        ConvenientModelList.add(position, convenientModel);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, ConvenientModelList.size());
    }
}
