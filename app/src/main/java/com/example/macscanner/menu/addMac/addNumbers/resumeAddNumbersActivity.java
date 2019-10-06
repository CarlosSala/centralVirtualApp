package com.example.macscanner.menu.addMac.addNumbers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.R;

public class resumeAddNumbersActivity extends AppCompatActivity {

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8,
    tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv_mac1, tv_mac2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_add_numbers);

        tv_mac1 = findViewById(R.id.tv_indicator_mac1);
        tv_mac2 = findViewById(R.id.tv_indicator_mac2);
        tv1 = findViewById(R.id.tv_numberOne);
        tv2 = findViewById(R.id.tv_numberTwo);
        tv3 = findViewById(R.id.tv_numberThree);
        tv4 = findViewById(R.id.tv_numberFour);
        tv5 = findViewById(R.id.tv_numberFive);
        tv6 = findViewById(R.id.tv_numberSix);
        tv7 = findViewById(R.id.tv_numberSeven);
        tv8 = findViewById(R.id.tv_numberEight);
        tv9 = findViewById(R.id.tv_numberNine);
        tv10 = findViewById(R.id.tv_numberTen);
        tv11 = findViewById(R.id.tv_numberEleven);
        tv12 = findViewById(R.id.tv_numberTwelve);
        tv13 = findViewById(R.id.tv_numberThirteen);
        tv14 = findViewById(R.id.tv_numberFourteen);
        tv15 = findViewById(R.id.tv_numberFifteen);
        tv16 = findViewById(R.id.tv_numberSixteen);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_mac1.setText(preferences.getString("mac_scanned1", "valor mac1"));
        tv_mac2.setText(preferences.getString("mac_scanned2", "valor mac2"));
        tv1.setText(preferences.getString("et_number_one", ""));
        tv2.setText(preferences.getString("et_number_two", ""));
        tv3.setText(preferences.getString("et_number_three", ""));
        tv4.setText(preferences.getString("et_number_four", ""));
        tv5.setText(preferences.getString("et_number_five", ""));
        tv6.setText(preferences.getString("et_number_six", ""));
        tv7.setText(preferences.getString("et_number_seven", ""));
        tv8.setText(preferences.getString("et_number_eight", ""));
        tv9.setText(preferences.getString("et_number_nine", ""));
        tv10.setText(preferences.getString("et_number_ten", ""));
        tv11.setText(preferences.getString("et_number_eleven", ""));
        tv12.setText(preferences.getString("et_number_twelve", ""));
        tv13.setText(preferences.getString("et_number_thirteen", ""));
        tv14.setText(preferences.getString("et_number_fourteen", ""));
        tv15.setText(preferences.getString("et_number_fifteen", ""));
        tv16.setText(preferences.getString("et_number_sixteen", ""));

    }


}
