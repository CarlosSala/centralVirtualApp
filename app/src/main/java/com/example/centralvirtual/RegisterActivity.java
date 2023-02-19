package com.example.centralvirtual;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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
    private LinearLayout linearLayout;
    private ProgressBar progressBarRegister;
    private FirebaseAuth firebaseAuth;
    private final static String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        // for Snack bar
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

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidForm()) {

                    UserRegister();
                }
            }
        });
    }

    private boolean ValidForm() {

        boolean status = true;

        String nameError = null;
        String lastNameError = null;
        String emailError = null;
        String password1Error = null;
        String password2Error = null;

        if (TextUtils.isEmpty(et_name.getText())) {
            nameError = "Este campo no puede estar vacío";
            status = false;
        }
        toggleTextInputLayoutError(til_name, nameError);

        if (TextUtils.isEmpty(et_lastName.getText())) {
            lastNameError = "Este campo no puede estar vacío";
            status = false;
        }
        toggleTextInputLayoutError(til_lastName, lastNameError);

        if (TextUtils.isEmpty(et_email.getText())) {
            emailError = "Este campo no puede estar vacío";
            status = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText()).matches()) {
            emailError = "El correo no es válido";
            status = false;
        }
        toggleTextInputLayoutError(til_email, emailError);

        if (TextUtils.isEmpty(et_password.getText())) {
            password1Error = "Este campo no puede estar vacío";
            status = false;
        } else if (et_password.length() < 6) {
            password1Error = "La contraseña debe tener 6 o mas caracteres ";
            status = false;
        }
        toggleTextInputLayoutError(til_password, password1Error);

        if (TextUtils.isEmpty(et_passwordAgain.getText())) {
            password2Error = "Este campo no puede estar vacío";
            status = false;
        } else if (et_passwordAgain.length() < 6) {
            password2Error = "La contraseña debe tener 6 o mas caracteres";
            status = false;
        } else if (!et_passwordAgain.getText().toString().equals(et_password.getText().toString())) {
            password2Error = "Las contraseñas no coinciden";
            status = false;
        }
        toggleTextInputLayoutError(til_passwordAgain, password2Error);

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

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view instanceof EditText) {
            view.clearFocus();
        }
    }

    private void UserRegister() {

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

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "El correo electrónico ya esta registrado en otra cuenta",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "No se pudo realizar el registro", Toast.LENGTH_LONG).show();
                            }
                            showProgressBar(false);
                        }
                    }
                });
    }


    // register additional user data
    private void registerUserData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();

        user.put("name", et_name.getText().toString());
        user.put("surname", et_lastName.getText().toString());
        user.put("email", et_email.getText().toString());
        user.put("timestampUser", FieldValue.serverTimestamp());

        db.collection("users").document(et_email.getText().toString())
                .collection("info").document("user_info")

                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Additional data saved");
                        // call to the method when this have completed  his task
                        registerDevice();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error additional data was not saved", e);
            }
        });
    }


    private void registerDevice() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Map<String, Object> device = new HashMap<>();

        device.put("manufacturer", Build.MANUFACTURER);
        device.put("model", Build.MODEL);
        device.put("timestampToken", FieldValue.serverTimestamp());
        //  device.put("serial",serial );
        // device.put("tokenFCM", refreshedToken);

        db.collection("users").document(et_email.getText().toString())
                .collection("info").document(Build.MODEL)

                .set(device, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Additional data saved");
                SendVerificationEmail();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error additional data was not saved", e);
            }
        });
    }


    private void SendVerificationEmail() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Clean_form();

                            showProgressBar(false);

                            Snackbar.make(linearLayout, "Se ha enviado un mensaje a su correo electrónico para verificar su cuenta",
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Volver", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            onBackPressed();

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.white))
                                    .show();
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    private void Clean_form() {

        et_email.setText("");
        et_lastName.setText("");
        et_name.setText("");
        et_password.setText("");
        et_passwordAgain.setText("");
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

}