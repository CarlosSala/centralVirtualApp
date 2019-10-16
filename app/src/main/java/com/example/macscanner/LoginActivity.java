package com.example.macscanner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.menu.principalActivity;
import com.google.android.material.textfield.TextInputEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextInputEditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        et_email.clearFocus();
        et_password.clearFocus();

        btn_login = findViewById(R.id.btn_login);


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
