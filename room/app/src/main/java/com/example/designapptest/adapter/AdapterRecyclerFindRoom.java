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

import com.example.designapptest.domain.classFunctionStatic;
import com.example.designapptest.model.FindRoomModel;
import com.example.designapptest.R;
import com.example.designapptest.view.room.FindRoomDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterRecyclerFindRoom extends RecyclerView.Adapter<AdapterRecyclerFindRoom.ViewHolder> {

    // ID object truyen qua find room detail.
    public final static String SHARE_FINDROOM = "FINDROOM";

    List<FindRoomModel> findRoomModelList;
    //Là biến lưu giao diện custom của từng row
    int resource;
    Context context;

    public AdapterRecyclerFindRoom(Context context, List<FindRoomModel> findRoomModelList, int resource) {
        this.context = context;
        this.findRoomModelList = findRoomModelList;
        this.resource = resource;
    }

    public void clearApplications() {
        int size = this.findRoomModelList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                findRoomModelList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameUser, txtTimeCreating, txtAboutPrice, txtLocationSearch;
        ImageView imgAvatarUser, imgGenderUser;
        CardView cardViewFindRoomList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtTimeCreating = (TextView) itemView.findViewById(R.id.txt_timeCreating);
            txtNameUser = (TextView) itemView.findViewById(R.id.txt_name_user);
            txtAboutPrice = (TextView) itemView.findViewById(R.id.txt_abouPrice);
            txtLocationSearch = (TextView) itemView.findViewById(R.id.txt_locationSearch);
            imgAvatarUser = (ImageView) itemView.findViewById(R.id.img_avatar_user);
            imgGenderUser = (ImageView) itemView.findViewById(R.id.img_gender_user);
            cardViewFindRoomList = (CardView) itemView.findViewById(R.id.cardViewFindRoomList);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerFindRoom.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        AdapterRecyclerFindRoom.ViewHolder viewHolder = new AdapterRecyclerFindRoom.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerFindRoom.ViewHolder viewHolder, int i) {
        //Lấy giá trị trong list
        final FindRoomModel findRoomModel = findRoomModelList.get(i);

        //Gán các giá trị vào giao diện
        //classFunctionStatic.showProgress(context, viewHolder.imgAvatarUser);

        // Gán tên cho chủ phòng trọ.
        viewHolder.txtNameUser.setText(findRoomModel.getFindRoomOwner().getName());

        // Gán thời gian cho tìm ở ghép.
        // Đổi string date to long.
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date;

        try {
            date = format.parse(findRoomModel.getTime());
            viewHolder.txtTimeCreating.setText(classFunctionStatic.getTimeAgo(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Gán giá trị cho khoảng giá cần tìm kiếm.
        viewHolder.txtAboutPrice.setText(String.valueOf(findRoomModel.getMinPrice())
                + " triệu - " + String.valueOf(findRoomModel.getMaxPrice()) + " triệu");

        // Chỉ xử lí nếu khác null
        String strLocationSearch = "";
        if (findRoomModel.getLocation() != null) {
            // gán giá trị cho vị trí tìm kiếm.
            int index;
            int sizeLocationSearchs = findRoomModel.getLocation().size();


            for (index = 0; index < sizeLocationSearchs; index++) {
                if (index != findRoomModel.getLocation().size() - 1)
                    strLocationSearch += findRoomModel.getLocation().get(index) + ", ";
                else
                    strLocationSearch += findRoomModel.getLocation().get(index);
            }
        } else {
            strLocationSearch = "Tất cả";
        }
        viewHolder.txtLocationSearch.setText(getTextHide(strLocationSearch));

        //Gán hình cho giới tính
        if (findRoomModel.getFindRoomOwner().isGender() == true) {
            viewHolder.imgGenderUser.setImageResource(R.drawable.ic_svg_male_100);
        } else {
            viewHolder.imgGenderUser.setImageResource(R.drawable.ic_svg_female_100);
        }
        //End Gán giá trị cho giới tính

        //Download ảnh dùng picaso cho đỡ lag
        findRoomModel.getCompressionImageFit().centerCrop().into(viewHolder.imgAvatarUser);
//        Picasso.get().load(findRoomModel.getFindRoomOwner().getAvatar()).fit().centerCrop().into(viewHolder.imgAvatarUser);

        viewHolder.cardViewFindRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDetailFindRoom = new Intent(context, FindRoomDetailActivity.class);
                iDetailFindRoom.putExtra(SHARE_FINDROOM, findRoomModel);
                context.startActivity(iDetailFindRoom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return findRoomModelList.size();
    }

    // Ẩn các text dài hơn kí tự.
    private String getTextHide(String string) {
        String result = "";

        if (string.length() >= 24) {
            int index = 0;
            for (index = 0; index < 25; index++)
                result += string.charAt(index);

            result += " ...";
        }
        else {
            return string;
        }

        return result;
    }

}
