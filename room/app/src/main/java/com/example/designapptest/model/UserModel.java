package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.IFindRoomAddModel;
import com.example.designapptest.controller.Interfaces.IUserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class UserModel implements Parcelable {
    private String userID;
    private String avatar;
    private String name;
    private String email;
    private String phoneNumber;
    private int owner;
    private boolean gender;
    //Lưu mảng comment của phòng trọ
    private List<RoomModel> listRooms;

    RequestCreator compressionImageFit;

    protected UserModel(Parcel in) {
        avatar = in.readString();
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        owner = in.readInt();
        gender = in.readByte() != 0;
        userID = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public List<RoomModel> getListRooms() {
        return listRooms;
    }

    public void setListRooms(List<RoomModel> listRooms) {
        this.listRooms = listRooms;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public RequestCreator getCompressionImageFit() {
        return compressionImageFit;
    }

    public void setCompressionImageFit(RequestCreator compressionImageFit) {
        this.compressionImageFit = compressionImageFit;
    }

    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    //hàm khởi tạo rỗng
    public UserModel() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeInt(owner);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(userID);
    }

    private DataSnapshot dataNode;
    private DataSnapshot dataRoot;
    private List<String> listUserID = new ArrayList<>();

    public void addUser(UserModel newUserModel, String uid) {
        DatabaseReference nodeUser = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        nodeUser.setValue(newUserModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Thêm mới tài khoản thành công");
            }
        });
    }
    public void getUserModel(final IFindRoomAddModel iFindRoomAddModel, final String id) {
        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.child("Users").child(id).getValue(UserModel.class);
                userModel.setUserID(id);
                iFindRoomAddModel.getUserModel(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Gán sự kiện listen cho nodeRoot
        nodeRoot.addValueEventListener(valueEventListener);
    }

    public void ListHosts(IUserModel iUserModel, int quantityHostToLoad, int quantityHostLoaded) {
        Query nodeHosts = nodeRoot.child("Users")
                .orderByChild("owner")
                .equalTo(2);

        // Tạo listen cho nodeHosts.
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListRoomEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        for (DataSnapshot userRoomsSnapShot : dataNode.getChildren()) {
                            //Lọc ra danh sách verified rooms.
                            listUserID.add(userRoomsSnapShot.getKey());
                        }

                        // set view
                        iUserModel.setQuantityTop(listUserID.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartListUsers(iUserModel, quantityHostToLoad, quantityHostLoaded);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                //Gán sự kiện listen cho nodeRoot
                nodeRoot.addListenerForSingleValueEvent(valueSpecialListRoomEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (dataNode != null) {
            if (dataRoot != null) {
                //Thêm dữ liệu và gửi về lại UI
                getPartListUsers(iUserModel, quantityHostToLoad, quantityHostLoaded);
            }
        } else {
            //Gán sự kiện listen nodeHosts
            nodeHosts.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void getPartListUsers(IUserModel iUserModel, int quantityRoomToLoad, int quantityRoomLoaded) {
        int i = 0;

        // Chạy từ cuối list đến đầu list (list truyền vào đã sắp xếp theo thời gian)
        for (String userID : listUserID) {

            // Nếu đã lấy đủ số lượng rooms tiếp theo thì ra khỏi vòng lặp
            if (i == quantityRoomToLoad) {
                break;
            }

            // Bỏ qua những room đã load
            if (i < quantityRoomLoaded) {
                i++;
                continue;
            }

            i++;

            //Duyệt vào room cần lấy dữ liệu
            DataSnapshot dataSnapshotValueUser = dataRoot.child("Users").child(userID);

            //Lấy ra giá trị ép kiểu qua kiểu RoomModel
            UserModel userModel = dataSnapshotValueUser.getValue(UserModel.class);

            userModel.setUserID(userID);

            //Thêm danh sách phòng của user đăng
            List<RoomModel> listRoomModels = new ArrayList<>();

            DataSnapshot dataSnapshotRoom = dataRoot.child("Room");

            for (DataSnapshot valueRoom : dataSnapshotRoom.getChildren()) {
                RoomModel roomModel = valueRoom.getValue(RoomModel.class);
                if(roomModel.getOwner().equals(userID) && roomModel.isApprove()) {
                    //Duyệt tất cả các giá trị trong node tương ứng
                    String RoomID = valueRoom.getKey();

                    roomModel.setRoomID(RoomID);

                    //Set loại phòng trọ
                    String tempType = dataRoot.child("RoomTypes")
                            .child(roomModel.getTypeID())
                            .getValue(String.class);

                    roomModel.setRoomType(tempType);

                    //Thêm tên danh sách tên hình vào phòng trọ

                    //Duyệt vào node RoomImages trên firebase và duyệt vào node có mã room tương ứng
                    DataSnapshot dataSnapshotImageRoom = dataRoot.child("RoomImages").child(RoomID);
                    List<String> tempImageList = new ArrayList<String>();
                    //Duyêt tất cả các giá trị của node tương ứng
                    for (DataSnapshot valueImage : dataSnapshotImageRoom.getChildren()) {
                        tempImageList.add(valueImage.getValue(String.class));
                    }

                    //set mảng hình vào list
                    roomModel.setListImageRoom(tempImageList);

                    //End Thêm tên danh sách tên hình vào phòng trọ

                    //Thêm vào hình dung lượng thấp của phòng trọ
                    DataSnapshot dataSnapshotComPress = dataRoot.child("RoomCompressionImages").child(RoomID);
                    //Kiểm tra nếu có dữ liệu
                    if (dataSnapshotComPress.getChildrenCount() > 0) {
                        for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                            roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                        }
                    } else {
                        roomModel.setCompressionImage(tempImageList.get(0));
                    }

                    //Thêm danh sách bình luận của phòng trọ

                    DataSnapshot dataSnapshotCommentRoom = dataRoot.child("RoomComments").child(RoomID);
                    List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                        CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                        commentModel.setCommentID(CommentValue.getKey());
                        //Duyệt user tương ứng để lấy ra thông tin user bình luận
                        UserModel tempUser = dataRoot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                        tempUser.setUserID(commentModel.getUser());
                        commentModel.setUserComment(tempUser);
                        //End duyệt user tương ứng để lấy ra thông tin user bình luận

                        tempCommentList.add(commentModel);
                    }

                    roomModel.setListCommentRoom(tempCommentList);

                    //End Thêm danh sách bình luận của phòng trọ

                    //Thêm danh sách tiện nghi của phòng trọ

                    DataSnapshot dataSnapshotConvenientRoom = dataRoot.child("RoomConvenients").child(RoomID);
                    List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                        String convenientId = valueConvenient.getValue(String.class);
                        ConvenientModel convenientModel = dataRoot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                        convenientModel.setConvenientID(convenientId);

                        tempConvenientList.add(convenientModel);
                    }

                    roomModel.setListConvenientRoom(tempConvenientList);

                    //End Thêm danh sách tiện nghi của phòng trọ

                    //Thêm danh sách giá của phòng trọ

                    DataSnapshot dataSnapshotRoomPrice = dataRoot.child("RoomPrice").child(RoomID);
                    List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                        String roomPriceId = valueRoomPrice.getKey();
                        double price = valueRoomPrice.getValue(double.class);

                        if (roomPriceId.equals("IDRPT4")) {
                            continue;
                        }
                        RoomPriceModel roomPriceModel = dataRoot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                        roomPriceModel.setRoomPriceID(roomPriceId);
                        roomPriceModel.setPrice(price);

                        tempRoomPriceList.add(roomPriceModel);
                    }

                    roomModel.setListRoomPrice(tempRoomPriceList);

                    //End Thêm danh sách giá của phòng trọ

                    //Thêm thông tin chủ sở hữu cho phòng trọ
                    UserModel tempUser = dataRoot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                    tempUser.setUserID(roomModel.getOwner());
                    roomModel.setRoomOwner(tempUser);

                    //End thêm thông tin chủ sở hữu cho phòng trọ

                    //Thêm vào lượt xem của phòng trọ
                    int tempViews = (int) dataRoot.child("RoomViews").child(RoomID).getChildrenCount();
                    roomModel.setViews(tempViews);
                    //End thêm vào lượt xem của phòng trọ

                    listRoomModels.add(roomModel);
                }

            }

            userModel.setListRooms(listRoomModels);

            iUserModel.getListUsers(userModel);
        }

        // Ẩn progress bar load more.
        iUserModel.setProgressBarLoadMore();
    }

    public void SumHosts(IUserModel iUserModel) {
        Query nodeHosts = nodeRoot.child("Users").orderByChild("owner").equalTo(2);

        // Tạo listen cho nodeHosts.
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long quantityHosts = dataSnapshot.getChildrenCount();

                // set view
                iUserModel.setSumHostsAdminView(quantityHosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeHosts.addValueEventListener(valueEventListener);
    }


}
