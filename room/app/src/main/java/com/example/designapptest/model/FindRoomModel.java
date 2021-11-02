package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.IFindRoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.RequestCreator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FindRoomModel implements Parcelable {
    String user;
    String time;
    double maxPrice, minPrice;
    boolean gender;

    //Mã tìm ở ghép
    String findRoomID;

    //Chủ tìm ở ghép
    UserModel findRoomOwner;

    // Lưu danh sách tiện nghi phòng trọ
    List<ConvenientModel> listConvenientRoom;

    // Lưu danh sách các tiện nghi của phòng trọ.
    private List<String> convenients;

    // Lưu danh sách các id của vị trí phòng trọ.
    private List<String> location;

    // Ảnh nén
    private RequestCreator compressionImageFit;

    protected FindRoomModel(Parcel in) {
        user = in.readString();
        time = in.readString();
        maxPrice = in.readDouble();
        minPrice = in.readDouble();
        gender = in.readByte() != 0;
        findRoomID = in.readString();
        findRoomOwner = in.readParcelable(UserModel.class.getClassLoader());
        listConvenientRoom = in.createTypedArrayList(ConvenientModel.CREATOR);
        convenients = in.createStringArrayList();
        location = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(time);
        dest.writeDouble(maxPrice);
        dest.writeDouble(minPrice);
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(findRoomID);
        dest.writeParcelable(findRoomOwner, flags);
        dest.writeTypedList(listConvenientRoom);
        dest.writeStringList(convenients);
        dest.writeStringList(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FindRoomModel> CREATOR = new Creator<FindRoomModel>() {
        @Override
        public FindRoomModel createFromParcel(Parcel in) {
            return new FindRoomModel(in);
        }

        @Override
        public FindRoomModel[] newArray(int size) {
            return new FindRoomModel[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTimee(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public List<String> getConvenients() {
        return convenients;
    }

    public void setConvenients(List<String> convenients) {
        this.convenients = convenients;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public String getFindRoomID() {
        return findRoomID;
    }

    public void setFindRoomID(String findRoomID) {
        this.findRoomID = findRoomID;
    }

    public UserModel getFindRoomOwner() {
        return findRoomOwner;
    }

    public void setFindRoomOwner(UserModel findRoomOwner) {
        this.findRoomOwner = findRoomOwner;
    }

    public List<ConvenientModel> getListConvenientRoom() {
        return listConvenientRoom;
    }

    public void setListConvenientRoom(List<ConvenientModel> listConvenientRoom) {
        this.listConvenientRoom = listConvenientRoom;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {

        this.minPrice = minPrice;
    }

    public double getMaxPrice() {

        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {

        this.maxPrice = maxPrice;
    }

    public RequestCreator getCompressionImageFit() {
        return compressionImageFit;
    }

    public void setCompressionImageFit(RequestCreator compressionImageFit) {
        this.compressionImageFit = compressionImageFit;
    }

    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    //Lưu ý phải có hàm khởi tạo rỗng
    public FindRoomModel() {
        //Trả về node root của database
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    // Hàm khởi tạo có tham số đầy đủ
    public FindRoomModel(String user, String time, double minPrice, double maxPrice, boolean gender,
                         String findRoomID, List<String> convenients, List<String> location) {
        this.user = user;
        this.time = time;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.gender = gender;
        this.findRoomID = findRoomID;
        this.convenients = convenients;
        this.location = location;
    }

    private DataSnapshot dataRoot;
    private List<FindRoomModel> listFindRoomModels = new ArrayList<>();
    List<FindRoomModel> tempListFindRoomModels = new ArrayList<>();

    public void ListFindRoom(IFindRoomModel findRoomModelInterface, FindRoomFilterModel findRoomFilterModel, String UID,
                             int quantityToLoad, int quantityLoaded) {

        //Tạo listen cho firebase
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;

                //Duyệt vào node Room trên firebase
                DataSnapshot dataSnapshotRoom = dataSnapshot.child("FindRoom");

                //Duyệt hết trong danh sách tìm ở ghép.
                for (DataSnapshot valueFindRoom : dataSnapshotRoom.getChildren()) {
                    FindRoomModel findRoomModel = valueFindRoom.getValue(FindRoomModel.class);
                    findRoomModel.setFindRoomID(valueFindRoom.getKey());

                    // Chỉ xử lí nếu khác null
                    if (findRoomModel.getConvenients() != null) {
                        //Thêm danh sách tiện nghi của phòng trọ dựa vào danh sách id đã có ở findRoomModel
                        List<ConvenientModel> tempConvenientList = new ArrayList<ConvenientModel>();
                        //Duyệt tất cả các giá trị trong node tương ứng
                        int index;
                        int size = findRoomModel.getConvenients().size();

                        for (index = 0; index < size; index++) {
                            String convenientId = findRoomModel.getConvenients().get(index);
                            if (convenientId != null) {
                                ConvenientModel convenientModel = dataSnapshot.child("Convenients").child(convenientId).getValue(ConvenientModel.class);
                                convenientModel.setConvenientID(convenientId);

                                tempConvenientList.add(convenientModel);
                            }
                        }

                        findRoomModel.setListConvenientRoom(tempConvenientList);
                    }
                    //End Thêm danh sách tiện nghi của phòng trọ

                    //Thêm thông tin chủ sở hữu cho phòng trọ
                    UserModel tempUser = dataSnapshot.child("Users").child(findRoomModel.getUser()).getValue(UserModel.class);
                    tempUser.setUserID(findRoomModel.getUser());
                    findRoomModel.setFindRoomOwner(tempUser);

                    //End thêm thông tin chủ sở hữu cho phòng trọ

                    listFindRoomModels.add(findRoomModel);
                }

                sortListFindRoom(listFindRoomModels);

                // Nếu đang filter
                if (findRoomFilterModel != null) {
                    tempListFindRoomModels.clear();

                    for (FindRoomModel findRoomModel : listFindRoomModels) {
                        if (filterListFindRoomFilter(findRoomFilterModel, findRoomModel) == true) {
                            tempListFindRoomModels.add(findRoomModel);
                        }
                    }

                    // thông báo số lượng
                    findRoomModelInterface.getSuccessNotify(tempListFindRoomModels.size());

                    getPartListFindRoom(tempListFindRoomModels, findRoomModelInterface, quantityToLoad, quantityLoaded);
                } else if (!UID.equals("")) {
                    for (FindRoomModel findRoomModel : listFindRoomModels) {
                        // Nếu bộ lọc thỏa mãn thì thêm vào recycle
                        if (findRoomModel.getFindRoomOwner().getUserID().equals(UID)) {
                            tempListFindRoomModels.add(findRoomModel);
                        }
                    }

                    // thông báo số lượng
                    findRoomModelInterface.getSuccessNotify(tempListFindRoomModels.size());

                    getPartListFindRoom(tempListFindRoomModels, findRoomModelInterface, quantityToLoad, quantityLoaded);
                } else {
                    // thông báo số lượng
                    findRoomModelInterface.getSuccessNotify(listFindRoomModels.size());

                    getPartListFindRoom(listFindRoomModels, findRoomModelInterface, quantityToLoad, quantityLoaded);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (dataRoot != null) {
            // Nếu đang filter
            if (findRoomFilterModel != null) {
                getPartListFindRoom(tempListFindRoomModels, findRoomModelInterface, quantityToLoad, quantityLoaded);
            } else if (!UID.equals("")) {
                getPartListFindRoom(tempListFindRoomModels, findRoomModelInterface, quantityToLoad, quantityLoaded);
            } else {
                getPartListFindRoom(listFindRoomModels, findRoomModelInterface, quantityToLoad, quantityLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoot
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public boolean filterListFindRoomFilter(FindRoomFilterModel findRoomFilterModel, FindRoomModel findRoomModel) {
        boolean flagCondition = true;

        // Xem thử người dùng có đặt điều kiện cho tiện nghi không.
        if (findRoomFilterModel.getLstConvenients().size() > 0) {
            // Nếu trong vị trí cần tìm không khớp thì bỏ qua giá trí này.
            if (!checkTwoList(findRoomFilterModel.getLstConvenients(), findRoomModel.getConvenients()))
                flagCondition = false;
        }

        // Xem thử người dùng có đặt điều kiện cho vị trí cần tìm không.
        if (findRoomFilterModel.getLstLocationSearchs().size() > 0) {
            // Nếu trong vị trí cần tìm không khớp thì bỏ qua giá trí này.
            if (!checkTwoList(findRoomFilterModel.getLstLocationSearchs(), findRoomModel.getLocation()))
                flagCondition = false;
        }

        // Xem thử người dùng có đặt điều kiện cho giới tính không.
        if (findRoomFilterModel.getGender() != 2) {
            // Cần tìm nam là nam mà ra nữ hoặc cần tìm nữa hoặc ra nam.
            int filterGender = findRoomFilterModel.getGender(); // Giới tính cần tìm
            boolean valueGender = findRoomModel.getFindRoomOwner().isGender(); // Giới tính của data
            if (filterGender == 0 && valueGender == false
                    || filterGender == 1 && valueGender == true)
                flagCondition = false;
        }

        // Kiểm tra điều kiện khoản giá.
        // Khoảng giá nhỏ nhất cần tìm lớn hớn khoản giá nhỏ nhất hoặc
        // Khoảng lớn lớn nhất cần tìm nhỏ hớn khoản giá lớn nhất
        double minFilter = findRoomFilterModel.getMinPrice();
        double minValue = findRoomModel.getMinPrice();

        double maxFilter = findRoomFilterModel.getMaxPrice();
        double maxValue = findRoomModel.getMaxPrice();

        if (minFilter > minValue || maxFilter < maxValue)
            flagCondition = false;

        return flagCondition;
    }

    private void getPartListFindRoom(List<FindRoomModel> listFindRoomModels, IFindRoomModel findRoomModelInterface, int quantityToLoad, int quantityLoaded) {
        int i = 0;

        //Duyệt hết trong danh sách tìm ở ghép.
        for (FindRoomModel findRoomModel : listFindRoomModels) {

            // Nếu đã lấy đủ số lượng comments tiếp theo thì ra khỏi vòng lặp
            if (i == quantityToLoad) {
                break;
            }

            // Bỏ qua những comments đã load
            if (i < quantityLoaded) {
                i++;
                continue;
            }

            i++;

            //Kích hoạt interface
            findRoomModelInterface.getListFindRoom(findRoomModel);
        }

        findRoomModelInterface.setProgressBarLoadMore();
    }

    public void addFindRoom(final FindRoomModel findRoomModel, final IFindRoomModel findRoomModelInterface) {
        // Tại sao ở đây nếu dùng notRoot thì nó bằng null
        DatabaseReference nodeFindRoom = FirebaseDatabase.getInstance().getReference().child("FindRoom");

        //Lấy Key push động vào firebase
        String findRoomID = nodeFindRoom.push().getKey();

        //push vào node room
        nodeFindRoom.child(findRoomID).setValue(findRoomModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                findRoomModelInterface.addSuccessNotify();
            }
        });
    }

    public void sortListFindRoom(List<FindRoomModel> listFindRooms) {
        Collections.sort(listFindRooms, new Comparator<FindRoomModel>() {
            @Override
            public int compare(FindRoomModel findRoomModel1, FindRoomModel findRoomModel2) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = df.parse(findRoomModel1.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date2 = null;
                try {
                    date2 = df.parse(findRoomModel2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date2.compareTo(date1);
            }
        });
    }

    // Kiểm tra xem hai lst có phần tử nào giống nhau ko
    private boolean checkTwoList(List<String> lst1, List<String> lst2) {
        int i, j;

        // Nếu lst data không có giá trị
        if (lst2 == null) {
            return false;
        }
        // Nếu hai list có số lượng phần từ bằng nhau;
        else if (lst1.size() == lst2.size()) {
            lst1 = new ArrayList<String>(lst1);
            lst2 = new ArrayList<String>(lst2);

            return lst1.equals(lst2);
        } else if (lst1.size() > lst2.size()) {
            // Nếu lst cần tìm có số lượng phần từ lớn hơn.

            return false;
        } else {
            // Nếu lst cần tìm có số lượng phần từ lớn hơn.
            for (i = 0; i < lst1.size(); i++) {
                for (j = 0; j < lst2.size(); j++) {
                    if (lst1.get(i).equals(lst2.get(j))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
