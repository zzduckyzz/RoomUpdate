package com.example.designapptest.model;

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.designapptest.controller.Interfaces.IViewModel;
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

public class ViewModel {
    String time;
    UserModel userView;
    String userID;

    //Ảnh nén
    private RequestCreator compressionImageFit;

    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    public RequestCreator getCompressionImageFit() {
        return compressionImageFit;
    }

    public void setCompressionImageFit(RequestCreator compressionImageFit) {
        this.compressionImageFit = compressionImageFit;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserModel getUserView() {
        return userView;
    }

    public void setUserView(UserModel userView) {
        this.userView = userView;
    }


    private DataSnapshot dataRoot;
    private DataSnapshot dataNode;
    private List<ViewModel> listViewModel = new ArrayList<>();

    public ViewModel() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }


    public void getListRoomComments(IViewModel viewModelInterface, String roomID,
                                    int quantityViewToLoad, int quantityViewLoaded) {
        // Tạo listen cho nodeRoomComments
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListCommentEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        for (DataSnapshot viewSnapshot : dataNode.getChildren()) {
                            ViewModel viewModel = new ViewModel();
                            viewModel.setTime(viewSnapshot.getValue(String.class));
                            viewModel.setUserID(viewSnapshot.getKey());

                            // Lọc ra danh sách comments id.
                            listViewModel.add(viewModel);
                        }

                        sortListViews(listViewModel);

                        // set view
                        viewModelInterface.setLinearLayoutTopAllView(listViewModel);
                        viewModelInterface.setQuantityViews(listViewModel.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartListView(dataRoot, listViewModel, viewModelInterface,
                                quantityViewToLoad, quantityViewLoaded);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                //Gán sự kiện listen cho nodeRoot
                nodeRoot.addListenerForSingleValueEvent(valueSpecialListCommentEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        if (dataNode != null) {
            if (dataRoot != null) {
                //Thêm dữ liệu và gửi về lại UI
                getPartListView(dataRoot, listViewModel, viewModelInterface,
                        quantityViewToLoad, quantityViewLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomComments
            nodeRoot.child("RoomViews").child(roomID).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    //Lấy ra một phần của lượt view
    private void getPartListView(DataSnapshot dataSnapshot, List<ViewModel> listviewsModel,
                                 IViewModel viewModelInterface, int quantityViewToLoad,
                                 int quantityViewLoaded) {
        int i = 0;

        // Chạy từ cuối list đến đầu list (list truyền vào đã sắp xếp theo thời gian)
        for (ViewModel viewModel : listviewsModel) {

            // Nếu đã lấy đủ số lượng comments tiếp theo thì ra khỏi vòng lặp
            if (i == quantityViewToLoad) {
                break;
            }

            // Bỏ qua những comments đã load
            if (i < quantityViewLoaded) {
                i++;
                continue;
            }

            i++;

            //Duyệt user tương ứng để lấy ra thông tin user bình luận
            UserModel tempUser = dataSnapshot.child("Users").child(viewModel.getUserID()).getValue(UserModel.class);
            viewModel.setUserView(tempUser);
            //End duyệt user tương ứng để lấy ra thông tin user bình luận
            Log.d("check3", tempUser.getName());
            //Kích hoạt interface
            viewModelInterface.getViewInfo(viewModel);
        }
        //End Thêm danh sách bình luận của phòng trọ

        // Ẩn progress bar load more.
        viewModelInterface.setProgressBarLoadMore();
    }

    //Sắp xếp lish theo thời gian
    public void sortListViews(List<ViewModel> listComments) {
        Collections.sort(listComments, new Comparator<ViewModel>() {
            @Override
            public int compare(ViewModel viewModel1, ViewModel viewModel2) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = df.parse(viewModel1.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date2 = null;
                try {
                    date2 = df.parse(viewModel2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date2.compareTo(date1);
            }
        });
    }
}
