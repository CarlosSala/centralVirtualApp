package com.example.centralvirtual.menu.addMac.addNumbers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.centralvirtual.R;
import com.example.centralvirtual.databinding.ActivityAddNumbers1Binding;
import com.example.centralvirtual.databinding.ActivitySendDataBinding;
import com.example.centralvirtual.menu.PrincipalActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class sendDataActivity extends AppCompatActivity {

    private ActivitySendDataBinding binding;
    private final static String TAG = "sendDataActivity";
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySendDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        data = getIntent().getExtras().getString("data", "no data");

        binding.btnSend.setVisibility(View.VISIBLE);
        binding.tvMsg.setVisibility(View.VISIBLE);
        binding.tvLoading.setVisibility(View.INVISIBLE);
        binding.progressBarSendData.setVisibility(View.INVISIBLE);

        binding.btnSend.setEnabled(true);
        binding.tvMsg.setEnabled(true);
        binding.tvLoading.setEnabled(false);
        binding.progressBarSendData.setEnabled(false);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OccultControls(false);
                SendData();
            }
        });
    }


    private void OccultControls(boolean status) {

        if (status) {

            binding.btnSend.setEnabled(true);
            binding.tvMsg.setEnabled(true);
            binding.tvLoading.setEnabled(false);
            binding.progressBarSendData.setEnabled(false);

            binding.btnSend.setVisibility(View.VISIBLE);
            binding.tvMsg.setVisibility(View.VISIBLE);
            binding.tvLoading.setVisibility(View.INVISIBLE);
            binding.progressBarSendData.setVisibility(View.INVISIBLE);

        } else {

            binding.btnSend.setEnabled(false);
            binding.tvMsg.setEnabled(false);
            binding.tvLoading.setEnabled(true);
            binding.progressBarSendData.setEnabled(true);

            binding.btnSend.setVisibility(View.INVISIBLE);
            binding.tvMsg.setVisibility(View.INVISIBLE);
            binding.tvLoading.setVisibility(View.VISIBLE);
            binding.progressBarSendData.setVisibility(View.VISIBLE);
        }
    }


    private void SendData() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user != null) {

            String email = user.getEmail();

            if (email != null) {

                //FirebaseAuth.getInstance().getCurrentUser().getEmail();

                Map<String, Object> device = new HashMap<>();

                device.put("stringData", data);

                SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                String num_solicitude = String.valueOf(preferences.getInt("applicationNumber", 0));

                db.collection("users").document(email).collection("broadsoft")
                        .document(num_solicitude).set(device, SetOptions.merge())

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                // the accumulated data is sent for generate the qr code
                                Intent intent = new Intent(sendDataActivity.this, shareQrActivity.class);
                                intent.putExtra("data", data);

                                Toast.makeText(sendDataActivity.this, "Se envió la información", Toast.LENGTH_LONG).show();

                                startActivity(intent);

                                Log.d(TAG, "Additional data saved");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.w(TAG, "Error additional data was not saved", e);
                                OccultControls(true);
                            }
                        });
            }
        }
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
}
