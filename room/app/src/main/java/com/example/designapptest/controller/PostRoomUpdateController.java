package com.example.designapptest.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.designapptest.controller.Interfaces.IPostRoomUpdateModel;
import com.example.designapptest.controller.Interfaces.IUpdateRoomModel;
import com.example.designapptest.model.RoomModel;

import java.util.List;

public class PostRoomUpdateController {
    Context context;
    RoomModel roomModel;

    public PostRoomUpdateController(Context context) {
        this.context = context;
        roomModel = new RoomModel();
    }

    public void postRoomStep1Update(String roomID, String city, String district, String ward, String street, String no,
                                    String oldCity, String oldDistrict, String oldWard, String oldStreet, String oldNo, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {
                Toast.makeText(context, "Step 1 here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
                Toast.makeText(context, "Step 2 here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
                Toast.makeText(context, "Step 3 here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {
                Toast.makeText(context, "Step 4 here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }
        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep1Update(roomID, city, district, ward, street, no, iPostRoomUpdateModel,
                oldCity, oldDistrict, oldWard, oldStreet, oldNo);
    }

    public void postRoomStep2Update(String roomId, String typeId, boolean genderRoom, long currentNumber, long maxNumber, float width, float length,
                                    float priceRoom, float electricBill, float warterBill, float InternetBill, float parkingBill, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {

            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
                Toast.makeText(context, "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();

//                postRoomAdapterUpdate.setOnPostRoomStep2Update( typeId,  genderRoom,  currentNumber,
//                 maxNumber,  width,  length,  priceRoom,
//                 electricBill,  warterBill,  InternetBill,  parkingBill);

            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {
            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }

        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep2Update(roomId, typeId, genderRoom, maxNumber, width, length,
                priceRoom, electricBill, warterBill, InternetBill, parkingBill, iPostRoomUpdateModel);
    }

    public void postRoomStep3Update(String roomId, String owner, List<String> listConvenient, List<String> listPathImageChoosed,
                                    boolean isChangeConvenient, boolean isChangeImageRoom, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
                Toast.makeText(context, "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {

            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }

        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep3Update(roomId, owner, listConvenient, listPathImageChoosed,
                isChangeConvenient, isChangeImageRoom, iPostRoomUpdateModel, context);
    }

    public void postRoomStep4Update(String roomId, String name, String describe, IUpdateRoomModel iUpdateRoomModel) {

        //Tạo interface để truyền dữ liệu lên từ model
        IPostRoomUpdateModel iPostRoomUpdateModel = new IPostRoomUpdateModel() {
            @Override
            public void getSuccessNotifyPostRoomStep1() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep2() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep3() {
            }

            @Override
            public void getSuccessNotifyPostRoomStep4() {
                Toast.makeText(context, "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getRoomFollowId(RoomModel roomModel) {
                iUpdateRoomModel.getSuccessNotifyRoomMode1(roomModel);
            }
        };

        //Gọi hàm lấy dữ liệu trong model
        roomModel.postRoomStep4Update(roomId, name, describe, iPostRoomUpdateModel);
    }
}
