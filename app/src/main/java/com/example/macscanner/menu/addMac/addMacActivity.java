package com.example.macscanner.menu.addMac;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.R;
import com.example.macscanner.RutValidator;
import com.example.macscanner.menu.addMac.addNumbers.addNumbers1Activity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class addMacActivity extends AppCompatActivity {

    private int NumSolicitud;
    private boolean client_rut;
    private boolean community_id;

    private TextInputLayout til_client_rut, til_community_id;
    private EditText et_client_rut, et_community_id;
    private Button btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mac);

        til_client_rut = findViewById(R.id.til_client_rut);
        et_client_rut = findViewById(R.id.et_client_rut);
        til_community_id = findViewById(R.id.til_community_id);
        et_community_id = findViewById(R.id.et_community_id);


       /* SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_client_rut", "");
        obj_editor.putString("et_community_id", "");
        obj_editor.commit();*/

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        et_client_rut.setText(preferences.getString("et_client_rut", ""));
        et_community_id.setText(preferences.getString("et_community_id", ""));


        et_client_rut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_client_rut.setError(null);
                Client_rut_validate();
                Enable_btn();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_community_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_community_id.setError(null);
                Community_id_validate();
                Enable_btn();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setEnabled(false);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Num_request();
                Save_data();
                Intent intent = new Intent(addMacActivity.this, addNumbers1Activity.class);
                startActivity(intent);
            }
        });

        if (et_community_id.getText().toString().isEmpty() && et_client_rut.getText().toString().isEmpty()){

        } else {
            Client_rut_validate();
            Community_id_validate();
            Enable_btn();
        }

    }

    private void Enable_btn() {

        if (client_rut && community_id) {
            btn_enter.setEnabled(true);
        } else {
            btn_enter.setEnabled(false);
        }
    }

    private void Client_rut_validate() {

        String rut_client = til_client_rut.getEditText().getText().toString();
        client_rut = RutValidator.validarRut(rut_client);

        if (!client_rut) {
            til_client_rut.setError("Rut inválido");
        } else {
            til_client_rut.setError(null);
        }
    }

    private void Community_id_validate() {

        String id = til_community_id.getEditText().getText().toString();

        Pattern patron = Pattern.compile("\\A[\\w]{1,19}(_cloudpbx)\\Z");
        if (!patron.matcher(id).matches()) {
            til_community_id.setError("'Community id' inválido");
            community_id = false;
        } else {
            til_community_id.setError(null);
            community_id = true;
        }
    }

    public void Save_data() {
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_client_rut", et_client_rut.getText().toString());
        obj_editor.putString("et_community_id", et_community_id.getText().toString());
        obj_editor.commit();
    }

    public void Num_request() {

        NumSolicitud = (int) (Math.random() * 1000000000) + 1;

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putInt("NumSolicitud", NumSolicitud);
        obj_editor.commit();
    }

    //method back of the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
