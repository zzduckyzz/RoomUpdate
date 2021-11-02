package com.example.designapptest.view.room;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.R;
import com.example.designapptest.adapter.AdapterRecyclerConvenient;
import com.example.designapptest.adapter.AdapterRecyclerFindRoom;
import com.example.designapptest.domain.classFunctionStatic;
import com.example.designapptest.model.FindRoomModel;
import com.squareup.picasso.Picasso;

public class FindRoomDetailActivity extends AppCompatActivity {
    TextView txtNameUser, txtAboutPrice, txtWantGender, txtExpandConvenients;

    Button btnCallPhone;

    ImageView imgGenderUser, imgAvatarUser;
    GridView grVLocationSearch;

    LinearLayout lnLtExpandConvenients;

    // Các recycler.
    RecyclerView recyclerConvenientsFindRoomDetail;
    AdapterRecyclerConvenient adapterRecyclerConvenient;

    ProgressBar progressBarFindRoomDetail;

    FindRoomModel findRoomModel;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room_detail_view);

        getShareInfo();

        initControl();

        // loadProgress();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();
        clickCallPhone();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void loadProgress() {
        classFunctionStatic.showProgress(this, imgAvatarUser);
    }

    private void getShareInfo() {
        findRoomModel = getIntent().getParcelableExtra(AdapterRecyclerFindRoom.SHARE_FINDROOM);
    }

    // Khởi tạo các control trong room detail.
    private void initControl() {
        toolbar = findViewById(R.id.toolbar);

        txtNameUser = (TextView) findViewById(R.id.txt_name_user_find_room_detail);
        txtAboutPrice = (TextView) findViewById(R.id.txt_abouPrice_find_room_detail);
        txtExpandConvenients = (TextView) findViewById(R.id.txt_expand_convenients_find_room_detail);

        lnLtExpandConvenients = (LinearLayout) findViewById(R.id.lnLt_expand_convenients_find_room_detail);

        grVLocationSearch = (GridView) findViewById(R.id.grV_locationSearch_find_room_detail);

        txtWantGender = (TextView) findViewById(R.id.txt_wantGender_find_room_detail);

        btnCallPhone = (Button) findViewById(R.id.btn_callPhone_find_room_detail);

        imgGenderUser = (ImageView) findViewById(R.id.img_gender_user_find_room_detail);
        imgAvatarUser = (ImageView) findViewById(R.id.img_avatar_user_find_room_detail);

        recyclerConvenientsFindRoomDetail = (RecyclerView) findViewById(R.id.recycler_convenients_find_room_detail);
    }

    // Khởi tạo các giá trị cho các control.
    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Chi tiết tìm ở ghép");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Gán các giá trị vào giao diện
        // Gán tên cho người dùng
        txtNameUser.setText(findRoomModel.getFindRoomOwner().getName());

        // Gán giá trị cho khoảng giá cần tìm kiếm.
        txtAboutPrice.setText(String.valueOf(findRoomModel.getMinPrice())
                + " - " + String.valueOf(findRoomModel.getMaxPrice()));

        // Chỉ xử lý khi khác null
        if (findRoomModel.getLocation() != null) {
            // gán giá trị cho vị trí tìm kiếm.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.grid_element_find_room_detail, findRoomModel.getLocation());
            grVLocationSearch.setAdapter(adapter);
            // End gán giá trị cho vị trí tìm kiếm
        }

        //Gán hình cho giới tính cần tìm
        if (findRoomModel.isGender()) {
            txtWantGender.setText("Nam");
        } else {
            txtWantGender.setText("Nữ");
        }
        //End Gán hình cho giới tính cần tìm

        //Gán hình cho giới tính cuả người tìm ở ghép
        if (findRoomModel.getFindRoomOwner().isGender()) {
            imgGenderUser.setImageResource(R.drawable.ic_svg_male_100);
        } else {
            imgGenderUser.setImageResource(R.drawable.ic_svg_female_100);
        }
        //End Gán hình cho giới tính cuả người tìm ở ghép


        if (findRoomModel.getListConvenientRoom() != null) {
            expandRoomConvenients();

            // Load danh sách tiện nghi của phòng trọ
            RecyclerView.LayoutManager layoutManagerConvenient = new GridLayoutManager(this, 3);
            recyclerConvenientsFindRoomDetail.setLayoutManager(layoutManagerConvenient);
            adapterRecyclerConvenient = new AdapterRecyclerConvenient(this, getApplicationContext(),
                    R.layout.utility_element_grid_room_detail_view, findRoomModel.getListConvenientRoom());
            recyclerConvenientsFindRoomDetail.setAdapter(adapterRecyclerConvenient);
            adapterRecyclerConvenient.notifyDataSetChanged();
        }

        // Hiển thị hình ảnh người dùng.
        Picasso.get().load(findRoomModel.getFindRoomOwner().getAvatar()).into(imgAvatarUser);

        //progressBarFindRoomDetail.setVisibility(View.GONE);

    }

    // Hàm gọi điện thoại cho chủ phòng trọ.
    private void clickCallPhone() {
        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPhoneNumbet = findRoomModel.getFindRoomOwner().getPhoneNumber();
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strPhoneNumbet, null));
                startActivity(intentCall);
            }
        });
    }

    private void expandRoomConvenients() {
        int convenientRoomSize = findRoomModel.getListConvenientRoom().size();
        int rowConvenientHeight = 203;
        int fullRowConvenientHeight = (convenientRoomSize % 3 == 0) ?
                (convenientRoomSize / 3) * rowConvenientHeight : (convenientRoomSize / 3) * rowConvenientHeight + rowConvenientHeight;

        txtExpandConvenients.setText(R.string.stringExpand);

        if (convenientRoomSize > 3) {
            resizeRecyclerConvenientsFindRoomDetailAnimation(recyclerConvenientsFindRoomDetail.getHeight(), rowConvenientHeight);
        } else {
            lnLtExpandConvenients.setVisibility(View.GONE);
        }

        lnLtExpandConvenients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtExpandConvenients.getText().toString().equals(getString(R.string.stringCollapse))) {
                    resizeRecyclerConvenientsFindRoomDetailAnimation(recyclerConvenientsFindRoomDetail.getHeight(), rowConvenientHeight);
                    txtExpandConvenients.setText(R.string.stringExpand);
                } else {
                    resizeRecyclerConvenientsFindRoomDetailAnimation(recyclerConvenientsFindRoomDetail.getHeight(), fullRowConvenientHeight);
                    txtExpandConvenients.setText(R.string.stringCollapse);
                }
            }
        });
    }

    private void resizeRecyclerConvenientsFindRoomDetailAnimation(int fromHeight, int toHeight) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = recyclerConvenientsFindRoomDetail.getLayoutParams();
                layoutParams.height = val;

                recyclerConvenientsFindRoomDetail.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }
}
