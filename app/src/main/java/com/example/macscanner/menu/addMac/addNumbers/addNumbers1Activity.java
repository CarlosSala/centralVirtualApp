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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.macscanner.CustomScannerActivity;
import com.example.macscanner.InterfaceApi;
import com.example.macscanner.ItemTouchListenner;
import com.example.macscanner.MainAdapter;
import com.example.macscanner.R;
import com.example.macscanner.SimpleItemTouchHelperCallback;
import com.example.macscanner.menu.PrincipalActivity;
import com.example.macscanner.responseServiceNumber;
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

    private final static String TAG = "addNumbers1Activity";

    private Button btn_next;
    private TextView tv_mac;

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_numbers1);

        mRecyclerView = findViewById(R.id.recycler_sample1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MainAdapter();
        mRecyclerView.setAdapter(mAdapter);

        tv_mac = findViewById(R.id.tv_add_mac1);

        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        tv_mac.setText(preferences.getString("mac_scanned1", ""));

        Button btn_scan_mac = findViewById(R.id.btn_scan_mac1);
        btn_scan_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customScanner();
            }
        });

        btn_next = findViewById(R.id.btn_next1);
        btn_next.setEnabled(false);

        btn_next.setOnClickListener(new View.OnClickListener() {
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

                    int code = response.code();

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


    // method to enable or disabled button
    private void Enable_btn() {

        if (mAdapter.getItemCount() == 8 && !tv_mac.getText().toString().isEmpty()) {
            btn_next.setEnabled(true);
        } else
            btn_next.setEnabled(false);
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

                tv_mac.setText(result.getContents());

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
        integrator.addExtra("title", "MAC SCANNER");
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
