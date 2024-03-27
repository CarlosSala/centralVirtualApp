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

import com.example.centralvirtual.databinding.ActivityLoginBinding;
import com.example.centralvirtual.databinding.ActivityRegisterBinding;
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

    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    private final static String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
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

        if (TextUtils.isEmpty(binding.etName.getText())) {
            nameError = "This filed can not be empty";
            status = false;
        }
        toggleTextInputLayoutError(binding.textInputLayoutName, nameError);

        if (TextUtils.isEmpty(binding.etLastName.getText())) {
            lastNameError = "This filed can not be empty";
            status = false;
        }
        toggleTextInputLayoutError(binding.textInputLayoutLastname, lastNameError);

        if (TextUtils.isEmpty(binding.etEmail.getText())) {
            emailError = "This filed can not be empty";
            status = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText()).matches()) {
            emailError = "The email is not validate";
            status = false;
        }
        toggleTextInputLayoutError(binding.textInputLayoutMail, emailError);

        if (TextUtils.isEmpty(binding.etPassword.getText())) {
            password1Error = "This filed can not be empty";
            status = false;
        } else if (binding.etPassword.length() < 6) {
            password1Error = "The password must has at least 6 characters or more ";
            status = false;
        }
        toggleTextInputLayoutError(binding.textInputLayoutPassword1, password1Error);

        if (TextUtils.isEmpty(binding.etPasswordAgain.getText())) {
            password2Error = "This filed can not be empty";
            status = false;
        } else if (binding.etPasswordAgain.length() < 6) {
            password2Error = "The password must has at least 6 characters or more";
            status = false;
        } else if (!binding.etPasswordAgain.getText().toString().equals(binding.etPassword.getText().toString())) {
            password2Error = "The passwords are not same";
            status = false;
        }
        toggleTextInputLayoutError(binding.textInputLayoutPassword2, password2Error);

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

        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        //create user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            registerUserData();

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "This email is already registered",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "The register was not realized", Toast.LENGTH_LONG).show();
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

        user.put("name", binding.etName.getText().toString());
        user.put("surname", binding.etLastName.getText().toString());
        user.put("email", binding.etEmail.getText().toString());
        user.put("timestampUser", FieldValue.serverTimestamp());

        db.collection("users").document(binding.etEmail.getText().toString())
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

        db.collection("users").document(binding.etEmail.getText().toString())
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

                            Snackbar.make(binding.linearLayoutRegister, "It was sent a message to your email",
                                            Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Back", new View.OnClickListener() {
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

        binding.etEmail.setText("");
        binding.etLastName.setText("");
        binding.etName.setText("");
        binding.etPassword.setText("");
        binding.etPasswordAgain.setText("");
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