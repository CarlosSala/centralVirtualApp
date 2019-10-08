package com.example.macscanner.menu.addMac;

import androidx.appcompat.app.AppCompatActivity;
import com.example.macscanner.menu.addMac.addNumbers.addNumbers1Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.macscanner.R;

public class addMacActivity extends AppCompatActivity {

    private Button btn_enter;
    private EditText et_client_rut, et_community_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mac);

        et_client_rut = findViewById(R.id.et_client_rut);
        et_community_id = findViewById(R.id.et_community_id);

        btn_enter = findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar(view);
                Intent intent = new Intent(addMacActivity.this, addNumbers1Activity.class);
                startActivity(intent);
            }
        });


    }


    public void Guardar(View view){
        SharedPreferences preferencias = getSharedPreferences( "datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_client_rut", et_client_rut.getText().toString());
        obj_editor.putString("et_community_id", et_community_id.getText().toString());
        obj_editor.commit();

    }
}
