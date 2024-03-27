package com.example.centralvirtual.menu.addMac.addNumbers;

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

import com.example.centralvirtual.R;
import com.example.centralvirtual.databinding.ActivityAddNumbers1Binding;
import com.example.centralvirtual.databinding.ActivityResumeAddNumbersBinding;
import com.example.centralvirtual.menu.PrincipalActivity;

import java.util.ArrayList;

public class resumeAddNumbersActivity extends AppCompatActivity {

    private ActivityResumeAddNumbersBinding binding;
    private final ArrayList<String> list = new ArrayList<String>();
    private String accumulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResumeAddNumbersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //order fields
        //applicationNumber,client_rut,community_id,mac_scanned1,associated_numbers,mac_scanned2,associated_numbers

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        list.add(0, String.valueOf(preferences.getInt("applicationNumber", 0)));
        list.add(1, preferences.getString("client_rut", ""));
        list.add(2, preferences.getString("community_id", ""));
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

        binding.tvApplicationNumber.setText(list.get(0));
        binding.tvIndicatorMac1.setText(list.get(3));
        binding.tvIndicatorMac2.setText(list.get(12));
        binding.tvNumberOne.setText(list.get(4));
        binding.tvNumberTwo.setText(list.get(5));
        binding.tvNumberThree.setText(list.get(6));
        binding.tvNumberFour.setText(list.get(7));
        binding.tvNumberFive.setText(list.get(8));
        binding.tvNumberSix.setText(list.get(9));
        binding.tvNumberSeven.setText(list.get(10));
        binding.tvNumberEight.setText(list.get(11));
        binding.tvNumberNine.setText(list.get(13));
        binding.tvNumberTen.setText(list.get(14));
        binding.tvNumberEleven.setText(list.get(15));
        binding.tvNumberTwelve.setText(list.get(16));
        binding.tvNumberThirteen.setText(list.get(17));
        binding.tvNumberFourteen.setText(list.get(18));
        binding.tvNumberFifteen.setText(list.get(19));
        binding.tvNumberSixteen.setText(list.get(20));
        binding.tvCommunityId.setText(list.get(2));
        binding.tvClientRut.setText(list.get(1));


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accumulation = "";

                //Iterator iterator = list.iterator();
                for (int i = 0; i < list.size(); i++) {

                    // if (list.get(i) != "") {
                    accumulation += (list.get(i) + ";");
                    // }
                }
                Intent intent = new Intent(resumeAddNumbersActivity.this, sendDataActivity.class);
                intent.putExtra("data", accumulation);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.LightAlertDialog);

        alertDialogBuilder
                .setTitle("Central Virtual")
                .setMessage("Se descartarán los cambios, ¿Desea Salir?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();

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
