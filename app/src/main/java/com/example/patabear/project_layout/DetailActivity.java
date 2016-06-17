package com.example.patabear.project_layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class DetailActivity extends Activity implements OnClickListener{

    Intent intent;

    int ID, option;
    String date;
    String price;
    String storeName;
    String category;
    String memo;
    double gridX;
    double gridY;

    TextView detailDate;
    TextView detailPrice;
    TextView detailStore;
    TextView detailCategory;
    TextView detailMemo;

    private Button  buttonConfirm, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        initiate();
        setText();

    }

    private void setText() {
        if(date.length()!=0)detailDate.setText(date);
        if(price.length()!=0)detailPrice.setText(price);
        if(storeName.length()!=0)detailStore.setText(storeName);
        if(category.length()!=0)detailCategory.setText(category);
        if(memo.length()!=0)detailMemo.setText(memo);
    }

    private void initiate() {
        intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        date = intent.getStringExtra("date");
        price = intent.getStringExtra("price");
        storeName = intent.getStringExtra("storeName");
        category = intent.getStringExtra("category");
        memo = intent.getStringExtra("memo");
        gridX = intent.getDoubleExtra("gridX", 0.000000);
        gridY = intent.getDoubleExtra("gridY", 0.000000);
        option = intent.getIntExtra("option", 0);
        detailDate = (TextView) findViewById(R.id.detail_editTextDate);
        detailPrice = (TextView) findViewById(R.id.detail_editTextPrice);
        detailStore = (TextView) findViewById(R.id.detail_editTextStore);
        detailCategory = (TextView) findViewById(R.id.detail_editTextCategory);
        detailMemo = (TextView) findViewById(R.id.detail_editTextMemo);

        buttonConfirm = (Button) findViewById(R.id.confirmButton);
        buttonDelete = (Button) findViewById(R.id.deleteButton);

        buttonConfirm.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
    }/*
    Button.OnClickListener mlistener = new View.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.confirmButton:
                    DetailActivity.this.finish();
                    break;
                case R.id.deleteButton:
                    DetailActivity.this.finish();
                    break;
            }

        }
    };*/

    @Override
    public void onClick(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.confirmButton:
                date = detailDate.getText().toString();
                price = detailPrice.getText().toString();
                storeName = detailStore.getText().toString();
                category = detailCategory.getText().toString();
                memo = detailMemo.getText().toString();
                intent.putExtra("From", 1);
                intent.putExtra("Del", 0);
                intent.putExtra("ID", ID);
                intent.putExtra("date", date);
                intent.putExtra("price", price);
                intent.putExtra("storeName", storeName);
                intent.putExtra("category", category);
                intent.putExtra("memo", memo);
                intent.putExtra("gridX", gridX);
                intent.putExtra("gridY", gridY);
                intent.putExtra("option", option);
                setResult(RESULT_OK, intent);
                this.finish();
                break;
            case R.id.deleteButton:
                intent.putExtra("From", 1);
                intent.putExtra("Del", 1);
                intent.putExtra("ID", ID);
                intent.putExtra("option", option);
                setResult(RESULT_OK, intent);
                this.finish();
                break;
            default:
                break;
        }
    }
}