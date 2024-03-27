package com.example.centralvirtual;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.centralvirtual.databinding.ActivityLoginBinding;
import com.example.centralvirtual.databinding.ActivityRecoveryPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatActivity {

    private ActivityRecoveryPasswordBinding binding;
    private static final String TAG = "RecoverPasswordActivity";
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecoveryPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.progressBarRecover.setVisibility(View.INVISIBLE);

        binding.btnRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidForm()) {

                    RecoveryPassword();
                }
            }
        });
    }

    private boolean ValidForm() {

        boolean status = true;
        String emailError = null;

        if (TextUtils.isEmpty(binding.etRecover.getText())) {
            emailError = "This field can not be empty";
            status = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etRecover.getText()).matches()) {
            emailError = "The email is not validate";
            status = false;
        }
        toggleTextInputLayoutError(binding.tilRecover, emailError);

        clearFocus();

        return status;
    }

    private void RecoveryPassword() {

        showProgressBar(true);

        firebaseAuth.sendPasswordResetEmail(binding.etRecover.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Email sent.");

                    showProgressBar(false);

                    Snackbar.make(binding.linearLayoutRecover, "It was sent a message to the email indicated", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    onBackPressed();

                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .show();
                } else {

                    Log.d(TAG, "Email has not been sent.");
                    Snackbar.make(binding.linearLayoutRecover, task.getException().getMessage().toString(), Snackbar.LENGTH_LONG).show();
                    showProgressBar(false);
                }

            }
        });
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout, String msg) {

        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setError(msg);
            textInputLayout.setErrorEnabled(true);
        }
    }

    private void clearFocus() {
        View view = this.getCurrentFocus();
        if (view instanceof EditText) {
            view.clearFocus();
        }
    }


    private void showProgressBar(boolean status) {

        if (status) {
            binding.progressBarRecover.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            binding.progressBarRecover.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }

    //method back of the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}