package com.example.designapptest.model;

import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.ISearchRoomModel;
import com.example.designapptest.domain.ConvenientFilter;
import com.example.designapptest.domain.GenderFilter;
import com.example.designapptest.domain.PriceFilter;
import com.example.designapptest.domain.TypeFilter;
import com.example.designapptest.domain.myFilter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SearchRoomModel {

    String district;
    DatabaseReference nodeRoot;

    private GenderFilter genderFilter;
    private PriceFilter priceFilter;
    private TypeFilter typeFilter;
    private List<ConvenientFilter> convenientFilterList;

    public SearchRoomModel(String district, List<myFilter> filterList) {

        //Lọc ra từng loại filter
        genderFilter = null;
        priceFilter = null;
        typeFilter = null;
        convenientFilterList = new ArrayList<>();

        for (myFilter dataFilter : filterList) {
            if (dataFilter instanceof GenderFilter) {
                genderFilter = (GenderFilter) dataFilter;
            }
            if (dataFilter instanceof PriceFilter) {
                priceFilter = (PriceFilter) dataFilter;
            }
            if (dataFilter instanceof TypeFilter) {
                typeFilter = (TypeFilter) dataFilter;
            }
            if (dataFilter instanceof ConvenientFilter) {
                convenientFilterList.add((ConvenientFilter) dataFilter);
            }

        }
        //End lọc ra từng loại filter

        this.district = district;
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    private DataSnapshot dataRoot;
    private List<RoomModel> listSearchRoomsModel = new ArrayList<>();
    // Số lượng phòng đã duyệt khi load more.
    private int filterDataQuantity = 0;

    public void searchRoom(ISearchRoomModel searchRoomModelInterface, String currentRoomID, int quantityRoomsToLoad, int quantityRoomsLoaded) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;

                // Lấy số lượng search rooms mỗi lần click submit
                getAllSearchRoom(dataRoot, currentRoomID, searchRoomModelInterface);

                getPartSearchRoom(dataRoot, currentRoomID, searchRoomModelInterface, quantityRoomsToLoad, quantityRoomsLoaded);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (dataRoot != null) {
            getPartSearchRoom(dataRoot, currentRoomID, searchRoomModelInterface, quantityRoomsToLoad, quantityRoomsLoaded);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void getPartSearchRoom(DataSnapshot dataSnapshot, String currentRoomID, ISearchRoomModel searchRoomModelInterface, int quantityRoomsToLoad, int quantityRoomsLoaded) {
        DataSnapshot snapShotLocationRoom = dataSnapshot.child("LocationRoom").child(district);

        int i = 0;
        int j = 0;
        int quantityNotToLoad = filterDataQuantity;

        for (DataSnapshot snapshotward : snapShotLocationRoom.getChildren()) {
            for (DataSnapshot snapShotStreet : snapshotward.getChildren()) {
                for (DataSnapshot snapShotRoom : snapShotStreet.getChildren()) {
                    if (j < quantityNotToLoad) {
                        j++;

                        // Bỏ qua những rooms đã load
                        if (i < quantityRoomsLoaded) {
                            i++;
                        }

                        continue;
                    }

                    // Nếu đã lấy đủ số lượng rooms tiếp theo thì ra khỏi vòng lặp
                    if (i == quantityRoomsToLoad) {
                        break;
                    }

                    //Lấy ra thông tin Room theo ID truyền vào
                    Boolean isFound = filterData(dataSnapshot, currentRoomID, snapShotRoom.getValue().toString(), searchRoomModelInterface);
                    filterDataQuantity++;

                    if (isFound == true) {
                        i++;
                    }
                }
            }
        }

        // ẩn progress bar load more.
        searchRoomModelInterface.setProgressBarLoadMore();
    }

    private boolean filterData(DataSnapshot snapShotRoot, String currentRoomID, String RoomID, ISearchRoomModel iSearchRoomModel) {

        //Mảng lưu xem đã thỏa mãn hết các điều kiện chưa
        boolean[] arrCheck = {false, false, false};

        //Lấy ra dữ liệu thô nhất của phòng
        RoomModel roomModel = snapShotRoot.child("Room").child(RoomID).getValue(RoomModel.class);

        //Lọc dữ liệu
        //Lọc theo giới tính và số room
        if (genderFilter != null) {
            if (roomModel.isGender() == genderFilter.isGender() && roomModel.getMaxNumber() >= genderFilter.getMaxNumber()) {
                arrCheck[0] = true;
            } else {
                arrCheck[0] = false;
            }
        } else {
            arrCheck[0] = true;
        }

        //Lọc theo giá
        if (priceFilter != null) {
            if (priceFilter.getMinPrice() == 10) {
                if (roomModel.getRentalCosts() > 10) {
                    arrCheck[1] = true;
                } else {
                    arrCheck[1] = false;
                }
            } else {
                if (roomModel.getRentalCosts() >= priceFilter.getMinPrice() && roomModel.getRentalCosts() <= priceFilter.getMaxPrice()) {
                    arrCheck[1] = true;
                } else {
                    arrCheck[1] = false;
                }
            }
        } else {
            arrCheck[1] = true;
        }

        //Lọc theo loại phòng
        if (typeFilter != null) {
            if (roomModel.getTypeID().equals(typeFilter.getId())) {
                arrCheck[2] = true;
            } else {
                arrCheck[2] = false;
            }
        } else {
            arrCheck[2] = true;
        }


        if (arrCheck[0] && arrCheck[1] && arrCheck[2]) {
            //Lọc dữ liệu theo tiện ích

            //Lấy ra danh sách tiện ích của room
            List<String> lisConvenientID = new ArrayList<String>();
            DataSnapshot NodeRoomConvenient = snapShotRoot.child("RoomConvenients").child(RoomID);
            for (DataSnapshot convenientID : NodeRoomConvenient.getChildren()) {
                lisConvenientID.add(convenientID.getValue(String.class));
            }

            //Kiểm tra có chứa đầy đủ điều kiện không
            //Lấy ra danh sách ID tương ứng
            List<String> stringListID = new ArrayList<String>();
            for (ConvenientFilter convenientFilter : convenientFilterList) {
                stringListID.add(convenientFilter.getId());
            }

            //Kiểm tra xem chứa đủ hết ID hay không
            if (lisConvenientID.containsAll(stringListID)) {
                //Lấy ra toàn bộ dữ liệu tương ứng với Room

                //Set room ID
                roomModel.setRoomID(RoomID);

                //Set loại phòng trọ
                String tempType = snapShotRoot.child("RoomTypes")
                        .child(roomModel.getTypeID())
                        .getValue(String.class);
                roomModel.setRoomType(tempType);
                //End set loại phòng trọ

                //Lấy ra danh sách link hình của phòng trọ
                List<String> tempImageList = new ArrayList<String>();
                DataSnapshot nodeRoomImage = snapShotRoot.child("RoomImages").child(RoomID);
                for (DataSnapshot ImageLink : nodeRoomImage.getChildren()) {
                    tempImageList.add(ImageLink.getValue(String.class));
                }
                roomModel.setListImageRoom(tempImageList);
                //End lấy ra danh sách link hình phòng trọ

                //Thêm vào hình dung lượng thấp của phòng trọ
                DataSnapshot dataSnapshotComPress = snapShotRoot.child("RoomCompressionImages").child(RoomID);
                //Kiểm tra nếu có dữ liệu
                if (dataSnapshotComPress.getChildrenCount() > 0) {
                    for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                        roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                    }
                } else {
                    roomModel.setCompressionImage(tempImageList.get(0));
                }

                //Thêm danh sách bình luận của phòng trọ
                DataSnapshot dataSnapshotCommentRoom = snapShotRoot.child("RoomComments").child(RoomID);
                List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                    CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                    commentModel.setCommentID(CommentValue.getKey());
                    //Duyệt user tương ứng để lấy ra thông tin user bình luận
                    UserModel tempUser = snapShotRoot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                    commentModel.setUserComment(tempUser);
                    //End duyệt user tương ứng để lấy ra thông tin user bình luận

                    tempCommentList.add(commentModel);
                }

                roomModel.setListCommentRoom(tempCommentList);
                //End thêm danh sách bình luận của phòng trọ

                //Thêm danh sách tiện nghi của phòng trọ

                DataSnapshot dataSnapshotConvenientRoom = snapShotRoot.child("RoomConvenients").child(RoomID);
                List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                    String convenientId = valueConvenient.getValue(String.class);
                    ConvenientModel convenientModel = snapShotRoot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                    convenientModel.setConvenientID(convenientId);

                    tempConvenientList.add(convenientModel);
                }

                roomModel.setListConvenientRoom(tempConvenientList);
                //End thêm danh sách tiện nghi của phòng trọ

                //Thêm danh sách giá của phòng trọ

                DataSnapshot dataSnapshotRoomPrice = snapShotRoot.child("RoomPrice").child(RoomID);
                List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                    String roomPriceId = valueRoomPrice.getKey();
                    double price = valueRoomPrice.getValue(Double.class);

                    if (roomPriceId.equals("IDRPT4")) {
                        continue;
                    }
                    RoomPriceModel roomPriceModel = snapShotRoot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                    roomPriceModel.setRoomPriceID(roomPriceId);
                    roomPriceModel.setPrice(price);

                    tempRoomPriceList.add(roomPriceModel);
                }

                roomModel.setListRoomPrice(tempRoomPriceList);

                //End Thêm danh sách giá của phòng trọ

                //Thêm thông tin chủ sở hữu cho phòng trọ
                UserModel tempUser = snapShotRoot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                roomModel.setRoomOwner(tempUser);
                //End thêm thông tin chủ sở hữu cho phòng trọ

                //Thêm vào lượt xem của phòng trọ
                int tempViews = (int) snapShotRoot.child("RoomViews").child(RoomID).getChildrenCount();
                roomModel.setViews(tempViews);
                //End thêm vào lượt xem của phòng trọ

                //Kích hoạt interface truyền dữ liệu qua
                if (currentRoomID.equals(roomModel.getRoomID())) {
                    iSearchRoomModel.sendDataRoom(roomModel, false);
                    return false;
                } else if (roomModel.isApprove()) {
                    iSearchRoomModel.sendDataRoom(roomModel, true);
                    return true;
                } else {
                    iSearchRoomModel.sendDataRoom(roomModel, false);
                    return false;
                }
            } else {
                //Gửi thông báo không tìm thấy phòng trọ nào
                iSearchRoomModel.sendDataRoom(roomModel, false);
                return false;
            }
        } else {
            //Gửi thông báo không tìm thấy phòng trọ nào
            iSearchRoomModel.sendDataRoom(roomModel, false);
            return false;
        }
        //End lọc dữ liệu
    }

    private void getAllSearchRoom(DataSnapshot dataSnapshot, String currentRoomID, ISearchRoomModel searchRoomModelInterface) {
        DataSnapshot snapShotLocationRoom = dataSnapshot.child("LocationRoom").child(district);

        for (DataSnapshot snapshotWard : snapShotLocationRoom.getChildren()) {
            for (DataSnapshot snapShotStreet : snapshotWard.getChildren()) {
                for (DataSnapshot snapShotRoom : snapShotStreet.getChildren()) {
                    //Lấy ra thông tin Room theo ID truyền vào
                    filterDataToGetQuantity(dataSnapshot, currentRoomID, snapShotRoom.getValue().toString());
                }
            }
        }

        searchRoomModelInterface.setQuantityTop(listSearchRoomsModel.size());
    }

    private void filterDataToGetQuantity(DataSnapshot snapShotRoot, String currentRoomID, String RoomID) {

        //Mảng lưu xem đã thỏa mãn hết các điều kiện chưa
        boolean[] arrCheck = {false, false, false};

        //Lấy ra dữ liệu thô nhất của phòng
        RoomModel roomModel = snapShotRoot.child("Room").child(RoomID).getValue(RoomModel.class);

        //Lọc dữ liệu
        //Lọc theo giới tính và số room
        if (genderFilter != null) {
            if (roomModel.isGender() == genderFilter.isGender() && roomModel.getMaxNumber() >= genderFilter.getMaxNumber()) {
                arrCheck[0] = true;
            } else {
                arrCheck[0] = false;
            }
        } else {
            arrCheck[0] = true;
        }

        //Lọc theo giá
        if (priceFilter != null) {
            if (priceFilter.getMinPrice() == 10) {
                if (roomModel.getRentalCosts() > 10) {
                    arrCheck[1] = true;
                } else {
                    arrCheck[1] = false;
                }
            } else {
                if (roomModel.getRentalCosts() >= priceFilter.getMinPrice() && roomModel.getRentalCosts() <= priceFilter.getMaxPrice()) {
                    arrCheck[1] = true;
                } else {
                    arrCheck[1] = false;
                }
            }
        } else {
            arrCheck[1] = true;
        }

        //Lọc theo loại phòng
        if (typeFilter != null) {
            if (roomModel.getTypeID().equals(typeFilter.getId())) {
                arrCheck[2] = true;
            } else {
                arrCheck[2] = false;
            }
        } else {
            arrCheck[2] = true;
        }


        if (arrCheck[0] && arrCheck[1] && arrCheck[2]) {
            //Lấy ra danh sách tiện ích của room
            List<String> lisConvenientID = new ArrayList<String>();
            DataSnapshot NodeRoomConvenient = snapShotRoot.child("RoomConvenients").child(RoomID);
            for (DataSnapshot convenientID : NodeRoomConvenient.getChildren()) {
                lisConvenientID.add(convenientID.getValue(String.class));
            }

            //Kiểm tra có chứa đầy đủ điều kiện không
            //Lấy ra danh sách ID tương ứng
            List<String> stringListID = new ArrayList<String>();
            for (ConvenientFilter convenientFilter : convenientFilterList) {
                stringListID.add(convenientFilter.getId());
            }

            //Kiểm tra xem chứa đủ hết ID hay không
            if (lisConvenientID.containsAll(stringListID)) {
                //Lấy ra toàn bộ dữ liệu tương ứng với Room

                //Set room ID
                roomModel.setRoomID(RoomID);

                //Set loại phòng trọ
                String tempType = snapShotRoot.child("RoomTypes")
                        .child(roomModel.getTypeID())
                        .getValue(String.class);
                roomModel.setRoomType(tempType);
                //End set loại phòng trọ

                //Lấy ra danh sách link hình của phòng trọ
                List<String> tempImageList = new ArrayList<String>();
                DataSnapshot nodeRoomImage = snapShotRoot.child("RoomImages").child(RoomID);
                for (DataSnapshot ImageLink : nodeRoomImage.getChildren()) {
                    tempImageList.add(ImageLink.getValue(String.class));
                }
                roomModel.setListImageRoom(tempImageList);
                //End lấy ra danh sách link hình phòng trọ

                //Thêm vào hình dung lượng thấp của phòng trọ
                DataSnapshot dataSnapshotComPress = snapShotRoot.child("RoomCompressionImages").child(RoomID);
                //Kiểm tra nếu có dữ liệu
                if (dataSnapshotComPress.getChildrenCount() > 0) {
                    for (DataSnapshot valueCompressionImage : dataSnapshotComPress.getChildren()) {
                        roomModel.setCompressionImage(valueCompressionImage.getValue(String.class));
                    }
                } else {
                    roomModel.setCompressionImage(tempImageList.get(0));
                }

                //Thêm danh sách bình luận của phòng trọ
                DataSnapshot dataSnapshotCommentRoom = snapShotRoot.child("RoomComments").child(RoomID);
                List<CommentModel> tempCommentList = new ArrayList<CommentModel>();
                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot CommentValue : dataSnapshotCommentRoom.getChildren()) {
                    CommentModel commentModel = CommentValue.getValue(CommentModel.class);
                    commentModel.setCommentID(CommentValue.getKey());
                    //Duyệt user tương ứng để lấy ra thông tin user bình luận
                    UserModel tempUser = snapShotRoot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
                    commentModel.setUserComment(tempUser);
                    //End duyệt user tương ứng để lấy ra thông tin user bình luận

                    tempCommentList.add(commentModel);
                }

                roomModel.setListCommentRoom(tempCommentList);
                //End thêm danh sách bình luận của phòng trọ

                //Thêm danh sách tiện nghi của phòng trọ

                DataSnapshot dataSnapshotConvenientRoom = snapShotRoot.child("RoomConvenients").child(RoomID);
                List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot valueConvenient : dataSnapshotConvenientRoom.getChildren()) {
                    String convenientId = valueConvenient.getValue(String.class);
                    ConvenientModel convenientModel = snapShotRoot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                    convenientModel.setConvenientID(convenientId);

                    tempConvenientList.add(convenientModel);
                }

                roomModel.setListConvenientRoom(tempConvenientList);
                //End thêm danh sách tiện nghi của phòng trọ

                //Thêm danh sách giá của phòng trọ

                DataSnapshot dataSnapshotRoomPrice = snapShotRoot.child("RoomPrice").child(RoomID);
                List<RoomPriceModel> tempRoomPriceList = new ArrayList<RoomPriceModel>();
                //Duyệt tất cả các giá trị trong node tương ứng
                for (DataSnapshot valueRoomPrice : dataSnapshotRoomPrice.getChildren()) {
                    String roomPriceId = valueRoomPrice.getKey();
                    double price = valueRoomPrice.getValue(double.class);

                    if (roomPriceId.equals("IDRPT4")) {
                        continue;
                    }
                    RoomPriceModel roomPriceModel = snapShotRoot.child("RoomPriceType").child(roomPriceId).getValue(RoomPriceModel.class);
                    roomPriceModel.setRoomPriceID(roomPriceId);
                    roomPriceModel.setPrice(price);

                    tempRoomPriceList.add(roomPriceModel);
                }

                roomModel.setListRoomPrice(tempRoomPriceList);

                //End Thêm danh sách giá của phòng trọ

                //Thêm thông tin chủ sở hữu cho phòng trọ
                UserModel tempUser = snapShotRoot.child("Users").child(roomModel.getOwner()).getValue(UserModel.class);
                roomModel.setRoomOwner(tempUser);
                //End thêm thông tin chủ sở hữu cho phòng trọ

                //Thêm vào lượt xem của phòng trọ
                int tempViews = (int) snapShotRoot.child("RoomViews").child(RoomID).getChildrenCount();
                roomModel.setViews(tempViews);
                //End thêm vào lượt xem của phòng trọ


                if (currentRoomID.equals(roomModel.getRoomID())) {
                    // không làm gì cả
                } else if (roomModel.isApprove()) {
                    listSearchRoomsModel.add(roomModel);
                }
            } else {
            }
        } else {
        }
        //End lọc dữ liệu
    }

    //Sort lại theo ngày đăng, đăng sớm nhất lên đầu
    public void sortListSearchRoom(List<RoomModel> ListRooms) {
        Collections.sort(ListRooms, new Comparator<RoomModel>() {
            @Override
            public int compare(RoomModel roomModel1, RoomModel roomMode2) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = df.parse(roomModel1.getTimeCreated());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date2 = null;
                try {
                    date2 = df.parse(roomMode2.getTimeCreated());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date2.compareTo(date1);
            }
        });
    }
}
