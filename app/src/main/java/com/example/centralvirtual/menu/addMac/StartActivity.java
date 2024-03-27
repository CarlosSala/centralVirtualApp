package com.example.centralvirtual.menu.addMac;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.centralvirtual.R;
import com.example.centralvirtual.RutValidator;
import com.example.centralvirtual.databinding.ActivityLoginBinding;
import com.example.centralvirtual.databinding.ActivityStartBinding;
import com.example.centralvirtual.menu.PrincipalActivity;
import com.example.centralvirtual.menu.addMac.addNumbers.addNumbers1Activity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;
    private boolean client_rut;
    private boolean community_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        binding.etClientRut.setText(preferences.getString("client_rut", ""));
        binding.etCommunityId.setText(preferences.getString("community_id", ""));

        binding.etClientRut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tilClientRut.setError(null);
                ClientRutValidate();
                Enable_btn();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.etCommunityId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tilCommunityId.setError(null);
                CommunityIdValidate();
                Enable_btn();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnEnter.setEnabled(false);
        binding.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationNumber();
                Save_data();
                Intent intent = new Intent(StartActivity.this, addNumbers1Activity.class);
                startActivity(intent);
            }
        });

        if (!binding.etCommunityId.getText().toString().isEmpty() && !binding.etClientRut.getText().toString().isEmpty()) {

            if (ClientRutValidate() && CommunityIdValidate()) {
                Enable_btn();
            }
        }
    }

    private void Enable_btn() {

        if (client_rut && community_id) {
            binding.btnEnter.setEnabled(true);
        } else {
            binding.btnEnter.setEnabled(false);
        }
    }

    private boolean ClientRutValidate() {

        boolean status = false;

        String rut_client = binding.tilClientRut.getEditText().getText().toString();
        client_rut = RutValidator.RutValidate(rut_client);

        if (!client_rut) {
            binding.tilClientRut.setError("Rut inválido");
        } else {
            binding.tilClientRut.setError(null);
            status = true;
        }

        return status;
    }

    private boolean CommunityIdValidate() {

        boolean status = false;

        String id = binding.tilCommunityId.getEditText().getText().toString();
        Pattern patron = Pattern.compile("\\A[\\w]{1,19}(_cloudpbx)\\Z");

        if (!patron.matcher(id).matches()) {
            binding.tilCommunityId.setError("'Community id' inválido");
            community_id = false;

        } else {
            binding.tilCommunityId.setError(null);
            community_id = true;
            status = true;
        }

        return status;
    }

    public void Save_data() {
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferences.edit();
        obj_editor.putString("client_rut", binding.etClientRut.getText().toString());
        obj_editor.putString("community_id", binding.etCommunityId.getText().toString());
        obj_editor.apply();
    }

    public void ApplicationNumber() {

        int applicationNumber = (int) (Math.random() * 1000000000) + 1;

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferences.edit();
        obj_editor.putInt("applicationNumber", applicationNumber);
        obj_editor.apply();
    }

    //method back of the toolbar
    @Override
    public boolean onSupportNavigateUp() {

        // onBackPressed();
        getOnBackPressedDispatcher().onBackPressed();

        return false;
    }

    @Override
    public void onBackPressed() {

        AlertDialog();
    }

    private void AlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.LightAlertDialog);

        alertDialogBuilder
                .setTitle("Central Virtual")
                .setMessage("Se descartarán los cambios, ¿Desea Salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();

                        Intent intent = new Intent(StartActivity.this, PrincipalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }
}
