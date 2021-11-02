package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.IReportedRoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportedRoomModel implements Parcelable {
    String reason;
    String detail;
    String time;
    String reportID, userID;

    //Chủ report
    UserModel userReport;

    //Phòng bị report
    RoomModel reportedRoom;

    protected ReportedRoomModel(Parcel in) {
        reason = in.readString();
        detail = in.readString();
        time = in.readString();
        reportID = in.readString();
        userID = in.readString();
        userReport = in.readParcelable(UserModel.class.getClassLoader());
        reportedRoom = in.readParcelable(RoomModel.class.getClassLoader());
    }

    public static final Creator<ReportedRoomModel> CREATOR = new Creator<ReportedRoomModel>() {
        @Override
        public ReportedRoomModel createFromParcel(Parcel in) {
            return new ReportedRoomModel(in);
        }

        @Override
        public ReportedRoomModel[] newArray(int size) {
            return new ReportedRoomModel[size];
        }
    };

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserModel getUserReport() {
        return userReport;
    }

    public void setUserReport(UserModel userReport) {
        this.userReport = userReport;
    }

    public RoomModel getReportedRoom() {
        return reportedRoom;
    }

    public void setReportedRoom(RoomModel reportedRoom) {
        this.reportedRoom = reportedRoom;
    }

    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    public ReportedRoomModel() {
        //Trả về node comment của database
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public ReportedRoomModel ReportedRoomModel(ReportedRoomModel reportedRoomModel) {
        ReportedRoomModel reportedRoomModel1 = new ReportedRoomModel();
        reportedRoomModel1.setReason(reportedRoomModel.getReason());
        reportedRoomModel1.setDetail(reportedRoomModel.getDetail());
        reportedRoomModel1.setUserReport(reportedRoomModel.getUserReport());
        reportedRoomModel1.setReportedRoom(reportedRoomModel.getReportedRoom());
        reportedRoomModel1.setReportID(reportedRoomModel.getReportID());
        reportedRoomModel1.setTime(reportedRoomModel.getTime());
        reportedRoomModel1.setUserID(reportedRoomModel.getUserID());

        return reportedRoomModel1;
    }

    DataSnapshot dataRoot;
    private List<String> listReportRoomID = new ArrayList<>();

    public void ListReportedRooms(IReportedRoomModel iReportedRoomModel, int quantityReportRoomToLoad,
                                  int quantityReportRoomLoaded) {
        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;

                DataSnapshot dataSnapshotReportRooms = dataSnapshot.child("ReportedRoom");

                for (DataSnapshot dataSnapshotReportedRoom : dataSnapshotReportRooms.getChildren()) {
                    for (DataSnapshot dataSnapshotReport : dataSnapshotReportedRoom.getChildren()) {
                        //Lọc ra danh sách reports.
                        listReportRoomID.add(dataSnapshotReport.getKey());
                    }
                }

                // set view
                iReportedRoomModel.setQuantityTop(listReportRoomID.size());

                //Thêm dữ liệu và gửi về lại UI
                getPartListReportRooms(iReportedRoomModel, quantityReportRoomToLoad, quantityReportRoomLoaded);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (dataRoot != null) {
            //Thêm dữ liệu và gửi về lại UI
            getPartListReportRooms(iReportedRoomModel, quantityReportRoomToLoad, quantityReportRoomLoaded);
        } else {
            //Gán sự kiện listen cho nodeRoot
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void getPartListReportRooms(IReportedRoomModel iReportedRoomModel, int quantityReportRoomToLoad, int quantityReportRoomLoaded) {
        int i = 0;

        //Thêm danh sách phòng bị report
        DataSnapshot dataSnapshotReportedRooms = dataRoot.child("ReportedRoom");

        //Duyệt tất cả các giá trị trong node tương ứng
        for (DataSnapshot ReportedRoomsValue : dataSnapshotReportedRooms.getChildren()) {
            // Nếu đã lấy đủ số lượng rooms tiếp theo thì ra khỏi vòng lặp
            if (i == quantityReportRoomToLoad) {
                break;
            }

            // Bỏ qua những room đã load
            if (i < quantityReportRoomLoaded) {
                i++;
                continue;
            }

            i++;

            String roomId = ReportedRoomsValue.getKey();

            DataSnapshot dataSnapshotReportedRoom = dataSnapshotReportedRooms.child(roomId);

            for (DataSnapshot ReportValue : dataSnapshotReportedRoom.getChildren()) {
                ReportedRoomModel reportedRoomModel = ReportValue.getValue(ReportedRoomModel.class);
                reportedRoomModel.setReportID(ReportValue.getKey());

                //Duyệt user tương ứng để lấy ra thông tin user bình luận
                UserModel userModel = dataRoot.child("Users").child(reportedRoomModel.getUserID()).getValue(UserModel.class);
                userModel.setUserID(reportedRoomModel.getUserID());
                reportedRoomModel.setUserReport(userModel);
                //End duyệt user tương ứng để lấy ra thông tin user bình luận

                //Duyệt room tương ứng để lấy ra thông tin reported room
                RoomModel roomModel = dataRoot.child("Room").child(roomId).getValue(RoomModel.class);

                roomModel.setRoomID(roomId);

                //Set loại phòng trọ
                String tempType = dataRoot.child("RoomTypes")
                        .child(roomModel.getTypeID())
                        .getValue(String.class);

                roomModel.setRoomType(tempType);

                //Thêm tên danh sách tên hình vào phòng trọ

                //Duyệt vào node RoomImages trên firebase và duyệt vào node có mã room tương ứng
                DataSnapshot dataSnapshotImageRoom = dataRoot.child("RoomImages").child(roomId);
                List<String> tempImageList = new ArrayList<String>();
                //Duyêt tất cả các giá trị của node tương ứng
                for (DataSnapshot valueImage : dataSnapshotImageRoom.getChildren()) {
                    tempImageList.add(valueImage.getValue(String.class));
                }

                //set mảng hình vào list
                roomModel.setListImageRoom(tempImageList);

                //End Thêm tên danh sách tên hình vào phòng trọ

                //Thêm vào hình dung lượng thấp của phòng trọ
                DataSnapshot dataSnapshotComPress = dataRoot.child("RoomCompressionImages").child(roomId);
                //Kiểm tra nếu có dữ liệu
                if (dataSnapshotComPress.getChildrenCount() > 0) {
                    for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                        roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                    }
                } else {
                    roomModel.setCompressionImage(tempImageList.get(0));
                }

                //Thêm thông tin chủ sở hữu cho phòng trọ
                UserModel tempUser = dataRoot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                tempUser.setUserID(roomModel.getOwner());
                roomModel.setRoomOwner(tempUser);

                //End thêm thông tin chủ sở hữu cho phòng trọ

                reportedRoomModel.setReportedRoom(roomModel);
                //End duyệt room tương ứng để lấy ra thông tin reported room

                iReportedRoomModel.getListReportRooms(reportedRoomModel);
            }
        }

        iReportedRoomModel.setProgressBarLoadMore();

        //End Thêm danh sách report
    }


    public void addReport(ReportedRoomModel reportedRoomModel, String roomId, IReportedRoomModel iReportedRoomModel) {
        DatabaseReference nodeReportedRoom = FirebaseDatabase.getInstance().getReference().child("ReportedRoom");
        String reportId = nodeReportedRoom.child(roomId).push().getKey();

        nodeReportedRoom.child(roomId).child(reportId).setValue(reportedRoomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iReportedRoomModel.makeToast("Cảm ơn bạn đã báo cáo sai phạm");
                }
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reason);
        dest.writeString(detail);
        dest.writeString(time);
        dest.writeString(reportID);
        dest.writeString(userID);
        dest.writeParcelable(userReport, flags);
        dest.writeParcelable(reportedRoom, flags);
    }
}
