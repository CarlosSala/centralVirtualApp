package com.example.macscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.menu.principalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private TextInputEditText et_email, et_password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //User_register();
            }
        });

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


    private void User_register() {

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "El campo de email se encuentra vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "El campo de email se encuentra vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        //progressDialog.setMessage();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo realizar el registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void Login() {

        if (et_email.getText().toString().equals("correo@prueba.cl") &&
                et_password.getText().toString().equals("1234")) {
            Intent intent = new Intent(LoginActivity.this, principalActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error datos de usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
