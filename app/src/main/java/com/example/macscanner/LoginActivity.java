package com.example.macscanner;

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
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";

    private Button btn_login, btn_register, btn_recovery_password;
    private TextInputEditText et_email, et_password;
    private TextInputLayout til_email, til_password;

    private ProgressBar progressBarLogin;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firestoredb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        firestoredb = FirebaseFirestore.getInstance();

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
            }
        });

        btn_recovery_password = findViewById(R.id.btn_recovery_password);
        btn_recovery_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                //intent.addFlags()
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

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {

                                String email = user.getEmail();

                                // Check if user's email is verified
                                boolean emailVerified = user.isEmailVerified();

                                if (emailVerified) {

                                    DocumentReference docRef = firestoredb.collection("users")
                                            .document(email).collection("info")
                                            .document("user_info");
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    showProgressBar(false);

                                                    String name = document.getString("nombre");

                                                    Toast.makeText(LoginActivity.this, "Bienvenido " + name, Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });

                                } else {

                                    Toast.makeText(LoginActivity.this, "Debe verificar su correo", Toast.LENGTH_LONG).show();
                                    showProgressBar(false);
                                }
                            }

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

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout til,
                                                   String msg) {
        til.setError(msg);
        if (msg == null) {
            til.setErrorEnabled(false);
        } else {
            til.setErrorEnabled(true);
        }
    }

    //EditTExt Focus is cleaned
    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Check if user's email is verified

        if (user != null) {

            boolean emailVerified = user.isEmailVerified();

            if (emailVerified) {

                toStartActivity();
            }
        }
     /*   firebaseAuth = FirebaseAuth.getInstance();
        //mAuth.addAuthStateListener(firebaseAuthListener);

        if (firebaseAuth.getCurrentUser() != null) {
            toStartActivity();
        } else {


        }*/
    }

    private void toStartActivity() {

        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    // Name, email address, and profile photo Url
    //String name = user.getDisplayName();
    //Uri photoUrl = user.getPhotoUrl();

    // The user's ID, unique to the Firebase project. Do NOT use this value to
    // authenticate with your backend server, if you have one. Use
    // FirebaseUser.getIdToken() instead.
    //String uid = user.getUid();

}

