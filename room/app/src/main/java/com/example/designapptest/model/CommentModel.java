package com.example.designapptest.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.example.designapptest.controller.Interfaces.IRoomCommentModel;
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

public class CommentModel implements Parcelable { // Linh thêm

    String title;
    String content;
    String time;
    String user;
    long likes, stars;
    String commentID;

    // Ảnh nén
    private RequestCreator compressionImageFit;

    //Chủ bình luận
    UserModel userComment;

    protected CommentModel(Parcel in) {
        title = in.readString();
        content = in.readString();
        time = in.readString();
        user = in.readString();
        likes = in.readLong();
        stars = in.readLong();
        commentID = in.readString();
        userComment = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public RequestCreator getCompressionImageFit() {
        return compressionImageFit;
    }

    public void setCompressionImageFit(RequestCreator compressionImageFit) {
        this.compressionImageFit = compressionImageFit;
    }

    public UserModel getUserComment() {
        return userComment;
    }

    public void setUserComment(UserModel userComment) {
        this.userComment = userComment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getStars() {
        return stars;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }

    //Biến lưu root của firebase, lưu ý để biến là private
    private DatabaseReference nodeRoot;

    public CommentModel() {
        //Trả về node gốc của database
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(time);
        dest.writeString(user);
        dest.writeLong(likes);
        dest.writeLong(stars);
        dest.writeString(commentID);
        dest.writeParcelable(userComment, flags);
    }

    private DataSnapshot dataRoot;
    private DataSnapshot dataNode;
    private List<CommentModel> listCommentsModel = new ArrayList<>();

    public void getListRoomComments(IRoomCommentModel roomCommentModelInterface, RoomModel roomModel,
                                    int quantityCommentToLoad, int quantityCommentLoaded) {
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

                        for (DataSnapshot roomCommentsSnapShot : dataNode.getChildren()) {
                            CommentModel commentModel = roomCommentsSnapShot.getValue(CommentModel.class);
                            commentModel.setCommentID(roomCommentsSnapShot.getKey());

                            // Lọc ra danh sách comments id.
                            listCommentsModel.add(commentModel);
                        }

                        sortListComments(listCommentsModel);

                        // set view
                        roomCommentModelInterface.setLinearLayoutTopAllComments(listCommentsModel);
                        roomCommentModelInterface.setQuantityComments(listCommentsModel.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartListComments(dataRoot, listCommentsModel, roomCommentModelInterface,
                                quantityCommentToLoad, quantityCommentLoaded);
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
                getPartListComments(dataRoot, listCommentsModel, roomCommentModelInterface,
                        quantityCommentToLoad, quantityCommentLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomComments
            nodeRoot.child("RoomComments").child(roomModel.getRoomID()).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    public void getListMyRoomComments(IRoomCommentModel roomCommentModelInterface, RoomModel roomModel,
                                      String UID, int quantityCommentToLoad, int quantityCommentLoaded) {
        // Tạo listen cho nodeRoomComments.
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataNode = dataSnapshot;

                // Tạo listen cho nodeRoot.
                ValueEventListener valueSpecialListCommentEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataRoot = dataSnapshot;

                        for (DataSnapshot roomCommentsSnapShot : dataNode.getChildren()) {
                            CommentModel commentModel = roomCommentsSnapShot.getValue(CommentModel.class);
                            commentModel.setCommentID(roomCommentsSnapShot.getKey());

                            if (UID.equals(commentModel.getUser())) {
                                //Lọc ra danh sách my comments.
                                listCommentsModel.add(commentModel);
                            }
                        }

                        sortListComments(listCommentsModel);

                        // set view
                        roomCommentModelInterface.setLinearLayoutTopAllComments(listCommentsModel);
                        roomCommentModelInterface.setQuantityComments(listCommentsModel.size());

                        //Thêm dữ liệu và gửi về lại UI
                        getPartListComments(dataRoot, listCommentsModel, roomCommentModelInterface,
                                quantityCommentToLoad, quantityCommentLoaded);
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
                getPartListComments(dataRoot, listCommentsModel, roomCommentModelInterface,
                        quantityCommentToLoad, quantityCommentLoaded);
            }
        } else {
            //Gán sự kiện listen cho nodeRoomComments
            nodeRoot.child("RoomComments").child(roomModel.getRoomID()).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void getPartListComments(DataSnapshot dataSnapshot, List<CommentModel> listCommentsModel,
                                     IRoomCommentModel roomCommentModelInterface, int quantityCommentToLoad,
                                     int quantityCommentLoaded) {
        int i = 0;

        // Chạy từ cuối list đến đầu list (list truyền vào đã sắp xếp theo thời gian)
        for (CommentModel commentModel : listCommentsModel) {

            // Nếu đã lấy đủ số lượng comments tiếp theo thì ra khỏi vòng lặp
            if (i == quantityCommentToLoad) {
                break;
            }

            // Bỏ qua những comments đã load
            if (i < quantityCommentLoaded) {
                i++;
                continue;
            }

            i++;

            //Duyệt user tương ứng để lấy ra thông tin user bình luận
            UserModel tempUser = dataSnapshot.child("Users").child(commentModel.getUser()).getValue(UserModel.class);
            commentModel.setUserComment(tempUser);
            //End duyệt user tương ứng để lấy ra thông tin user bình luận

            //Kích hoạt interface
            roomCommentModelInterface.getListRoomComments(commentModel);
        }
        //End Thêm danh sách bình luận của phòng trọ

        // Ẩn progress bar load more.
        roomCommentModelInterface.setProgressBarLoadMore();
    }

    public void addComment(CommentModel newCommentModel, String roomId, IRoomCommentModel iRoomCommentModel) {
        DatabaseReference nodeComment = FirebaseDatabase.getInstance().getReference().child("RoomComments");
        String commentId = nodeComment.child(roomId).push().getKey();

        nodeComment.child(roomId).child(commentId).setValue(newCommentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iRoomCommentModel.makeToast("Đăng bình luận thành công");
                    iRoomCommentModel.setView();
                }
            }
        });
    }

    public void sortListComments(List<CommentModel> listComments) {
        Collections.sort(listComments, new Comparator<CommentModel>() {
            @Override
            public int compare(CommentModel commentModel1, CommentModel commentModel2) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                try {
                    date1 = df.parse(commentModel1.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date2 = null;
                try {
                    date2 = df.parse(commentModel2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date2.compareTo(date1);
            }
        });
    }
}
