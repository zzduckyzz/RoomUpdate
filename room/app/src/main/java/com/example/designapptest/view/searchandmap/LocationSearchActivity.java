package com.example.designapptest.view.searchandmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.controller.locationSearchController;
import com.example.designapptest.R;

public class LocationSearchActivity extends AppCompatActivity implements TextWatcher {

    EditText edTSearch;
    LinearLayout LinearContainSuggestions;
    RecyclerView recyclerSuggestion,recyclerHistorySearch;
    TextView txtFinish;
    Button btnClear;

    String stringFilter;

    locationSearchController SearchController;
    boolean isSearchViewCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        //Khởi tạo control
        initControl();
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getIntExtra(SearchViewActivity.REQUEST,0) == SearchViewActivity.REQUEST_DISTRICT){
                isSearchViewCall=true;
                Log.d("check3", "onCreate: ");
            }
            else {
                isSearchViewCall=false;
                Log.d("check3", "onCreate2: ");
            }
        }
        SearchController = new locationSearchController(this);


    }

    private void initControl(){
        edTSearch = findViewById(R.id.edT_search);
        edTSearch.addTextChangedListener(this);

        LinearContainSuggestions = findViewById(R.id.Linear_contain_suggestions);
        recyclerSuggestion = findViewById(R.id.recycler_suggestion);
        recyclerHistorySearch = findViewById(R.id.recycler_history_search);
        txtFinish = findViewById(R.id.txt_finish);
        txtFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edTSearch.setText("");
            }
        });
    }

    private void getDataFromControl(){
        //Lấy chuỗi từ user
        stringFilter  = edTSearch.getText().toString();

        //Chuẩn hóa chuỗi

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //Lấy chuỗi từ user và chuẩn hóa
        getDataFromControl();

        //Gọi controller
        SearchController.loadDistrictInData(recyclerSuggestion,stringFilter,isSearchViewCall);
    }
}
