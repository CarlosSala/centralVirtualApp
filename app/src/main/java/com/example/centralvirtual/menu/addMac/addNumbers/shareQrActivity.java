package com.example.centralvirtual.menu.addMac.addNumbers;

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

import com.example.centralvirtual.R;
import com.example.centralvirtual.menu.PrincipalActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class shareQrActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    ImageView imageView;
    private String data;
    private Bitmap bitmap;
    private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_qr);

        constraintLayout = findViewById(R.id.constraint_layout_shareQr);

        Button btn_share = findViewById(R.id.btn_share);
        Button btn_restart = findViewById(R.id.btn_restart);
        imageView = findViewById(R.id.imgv);

        // get data from sendDataActivity
        data = getIntent().getExtras().getString("data", "Qr");

        // data from sharedPreferences is deleted
        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
        settings.edit().clear().apply();

        GenerateQR();

        // Return to StartActivity
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Share();
            }
        });
    }

    private void GenerateQR() {

        if (data != null) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    private void Home() {

        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
        settings.edit().clear().apply();

        Intent intent = new Intent(shareQrActivity.this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void Share() {

        // check SDK
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            // if SDF is lower to android M, the permissions are declared in manifest
        } else {
            // check if permission was already granted
            if (ContextCompat.checkSelfPermission(shareQrActivity.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                //Share image
                Uri imageUri = Uri.parse(getImageUri(shareQrActivity.this, bitmap));
                Intent shareIntent = new Intent();

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Código QR generado de respaldo");
                shareIntent.putExtra(Intent.EXTRA_TEXT, data);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                //shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "send"));

            } else {

                        /*if (ActivityCompat.shouldShowRequestPermissionRationale(shareQrActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //explain why permissions are necessary
                            // Dialog();
                        }*/

                // It is applying permissions, there is to see the result
                ActivityCompat.requestPermissions(shareQrActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    // method to get Uri from Bitmap
    public String getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Share();

            } else {
                Manual_permissions();
            }
        }
    }

    private void Manual_permissions() {

        final CharSequence[] options = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(shareQrActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("si")) {
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

    private void Done() {

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

    /*private void Dialog() {

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
    }*/


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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.LightAlertDialog);

        alertDialogBuilder
                .setTitle("Central Virtual")
                .setMessage("¿Desea Salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
                        settings.edit().clear().apply();

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
}