package com.example.macscanner.menu.addMac.addNumbers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.macscanner.R;

public class sendDataActivity extends AppCompatActivity {


    private String data;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        data = getIntent().getExtras().getString("data", "QR");

        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sendDataActivity.this, shareQrActivity.class);
                intent.putExtra("data", data);
                Toast.makeText(sendDataActivity.this, "Se envió la información", Toast.LENGTH_LONG).show();
                startActivity(intent);
                //finish();
            }
        });
    }
}
