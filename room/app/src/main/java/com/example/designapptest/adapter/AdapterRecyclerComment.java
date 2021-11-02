package com.example.designapptest.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.model.CommentModel;
import com.example.designapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterRecyclerComment extends RecyclerView.Adapter<AdapterRecyclerComment.ViewHolder> {

    Context context;
    int layout;
    List<CommentModel> CommentModelList;
    String UID;
    String roomId;
    Boolean isShowAll;

    public AdapterRecyclerComment(Context context, int layout, List<CommentModel> CommentModelList, String roomId,
                                  String UID, Boolean isShowAll) {
        this.context = context;
        this.layout = layout;
        this.CommentModelList = CommentModelList;
        this.UID = UID;
        this.roomId = roomId;
        this.isShowAll = isShowAll;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avt_comment_room_detail;
        TextView txt_name_comment_room_detail, txt_quantityLike_comment_room_detail,
                txt_rate_comment_room_detail, txt_title_comment_room_detail, txt_content_comment_room_detail,
                txt_time_comment_room_detail, txt_like_comment_room_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_avt_comment_room_detail = (ImageView) itemView.findViewById(R.id.img_avt_comment_room_detail);
            txt_name_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_name_comment_room_detail);
            txt_quantityLike_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_quantityLike_comment_room_detail);
            txt_rate_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_rate_comment_room_detail);
            txt_title_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_title_comment_room_detail);
            txt_content_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_content_comment_room_detail);
            txt_time_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_time_comment_room_detail);
            txt_like_comment_room_detail = (TextView) itemView.findViewById(R.id.txt_like_comment_room_detail);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerComment.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerComment.ViewHolder viewHolder, int i) {
        final CommentModel commentModel = CommentModelList.get(i);

        //Gán các giá trị vào giao diện
        viewHolder.txt_name_comment_room_detail.setText(commentModel.getUserComment().getName());
        viewHolder.txt_quantityLike_comment_room_detail.setText(commentModel.getLikes() + "");
        viewHolder.txt_rate_comment_room_detail.setText(commentModel.getStars() + "");
        viewHolder.txt_title_comment_room_detail.setText(commentModel.getTitle());
        viewHolder.txt_content_comment_room_detail.setText(commentModel.getContent());
        viewHolder.txt_time_comment_room_detail.setText(commentModel.getTime());

        //Download hình ảnh cho user
        commentModel.getCompressionImageFit().centerCrop().into(viewHolder.img_avt_comment_room_detail);

        //Hiển thị nút Like this hay Liked comment đối với user đăng nhập app.
        DatabaseReference nodeInteractiveComment = FirebaseDatabase.getInstance().getReference()
                .child("InteractiveComment").child(commentModel.getCommentID());

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.txt_like_comment_room_detail.setText("Thích");

                for (DataSnapshot valueUserLikeComment : dataSnapshot.getChildren()) {
                    String userLikeCommentId = valueUserLikeComment.getKey();
                    if (userLikeCommentId.equals(UID)) {
                        viewHolder.txt_like_comment_room_detail.setText("Đã thích");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeInteractiveComment.addValueEventListener(valueEventListener);

        // Bấm thích/ ko thích, lưu dữ liệu vào InteractiveComment
        viewHolder.txt_like_comment_room_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeComment(commentModel, viewHolder.txt_like_comment_room_detail, viewHolder.txt_quantityLike_comment_room_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        int comments = CommentModelList.size();
        if (isShowAll == false) {
            if (comments > 5) {
                return 5;
            } else {
                return comments;
            }
        } else {
            return comments;
        }
    }

    private void likeComment(CommentModel commentModel, TextView txtLikeComment, final TextView txtQuantityLikeComment) {
        DatabaseReference nodeInteractiveComment = FirebaseDatabase.getInstance().getReference()
                .child("InteractiveComment");

        final String commentId = commentModel.getCommentID();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        txtLikeComment.setEnabled(false);

        if (txtLikeComment.getText().toString().equals("Thích")) {
            // Thêm dữ liệu vào InteractiveComment.
            nodeInteractiveComment.child(commentId).child(UID).child("time").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Cập nhật số lượt thích.
                        DatabaseReference nodeRoomComments = FirebaseDatabase.getInstance().getReference()
                                .child("RoomComments");

                        final long likes = Long.valueOf(txtQuantityLikeComment.getText().toString()) + 1;
                        nodeRoomComments.child(roomId).child(commentId).child("likes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                txtQuantityLikeComment.setText(likes + "");
                                txtLikeComment.setEnabled(true);
                            }
                        });
                    }
                }
            });
        } else if (txtLikeComment.getText().toString().equals("Đã thích")) {
            // Xóa dữ liệu khỏi InteractiveComment
            nodeInteractiveComment.child(commentId).child(UID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Cập nhật số lượt thích.
                        DatabaseReference nodeRoomComments = FirebaseDatabase.getInstance().getReference()
                                .child("RoomComments");

                        final long likes = Long.valueOf(txtQuantityLikeComment.getText().toString()) - 1;
                        nodeRoomComments.child(roomId).child(commentId).child("likes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                txtQuantityLikeComment.setText(likes + "");
                                txtLikeComment.setEnabled(true);
                            }
                        });
                    }
                }
            });
        }
    }
}
