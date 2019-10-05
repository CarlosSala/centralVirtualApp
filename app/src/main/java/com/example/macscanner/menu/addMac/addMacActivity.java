package com.example.macscanner.menu.addMac;

import androidx.appcompat.app.AppCompatActivity;
import com.example.macscanner.menu.addMac.addNumbers.addNumbersActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.macscanner.R;

public class addMacActivity extends AppCompatActivity {

    private Button btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mac);

        btn_enter = (Button)findViewById(R.id.btn_enter);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addMacActivity.this, addNumbersActivity.class);
                startActivity(intent);
            }
        });
    }
}
