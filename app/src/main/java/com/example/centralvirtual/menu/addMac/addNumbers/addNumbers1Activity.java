package com.example.centralvirtual.menu.addMac.addNumbers;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.centralvirtual.CustomScannerActivity;
import com.example.centralvirtual.InterfaceApi;
import com.example.centralvirtual.ItemTouchListenner;
import com.example.centralvirtual.MainAdapter;
import com.example.centralvirtual.R;
import com.example.centralvirtual.SimpleItemTouchHelperCallback;
import com.example.centralvirtual.databinding.ActivityAddNumbers1Binding;
import com.example.centralvirtual.databinding.ActivityStartBinding;
import com.example.centralvirtual.menu.PrincipalActivity;
import com.example.centralvirtual.responseServiceNumber;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addNumbers1Activity extends AppCompatActivity {

    private ActivityAddNumbers1Binding binding;
    private final static String TAG = "addNumbers1Activity";
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers1);

        binding = ActivityAddNumbers1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.recyclerSample1.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MainAdapter();
        binding.recyclerSample1.setAdapter(mAdapter);

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        binding.tvAddMac1.setText(preferences.getString("mac_scanned1", ""));

        binding.btnScanMac1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customScanner();
            }
        });

        binding.btnNext1.setEnabled(false);

        binding.btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveData();
                Intent intent = new Intent(view.getContext(), addNumbers2Activity.class);
                startActivity(intent);
            }
        });

        GetPosts();

        Enable_btn();
    }

    private void GetPosts() {

        final String url = "https://my-json-server.typicode.com/CarlosSala/response-json/";

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Call<List<responseServiceNumber>> call = interfaceApi.getNumbers();

        call.enqueue(new Callback<List<responseServiceNumber>>() {
            @Override
            public void onResponse(Call<List<responseServiceNumber>> call, Response<List<responseServiceNumber>> response) {

                if (response.isSuccessful()) {

                    //     int code = response.code();
                    List<responseServiceNumber> postsList = response.body();

                    for (responseServiceNumber res : postsList) {

                        String num1 = res.getNum1();
                        String num2 = res.getNum2();
                        String num3 = res.getNum3();
                        String num4 = res.getNum4();
                        String num5 = res.getNum5();
                        String num6 = res.getNum6();
                        String num7 = res.getNum7();
                        String num8 = res.getNum8();

                        List<String> data = new ArrayList<>();

                        data.add(num1);
                        data.add(num2);
                        data.add(num3);
                        data.add(num4);
                        data.add(num5);
                        data.add(num6);
                        data.add(num7);
                        data.add(num8);

                        InitRecyclerView(data);

                        Enable_btn();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<responseServiceNumber>> call, Throwable t) {

                Log.e(TAG, t.getMessage());

            }
        });
    }


    private void InitRecyclerView(List<String> data) {

        mAdapter.addData(data);

        addItemTouchCallback(binding.recyclerSample1);
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


    // method to enable or disabled button
    private void Enable_btn() {

        if (mAdapter.getItemCount() == 8 && !binding.tvAddMac1.getText().toString().isEmpty()) {
            binding.btnNext1.setEnabled(true);
        } else
            binding.btnNext1.setEnabled(false);
    }


    public void SaveData() {

        List<String> list = mAdapter.getmData();

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferences.edit();

        for (int i = 0; i < list.size(); i++) {

            obj_editor.putString("et_number_" + i, String.valueOf(list.get(i)));
        }
        obj_editor.apply();
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

                binding.tvAddMac1.setText(result.getContents());

                SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor obj_editor = preferences.edit();
                obj_editor.putString("mac_scanned1", result.getContents());
                obj_editor.apply();

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
        //integrator.setPrompt("");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setTimeout(20000);
        integrator.addExtra("title", "ESCANEE LA MAC 1");
        integrator.initiateScan();
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

        super.onBackPressed();
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

                        Intent intent = new Intent(addNumbers1Activity.this, PrincipalActivity.class);
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
