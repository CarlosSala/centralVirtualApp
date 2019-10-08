package com.example.macscanner.menu.addMac;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.R;
import com.example.macscanner.menu.addMac.addNumbers.addNumbers1Activity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class addMacActivity extends AppCompatActivity {

    private TextInputLayout til_client_rut, til_community_id;
    private EditText et_client_rut, et_community_id;
    private Button btn_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mac);

        til_client_rut = findViewById(R.id.til_client_rut);
        til_community_id = findViewById(R.id.til_community_id);

        et_client_rut = findViewById(R.id.et_client_rut);
        et_community_id = findViewById(R.id.et_community_id);

        et_client_rut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_client_rut.setError(null);
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Guardar(view);
                validarDatos();
               /* Intent intent = new Intent(addMacActivity.this, addNumbers1Activity.class);
                startActivity(intent);*/
            }
        });
    }

    private boolean client_rut_validate(String rut) {
        Pattern patron = Pattern.compile("\\A[a-no-zA-NO-Z0-9]{12}\\Z");
        if (!patron.matcher(rut).matches() || rut.length() > 30) {
            til_client_rut.setError("Rut inválido");
            return false;
        } else {
            til_client_rut.setError(null);
        }
        return true;
    }

    private boolean community_id_validate(String id) {
        Pattern patron = Pattern.compile("\\A[a-no-zA-NO-Z0-9]{5,30}\\Z");
        if (!patron.matcher(id).matches() || id.length() > 30) {
            til_community_id.setError("community id inválido");
            return false;
        } else {
            til_community_id.setError(null);
        }
        return true;
    }

    private void validarDatos() {
        String rut_client = til_client_rut.getEditText().getText().toString();
        String community_id = til_community_id.getEditText().getText().toString();

        boolean a = client_rut_validate(rut_client);
        boolean b = community_id_validate(community_id);

        if (a && b) {
            // OK, se pasa a la siguiente acción
            Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        }
    }

    public void Guardar(View view) {
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_client_rut", et_client_rut.getText().toString());
        obj_editor.putString("et_community_id", et_community_id.getText().toString());
        obj_editor.commit();

    }
}
