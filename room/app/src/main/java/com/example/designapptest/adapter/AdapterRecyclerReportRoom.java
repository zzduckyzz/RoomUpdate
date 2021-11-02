package com.example.designapptest.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.controller.Interfaces.IReportedRoomModel;
import com.example.designapptest.model.ReportedRoomModel;
import com.example.designapptest.R;
import com.example.designapptest.view.admin.AdminReportRoomDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterRecyclerReportRoom extends RecyclerView.Adapter<AdapterRecyclerReportRoom.ViewHolder> {
    List<ReportedRoomModel> ReportRoomModelList;
    //Là biến lưu giao diện custom của từng row
    int resource;
    Context context;

    public AdapterRecyclerReportRoom(Context context, List<ReportedRoomModel> ReportRoomModelList, int resource) {
        this.context = context;
        this.ReportRoomModelList = ReportRoomModelList;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtReasonReport, txtRoomName, txtRoomAddress, txtTimeReport, txtUserReport;
        ImageView imgRoom, imgVerified;
        CardView cardViewReportRoomList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtReasonReport = (TextView) itemView.findViewById(R.id.txt_reason_report);
            txtRoomName = (TextView) itemView.findViewById(R.id.txt_room_name);
            txtRoomAddress = (TextView) itemView.findViewById(R.id.txt_room_address);
            txtTimeReport = (TextView) itemView.findViewById(R.id.txt_time_report);
            txtUserReport = (TextView) itemView.findViewById(R.id.txt_user_report);
            imgRoom = (ImageView) itemView.findViewById(R.id.img_room);
            imgVerified = (ImageView) itemView.findViewById(R.id.img_verified);
            cardViewReportRoomList = (CardView) itemView.findViewById(R.id.cardViewReportRoomList);
        }
    }

    @NonNull
    @Override
    public AdapterRecyclerReportRoom.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        AdapterRecyclerReportRoom.ViewHolder viewHolder = new AdapterRecyclerReportRoom.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecyclerReportRoom.ViewHolder viewHolder, int i) {
        //Lấy giá trị trong list
        final ReportedRoomModel reportedRoomModel = ReportRoomModelList.get(i);

        //Gán các giá trị vào giao diện

        viewHolder.txtReasonReport.setText(reportedRoomModel.getReason());
        viewHolder.txtTimeReport.setText(reportedRoomModel.getTime());
        viewHolder.txtUserReport.setText(reportedRoomModel.getUserReport().getEmail());
        viewHolder.txtRoomName.setText(reportedRoomModel.getReportedRoom().getName());

        //Set address for room
        String longAddress = reportedRoomModel.getReportedRoom().getApartmentNumber() + " "
                + reportedRoomModel.getReportedRoom().getStreet() + ", "
                + reportedRoomModel.getReportedRoom().getWard() + ", "
                + reportedRoomModel.getReportedRoom().getCounty() + ", "
                + reportedRoomModel.getReportedRoom().getCity();
        viewHolder.txtRoomAddress.setText(longAddress);
        //End Set address for room

        //Hiển thị phòng đã được chứng thực
        if (reportedRoomModel.getReportedRoom().isAuthentication()) {
            viewHolder.imgVerified.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgVerified.setVisibility(View.GONE);
        }
        //End hiển thị phòng đã được chúng thực

        //Download ảnh dùng picaso cho đỡ lag, dùng thuộc tính fit() để giảm dung lượng xuống thấp nhất có thể
        reportedRoomModel.getReportedRoom().getCompressionImageFit().centerCrop().into(viewHolder.imgRoom);

        // Đăng kí sự kiện click cho cardView // Linh thêm
        viewHolder.cardViewReportRoomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iReportRoom = new Intent(context, AdminReportRoomDetailActivity.class);
                iReportRoom.putExtra("reportRoom", reportedRoomModel);
                context.startActivity(iReportRoom);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ReportRoomModelList.size();
    }

    public void removeItem(RecyclerView.ViewHolder viewHolder, RecyclerView recyclerView,
                           AdapterRecyclerReportRoom adapterRecyclerReportRoom, TextView txtQuantity,
                           IReportedRoomModel iReportedRoomModel, ReportedRoomModel reportedRoomModel,
                           int quantityLoaded, int quantityEachTime) {
        int removedPosition = viewHolder.getAdapterPosition();
        removeReports(removedPosition, recyclerView, adapterRecyclerReportRoom, txtQuantity,
                iReportedRoomModel, reportedRoomModel, quantityLoaded, quantityEachTime);
    }

    private void removeReports(int removedPosition, RecyclerView recyclerView,
                                    AdapterRecyclerReportRoom adapterRecyclerReportRoom, TextView txtQuantity,
                                    IReportedRoomModel iReportedRoomModel, ReportedRoomModel reportedRoomModel,
                                    int quantityLoaded, int quantityEachTime) {
        DatabaseReference nodeReportedRooms = FirebaseDatabase.getInstance().getReference()
                .child("ReportedRoom");
        String roomId = ReportRoomModelList.get(removedPosition).getReportedRoom().getRoomID();
        String reportId = ReportRoomModelList.get(removedPosition).getReportID();

        nodeReportedRooms.child(roomId).child(reportId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ReportedRoomModel deletedReportedRoomModel = ReportRoomModelList.get(removedPosition);

//                if (RoomModelList.size() == 0) {
//                    adapterRecyclerFavoriteRoom.notifyItemRemoved(removedPosition);
//                }

                // xóa khỏi list
                ReportRoomModelList.remove(removedPosition);
                adapterRecyclerReportRoom.notifyItemRemoved(removedPosition);

                // Cập nhật số lượng ở view
                int reportQuantity = Integer.valueOf(txtQuantity.getText().toString().trim()) - 1;
                iReportedRoomModel.setQuantityTop(reportQuantity);

                // Load more một lượng rooms nữa
                reportedRoomModel.getPartListReportRooms(iReportedRoomModel,
                        quantityLoaded + 2*quantityEachTime, quantityLoaded + quantityEachTime);
                // Cập nhật lại số lượng đã tải cho controller biết có thay đổi
                iReportedRoomModel.setQuantityLoadMore(quantityLoaded + quantityEachTime);

                final ReportedRoomModel newReportedRoomModel = deletedReportedRoomModel.ReportedRoomModel(deletedReportedRoomModel);
                newReportedRoomModel.setReportedRoom(null);
                newReportedRoomModel.setReportID(null);
                newReportedRoomModel.setUserReport(null);

                Snackbar.make(recyclerView, "Đã xóa", Snackbar.LENGTH_LONG).setAction("HOÀN TÁC", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nodeReportedRooms.child(roomId).child(reportId).setValue(newReportedRoomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Thêm vào list
                                    ReportRoomModelList.add(removedPosition, deletedReportedRoomModel);

                                    // Load ảnh nén
                                    deletedReportedRoomModel.getReportedRoom().setCompressionImageFit(Picasso.get().load(deletedReportedRoomModel.getReportedRoom().getCompressionImage()).fit());

                                    adapterRecyclerReportRoom.notifyItemInserted(removedPosition);

                                    // Cập nhật số lượng ở view
                                    int roomQuantity = Integer.valueOf(txtQuantity.getText().toString().trim()) + 1;
                                    iReportedRoomModel.setQuantityTop(roomQuantity);

//                                    recyclerView.scrollToPosition(removedPosition);
                                }
                            }
                        });
                    }
                }).show();
            }
        });
    }
}