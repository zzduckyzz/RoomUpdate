package com.example.designapptest.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.designapptest.domain.FilePath;
import com.example.designapptest.controller.Interfaces.ICallBackFromAddRoom;
import com.example.designapptest.controller.Interfaces.IInfoOfAllRoomUser;
import com.example.designapptest.controller.Interfaces.IMainRoomModel;
import com.example.designapptest.controller.Interfaces.IMapViewModel;
import com.example.designapptest.controller.Interfaces.IPostRoomUpdateModel;
import com.example.designapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class RoomModel implements Parcelable { // Linh thêm
    String describe, name, owner, timeCreated;
    long currentNumber, maxNumber;
    double length, width, rentalCosts;
    boolean authentication;
    boolean gender;
    boolean approve;
    String apartmentNumber, county, street, ward, city;
    String typeID;
    String roomType;
    String compressionImage;

    //Đánh giá của người xem trọ
    long medium;
    long great;
    long prettyGood;
    long bad;

    //Mã Phòng trọ
    String roomID;

    //Chủ phòng trọ
    UserModel roomOwner;

    // Lưu dnh sách tiện nghi phòng trọ
    List<ConvenientModel> listConvenientRoom;

    // Lưu dnh sách giá phòng trọ
    List<RoomPriceModel> listRoomPrice;

    //Lưu mảng tên hình trên firebase
    List<String> listImageRoom;

    RequestCreator compressionImageFit;

    public static List<String> ListFavoriteRoomsID = new ArrayList<>();

    //Lưu mảng comment của phòng trọ
    List<CommentModel> listCommentRoom;

    //Lưu lượt xem
    int views;

    protected RoomModel(Parcel in) {

        describe = in.readString();
        name = in.readString();
        owner = in.readString();
        timeCreated = in.readString();
        currentNumber = in.readLong();
        maxNumber = in.readLong();
        length = in.readDouble();
        width = in.readDouble();
        rentalCosts = in.readDouble();
        authentication = in.readByte() != 0;
        gender = in.readByte() != 0;
        typeID = in.readString();
        roomType = in.readString();
        medium = in.readLong();
        great = in.readLong();
        prettyGood = in.readLong();
        bad = in.readLong();
        roomID = in.readString();
        apartmentNumber = in.readString();
        county = in.readString();
        street = in.readString();
        ward = in.readString();
        city = in.readString();
        roomOwner = in.readParcelable(UserModel.class.getClassLoader());
        listImageRoom = in.createStringArrayList();

        listConvenientRoom = new ArrayList<ConvenientModel>();
        in.readTypedList(listConvenientRoom, ConvenientModel.CREATOR);

        // Linh thêm
        listRoomPrice = new ArrayList<>();
        in.readTypedList(listRoomPrice, RoomPriceModel.CREATOR);

        listCommentRoom = new ArrayList<CommentModel>();
        in.readTypedList(listCommentRoom, CommentModel.CREATOR);
    }

    public static final Creator<RoomModel> CREATOR = new Creator<RoomModel>() {
        @Override
        public RoomModel createFromParcel(Parcel in) {
            return new RoomModel(in);
        }

        @Override
        public RoomModel[] newArray(int size) {
            return new RoomModel[size];
        }
    };

    public String getCompressionImage() {
        return compressionImage;
    }

    public void setCompressionImage(String compressionImage) {
        this.compressionImage = compressionImage;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public UserModel getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(UserModel roomOwner) {
        this.roomOwner = roomOwner;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public List<ConvenientModel> getListConvenientRoom() {
        return listConvenientRoom;
    }

    public void setListConvenientRoom(List<ConvenientModel> listConvenientRoom) {
        this.listConvenientRoom = listConvenientRoom;
    }

    public List<RoomPriceModel> getListRoomPrice() {
        return listRoomPrice;
    }

    public void setListRoomPrice(List<RoomPriceModel> listRoomPrice) {
        this.listRoomPrice = listRoomPrice;
    }

    public List<CommentModel> getListCommentRoom() {
        return listCommentRoom;
    }

    public void setListCommentRoom(List<CommentModel> listCommentRoom) {
        this.listCommentRoom = listCommentRoom;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public long getMedium() {
        return medium;
    }

    public void setMedium(long medium) {
        this.medium = medium;
    }

    public long getGreat() {
        return great;
    }

    public void setGreat(long great) {
        this.great = great;
    }

    public long getPrettyGood() {
        return prettyGood;
    }

    public void setPrettyGood(long prettyGood) {
        this.prettyGood = prettyGood;
    }

    public long getBad() {
        return bad;
    }

    public void setBad(long bad) {
        this.bad = bad;
    }

    public List<String> getListImageRoom() {
        return listImageRoom;
    }

    public void setListImageRoom(List<String> listImageRoom) {
        this.listImageRoom = listImageRoom;
    }

    public RequestCreator getCompressionImageFit() {
        return compressionImageFit;
    }

    public void setCompressionImageFit(RequestCreator compressionImageFit) {
        this.compressionImageFit = compressionImageFit;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    //Lưu ý phải có hàm khởi tạo rỗng
    public RoomModel() {
        //Trả về node root của database
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public long getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(long maxNumber) {
        this.maxNumber = maxNumber;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getRentalCosts() {
        return rentalCosts;
    }

    public void setRentalCosts(double rentalCosts) {
        this.rentalCosts = rentalCosts;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    private DataSnapshot dataRoot;
    private DataSnapshot dataNode;
    private List<String> listRoomsID = new ArrayList<>();

    public void ListRoom(final IMainRoomModel mainRoomModelInterface, int quantityRoomToLoad, int quantityRoomLoaded) {
        Query nodeRoomApprove = nodeRoot.child("Room")
                .orderByChild("approve")
                .equalTo(true);

        //Tạo listen cho nodeRoomApprove
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListRoomEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        List<RoomModel> listRoomsModel = new ArrayList<>();

                        //Duyệt hết trong danh sách phòng trọ
                        for (DataSnapshot valueRoom : dataNode.getChildren()) {
                            //listRoomsID.add(valueRoom.getKey());

                            //Lọc ra danh sách favorite rooms.
                            RoomModel roomModel = valueRoom.getValue(RoomModel.class);
                            roomModel.setRoomID(valueRoom.getKey());

                            listRoomsModel.add(roomModel);
                        }

                        sortListRooms(listRoomsModel);

                        for (RoomModel roomModel : listRoomsModel) {
                            listRoomsID.add(roomModel.getRoomID());
                        }

                        mainRoomModelInterface.setQuantityTop(listRoomsID.size());

                        getPartListRoom(dataRoot, mainRoomModelInterface, quantityRoomToLoad, quantityRoomLoaded);
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
                getPartListRoom(dataRoot, mainRoomModelInterface, quantityRoomToLoad, quantityRoomLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomApprove
            nodeRoomApprove.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void ListRoomWaitForApproval(final IMainRoomModel mainRoomModelInterface, int quantityRoomToLoad, int quantityRoomLoaded) {
        Query nodeRoomApprove = nodeRoot.child("Room")
                .orderByChild("approve")
                .equalTo(false);

        //Tạo listen cho nodeRoomApprove
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListRoomEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        //Duyệt hết trong danh sách phòng trọ
                        for (DataSnapshot valueRoom : dataNode.getChildren()) {
                            listRoomsID.add(valueRoom.getKey());
                        }

                        mainRoomModelInterface.setQuantityTop(listRoomsID.size());

                        getPartListRoom(dataRoot, mainRoomModelInterface, quantityRoomToLoad, quantityRoomLoaded);
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
                getPartListRoom(dataRoot, mainRoomModelInterface, quantityRoomToLoad, quantityRoomLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomApprove
            nodeRoomApprove.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void getPartListRoom(DataSnapshot dataSnapshot, IMainRoomModel mainRoomModelInterface, int quantityRoomToLoad, int quantityRoomLoaded) {
        int i = 0;

        //Duyệt hết trong danh sách phòng trọ
        for (String RoomID : listRoomsID) {

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
            DataSnapshot dataSnapshotValueRoom = dataRoot.child("Room").child(RoomID);

            //Lấy ra giá trị ép kiểu qua kiểu RoomModel
            RoomModel roomModel = dataSnapshotValueRoom.getValue(RoomModel.class);
            //Set mã phòng trọ
            roomModel.setRoomID(dataSnapshotValueRoom.getKey());

            //Set loại phòng trọ
            String tempType = dataSnapshot.child("RoomTypes")
                    .child(roomModel.getTypeID())
                    .getValue(String.class);

            roomModel.setRoomType(tempType);

            //Thêm tên danh sách tên hình vào phòng trọ

            //Duyệt vào node RoomImages trên firebase và duyệt vào node có mã room tương ứng
            DataSnapshot dataSnapshotImageRoom = dataSnapshot.child("RoomImages").child(dataSnapshotValueRoom.getKey());
            List<String> tempImageList = new ArrayList<String>();
            //Duyêt tất cả các giá trị của node tương ứng
            for (DataSnapshot valueImage : dataSnapshotImageRoom.getChildren()) {
                tempImageList.add(valueImage.getValue(String.class));
            }

            //set mảng hình vào list
            roomModel.setListImageRoom(tempImageList);

            //Thêm vào hình dung lượng thấp của phòng trọ
            DataSnapshot dataSnapshotComPress = dataSnapshot.child("RoomCompressionImages").child(dataSnapshotValueRoom.getKey());
            //Kiểm tra nếu có dữ liệu
            if (dataSnapshotComPress.getChildrenCount() > 0) {
                for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                    roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                }
            } else {
                roomModel.setCompressionImage(tempImageList.get(0));
            }

            //End Thêm tên danh sách tên hình vào phòng trọ

            //Thêm danh sách bình luận của phòng trọ

            DataSnapshot dataSnapshotCommentRoom = dataSnapshot.child("RoomComments").child(dataSnapshotValueRoom.getKey());
            List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
            //Duyệt tất cả các giá trị trong node tương ứng
            for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                commentModel.setCommentID(CommentValue.getKey());
                //Duyệt user tương ứng để lấy ra thông tin user bình luận
                UserModel tempUser = dataSnapshot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                tempUser.setUserID(commentModel.getUser());
                commentModel.setUserComment(tempUser);
                //End duyệt user tương ứng để lấy ra thông tin user bình luận

                tempCommentList.add(commentModel);
            }

            roomModel.setListCommentRoom(tempCommentList);

            //End Thêm danh sách bình luận của phòng trọ

            //Thêm danh sách tiện nghi của phòng trọ

            DataSnapshot dataSnapshotConvenientRoom = dataSnapshot.child("RoomConvenients").child(dataSnapshotValueRoom.getKey());
            List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
            //Duyệt tất cả các giá trị trong node tương ứng
            for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                String convenientId = valueConvenient.getValue(String.class);
                ConvenientModel convenientModel = dataSnapshot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                convenientModel.setConvenientID(convenientId);

                tempConvenientList.add(convenientModel);
            }

            roomModel.setListConvenientRoom(tempConvenientList);

            //End Thêm danh sách tiện nghi của phòng trọ

            //Thêm danh sách giá của phòng trọ

            DataSnapshot dataSnapshotRoomPrice = dataSnapshot.child("RoomPrice").child(dataSnapshotValueRoom.getKey());
            List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
            //Duyệt tất cả các giá trị trong node tương ứng
            for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                String roomPriceId = valueRoomPrice.getKey();
                double price = valueRoomPrice.getValue(double.class);

                if (roomPriceId.equals("IDRPT4")) {
                    continue;
                }
                RoomPriceModel roomPriceModel = dataSnapshot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                roomPriceModel.setRoomPriceID(roomPriceId);
                roomPriceModel.setPrice(price);

                tempRoomPriceList.add(roomPriceModel);
            }

            roomModel.setListRoomPrice(tempRoomPriceList);

            //End Thêm danh sách giá của phòng trọ

            //Thêm thông tin chủ sở hữu cho phòng trọ
            UserModel tempUser = dataSnapshot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
            tempUser.setUserID(roomModel.getOwner());
            roomModel.setRoomOwner(tempUser);

            //End thêm thông tin chủ sở hữu cho phòng trọ

            //Thêm vào lượt xem của phòng trọ
            int tempViews = (int) dataSnapshot.child("RoomViews").child(dataSnapshotValueRoom.getKey()).getChildrenCount();
            roomModel.setViews(tempViews);
            //End thêm vào lượt xem của phòng trọ

            //Kích hoạt interface
            mainRoomModelInterface.getListMainRoom(roomModel);
        }

        // Ẩn progress bar load more.
        mainRoomModelInterface.setProgressBarLoadMore();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(describe);
        dest.writeString(name);
        dest.writeString(owner);
        dest.writeString(timeCreated);
        dest.writeLong(currentNumber);
        dest.writeLong(maxNumber);
        dest.writeDouble(length);
        dest.writeDouble(width);
        dest.writeDouble(rentalCosts);
        dest.writeByte((byte) (authentication ? 1 : 0));
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(typeID);
        dest.writeString(roomType);
        dest.writeLong(medium);
        dest.writeLong(great);
        dest.writeLong(prettyGood);
        dest.writeLong(bad);
        dest.writeString(roomID);

        //update 21/4/2019
        dest.writeString(apartmentNumber);
        dest.writeString(county);
        dest.writeString(street);
        dest.writeString(ward);
        dest.writeString(city);
        dest.writeParcelable(roomOwner, flags);
        //end update 21/4/2019

        dest.writeStringList(listImageRoom);
        dest.writeTypedList(listConvenientRoom);
        // Linh thêm
        dest.writeTypedList(listRoomPrice);
        dest.writeTypedList(listCommentRoom);
    }


    //Hàm thêm phòng mới |Thêm theo thứ tự thêm hình trước và thêm vào node room sau cùng để tránh lỗi xảy ra
    public void addRoom(String UID, RoomModel roomModel, List<String> listConvenient, List<String> listPathImage
            , float electricBill, float warterBill, float InternetBill, float parkingBill, ICallBackFromAddRoom iCallBackFromAddRoom, Context context) {

        //Lấy ra node room
        DatabaseReference nodeRoom = nodeRoot.child("Room");
        //Lấy Key push động vào firebase
        String RoomID = nodeRoom.push().getKey();

        //Tải hình lên trước sau khi hoàn tất tải hình mới thêm vào các thông tin cần thiết
        //Tải hình lên
        final int[] count = {0};
        for (String pathImage : listPathImage) {

            //Lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh
            DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
            String date = df.format(Calendar.getInstance().getTime());
            //End lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh

            //Lấy đường dẫn Uri của hình
            Uri file = Uri.parse(pathImage);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Images/" + date + file.getLastPathSegment());

            //Tải hình lên bằng hàm putFile
            storageReference.putFile(file).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Lấy URL hình mới upload
                    String dowloadURL = uri.toString();
                    //Push hình vào danh sách hình tương ứng với room
                    nodeRoot.child("RoomImages").child(RoomID).push().setValue(dowloadURL);
                    count[0]++;
                    if (count[0] == listPathImage.size()) {
                        //Thêm vào thông tin khác
                        //Thêm danh sách tiện ích
                        for (String dataConvenient : listConvenient) {
                            nodeRoot.child("RoomConvenients").child(RoomID).push().setValue(dataConvenient);
                        }
                        //End thêm danh sách tiện ích

                        //Thêm chi tiết các giá phòng
                        addDetailRoomPrice(RoomID, electricBill, warterBill, InternetBill, parkingBill, (float) roomModel.getRentalCosts());
                        //End thêm chi tiết các giá phòng

                        //Thêm vào thông tin phòng
                        nodeRoom.child(RoomID).setValue(roomModel);

                        //Thêm vào node RoomLocation để filter

                        //Cắt bỏ P. trước phường
                        String SplitWarn = roomModel.getWard().substring(2);

                        nodeRoot.child("LocationRoom").child(roomModel.getCounty())
                                .child(SplitWarn)
                                .child(roomModel.getStreet())
                                .push().setValue(RoomID);

                        //End thêm vào node RoomLocation để filter

                        //Đăng hình ảnh đã nén ở trang đầu để load nhanh hơn
                        //Tạo đường dẫn file

                        //Lấy ra đường dẫn file
                        File image_filePath = null;
                        String path = FilePath.getPath(context, Uri.parse(listPathImage.get(0)));
                        if (path != null) {
                            //Tạo file tương ứng với đường dẫn đó
                            image_filePath = new File(path);
                        }
                        if (image_filePath != null) {
                            //Tạo Bitmap để nén ảnh và tải lên
                            Bitmap image_bitmap = null;
                            try {

                                image_bitmap = new Compressor(context)
                                        .setMaxHeight(320)
                                        .setMaxWidth(240)
                                        .setQuality(50)
                                        .compressToBitmap(image_filePath);
                                //Tạo mảng byte để đổ dữ liệu từ bitmap sang để up lên storage
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                //Chuyển ảnh về JPG và đổ vào mảng byte
                                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

                                //Đổ dữ liệu ra mảng byte
                                final byte[] image_byte = byteArrayOutputStream.toByteArray();

                                //Lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh
                                DateFormat df2 = new SimpleDateFormat("ddMMyyyyHHmmss");
                                String date2 = df2.format(Calendar.getInstance().getTime());
                                //End lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh

                                //Tạo đường dẫn đến thu mục upload hình
                                StorageReference CompressStorage = FirebaseStorage.getInstance().getReference().child("CompressionImages/" + roomModel.getOwner() + date2 + ".jpg");

                                //Up load hình
                                UploadTask uploadTask = CompressStorage.putBytes(image_byte);
                                uploadTask.addOnSuccessListener(taskSnapshot1 -> CompressStorage.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                    //Lấy ra đường dẫn download
                                    String ComPressDowloadURL = uri1.toString();
                                    //Thêm vào trong node Room
                                    nodeRoot.child("RoomCompressionImages").child(RoomID).push().setValue(ComPressDowloadURL);

                                    iCallBackFromAddRoom.stopProgess(true);
                                }));

                                // thay đổi giá trị owner ở node Users

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                                nodeRoot.child("Users").child(UID).child("owner").setValue(true);
                    }
                }
            }));
            iCallBackFromAddRoom.stopProgess(true);
        }
        //End tải hình lên

        //push vào node room

    }

    //Hàm thêm vào chi tiết giá cả của phòng
    private void addDetailRoomPrice(String roomID, float electricBill, float warterBill, float InternetBill, float parkingBill, float roomBill) {
        //Thêm tiền nước
        nodeRoot.child("RoomPrice").child(roomID).child("IDRPT0").setValue(warterBill);
        //Thêm tiền điện
        nodeRoot.child("RoomPrice").child(roomID).child("IDRPT3").setValue(electricBill);
        //Thêm tiền mạng
        nodeRoot.child("RoomPrice").child(roomID).child("IDRPT1").setValue(InternetBill);
        //Thêm tiền giữ xe
        nodeRoot.child("RoomPrice").child(roomID).child("IDRPT2").setValue(parkingBill);
        //Thêm tiền phòng
        nodeRoot.child("RoomPrice").child(roomID).child("IDRPT4").setValue(roomBill);
    }

    public void SendData(List<String> ListRoomID, IMainRoomModel mainRoomModelInterface) {
        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String RoomID : ListRoomID) {
                    //Duyệt vào room cần lấy dữ liệu
                    DataSnapshot dataSnapshotValueRoom = dataSnapshot.child("Room").child(RoomID);

                    //Lấy ra giá trị ép kiểu qua kiểu RoomModel
                    RoomModel roomModel = dataSnapshotValueRoom.getValue(RoomModel.class);
                    Log.d("check", RoomID);

                    roomModel.setRoomID(RoomID);

                    //Set loại phòng trọ
                    String tempType = dataSnapshot.child("RoomTypes")
                            .child(roomModel.getTypeID())
                            .getValue(String.class);

                    roomModel.setRoomType(tempType);

                    //Thêm tên danh sách tên hình vào phòng trọ

                    //Duyệt vào node RoomImages trên firebase và duyệt vào node có mã room tương ứng
                    DataSnapshot dataSnapshotImageRoom = dataSnapshot.child("RoomImages").child(RoomID);
                    List<String> tempImageList = new ArrayList<String>();
                    //Duyêt tất cả các giá trị của node tương ứng
                    for (DataSnapshot valueImage : dataSnapshotImageRoom.getChildren()) {
                        tempImageList.add(valueImage.getValue(String.class));
                    }

                    //set mảng hình vào list
                    roomModel.setListImageRoom(tempImageList);

                    //End Thêm tên danh sách tên hình vào phòng trọ

                    //Thêm vào hình dung lượng thấp của phòng trọ
                    DataSnapshot dataSnapshotComPress = dataSnapshot.child("RoomCompressionImages").child(RoomID);
                    //Kiểm tra nếu có dữ liệu
                    if (dataSnapshotComPress.getChildrenCount() > 0) {
                        for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                            roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                        }
                    } else {
                        roomModel.setCompressionImage(tempImageList.get(0));
                    }

                    //Thêm danh sách bình luận của phòng trọ

                    DataSnapshot dataSnapshotCommentRoom = dataSnapshot.child("RoomComments").child(RoomID);
                    List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                        CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                        commentModel.setCommentID(CommentValue.getKey());
                        //Duyệt user tương ứng để lấy ra thông tin user bình luận
                        UserModel tempUser = dataSnapshot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                        tempUser.setUserID(commentModel.getUser());
                        commentModel.setUserComment(tempUser);
                        //End duyệt user tương ứng để lấy ra thông tin user bình luận

                        tempCommentList.add(commentModel);
                    }

                    roomModel.setListCommentRoom(tempCommentList);

                    //End Thêm danh sách bình luận của phòng trọ

                    //Thêm danh sách tiện nghi của phòng trọ

                    DataSnapshot dataSnapshotConvenientRoom = dataSnapshot.child("RoomConvenients").child(RoomID);
                    List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                        String convenientId = valueConvenient.getValue(String.class);
                        ConvenientModel convenientModel = dataSnapshot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                        convenientModel.setConvenientID(convenientId);

                        tempConvenientList.add(convenientModel);
                    }

                    roomModel.setListConvenientRoom(tempConvenientList);

                    //End Thêm danh sách tiện nghi của phòng trọ

                    //Thêm danh sách giá của phòng trọ

                    DataSnapshot dataSnapshotRoomPrice = dataSnapshot.child("RoomPrice").child(RoomID);
                    List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                        String roomPriceId = valueRoomPrice.getKey();
                        double price = valueRoomPrice.getValue(double.class);

                        if (roomPriceId.equals("IDRPT4")) {
                            continue;
                        }
                        RoomPriceModel roomPriceModel = dataSnapshot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                        roomPriceModel.setRoomPriceID(roomPriceId);
                        roomPriceModel.setPrice(price);

                        tempRoomPriceList.add(roomPriceModel);
                    }

                    roomModel.setListRoomPrice(tempRoomPriceList);

                    //End Thêm danh sách giá của phòng trọ

                    //Thêm thông tin chủ sở hữu cho phòng trọ
                    UserModel tempUser = dataSnapshot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                    tempUser.setUserID(roomModel.getOwner());
                    roomModel.setRoomOwner(tempUser);

                    //End thêm thông tin chủ sở hữu cho phòng trọ

                    //Thêm vào lượt xem của phòng trọ
                    int tempViews = (int) dataSnapshot.child("RoomViews").child(RoomID).getChildrenCount();
                    roomModel.setViews(tempViews);
                    //End thêm vào lượt xem của phòng trọ

                    //Kích hoạt interface
                    mainRoomModelInterface.getListMainRoom(roomModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Gán sự kiện listen cho nodeRoot
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    public void SendData_NoLoadMore(List<String> ListRoomID, IMapViewModel mapViewModelInterface) {
        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String RoomID : ListRoomID) {
                    //Duyệt vào room cần lấy dữ liệu
                    DataSnapshot dataSnapshotValueRoom = dataSnapshot.child("Room").child(RoomID);

                    //Lấy ra giá trị ép kiểu qua kiểu RoomModel
                    RoomModel roomModel = dataSnapshotValueRoom.getValue(RoomModel.class);
                    Log.d("check", RoomID);

                    roomModel.setRoomID(RoomID);

                    //Set loại phòng trọ
                    String tempType = dataSnapshot.child("RoomTypes")
                            .child(roomModel.getTypeID())
                            .getValue(String.class);

                    roomModel.setRoomType(tempType);

                    //Thêm tên danh sách tên hình vào phòng trọ

                    //Duyệt vào node RoomImages trên firebase và duyệt vào node có mã room tương ứng
                    DataSnapshot dataSnapshotImageRoom = dataSnapshot.child("RoomImages").child(RoomID);
                    List<String> tempImageList = new ArrayList<String>();
                    //Duyêt tất cả các giá trị của node tương ứng
                    for (DataSnapshot valueImage : dataSnapshotImageRoom.getChildren()) {
                        tempImageList.add(valueImage.getValue(String.class));
                    }

                    //set mảng hình vào list
                    roomModel.setListImageRoom(tempImageList);

                    //End Thêm tên danh sách tên hình vào phòng trọ

                    //Thêm vào hình dung lượng thấp của phòng trọ
                    DataSnapshot dataSnapshotComPress = dataSnapshot.child("RoomCompressionImages").child(RoomID);
                    //Kiểm tra nếu có dữ liệu
                    if (dataSnapshotComPress.getChildrenCount() > 0) {
                        for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                            roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                        }
                    } else {
                        roomModel.setCompressionImage(tempImageList.get(0));
                    }

                    //Thêm danh sách bình luận của phòng trọ

                    DataSnapshot dataSnapshotCommentRoom = dataSnapshot.child("RoomComments").child(RoomID);
                    List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                        CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                        commentModel.setCommentID(CommentValue.getKey());
                        //Duyệt user tương ứng để lấy ra thông tin user bình luận
                        UserModel tempUser = dataSnapshot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                        tempUser.setUserID(commentModel.getUser());
                        commentModel.setUserComment(tempUser);
                        //End duyệt user tương ứng để lấy ra thông tin user bình luận

                        tempCommentList.add(commentModel);
                    }

                    roomModel.setListCommentRoom(tempCommentList);

                    //End Thêm danh sách bình luận của phòng trọ

                    //Thêm danh sách tiện nghi của phòng trọ

                    DataSnapshot dataSnapshotConvenientRoom = dataSnapshot.child("RoomConvenients").child(RoomID);
                    List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                        String convenientId = valueConvenient.getValue(String.class);
                        ConvenientModel convenientModel = dataSnapshot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                        convenientModel.setConvenientID(convenientId);

                        tempConvenientList.add(convenientModel);
                    }

                    roomModel.setListConvenientRoom(tempConvenientList);

                    //End Thêm danh sách tiện nghi của phòng trọ

                    //Thêm danh sách giá của phòng trọ

                    DataSnapshot dataSnapshotRoomPrice = dataSnapshot.child("RoomPrice").child(RoomID);
                    List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
                    //Duyệt tất cả các giá trị trong node tương ứng
                    for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                        String roomPriceId = valueRoomPrice.getKey();
                        double price = valueRoomPrice.getValue(double.class);

                        if (roomPriceId.equals("IDRPT4")) {
                            continue;
                        }
                        RoomPriceModel roomPriceModel = dataSnapshot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                        roomPriceModel.setRoomPriceID(roomPriceId);
                        roomPriceModel.setPrice(price);

                        tempRoomPriceList.add(roomPriceModel);
                    }

                    roomModel.setListRoomPrice(tempRoomPriceList);

                    //End Thêm danh sách giá của phòng trọ

                    //Thêm thông tin chủ sở hữu cho phòng trọ
                    UserModel tempUser = dataSnapshot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                    tempUser.setUserID(roomModel.getOwner());
                    roomModel.setRoomOwner(tempUser);

                    //End thêm thông tin chủ sở hữu cho phòng trọ

                    //Thêm vào lượt xem của phòng trọ
                    int tempViews = (int) dataSnapshot.child("RoomViews").child(RoomID).getChildrenCount();
                    roomModel.setViews(tempViews);
                    //End thêm vào lượt xem của phòng trọ

                    //Kích hoạt interface
                    mapViewModelInterface.getListRoom(roomModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Gán sự kiện listen cho nodeRoot
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    public void ListRoomUser(final IMainRoomModel mainRoomModelInterface, String userID, int quantityRoomToLoad, int quantityRoomLoaded) {
        Query nodeRoomUser = nodeRoot.child("Room")
                .orderByChild("owner")
                .equalTo(userID);

        // Tạo listen cho nodeRoomUser.
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
                            RoomModel roomModel = userRoomsSnapShot.getValue(RoomModel.class);

                            if (roomModel.isApprove()) {
                                //Lọc ra danh sách verified rooms.
                                listRoomsID.add(userRoomsSnapShot.getKey());
                            }
                        }

                        // set view
                        mainRoomModelInterface.setQuantityTop(listRoomsID.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartSpecialListRoom(mainRoomModelInterface, quantityRoomToLoad, quantityRoomLoaded);
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
                getPartSpecialListRoom(mainRoomModelInterface, quantityRoomToLoad, quantityRoomLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomUser
            nodeRoomUser.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void getListAuthenticationRoomsAtMainView(final IMainRoomModel iMainRoomModel, int quantity) {
        Query nodeRoomOrderByAuthentication = nodeRoot.child("Room")
                .orderByChild("authentication")
                .equalTo(true);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> listAuthenticationRoomID = new ArrayList<String>();

                for (DataSnapshot authenticationRoomsSnapShot : dataSnapshot.getChildren()) {
                    // Lọc ra danh sách verified rooms.
                    listAuthenticationRoomID.add(authenticationRoomsSnapShot.getKey());

                    // Chỉ show ở main view số lượng nhất định.
                    if (listAuthenticationRoomID.size() == quantity) {
                        break;
                    }
                }

                //Thêm dữ liệu và gửi về lại UI
                SendData(listAuthenticationRoomID, iMainRoomModel);
                if (dataSnapshot.getChildrenCount() > quantity) {
                    iMainRoomModel.setButtonLoadMoreVerifiedRooms();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Gán sự kiện listen cho nodeRoomOrderByAuthentication
        nodeRoomOrderByAuthentication.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getListAuthenticationRoomsAtVerifiedRoomsView(final IMainRoomModel iMainRoomModel, int quantityRoomToLoad,
                                                              int quantityRoomLoaded) {
        Query nodeRoomOrderByAuthentication = nodeRoot.child("Room")
                .orderByChild("authentication")
                .equalTo(true);

        // Tạo listen cho nodeRoomOrderByAuthentication.
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListRoomEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        for (DataSnapshot authenticationRoomsSnapShot : dataNode.getChildren()) {
                            //Lọc ra danh sách verified rooms.
                            listRoomsID.add(authenticationRoomsSnapShot.getKey());
                        }

                        // set view
                        iMainRoomModel.setQuantityTop(listRoomsID.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartSpecialListRoom(iMainRoomModel, quantityRoomToLoad, quantityRoomLoaded);
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
                getPartSpecialListRoom(iMainRoomModel, quantityRoomToLoad, quantityRoomLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomOrderByAuthentication
            nodeRoomOrderByAuthentication.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void getPartSpecialListRoom(IMainRoomModel mainRoomModelInterface,
                                       int quantityRoomToLoad, int quantityRoomLoaded) {
        int i = 0;

        // Chạy từ cuối list đến đầu list (list truyền vào đã sắp xếp theo thời gian)
        for (String RoomID : listRoomsID) {

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
            DataSnapshot dataSnapshotValueRoom = dataRoot.child("Room").child(RoomID);

            //Lấy ra giá trị ép kiểu qua kiểu RoomModel
            RoomModel roomModel = dataSnapshotValueRoom.getValue(RoomModel.class);

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

            //Kích hoạt interface
            mainRoomModelInterface.getListMainRoom(roomModel);
        }

        // Ẩn progress bar load more.
        mainRoomModelInterface.setProgressBarLoadMore();
    }

    // detail room dùng
    public static void getListFavoriteRoomsId(String UID) {

        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListFavoriteRoomsID.clear();

                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot favoriteRoom : dataSnapshot.getChildren()) {
                    String roomId = favoriteRoom.getKey();
                    ListFavoriteRoomsID.add(roomId);
                }
                //End thêm danh sách id trọ yêu thích
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Gán sự kiện listen cho nodeRoot
        DatabaseReference node = FirebaseDatabase.getInstance().getReference();
        node.child("FavoriteRooms").child(UID).addValueEventListener(valueEventListener);
    }

    public void getListFavoriteRooms(final IMainRoomModel iMainRoomModel, final String UID, int quantityRoomToLoad, int quantityRoomLoaded) {
        // Tạo listen cho nodeFavoriteRooms.
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListRoomEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        List<FavoriteRoomModel> listFavoriteRoomsModel = new ArrayList<>();

                        for (DataSnapshot favoriteRoomsSnapShot : dataNode.getChildren()) {
                            //Lọc ra danh sách favorite rooms.
                            FavoriteRoomModel favoriteRoomModel = favoriteRoomsSnapShot.getValue(FavoriteRoomModel.class);
                            favoriteRoomModel.setRoomId(favoriteRoomsSnapShot.getKey());

                            listFavoriteRoomsModel.add(favoriteRoomModel);
                        }

                        sortListFavorites(listFavoriteRoomsModel);

                        for (FavoriteRoomModel favoriteRoomModel : listFavoriteRoomsModel) {
                            listRoomsID.add(favoriteRoomModel.getRoomId());
                        }

                        // set view
                        iMainRoomModel.setQuantityTop(listRoomsID.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartSpecialListRoom(iMainRoomModel,
                                quantityRoomToLoad, quantityRoomLoaded);
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
                getPartSpecialListRoom(iMainRoomModel,
                        quantityRoomToLoad, quantityRoomLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeFavoriteRooms
            nodeRoot.child("FavoriteRooms").child(UID).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void addToFavoriteRooms(String roomId, IMainRoomModel iMainRoomModel, String UID) {
        DatabaseReference nodeFavoriteRooms = FirebaseDatabase.getInstance().getReference().child("FavoriteRooms");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        nodeFavoriteRooms.child(UID).child(roomId).child("time").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iMainRoomModel.makeToast("Đã thêm vào danh sách yêu thích");
                    iMainRoomModel.setIconFavorite(R.drawable.ic_favorite_full_white);
                }
            }
        });
    }

    public void removeFromFavoriteRooms(String roomId, IMainRoomModel iMainRoomModel, String UID) {
        DatabaseReference nodeFavoriteRooms = FirebaseDatabase.getInstance().getReference().child("FavoriteRooms");

        nodeFavoriteRooms.child(UID).child(roomId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iMainRoomModel.makeToast("Đã xóa khỏi danh sách yêu thích");
                    iMainRoomModel.setIconFavorite(R.drawable.ic_favorite_border_white);
                }
            }
        });
    }

    public void SumRooms(IMainRoomModel iMainRoomModel) {
        Query nodeRooms = nodeRoot.child("Room").orderByChild("approve").equalTo(true);

        // Tạo listen cho nodeHosts.
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long quantityRooms = dataSnapshot.getChildrenCount();

                // set view
                iMainRoomModel.setSumRoomsAdminView(quantityRooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRooms.addListenerForSingleValueEvent(valueEventListener);
    }

    public void sortListFavorites(List<FavoriteRoomModel> listFavoriteRoomsModel) {
        Collections.sort(listFavoriteRoomsModel, new Comparator<FavoriteRoomModel>() {
            @Override
            public int compare(FavoriteRoomModel favoriteRoomModel1, FavoriteRoomModel favoriteRoomModel2) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = df.parse(favoriteRoomModel1.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date2 = null;
                try {
                    date2 = df.parse(favoriteRoomModel2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date2.compareTo(date1);
            }
        });
    }

    public void sortListRooms(List<RoomModel> listRoomsModel) {
        Collections.sort(listRoomsModel, new Comparator<RoomModel>() {
            @Override
            public int compare(RoomModel roomModel1, RoomModel roomModel2) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = df.parse(roomModel1.getTimeCreated());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date2 = null;
                try {
                    date2 = df.parse(roomModel2.getTimeCreated());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date2.compareTo(date1);
            }
        });
    }

    //Thông tin về tất cả các phòng của người dùng. tổng phòng, tổng lượt xem, tổng bình luận
    public void infoOfAllRoomOfUser(String UID, IInfoOfAllRoomUser iInfoOfAllRoomUser) {
        Query nodeRoomOrderbyUserID = nodeRoot.child("Room")
                .orderByChild("owner")
                .equalTo(UID);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Lấy ra tổng số phòng
                int CountRoom = (int) dataSnapshot.getChildrenCount();
                //Gửi thông tin tổng số phòng về UI
                iInfoOfAllRoomUser.sendQuantity(CountRoom, 0);

                //Duyệt hết số phòng để đếm số lượng lượt xem và bình luận
                int count = 0;
                List<String> listRoomID = new ArrayList<>();
                for (DataSnapshot snapshotRoom : dataSnapshot.getChildren()) {
                    count++;
                    listRoomID.add(snapshotRoom.getKey());
                    //Lấy ra key và tìm trong
                    if (count == CountRoom) {
                        //Gửi dữ liệu về controller
                        SendInfoAllOfRoom(listRoomID, iInfoOfAllRoomUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoomOrderbyUserID.addListenerForSingleValueEvent(valueEventListener);
    }

    //Gửi thông tin về UI
    private void SendInfoAllOfRoom(List<String> listRoomID, IInfoOfAllRoomUser iInfoOfAllRoomUser) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int CountViews = 0;
                int CountComments = 0;
                for (String roomID : listRoomID) {
                    int views = (int) dataSnapshot.child("RoomViews").child(roomID).getChildrenCount();
                    CountViews += views;

                    int comments = (int) dataSnapshot.child("RoomComments").child(roomID).getChildrenCount();
                    CountComments += comments;

                }

                //iInfoOfAllRoomUser.sendQuantity(CountViews,1);
                //Gửi thông tin về UI
                iInfoOfAllRoomUser.sendQuantity(CountViews, 1);
                iInfoOfAllRoomUser.sendQuantity(CountComments, 2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    //Hàm xóa phòng
    public void DeleteRoom(String RoomID) {
        //Xóa trong node location Room
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Lấy ra tên đường quận để xóa
                RoomModel roomModel = dataSnapshot.child("Room").child(RoomID).getValue(RoomModel.class);

                String SplitWarn = roomModel.getWard().substring(2);
                //Xóa trong node location
                nodeRoot.child("LocationRoom").child(roomModel.getCounty())
                        .child(SplitWarn).child(roomModel.getStreet()).orderByValue().equalTo(RoomID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot nodeRoom : dataSnapshot.getChildren()) {
                                    Log.d("check3", nodeRoom.getValue(String.class));
                                    //Xóa node
                                    nodeRoom.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                DataSnapshot snapshotFavoriteRoom = dataSnapshot.child("FavoriteRooms");
                for (DataSnapshot snapshotUser : snapshotFavoriteRoom.getChildren()) {
                    for (DataSnapshot snapshotRoomFavorite : snapshotUser.getChildren()) {
                        if (snapshotRoomFavorite.getKey().equals(RoomID)) {
                            Log.d("mycheck", snapshotRoomFavorite.getKey());
                            snapshotRoomFavorite.getRef().removeValue();
                        }
                    }
                }

                //Xóa trong node FacoritRoom

                //Xóa trong node Room
                nodeRoot.child("Room").child(RoomID).getRef().removeValue();

                //Xóa trong node ReportedRoom
                nodeRoot.child("ReportedRoom").child(RoomID).getRef().removeValue();

                //Xóa trong node roomComment
                nodeRoot.child("RoomComments").child(RoomID).getRef().removeValue();

                //Xóa trong node RoomCompressImage
                nodeRoot.child("RoomCompressionImages").child(RoomID).getRef().removeValue();

                //Xóa trong node RoomImage
                nodeRoot.child("RoomImages").child(RoomID).getRef().removeValue();

                //Xóa trong node convenient
                nodeRoot.child("RoomConvenients").child(RoomID).getRef().removeValue();

                //Xóa trong node RoomPrice
                nodeRoot.child("RoomPrice").child(RoomID).getRef().removeValue();

                //Xóa trong node RoomViews
                nodeRoot.child("RoomViews").child(RoomID).getRef().removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.addListenerForSingleValueEvent(valueEventListener);

    }

    //Hàm Change State Room: sang đã thuê
    public void changeState(String RoomID, int MaxNumber) {
        nodeRoot.child("Room").child(RoomID).child("currentNumber").setValue(MaxNumber);
    }

    //Hàm Change State Room: sang còn trống
    public void changeState(String RoomID) {
        nodeRoot.child("Room").child(RoomID).child("currentNumber").setValue(0);
    }


    public void getOneRoomFollowRoomId(String roomId, IPostRoomUpdateModel iPostRoomUpdateModel) {

        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotRoom = dataSnapshot.child("Room");

                //Duyệt hết trong danh sách phòng trọ
                for (DataSnapshot valueRoom : dataSnapshotRoom.getChildren()) {

                    if (valueRoom.getKey().equals(roomId)) {
                        //Lấy ra giá trị ép kiểu qua kiểu RoomModel
                        RoomModel roomModel = valueRoom.getValue(RoomModel.class);
                        //Set mã phòng trọ
                        roomModel.setRoomID(valueRoom.getKey());

                        //Set loại phòng trọ
                        String tempType = dataSnapshot.child("RoomTypes")
                                .child(roomModel.getTypeID())
                                .getValue(String.class);

                        roomModel.setRoomType(tempType);

                        //Thêm tên danh sách tên hình vào phòng trọ

                        //Duyệt vào node RoomImages trên firebase và duyệt vào node có mã room tương ứng
                        DataSnapshot dataSnapshotImageRoom = dataSnapshot.child("RoomImages").child(valueRoom.getKey());
                        List<String> tempImageList = new ArrayList<String>();
                        //Duyêt tất cả các giá trị của node tương ứng
                        for (DataSnapshot valueImage : dataSnapshotImageRoom.getChildren()) {
                            tempImageList.add(valueImage.getValue(String.class));
                        }

                        //set mảng hình vào list
                        roomModel.setListImageRoom(tempImageList);

                        //Thêm vào hình dung lượng thấp của phòng trọ
                        DataSnapshot dataSnapshotComPress = dataSnapshot.child("RoomCompressionImages").child(valueRoom.getKey());
                        //Kiểm tra nếu có dữ liệu
                        if (dataSnapshotComPress.getChildrenCount() > 0) {
                            for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                                roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                            }
                        } else {
                            roomModel.setCompressionImage(tempImageList.get(0));
                        }

                        //End Thêm tên danh sách tên hình vào phòng trọ

                        //Thêm danh sách bình luận của phòng trọ

                        DataSnapshot dataSnapshotCommentRoom = dataSnapshot.child("RoomComments").child(valueRoom.getKey());
                        List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
                        //Duyệt tất cả các giá trị trong node tương ứng
                        for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                            CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                            commentModel.setCommentID(CommentValue.getKey());
                            //Duyệt user tương ứng để lấy ra thông tin user bình luận
                            UserModel tempUser = dataSnapshot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                            commentModel.setUserComment(tempUser);
                            //End duyệt user tương ứng để lấy ra thông tin user bình luận

                            tempCommentList.add(commentModel);
                        }

                        roomModel.setListCommentRoom(tempCommentList);

                        //End Thêm danh sách bình luận của phòng trọ

                        //Thêm danh sách tiện nghi của phòng trọ

                        DataSnapshot dataSnapshotConvenientRoom = dataSnapshot.child("RoomConvenients").child(valueRoom.getKey());
                        List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                        //Duyệt tất cả các giá trị trong node tương ứng
                        for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                            String convenientId = valueConvenient.getValue(String.class);
                            ConvenientModel convenientModel = dataSnapshot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                            convenientModel.setConvenientID(convenientId);

                            tempConvenientList.add(convenientModel);
                        }

                        roomModel.setListConvenientRoom(tempConvenientList);

                        //End Thêm danh sách tiện nghi của phòng trọ

                        //Thêm danh sách giá của phòng trọ

                        DataSnapshot dataSnapshotRoomPrice = dataSnapshot.child("RoomPrice").child(valueRoom.getKey());
                        List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
                        //Duyệt tất cả các giá trị trong node tương ứng
                        for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                            String roomPriceId = valueRoomPrice.getKey();
                            double price = valueRoomPrice.getValue(double.class);

                            if (roomPriceId.equals("IDRPT4")) {
                                continue;
                            }
                            RoomPriceModel roomPriceModel = dataSnapshot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                            roomPriceModel.setRoomPriceID(roomPriceId);
                            roomPriceModel.setPrice(price);

                            tempRoomPriceList.add(roomPriceModel);
                        }

                        roomModel.setListRoomPrice(tempRoomPriceList);

                        //End Thêm danh sách giá của phòng trọ

                        //Thêm thông tin chủ sở hữu cho phòng trọ
                        UserModel tempUser = dataSnapshot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                        roomModel.setRoomOwner(tempUser);

                        //End thêm thông tin chủ sở hữu cho phòng trọ

                        //Thêm vào lượt xem của phòng trọ
                        int tempViews = (int) dataSnapshot.child("RoomViews").child(valueRoom.getKey()).getChildrenCount();
                        roomModel.setViews(tempViews);
                        //End thêm vào lượt xem của phòng trọ

                        //Kích hoạt interface
                        iPostRoomUpdateModel.getRoomFollowId(roomModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    // Hàm cập nhật post room step 1
    public void postRoomStep1Update(String roomId, String city, String district, String ward, String street, String no, IPostRoomUpdateModel iPostRoomUpdateModel,
                                    String oldCity, String oldDistrict, String oldWard, String oldStreet, String oldNo) {

        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot dataSnapshotLocationRoom = dataSnapshot.child("LocationRoom");
                boolean isHaveLocationRoom = false;

                //Cắt bỏ P. trước phường
                String SplitWarnOld = oldWard.substring(2);

                for (DataSnapshot valueLocation : dataSnapshotLocationRoom.child(oldDistrict).child(SplitWarnOld).child(oldStreet).getChildren()) {

                    if (valueLocation.getValue().equals(roomId)) {

                        isHaveLocationRoom = true;

                        updateHaveKey(roomId, city, district, ward, street, no, iPostRoomUpdateModel, oldCity,
                                oldDistrict, oldWard, SplitWarnOld, valueLocation.getKey(), oldStreet, oldNo);
                    }
                }

                if (!isHaveLocationRoom) {
                    updateNotHaveKey(roomId, city, district, ward, street, no, iPostRoomUpdateModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    // Update khi chưa có id trong location room
    public void updateNotHaveKey(String roomId, String city, String district, String ward, String street, String no, IPostRoomUpdateModel iPostRoomUpdateModel) {

        //Cắt bỏ P. trước phường
        String splitWarn = ward.substring(2);

        //Push ID room vào
        nodeRoot.child("LocationRoom")
                .child(district)
                .child(splitWarn)
                .child(street)
                .push()
                .setValue(roomId);

        Map<String, Object> map = new HashMap<>();
        map.put("city", city);
        map.put("county", district);
        map.put("ward", ward);
        map.put("street", street);
        map.put("apartmentNumber", no);
        nodeRoot.child("Room").child(roomId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                iPostRoomUpdateModel.getSuccessNotifyPostRoomStep1();

                getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
            }
        });
    }

    // Update khi chưa có id trong location room
    public void updateHaveKey(String roomId, String city, String district, String ward, String street, String no, IPostRoomUpdateModel iPostRoomUpdateModel,
                              String oldCity, String oldDistrict, String oldWard, String SplitWarnOld, String value, String oldStreet, String oldNo) {

        Log.d("delete", oldDistrict + " " + SplitWarnOld + " " + oldStreet + " " + value);

        nodeRoot.child("LocationRoom")
                .child(oldDistrict)
                .child(SplitWarnOld)
                .child(oldStreet)
                .child(value)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //Cắt bỏ P. trước phường
                        String splitWarn = ward.substring(2);

                        //Push ID room vào
                        nodeRoot.child("LocationRoom").child(district)
                                .child(splitWarn)
                                .child(street)
                                .push()
                                .setValue(roomId);

                        Map<String, Object> map = new HashMap<>();
                        map.put("city", city);
                        map.put("county", district);
                        map.put("ward", ward);
                        map.put("street", street);
                        map.put("apartmentNumber", no);
                        nodeRoot.child("Room").child(roomId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                iPostRoomUpdateModel.getSuccessNotifyPostRoomStep1();

                                getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
                            }
                        });
                    }
                });
    }

    // Hàm cập nhật post room step 1
    public void postRoomStep2Update(String roomId, String typeId, boolean genderRoom, long maxNumber, float width, float length,
                                    float priceRoom, float electricBill, float warterBill, float InternetBill, float parkingBill, IPostRoomUpdateModel iPostRoomUpdateModel) {

        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot dataSnapshotPriceRoom = dataSnapshot.child("RoomPrice");
                boolean isHavePriceRoom = false;

                for (DataSnapshot valuePriceroom : dataSnapshotPriceRoom.getChildren()) {

                    if (valuePriceroom.getKey().equals(roomId)) {

                        nodeRoot.child("RoomPrice")
                                .child(valuePriceroom.getKey())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                addDetailRoomPrice(roomId, electricBill, warterBill, InternetBill, parkingBill, priceRoom);

                                Map<String, Object> map = new HashMap<>();
                                map.put("typeID", typeId);
                                map.put("gender", genderRoom);
                                map.put("maxNumber", maxNumber);
                                map.put("width", width);
                                map.put("length", length);
                                map.put("rentalCosts", priceRoom);
                                nodeRoot.child("Room").child(roomId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        iPostRoomUpdateModel.getSuccessNotifyPostRoomStep2();

                                        getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
                                    }
                                });
                            }
                        });
                    }
                }

                if (isHavePriceRoom) {
                    return;
                }

                addDetailRoomPrice(roomId, electricBill, warterBill, InternetBill, parkingBill, priceRoom);

                Map<String, Object> map = new HashMap<>();
                map.put("typeID", typeId);
                map.put("gender", genderRoom);
                map.put("maxNumber", maxNumber);
                map.put("width", width);
                map.put("length", length);
                map.put("rentalCosts", priceRoom);
                nodeRoot.child("Room").child(roomId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        iPostRoomUpdateModel.getSuccessNotifyPostRoomStep2();

                        getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    // Hàm cập nhật post room step 1
    public void postRoomStep3Update(String roomId, String owner, List<String> listConvenient, List<String> listPathImageChoosed,
                                    boolean isChangeConvenient, boolean isChangeImageRoom, IPostRoomUpdateModel iPostRoomUpdateModel, Context context) {
        if (isChangeConvenient == true && isChangeImageRoom == false) {
            //Tạo listen cho firebase
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DataSnapshot dataSnapshotConvenientsRoom = dataSnapshot.child("RoomConvenients");
                    boolean isHaveConvenient = false;

                    for (DataSnapshot valueConvenient : dataSnapshotConvenientsRoom.getChildren()) {

                        if (valueConvenient.getKey().equals(roomId)) {

                            isHaveConvenient = true;
                            deleteConvenients(roomId, valueConvenient.getKey(), listConvenient, iPostRoomUpdateModel);
                        }
                    }

                    if (!isHaveConvenient) {
                        addConvenients(roomId, listConvenient, iPostRoomUpdateModel);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        } else if (!isChangeConvenient && isChangeImageRoom) {
            //Tạo listen cho firebase

            final int[] count = {0};

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DataSnapshot dataSnapshotRoom = dataSnapshot.child("RoomImages");
                    boolean isHaveImage = false;

                    for (DataSnapshot valueNodeImage : dataSnapshotRoom.getChildren()) {

                        if (valueNodeImage.getKey().equals(roomId)) {
                            isHaveImage = true;

                            for (DataSnapshot valueImage : valueNodeImage.getChildren()) {
                                boolean isFind = false;

                                for (String pathImage : listPathImageChoosed) {
                                    if (valueImage.getValue().equals(pathImage) == true) {
                                        isFind = true;

                                        // Số lần tìm thấy lặp lại ảnh đã up
                                        count[0]++;
                                        break;
                                    }
                                }

                                if (isFind == false) {
                                    nodeRoot.child("RoomImages")
                                            .child(roomId)
                                            .child(valueImage.getKey())
                                            .removeValue();
                                }
                                //deleteImagesRoom(roomId, valueImage.getKey(), listPathImageChoosed, iPostRoomUpdateModel, context);
                            }
                            //Log.d("countingUpdate", count[0] + "");

                            addImagesRoom(roomId, listPathImageChoosed, iPostRoomUpdateModel, context, count[0]);
                        }
                    }

                    if (isHaveImage == false) {

                        //Log.d("isHaveImage", false + "");

                        addImagesRoom(roomId, listPathImageChoosed, iPostRoomUpdateModel, context, count[0]);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        } else if (isChangeConvenient == true && isChangeImageRoom == true) {
            //Tạo listen cho firebase
            ValueEventListener valueEventListenerConvenients = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DataSnapshot dataSnapshotRoom = dataSnapshot.child("RoomConvenients");
                    boolean isHaveConvenient = false;

                    for (DataSnapshot valueConvenient : dataSnapshotRoom.getChildren()) {

                        if (valueConvenient.getKey().equals(roomId)) {

                            isHaveConvenient = true;
                            deleteConvenientsNotNotify(roomId, valueConvenient.getKey(), listConvenient, iPostRoomUpdateModel);
                        }
                    }

                    if (isHaveConvenient == false) {
                        addConvenientsNotNotify(roomId, listConvenient, iPostRoomUpdateModel);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            final int[] count = {0};

            ValueEventListener valueEventListenerImage = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    DataSnapshot dataSnapshotRoom = dataSnapshot.child("RoomImages");
                    boolean isHaveImage = false;

                    for (DataSnapshot valueNodeImage : dataSnapshotRoom.getChildren()) {

                        if (valueNodeImage.getKey().equals(roomId)) {
                            isHaveImage = true;

                            for (DataSnapshot valueImage : valueNodeImage.getChildren()) {
                                boolean isFind = false;

                                for (String pathImage : listPathImageChoosed) {
                                    if (valueImage.getValue().equals(pathImage) == true) {
                                        isFind = true;

                                        // Số lần tìm thấy lặp lại ảnh đã up
                                        count[0]++;
                                        break;
                                    }
                                }

                                if (isFind == false) {
                                    nodeRoot.child("RoomImages")
                                            .child(roomId)
                                            .child(valueImage.getKey())
                                            .removeValue();
                                }
                            }

                            addImagesRoom(roomId, listPathImageChoosed, iPostRoomUpdateModel, context, count[0]);
                        }
                    }

                    if (isHaveImage == false) {
                        addImagesRoom(roomId, listPathImageChoosed, iPostRoomUpdateModel, context, count[0]);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            nodeRoot.addListenerForSingleValueEvent(valueEventListenerConvenients);

            nodeRoot.addListenerForSingleValueEvent(valueEventListenerImage);
        }
    }

    public void deleteConvenients(String roomId, String key, List<String> listConvenient, IPostRoomUpdateModel iPostRoomUpdateModel) {
        // Xóa convenients room hiện tại
        nodeRoot.child("RoomConvenients")
                .child(key)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addConvenients(roomId, listConvenient, iPostRoomUpdateModel);

            }
        });
    }

    public void deleteConvenientsNotNotify(String roomId, String key, List<String> listConvenient, IPostRoomUpdateModel iPostRoomUpdateModel) {
        // Xóa convenients room hiện tại
        nodeRoot.child("RoomConvenients")
                .child(key)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addConvenientsNotNotify(roomId, listConvenient, iPostRoomUpdateModel);

            }
        });
    }

    public void addConvenients(String roomId, List<String> listConvenient, IPostRoomUpdateModel iPostRoomUpdateModel) {
        // Thêm convenients room hiện tại
        for (String dataConvenient : listConvenient) {
            nodeRoot.child("RoomConvenients").child(roomId).push().setValue(dataConvenient);
        }

        iPostRoomUpdateModel.getSuccessNotifyPostRoomStep3();

        getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
    }

    public void addConvenientsNotNotify(String roomId, List<String> listConvenient, IPostRoomUpdateModel iPostRoomUpdateModel) {
        // Thêm convenients room hiện tại
        for (String dataConvenient : listConvenient) {
            nodeRoot.child("RoomConvenients").child(roomId).push().setValue(dataConvenient);
        }
    }

    public void deleteImagesRoom(String roomId, String key, List<String> listPathImageChoosed,
                                 IPostRoomUpdateModel iPostRoomUpdateModel, Context context, int countImageUploaded) {

        // Xóa convenients room hiện tại
        nodeRoot.child("RoomImages")
                .child(key)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addImagesRoom(roomId, listPathImageChoosed, iPostRoomUpdateModel, context, countImageUploaded);
            }
        });
    }

    public void addImagesRoom(String roomId, List<String> listPathImageChoosed,
                              IPostRoomUpdateModel iPostRoomUpdateModel, Context context, int countImageUploaded) {
        //Lấy ra node room
        DatabaseReference nodeRoom = nodeRoot.child("Room");

        //Tải hình lên trước sau khi hoàn tất tải hình mới thêm vào các thông tin cần thiết
        //Tải hình lên
        final int[] count = {0};
        final boolean isExitsFile = false;
        int countImageInFireBase = 0;


        for (String pathImage : listPathImageChoosed) {
            String header = pathImage.substring(0, Math.min(pathImage.length(), 23));

            // Kiểm tra xem đường dẫn này có phải trên fire base ko
            if (header.equals("https://firebasestorage") == true) {
                countImageInFireBase++;
            }
        }

        Log.d("countImageInFireBase", countImageInFireBase + "");
        Log.d("ImageChoosed.size()", listPathImageChoosed.size() + "");

        // Tất cả đã có trên fire base
        if (countImageInFireBase == listPathImageChoosed.size()) {

            iPostRoomUpdateModel.getSuccessNotifyPostRoomStep3();

            getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);

            return;
        }

        for (String pathImage : listPathImageChoosed) {
            //Lấy đường dẫn Uri của hình
            Uri file = Uri.parse(pathImage);

            //Lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh
            DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
            String date = df.format(Calendar.getInstance().getTime());
            //End lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Images/" + date + file.getLastPathSegment());

            //Tải hình lên bằng hàm putFile
            storageReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //isExitsFile = true;

                            //Lấy URL hình mới upload
                            String dowloadURL = uri.toString();
                            //Push hình vào danh sách hình tương ứng với room
                            nodeRoot.child("RoomImages").child(roomId).push().setValue(dowloadURL);

                            count[0]++;
                            if ((count[0] + countImageUploaded) == listPathImageChoosed.size()) {

                                String header = listPathImageChoosed.get(0).substring(0, Math.min(listPathImageChoosed.get(0).length(), 23));


                                Log.d("header", header);

                                if (header.equals("https://firebasestorage") == false) {

                                    String path = FilePath.getPath(context, Uri.parse(listPathImageChoosed.get(0)));

                                    // Nếu đường dẫn [0] file không tồn tại ở máy
                                    if (new File(path).exists() == true) {
                                        //Lấy ra đường dẫn file
                                        //Tạo file tương ứng với đường dẫn đó
                                        File image_filePath = new File(path);

                                        //Tạo Bitmap để nén ảnh và tải lên
                                        Bitmap image_bitmap = null;
                                        try {
                                            image_bitmap = new Compressor(context)
                                                    .setMaxHeight(320)
                                                    .setMaxWidth(240)
                                                    .setQuality(50)
                                                    .compressToBitmap(image_filePath);
                                            //Tạo mảng byte để đổ dữ liệu từ bitmap sang để up lên storage
                                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                            //Chuyển ảnh về JPG và đổ vào mảng byte
                                            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

                                            //Đổ dữ liệu ra mảng byte
                                            final byte[] image_byte = byteArrayOutputStream.toByteArray();

                                            //Lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh
                                            DateFormat df2 = new SimpleDateFormat("ddMMyyyyHHmmss");
                                            String date2 = df2.format(Calendar.getInstance().getTime());
                                            //End lấy ra ngày giờ hiện tại để phân biệt giữa các ảnh

                                            //Tạo đường dẫn đến thu mục upload hình
                                            StorageReference CompressStorage = FirebaseStorage.getInstance().getReference().child("CompressionImages/" + owner + date2 + ".jpg");

                                            //Up load hình
                                            UploadTask uploadTask = CompressStorage.putBytes(image_byte);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    CompressStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            //Lấy ra đường dẫn download
                                                            String ComPressDowloadURL = uri.toString();
                                                            //Thêm vào trong node Room
                                                            nodeRoot.child("RoomCompressionImages").child(roomId).removeValue();
                                                            nodeRoot.child("RoomCompressionImages").child(roomId).push().setValue(ComPressDowloadURL);

                                                            iPostRoomUpdateModel.getSuccessNotifyPostRoomStep3();
                                                            getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
                                                        }
                                                    });
                                                }
                                            });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.d("myroomroom", "run");
                                        iPostRoomUpdateModel.getSuccessNotifyPostRoomStep3();

                                        getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
                                    }
                                } else {
                                    Log.d("myroomroom", "run");
                                    iPostRoomUpdateModel.getSuccessNotifyPostRoomStep3();

                                    getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
                                }
                            }
                        }
                    });
                }
            });
        }

        // Không có file nào tại máy
//        if (isExitsFile == false) {
//            iPostRoomUpdateModel.getSuccessNotifyPostRoomStep3();
//
//            getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
//        }
    }

    // Hàm cập nhật post room step 1
    public void postRoomStep4Update(String roomId, String name, String describe, IPostRoomUpdateModel iPostRoomUpdateModel) {
        DatabaseReference nodeRoom = FirebaseDatabase.getInstance().getReference().child("Room");

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("describe", describe);
        nodeRoom.child(roomId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                iPostRoomUpdateModel.getSuccessNotifyPostRoomStep4();

                getOneRoomFollowRoomId(roomId, iPostRoomUpdateModel);
            }
        });
    }

    //Duyệt phòng
    public void verifyRoom(String RoomID) {
        nodeRoot.child("Room").child(RoomID).child("approve").setValue(true);
    }
}
