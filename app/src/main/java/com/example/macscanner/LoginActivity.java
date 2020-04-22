package com.example.macscanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.menu.PrincipalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText et_email, et_password;
    private TextInputLayout til_email, til_password;

    private ProgressBar progressBarLogin;

    private FirebaseAuth firebaseAuth;

    //public final static String TAG = "Seguimiento";

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

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //intent.addFlags()
                startActivity(intent);
            }
        });

        Button btn_recovery_password = findViewById(R.id.btn_recovery_password);
        btn_recovery_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidForm()) {

                    if (isNetAvailable()) {

                        Login();

                    } else {
                        Toast.makeText(LoginActivity.this, "Conectese a una red wifi o habilite los datos moviles",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private boolean ValidForm() {

        boolean status = true;

        String emailError = null;
        String passwordError = null;

        if (TextUtils.isEmpty(et_email.getText())) {
            emailError = "Este campo no puede estar vacío";
            status = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText()).matches()) {
            emailError = "El correo no es válido";
            status = false;
        }
        toggleTextInputLayoutError(til_email, emailError);

        if (TextUtils.isEmpty(et_password.getText())) {
            passwordError = "Este campo no puede estar vacío";
            status = false;
        } else if ((et_password.length() < 6)) {
            passwordError = "La contraseña debe tener 6 o mas caracteres";
            status = false;
        }
        toggleTextInputLayoutError(til_password, passwordError);

        clearFocus();

        return status;
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout til, String msg) {

        if (msg == null) {
            til.setErrorEnabled(false);
        } else {
            til.setError(msg);
            til.setErrorEnabled(true);
        }
    }

    //EditTExt Focus is cleaned
    private void clearFocus() {

        View view = this.getCurrentFocus();
        if (view instanceof EditText) {
            /*InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
            view.clearFocus();
        }
    }

    private boolean isNetAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    private void Login() {

        showProgressBar(true);

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            // if (user != null) {

                            String email = user.getEmail();

                            // Check if user's email is verified
                            if (email != null && user.isEmailVerified()) {

                                showProgressBar(false);

                                //Log.i(TAG, "Mi log de prueba");
                                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Toast.makeText(LoginActivity.this, "Debe verificar su correo", Toast.LENGTH_LONG).show();
                                showProgressBar(false);
                            }
                            // }

                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo ingresar", Toast.LENGTH_LONG).show();
                            showProgressBar(false);
                        }
                    }
                });
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


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Check if user's email is verified
        if (user != null) {

            if (user.isEmailVerified()) {
                toStartActivity();
            }
        }
    }

    private void toStartActivity() {

        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

