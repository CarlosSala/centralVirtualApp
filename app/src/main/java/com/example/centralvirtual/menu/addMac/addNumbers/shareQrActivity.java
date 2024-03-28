package com.example.centralvirtual.menu.addMac.addNumbers;

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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.centralvirtual.R;
import com.example.centralvirtual.databinding.ActivityShareQrBinding;
import com.example.centralvirtual.menu.PrincipalActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class shareQrActivity extends AppCompatActivity {

    private ActivityShareQrBinding binding;
    private String data;
    private Bitmap bitmap;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShareQrBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // get data from sendDataActivity
        data = getIntent().getExtras().getString("data", "Qr");

        // data from sharedPreferences is deleted
        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
        settings.edit().clear().apply();

        GenerateQR();

        // Return to StartActivity
        binding.btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });

        binding.btnShare.setOnClickListener(new View.OnClickListener() {

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
                binding.imgv.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    // method to get Uri from Bitmap
    public String getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    }

    private void backHome() {

        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
        settings.edit().clear().apply();

        Intent intent = new Intent(shareQrActivity.this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void Share() {

/*        // Verificar si la versión del SDK es mayor o igual a 23 (Android 6.0) para manejar permisos en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Verificar si los permisos necesarios están otorgados
            if (!checkPermissions()) {
                // Si no están otorgados, solicitar permisos
                requestPermissions();
            } else {

                // if the permission is granted, realized actions
                Log.i("test", "the permissions are granted");
            }
        }*/

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

    }

    // Método para verificar si los permisos están otorgados
    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // Método para solicitar permisos
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                // execute actions


            } else {
                // at least one permission was denied

            }
        }

    }

    private void Manual_permissions() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permisos necesarios")
                .setMessage("Para utilizar todas las funciones de la aplicación, necesitas otorgar los permisos requeridos.")
                .setPositiveButton("Configuración", (dialog, which) -> openAppSettings())
                .setNegativeButton("Cancelar", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        AlertDialog();
    }

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