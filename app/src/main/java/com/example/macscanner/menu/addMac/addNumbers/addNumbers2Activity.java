package com.example.macscanner.menu.addMac.addNumbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macscanner.CustomScannerActivity;
import com.example.macscanner.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Pattern;

public class addNumbers2Activity extends AppCompatActivity {

    private TextView tv_mac;
    private Button btn_scan_mac, btn_next;
    private EditText et1, et2, et3, et4, et5, et6, et7, et8;

    private LinearLayoutCompat linearLayout;
    private boolean isAllFill;


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
                customScanner(view);
            }
        });


        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Validate_data();

                if (isAllFill) {

                    Guardar(view);
                    Intent intent = new Intent(view.getContext(), resumeAddNumbersActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
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
    }


    private void Validate_data() {

        // layout con los EditText

        // Obtiene el numero de EditText que contiene el layout
        int count = linearLayout.getChildCount();

        // Recorres todos los editText y si hay alguno vacio cambias el valor de la
        // variable isAllFill a false, lo que indica que aun hay editText vacios.

        Pattern patron = Pattern.compile("\\A[0-9]{9}\\Z");

        isAllFill = true;

        for (int i = 1; i < count - 1; i += 2) {

            // En cada iteración obtienes uno de los editText que se encuentran el
            // layout.
            AppCompatEditText editText = (AppCompatEditText) linearLayout.getChildAt(i);

            // Compruebas su el editText esta vacio.
            // if (editText.getText().toString().isEmpty()) {

            if (patron.matcher(editText.getText().toString()).matches() ||
                    editText.getText().toString().isEmpty()) {

                editText.setTextColor(Color.WHITE);

            } else {

                editText.setTextColor(Color.RED);
                isAllFill = false;
            }
                /*isAllFill = false;
                break;*/
        }
    }


    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                tv_mac.setText(result.getContents());

                SharedPreferences preferencias = getSharedPreferences( "datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor obj_editor = preferencias.edit();
                obj_editor.putString("mac_scanned2", result.getContents());
                obj_editor.commit();


              /*  Intent intent = new Intent(this, QrActivity.class);
                intent.putExtra("codeScanned", result.getContents());
                startActivity(intent);*/
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void customScanner(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.CODE_128);
        integrator.setPrompt("");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setTimeout(20000);
        integrator.addExtra("title", "MACs SCANNER");
        integrator.initiateScan();
    }

    public void Guardar(View view){
        SharedPreferences preferencias = getSharedPreferences( "datos", Context.MODE_PRIVATE);
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
}