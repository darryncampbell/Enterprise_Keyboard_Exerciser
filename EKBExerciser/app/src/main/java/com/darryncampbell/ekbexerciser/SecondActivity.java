package com.darryncampbell.ekbexerciser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnSetLayoutDW001 = findViewById(R.id.btnSetLayoutDW001);
        btnSetLayoutDW001.setOnClickListener(this);
        Button btnSetLayoutDW002 = findViewById(R.id.btnSetLayoutDW002);
        btnSetLayoutDW002.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetLayoutDW001:
                DataWedgeUtilities.SetProfileConfig( MainActivity.LAYOUT_GROUP_NAME,
                        MainActivity.LAYOUT_DATAWEDGE_001, getApplicationContext());
                break;
            case R.id.btnSetLayoutDW002:
                DataWedgeUtilities.SetProfileConfig( MainActivity.LAYOUT_GROUP_NAME,
                        MainActivity.LAYOUT_DATAWEDGE_002, getApplicationContext());
                break;
        }
    }
}
