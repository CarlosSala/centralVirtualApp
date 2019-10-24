package com.example.macscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.menu.principalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private TextInputEditText et_email, et_password;

    private ProgressBar progressBarLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBarLogin = findViewById(R.id.progressBar);
        progressBarLogin.setVisibility(View.INVISIBLE);

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

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();

            }
        });
    }


    private void Login() {

        showProgressBar(true);

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        //progressDialog.setMessage("Iniciando sesión");
        //progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                            showProgressBar(false);

                            Intent intent = new Intent(LoginActivity.this, principalActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo ingresar", Toast.LENGTH_SHORT).show();
                            showProgressBar(false);
                        }
                    }

                });
        //progressDialog.dismiss();
    }

    private void showProgressBar(boolean status) {

        if (status) {
            progressBarLogin.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progressBarLogin.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }
}

