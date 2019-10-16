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

                if (Validate_data()) {

                    Save_data();
                    Intent intent = new Intent(addMacActivity.this, addNumbers1Activity.class);
                    startActivity(intent);

                }
            }
        });
    }


    private boolean Validate_data() {

        String community_id = til_community_id.getEditText().getText().toString();
        String rut_client = til_client_rut.getEditText().getText().toString();

        boolean rut_state = client_rut_validate(rut_client);
        boolean id_state = community_id_validate(community_id);

        if (rut_state && id_state) {

            return true;
        } else {
            return false;
        }
    }

    /*private boolean client_rut_validate(String rut) {
        Pattern patron = Pattern.compile("\\A[a-no-zA-NO-Z0-9]{12}\\Z");
        if (!patron.matcher(rut).matches() || rut.length() > 30) {
            til_client_rut.setError("Rut inválido");
            return false;
        } else {
            til_client_rut.setError(null);
        }
        return true;
    }
*/

    private boolean client_rut_validate(String rut) {

        boolean rut_state = RutValidator.validarRut(rut);

        if (!rut_state) {
            til_client_rut.setError("Rut invalido");
            return false;
        } else {
            til_client_rut.setError(null);
        }
        return true;
    }

    private boolean community_id_validate(String id) {
        Pattern patron = Pattern.compile("\\A[\\w]{1,19}(_cloudpbx)\\Z");
        if (!patron.matcher(id).matches() || id.length() > 30) {
            til_community_id.setError("Community id inválido");
            return false;
        } else {
            til_community_id.setError(null);
        }
        return true;
    }


    public void Save_data() {
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_client_rut", et_client_rut.getText().toString());
        obj_editor.putString("et_community_id", et_community_id.getText().toString());
        obj_editor.commit();
    }
}
