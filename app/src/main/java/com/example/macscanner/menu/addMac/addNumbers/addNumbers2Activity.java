package com.example.macscanner.menu.addMac.addNumbers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macscanner.CustomScannerActivity;
import com.example.macscanner.R;
import com.example.macscanner.menu.PrincipalActivity;
import com.example.macscanner.menu.addMac.addMacActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Pattern;

public class addNumbers2Activity extends AppCompatActivity {

    private final static String TAG = "addNumbers1Activity";

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firestoredb;

    private Button btn_scan_mac, btn_next;
    private TextView tv_mac;
    private EditText et1, et2, et3, et4, et5, et6, et7, et8;
    private LinearLayoutCompat linearLayout;

    private int et_empty;
    private int et_valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers2);

        linearLayout = findViewById(R.id.ly_addNumbers2);

        tv_mac = findViewById(R.id.tv_mac);

        btn_scan_mac = findViewById(R.id.btn_scan_mac);
        btn_scan_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customScanner();
            }
        });

        btn_next = findViewById(R.id.btn_next);
        btn_next.setEnabled(false);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Save_data();
                Intent intent = new Intent(view.getContext(), resumeAddNumbersActivity.class);
                startActivity(intent);
            }
        });

        et1 = findViewById(R.id.et_number_one);
        et2 = findViewById(R.id.et_number_two);
        et3 = findViewById(R.id.et_number_three);
        et4 = findViewById(R.id.et_number_four);
        et5 = findViewById(R.id.et_number_five);
        et6 = findViewById(R.id.et_number_six);
        et7 = findViewById(R.id.et_number_seven);
        et8 = findViewById(R.id.et_number_eight);

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et3);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et4);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et5);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et6);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et7);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        et8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Validate_data(et8);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        et1.setText(preferences.getString("et_number_nine", ""));
        et2.setText(preferences.getString("et_number_ten", ""));
        et3.setText(preferences.getString("et_number_eleven", ""));
        et4.setText(preferences.getString("et_number_twelve", ""));
        et5.setText(preferences.getString("et_number_thirteen", ""));
        et6.setText(preferences.getString("et_number_fourteen", ""));
        et7.setText(preferences.getString("et_number_fifteen", ""));
        et8.setText(preferences.getString("et_number_sixteen", ""));
        tv_mac.setText(preferences.getString("mac_scanned2", ""));

        Enable_btn();
    }


    private void Validate_data(EditText num_editText) {

        Pattern patron = Pattern.compile("\\A[0-9]{9}\\Z");

        if (patron.matcher(num_editText.getText().toString()).matches()) {

            num_editText.setTextColor(Color.WHITE);
            Enable_btn();

        } else if (num_editText.getText().toString().isEmpty()) {
            Enable_btn();

        } else {
            num_editText.setTextColor(Color.RED);
            Enable_btn();
        }
    }


    // method to enable or disabled button
    private void Enable_btn() {

        et_empty = 0;
        et_valid = 0;

        Pattern patron = Pattern.compile("\\A[0-9]{9}\\Z");

        // get the number of elements within the linearLayout
        int count = linearLayout.getChildCount();

        for (int i = 1; i < count - 1; i += 2) {

            // Each iteration gets the number of the editText within linearLayout
            AppCompatEditText editText = (AppCompatEditText) linearLayout.getChildAt(i);

            if (patron.matcher(editText.getText().toString()).matches()) {
                et_valid += 1;

            } else if (editText.getText().toString().isEmpty()) {
                et_empty += 1;
            }
        }


        if ((et_valid + et_empty) == 8 && et_empty < 8 && !tv_mac.getText().toString().isEmpty()) {

            btn_next.setEnabled(true);

        } else if (et_empty == 8 && tv_mac.getText().toString().isEmpty()) {

            btn_next.setEnabled(true);

        } else {
            btn_next.setEnabled(false);
        }
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

                // GetNumbers();
                GetNumbersFirebase();

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
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_number_nine", et1.getText().toString());
        obj_editor.putString("et_number_ten", et2.getText().toString());
        obj_editor.putString("et_number_eleven", et3.getText().toString());
        obj_editor.putString("et_number_twelve", et4.getText().toString());
        obj_editor.putString("et_number_thirteen", et5.getText().toString());
        obj_editor.putString("et_number_fourteen", et6.getText().toString());
        obj_editor.putString("et_number_fifteen", et7.getText().toString());
        obj_editor.putString("et_number_sixteen", et8.getText().toString());
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

                            et1.setText(num1);
                            et2.setText(num2);
                            et3.setText(num3);
                            et4.setText(num4);
                            et5.setText(num5);
                            et6.setText(num6);
                            et7.setText(num7);
                            et8.setText(num8);

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
}