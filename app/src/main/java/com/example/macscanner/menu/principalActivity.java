package com.example.macscanner.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.macscanner.menu.addMac.addMacActivity;
import com.example.macscanner.R;

public class principalActivity extends AppCompatActivity {

    private Button btn_add_mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btn_add_mac = findViewById(R.id.btn_add_mac);

        btn_add_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(principalActivity.this, addMacActivity.class);
                startActivity(intent);
            }
        });

    }
}
