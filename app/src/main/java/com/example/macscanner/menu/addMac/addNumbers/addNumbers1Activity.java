package com.example.macscanner.menu.addMac.addNumbers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class addNumbers1Activity extends AppCompatActivity {

    private Button btn_scan_mac, btn_next;
    private TextView tv_mac;
    private EditText et1, et2, et3, et4, et5, et6, et7, et8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers1);


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
                Guardar(view);
                Intent intent = new Intent(addNumbers1Activity.this, addNumbers2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        et1 = (EditText)findViewById(R.id.et_number_one);
        et2 = (EditText)findViewById(R.id.et_number_two);
        et3 = (EditText)findViewById(R.id.et_number_three);
        et4 = (EditText)findViewById(R.id.et_number_four);
        et5 = (EditText)findViewById(R.id.et_number_five);
        et6 = (EditText)findViewById(R.id.et_number_six);
        et7 = (EditText)findViewById(R.id.et_number_seven);
        et8 = (EditText)findViewById(R.id.et_number_eight);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        et1.setText(preferences.getString("et_number_one", ""));
        et2.setText(preferences.getString("et_number_two", ""));
        et3.setText(preferences.getString("et_number_three", ""));
        et4.setText(preferences.getString("et_number_four", ""));
        et5.setText(preferences.getString("et_number_five", ""));
        et6.setText(preferences.getString("et_number_six", ""));
        et7.setText(preferences.getString("et_number_seven", ""));
        et8.setText(preferences.getString("et_number_eight", ""));
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
                obj_editor.putString("mac_scanned1", result.getContents());
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
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
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
        obj_editor.putString("et_number_one", et1.getText().toString());
        obj_editor.putString("et_number_two", et2.getText().toString());
        obj_editor.putString("et_number_three", et3.getText().toString());
        obj_editor.putString("et_number_four", et4.getText().toString());
        obj_editor.putString("et_number_five", et5.getText().toString());
        obj_editor.putString("et_number_six", et6.getText().toString());
        obj_editor.putString("et_number_seven", et7.getText().toString());
        obj_editor.putString("et_number_eight", et8.getText().toString());
        obj_editor.commit();
        finish();
    }
}
