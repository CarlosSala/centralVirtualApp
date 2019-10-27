package com.example.macscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity {


    private static String TAG = "RecoverPasswordActivity";

    private FirebaseAuth firebaseAuth;

    private TextInputEditText et_recover;
    private TextInputLayout til_recover;
    private Button btn_recover;

    private LinearLayout linearLayout;

    private ProgressBar progressBarRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);

        firebaseAuth = FirebaseAuth.getInstance();

        linearLayout = findViewById(R.id.linearLayout_recover);

        progressBarRecover = findViewById(R.id.progressBar_recover);
        progressBarRecover.setVisibility(View.INVISIBLE);

        btn_recover = findViewById(R.id.btn_recover_password);

        et_recover = findViewById(R.id.et_recover);
        til_recover = findViewById(R.id.til_recover);


        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressBar(true);

                if (validform()) {

                    //firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.sendPasswordResetEmail(et_recover.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                showProgressBar(false);

                                Snackbar.make(linearLayout, "Se ha enviado un mensaje al correo electrónico proporcionado", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Volver", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                onBackPressed();

                                                // onDestroy();

                                              /*  Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();*/
                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(R.color.white))
                                        .show();
                            } else {

                                Log.d(TAG, "Email has not been sent.");
                                Snackbar.make(linearLayout, task.getException().getMessage().toString(), Snackbar.LENGTH_LONG).show();
                                showProgressBar(false);
                            }

                        }
                    });
                } else {
                    showProgressBar(false);
                }
            }
        });
    }

    private boolean validform() {

        boolean resp = true;
        String mailError = null;

        if (TextUtils.isEmpty(et_recover.getText())) {
            mailError = "El campo email esta vacio";
            resp = false;
        }
        if ((!Patterns.EMAIL_ADDRESS.matcher(et_recover.getText()).matches()) && (!TextUtils.isEmpty(et_recover.getText()))) {
            mailError = "Introducir un email valido";
            resp = false;
        }

        toggleTextInputLayoutError(til_recover, mailError);
        clearFocus();


        return resp;

    }

    /*
            *Display/
    hides TextInputLayout
    error
    @param
    msg the
    message,or null
    to hide*/


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


    private void showProgressBar(boolean status) {

        if (status) {
            progressBarRecover.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progressBarRecover.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }

    //method back of the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


   /* @Override
    public void onBackPressed() {

        Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();

        *//* Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();*//*
    }*/


}
