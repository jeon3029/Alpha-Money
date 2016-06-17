package com.example.patabear.project_layout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Add_dataActivity_notuse extends ActionBarActivity {

    TextView editTextDate;
    TextView editTextPrice;
    TextView editTextStore;
    TextView editTextCategory;
    TextView editTextMemo;
    Intent intentadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data);

        editTextDate = (TextView) findViewById(R.id.editTextDate);
        editTextPrice = (TextView) findViewById(R.id.editTextPrice);
        editTextStore = (TextView) findViewById(R.id.editTextStore);
        editTextCategory = (TextView) findViewById(R.id.editTextCategory);
        editTextMemo = (TextView) findViewById(R.id.editTextMemo);
    }


    public void savedata(View v) {
        //mydb.onInsertdata(editTextDate.toString(), Integer.parseInt(editTextPrice.toString()), editTextStore.toString(), editTextCategory.toString(), editTextMemo.toString(), "37.125431", "127.125462");
        intentadd = new Intent(getApplicationContext(), MainActivity.class);
        intentadd.putExtra("date", editTextDate.toString());
        startActivity(intentadd);
        //Cursor cursor = mydb.onSelectdata("1");
        //println(cursor.getString(3));

        finish();
    }



}
