package com.example.designapptest.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.model.UserModel;
import com.example.designapptest.R;
import com.example.designapptest.view.admin.AdminHostRoomsActivity;

import java.util.List;

public class AdapterRecyclerHost extends RecyclerView.Adapter<AdapterRecyclerHost.ViewHolder> {
    List<UserModel> UserModelList;
    //Là biến lưu giao diện custom của từng row
    int resource;
    Context context;

    public AdapterRecyclerHost(Context context, List<UserModel> UserModelList, int resource) {
        this.context = context;
        this.UserModelList = UserModelList;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameHost, txtGenderHost, txtPhoneHost, txtEmailHost, txtQuantityRoomsHost;
        ImageView imgGenderHost, imgAvatarHost;
        CardView cardViewHostList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtNameHost = (TextView) itemView.findViewById(R.id.txt_name_host);
            txtGenderHost = (TextView) itemView.findViewById(R.id.txt_gender_host);
            txtPhoneHost = (TextView) itemView.findViewById(R.id.txt_phone_host);
            txtEmailHost = (TextView) itemView.findViewById(R.id.txt_email_host);
            txtQuantityRoomsHost = (TextView) itemView.findViewById(R.id.txt_quantity_rooms_host);
            imgGenderHost = (ImageView) itemView.findViewById(R.id.img_gender_host);
            imgAvatarHost = (ImageView) itemView.findViewById(R.id.img_avatar_host);
            cardViewHostList = (CardView) itemView.findViewById(R.id.cardViewHostList);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerHost.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        AdapterRecyclerHost.ViewHolder viewHolder = new AdapterRecyclerHost.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerHost.ViewHolder viewHolder, int i) {
        //Lấy giá trị trong list
        final UserModel userModel = UserModelList.get(i);

        //Gán các giá trị vào giao diện
        //classFunctionStatic.showProgress(context, viewHolder.imgRoom);

        viewHolder.txtNameHost.setText(userModel.getName());
        viewHolder.txtPhoneHost.setText(userModel.getPhoneNumber());
        viewHolder.txtEmailHost.setText(userModel.getEmail());
        viewHolder.txtQuantityRoomsHost.setText(userModel.getListRooms().size() + "");

        //Gán hình cho giới tính
        if (userModel.isGender()) {
            viewHolder.imgGenderHost.setImageResource(R.drawable.ic_svg_male_100);
        } else {
            viewHolder.imgGenderHost.setImageResource(R.drawable.ic_svg_female_100);
        }
        //End Gán giá trị cho giới tính

        //Download ảnh dùng picaso cho đỡ lag, dùng thuộc tính fit() để giảm dung lượng xuống thấp nhất có thể
        userModel.getCompressionImageFit().centerCrop().into(viewHolder.imgAvatarHost);

        // Đăng kí sự kiện click cho cardView // Linh thêm
        viewHolder.cardViewHostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iHostRooms = new Intent(context, AdminHostRoomsActivity.class);
                iHostRooms.putExtra("chutro", userModel);
                context.startActivity(iHostRooms);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserModelList.size();
    }
}
