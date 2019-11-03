package com.example.macscanner.menu.addMac.addNumbers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macscanner.CustomScannerActivity;
import com.example.macscanner.ItemTouchListenner;
import com.example.macscanner.MainAdapter;
import com.example.macscanner.R;
import com.example.macscanner.SimpleItemTouchHelperCallback;
import com.example.macscanner.menu.PrincipalActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class addNumbers2Activity extends AppCompatActivity {

    private final static String TAG = "addNumbers1Activity";

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firestoredb;

    private Button btn_scan_mac, btn_next;
    private TextView tv_mac;

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers2);

        mRecyclerView = findViewById(R.id.recycler_sample2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MainAdapter();
        mRecyclerView.setAdapter(mAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        GetNumbersFirebase();

        tv_mac = findViewById(R.id.tv_add_mac2);

        btn_scan_mac = findViewById(R.id.btn_scan_mac2);
        btn_scan_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customScanner();
            }
        });

        btn_next = findViewById(R.id.btn_next2);
        btn_next.setEnabled(false);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Save_data();
                Intent intent = new Intent(view.getContext(), resumeAddNumbersActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_mac.setText(preferences.getString("mac_scanned2", ""));

        Enable_btn();
    }


    // method to enable or disabled button
    private void Enable_btn() {

        if (mAdapter.getItemCount() == 8 && !tv_mac.getText().toString().isEmpty()) {
            btn_next.setEnabled(true);
        } else
            btn_next.setEnabled(false);
    }


    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        Pattern patron = Pattern.compile("\\A[a-fA-F0-9]{12}\\Z");

        if (result != null) {

            if (result.getContents() == null) {

                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();

            } else if (patron.matcher(result.getContents()).matches()) {

                tv_mac.setText(result.getContents());

                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor obj_editor = preferencias.edit();
                obj_editor.putString("mac_scanned2", result.getContents());
                obj_editor.commit();

                Toast.makeText(this, "Código escaneado: " + result.getContents(), Toast.LENGTH_LONG).show();

                Enable_btn();

            } else {
                Toast.makeText(this, "Código escaneado: " + result.getContents() + ", no cumple con el formato requerido", Toast.LENGTH_LONG).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void customScanner() {

        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.CODE_128);
        integrator.setPrompt("");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setTimeout(20000);
        integrator.addExtra("title", "MAC SCANNER");
        integrator.initiateScan();
    }

    public void Save_data() {

        List<String> lista = mAdapter.getmData();

        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();

        for (int i = 0; i < lista.size(); i++) {

            obj_editor.putString("et_number_" + (i+8), lista.get(i));

        }
        obj_editor.commit();
    }


    private void GetNumbersFirebase() {

        firestoredb = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            DocumentReference docRef = firestoredb.collection("numbers").document("group2");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            String num1 = document.getString("num1");
                            String num2 = document.getString("num2");
                            String num3 = document.getString("num3");
                            String num4 = document.getString("num4");
                            String num5 = document.getString("num5");
                            String num6 = document.getString("num6");
                            String num7 = document.getString("num7");
                            String num8 = document.getString("num8");

                            List<String> data = new ArrayList<>();
                            //  for (int i = 0; i < 7; i++) {
                            //data.add("Android " + i);

                            data.add(num1);
                            data.add(num2);
                            data.add(num3);
                            data.add(num4);
                            data.add(num5);
                            data.add(num6);
                            data.add(num7);
                            data.add(num8);

                            initReyclerView(data);

                            Enable_btn();

                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Central Virtual")
                .setMessage("Se descartarán los cambios, ¿Desea Salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences settings = getSharedPreferences("datos", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();

                        Intent intent = new Intent(addNumbers2Activity.this, PrincipalActivity.class);
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

    private void initReyclerView(List<String> data) {

        mAdapter.addData(data);

        addItemTouchCallback(mRecyclerView);
    }

    private void addItemTouchCallback(RecyclerView recyclerView) {

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListenner() {
            @Override
            public void onMove(int oldPosition, int newPosition) {
                mAdapter.onMove(oldPosition, newPosition);
            }

            @Override
            public void swipe(int position, int direction) {
                mAdapter.swipe(position, direction);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}