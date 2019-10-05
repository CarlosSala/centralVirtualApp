package com.example.macscanner;

import androidx.appcompat.app.AppCompatActivity;
import com.example.macscanner.menu.principalActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    btn_login = (Button)findViewById(R.id.btn_login);


    btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, principalActivity.class);
            startActivity(intent);
            finish();
        }
    });

    }
}
