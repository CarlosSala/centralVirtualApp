package com.example.centralvirtual;

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

import com.example.centralvirtual.databinding.ActivityLoginBinding;
import com.example.centralvirtual.menu.PrincipalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //intent.addFlags()
                startActivity(intent);
            }
        });

        binding.btnRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidForm()) {

                    if (isNetAvailable()) {

                        Login();

                    } else {
                        Toast.makeText(LoginActivity.this, "There is not network available",
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

        if (TextUtils.isEmpty(binding.etEmail.getText())) {
            emailError = "This field can not be empty";
            status = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText()).matches()) {
            emailError = "The email is not validate";
            status = false;
        }
        toggleTextInputLayoutError(binding.tilEmail, emailError);

        if (TextUtils.isEmpty(binding.etPassword.getText())) {
            passwordError = "This field can not be empty";
            status = false;
        } else if ((binding.etPassword.length() < 6)) {
            passwordError = "The password must be at least 6 or more characters";
            status = false;
        }
        toggleTextInputLayoutError(binding.tilPassword, passwordError);

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

        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {

                                String email = user.getEmail();

                                // Check if user's email is verified
                                if (email != null && user.isEmailVerified()) {

                                    showProgressBar(false);

                                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(LoginActivity.this, "You must verify your email", Toast.LENGTH_LONG).show();
                                    showProgressBar(false);
                                }
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "It wasn't possible to log in", Toast.LENGTH_LONG).show();
                            showProgressBar(false);
                        }
                    }
                });
    }

    private void showProgressBar(boolean status) {

        if (status) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
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

