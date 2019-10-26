package com.example.macscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.menu.principalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_register, btn_recovery_password;
    private TextInputEditText et_email, et_password;
    private TextInputLayout til_email, til_password;

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
        til_email = findViewById(R.id.til_email);

        et_password = findViewById(R.id.et_password);
        til_password = findViewById(R.id.til_password);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //User_register();
            }
        });

        btn_recovery_password = findViewById(R.id.btn_recovery_password);
        btn_recovery_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Valid_form()) {
                    Login();
                }
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


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                Uri photoUrl = user.getPhotoUrl();

                                // Check if user's email is verified
                                boolean emailVerified = user.isEmailVerified();


                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getIdToken() instead.
                                String uid = user.getUid();

                                if (emailVerified) {

                                    Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_LONG).show();

                                    showProgressBar(false);

                                    Intent intent = new Intent(LoginActivity.this, principalActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(LoginActivity.this, "Debe verificar su correo", Toast.LENGTH_LONG).show();

                                    showProgressBar(false);


                                }


                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo ingresar", Toast.LENGTH_SHORT).show();
                            showProgressBar(false);
                            return;
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

    private boolean Valid_form() {

        boolean resp = true;

        String mailError = null;
        String password1Error = null;

        if (TextUtils.isEmpty(et_email.getText())) {
            mailError = "Este campo no puede estar vacío";
            resp = false;
        }
        if ((!Patterns.EMAIL_ADDRESS.matcher(et_email.getText()).matches()) && (!TextUtils.isEmpty(et_email.getText()))) {
            mailError = "El correo no es válido";
            resp = false;

        }
        toggleTextInputLayoutError(til_email, mailError);

        if (TextUtils.isEmpty(et_password.getText())) {
            password1Error = "Este campo no puede estar vacío";
            resp = false;
        }
        if ((et_password.length() < 6) && (!TextUtils.isEmpty(et_password.getText()))) {
            password1Error = "La contraseña debe tener 6 o mas caracteres";
            resp = false;
        }

        toggleTextInputLayoutError(til_password, password1Error);

        clearFocus();

        return resp;
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}

