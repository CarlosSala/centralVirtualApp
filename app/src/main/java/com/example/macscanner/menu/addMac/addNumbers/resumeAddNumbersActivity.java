package com.example.macscanner.menu.addMac.addNumbers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.macscanner.R;

public class resumeAddNumbersActivity extends AppCompatActivity {

    private TextView tv_number_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_add_numbers);

        tv_number_one = findViewById(R.id.tv_numberOne);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_number_one.setText(preferences.getString("et_number_one", ""));
    }


}
