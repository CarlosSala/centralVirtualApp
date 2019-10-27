package com.example.macscanner;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText et_name, et_lastName, et_email, et_password, et_passwordAgain;
    private TextInputLayout til_name, til_lastName, til_email, til_password, til_passwordAgain;
    private Button btn_register;
    private LinearLayout linearLayout;
    private ProgressBar progressBarRegister;

    private FirebaseAuth firebaseAuth;

    private final static String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        linearLayout = findViewById(R.id.linearLayout_register);

        progressBarRegister = findViewById(R.id.progressBar);
        progressBarRegister.setVisibility(View.INVISIBLE);

        et_name = findViewById(R.id.et_name);
        et_lastName = findViewById(R.id.et_lastName);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_passwordAgain = findViewById(R.id.et_passwordAgain);

        til_name = findViewById(R.id.text_input_layout_name);
        til_lastName = findViewById(R.id.text_input_layout_lastname);
        til_email = findViewById(R.id.text_input_layout_mail);
        til_password = findViewById(R.id.text_input_layout_password1);
        til_passwordAgain = findViewById(R.id.text_input_layout_password2);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Valid_form()) {
                    User_register();
                }
            }
        });
    }


    private void User_register() {

        showProgressBar(true);

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        //create user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            registerUserData();

                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                            }
                                        }
                                    });

                            Clean_form();

                            showProgressBar(false);

                            Snackbar.make(linearLayout, "Se ha enviado un mensaje a su correo electrónico para verificar su cuenta", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Volver", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            onBackPressed();

                                            //onDestroy();

                                            /*Intent intent = new Intent(getApplication(), LoginActivity.class);
                                            startActivity(intent);
                                            finish();*/
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.white))
                                    .show();

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "El correo electrónico se encuentra registrado en otra cuenta", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "No se pudo realizar el registro", Toast.LENGTH_LONG).show();
                            }
                            showProgressBar(false);
                        }
                    }
                });
    }

    private boolean Valid_form() {

        boolean resp = true;

        String nameError = null;
        String lastnameError = null;
        String mailError = null;
        String password1Error = null;
        String password2Error = null;

        if (TextUtils.isEmpty(et_name.getText())) {
            nameError = "Este campo no puede estar vacío";
            resp = false;
        }

        toggleTextInputLayoutError(til_name, nameError);

        if (TextUtils.isEmpty(et_lastName.getText())) {
            lastnameError = "Este campo no puede estar vacío";
            resp = false;
        }
        toggleTextInputLayoutError(til_lastName, lastnameError);

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
            password1Error = "La contraseña debe tener 6 o mas caracteres ";
            resp = false;
        }

        toggleTextInputLayoutError(til_password, password1Error);

        if (TextUtils.isEmpty(et_passwordAgain.getText())) {
            password2Error = "Este campo no puede estar vacío";
            resp = false;
        }
        if ((et_passwordAgain.length() < 6) && (!TextUtils.isEmpty(et_passwordAgain.getText()))) {
            password2Error = "La contraseña debe tener 6 o mas caracteres";
            resp = false;
        }
        if (!et_passwordAgain.getText().toString().equals(et_password.getText().toString())) {
            password2Error = "Las contraseñas no coinciden";
            resp = false;
        }

        toggleTextInputLayoutError(til_passwordAgain, password2Error);

        clearFocus();

        return resp;
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout til, String msg) {
        til.setError(msg);
        if (msg == null) {
            til.setErrorEnabled(false);
        } else {
            til.setErrorEnabled(true);
        }
    }

    // register additional user data
    private void registerUserData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();

        user.put("nombre", et_name.getText().toString());
        user.put("apellido", et_lastName.getText().toString());
        user.put("email", et_email.getText().toString());
        user.put("timestampUser", FieldValue.serverTimestamp());

        db.collection("users").document(et_email.getText().toString())

                .set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Additional data saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error additional data was not saved", e);
            }
        });
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
            progressBarRegister.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progressBarRegister.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }

    //method back of the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    /*try {
                                Thread.currentThread().sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/

   /*  final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    buttons[inew][jnew].setBackgroundColor(Color.BLACK);
                                }
                            }, 5000);*/

    private void Clean_form() {

        et_email.setText("");
        et_lastName.setText("");
        et_name.setText("");
        et_password.setText("");
        et_passwordAgain.setText("");

    }


}