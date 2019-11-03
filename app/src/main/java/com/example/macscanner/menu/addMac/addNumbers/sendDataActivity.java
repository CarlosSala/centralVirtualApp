package com.example.macscanner.menu.addMac.addNumbers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.macscanner.R;
import com.example.macscanner.menu.PrincipalActivity;
import com.example.macscanner.menu.addMac.addMacActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class sendDataActivity extends AppCompatActivity {

    private final static String TAG = "sendDataActivity";

    private String data;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        data = getIntent().getExtras().getString("data", "QR");

        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sendDataActivity.this, shareQrActivity.class);
                intent.putExtra("data", data);

                registerData();

                Toast.makeText(sendDataActivity.this, "Se envió la información", Toast.LENGTH_LONG).show();
/*
                intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
*/
                startActivity(intent);
                //finish();
            }
        });
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Central Virtual")
                .setMessage("Se descartarán los cambios, ¿Desea Salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences settings = getSharedPreferences("datos", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();

                        Intent intent = new Intent(sendDataActivity.this, PrincipalActivity.class);
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

    private void registerData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> device = new HashMap<>();

        device.put("stringData", data);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String num_solicitud = String.valueOf(preferences.getInt("NumSolicitud", 0));

        db.collection("users").document(FirebaseAuth.getInstance()
                .getCurrentUser().getEmail()).collection("broadsoft")
                .document(num_solicitud)

                .set(device, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
