package com.example.macscanner.menu.addMac.addNumbers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.macscanner.R;
import com.example.macscanner.menu.PrincipalActivity;
import com.example.macscanner.menu.addMac.addMacActivity;

import java.util.ArrayList;

public class resumeAddNumbersActivity extends AppCompatActivity {

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8,
            tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv_mac1, tv_mac2,
            tv_id_community, tv_client_rut, tv_num_solicitud;

    private Button btn_next;
    private ArrayList<String> list = new ArrayList<String>();
    private String acumulacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_add_numbers);

        tv_num_solicitud = findViewById(R.id.tv_numSolicitud);
        tv_mac1 = findViewById(R.id.tv_indicator_mac1);
        tv_mac2 = findViewById(R.id.tv_indicator_mac2);
        tv1 = findViewById(R.id.tv_numberOne);
        tv2 = findViewById(R.id.tv_numberTwo);
        tv3 = findViewById(R.id.tv_numberThree);
        tv4 = findViewById(R.id.tv_numberFour);
        tv5 = findViewById(R.id.tv_numberFive);
        tv6 = findViewById(R.id.tv_numberSix);
        tv7 = findViewById(R.id.tv_numberSeven);
        tv8 = findViewById(R.id.tv_numberEight);
        tv9 = findViewById(R.id.tv_numberNine);
        tv10 = findViewById(R.id.tv_numberTen);
        tv11 = findViewById(R.id.tv_numberEleven);
        tv12 = findViewById(R.id.tv_numberTwelve);
        tv13 = findViewById(R.id.tv_numberThirteen);
        tv14 = findViewById(R.id.tv_numberFourteen);
        tv15 = findViewById(R.id.tv_numberFifteen);
        tv16 = findViewById(R.id.tv_numberSixteen);
        tv_id_community = findViewById(R.id.tv_community_id);
        tv_client_rut = findViewById(R.id.tv_client_rut);


        //numerosolicitud,rutcliente,idcomunidad,macbase1,numeroasociados,macbase2,numeroasoiados,

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        list.add(0, String.valueOf(preferences.getInt("NumSolicitud", 0)));
        list.add(1, preferences.getString("et_client_rut", ""));
        list.add(2, preferences.getString("et_community_id", ""));
        list.add(3, preferences.getString("mac_scanned1", ""));
        list.add(4, preferences.getString("et_number_0", ""));
        list.add(5, preferences.getString("et_number_1", ""));
        list.add(6, preferences.getString("et_number_2", ""));
        list.add(7, preferences.getString("et_number_3", ""));
        list.add(8, preferences.getString("et_number_4", ""));
        list.add(9, preferences.getString("et_number_5", ""));
        list.add(10, preferences.getString("et_number_6", ""));
        list.add(11, preferences.getString("et_number_7", ""));
        list.add(12, preferences.getString("mac_scanned2", ""));
        list.add(13, preferences.getString("et_number_8", ""));
        list.add(14, preferences.getString("et_number_9", ""));
        list.add(15, preferences.getString("et_number_10", ""));
        list.add(16, preferences.getString("et_number_11", ""));
        list.add(17, preferences.getString("et_number_12", ""));
        list.add(18, preferences.getString("et_number_13", ""));
        list.add(19, preferences.getString("et_number_14", ""));
        list.add(20, preferences.getString("et_number_15", ""));

        tv_num_solicitud.setText(list.get(0));
        tv_mac1.setText(list.get(3));
        tv_mac2.setText(list.get(12));
        tv1.setText(list.get(4));
        tv2.setText(list.get(5));
        tv3.setText(list.get(6));
        tv4.setText(list.get(7));
        tv5.setText(list.get(8));
        tv6.setText(list.get(9));
        tv7.setText(list.get(10));
        tv8.setText(list.get(11));
        tv9.setText(list.get(13));
        tv10.setText(list.get(14));
        tv11.setText(list.get(15));
        tv12.setText(list.get(16));
        tv13.setText(list.get(17));
        tv14.setText(list.get(18));
        tv15.setText(list.get(19));
        tv16.setText(list.get(20));
        tv_id_community.setText(list.get(2));
        tv_client_rut.setText(list.get(1));

        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                acumulacion = "";

                //Iterator iterator = list.iterator();
                for (int i = 0; i < list.size(); i++) {

                    // if (list.get(i) != "") {
                    acumulacion += list.get(i) + ";";
                    // }
                }
                Intent intent = new Intent(resumeAddNumbersActivity.this, sendDataActivity.class);
                intent.putExtra("data", acumulacion);
                startActivity(intent);

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

                        Intent intent = new Intent(resumeAddNumbersActivity.this, PrincipalActivity.class);
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
