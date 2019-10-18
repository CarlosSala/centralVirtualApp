package com.example.macscanner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.menu.principalActivity;
import com.google.android.material.textfield.TextInputEditText;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        et_email.setText("correo@prueba.cl");
        et_password.setText("1234");

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

    }


    private void Login (){

        if(et_email.getText().toString().equals("correo@prueba.cl") &&
                et_password.getText().toString().equals("1234")){
            Intent intent = new Intent(LoginActivity.this, principalActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error datos de usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
