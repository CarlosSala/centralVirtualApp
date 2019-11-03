package com.example.macscanner.menu.addMac.addNumbers;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.macscanner.R;
import com.example.macscanner.menu.PrincipalActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class shareQrActivity extends AppCompatActivity {

    private Button btn_share, btn_restart;
    private ImageView imageView;
    private String data;
    private Bitmap bitmap;

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_qr);

        constraintLayout = findViewById(R.id.constraint_layout_shareQr);

        btn_share = findViewById(R.id.btn_share);
        btn_restart = findViewById(R.id.btn_restart);
        imageView = findViewById(R.id.imgv);

        // get data from sendDataActivity
        data = getIntent().getExtras().getString("data", "Qr");

        if (data != null) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);

                SharedPreferences settings = getSharedPreferences("datos", Context.MODE_PRIVATE);
                settings.edit().clear().commit();

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        // Return to addMacActivity
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Home();
            }
        });


        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // check SDK
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    // if SDF is lower to android M, the permissions are declared in manifest

                }
                // check if permission was already granted
                if (ContextCompat.checkSelfPermission(shareQrActivity.this,
                        WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Share();

                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(shareQrActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Dialog();
                        //Toast.makeText(shareQrActivity.this, "Los permisos para acceder al almacenamiento externo son necesarios para guardar el codigo qr", Toast.LENGTH_SHORT).show();
                    }

                    ActivityCompat.requestPermissions(shareQrActivity.this,
                            new String[]{WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    if (MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE == 0) {

                    } else {
                        Share();
                    }
                }
            }
        });


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

           /* @Override
            protected void finalize() throws Throwable {
                super.finalize();

            }*/

            @Override
            public void run() {

                Snackbar
                        .make(constraintLayout, "La configuración se realizó correctamente", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //return;
                            }
                        })
                        .show();
            }
        }, 5000);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Manual_permissions();
            }
        }
    }

    private void Manual_permissions() {

        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(shareQrActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    private void Dialog() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(shareQrActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(shareQrActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, 100);
            }
        });
        dialogo.show();
    }


    // method to get Uri from Bitmap
    public String getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }

    //method to Share image
    public void Share() {

        Uri imageUri = Uri.parse(getImageUri(shareQrActivity.this, bitmap));
        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Código Qr generado");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        //shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    @Override
    public void onBackPressed() {

        AlertDialog();
        /*Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);*/
    }

/*    public void buttonPress(View v) {
        switch (v.getId()) {
            case R.id.btn_restart:

                break;

        }
    }*/

    private void AlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setTitle("Central Virtual")
                .setMessage("¿Desea Salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences settings = getSharedPreferences("datos", Context.MODE_PRIVATE);
                        settings.edit().clear().commit();


                        finishAffinity();
                        //Process.killProcess( Process.myPid() );
                        //finishAndRemoveTask();
                        //onDestroy();
                        //System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }


    private void Home() {

        SharedPreferences settings = getSharedPreferences("datos", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        Intent intent = new Intent(shareQrActivity.this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}