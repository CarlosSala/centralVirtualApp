package com.example.macscanner.menu.addMac.addNumbers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.macscanner.R;
import com.example.macscanner.menu.addMac.addMacActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class shareQrActivity extends AppCompatActivity {

    private Button btn_share, btn_restart;
    private ImageView imageView;
    private String data;
    private Bitmap bitmap;

    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_qr);
        btn_share = findViewById(R.id.btn_share);
        btn_restart = findViewById(R.id.btn_restart);
        imageView = findViewById(R.id.imgv);

        data = getIntent().getExtras().getString("data", "QR");

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

        btn_share.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(shareQrActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(shareQrActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(shareQrActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        if (MY_PERMISSIONS_REQUEST_READ_CONTACTS == 0) {

                        } else {
                            share();
                        }

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    share();
                    // Permission has already been granted
                }
            }
        });

        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Delete_data();

                Intent intent = new Intent(shareQrActivity.this, addMacActivity.class);
                //intent.putExtra("data", acumulacion);
                startActivity(intent);
                finish();
            }
        });

    }

    // method to get Uri from Bitmap
    public String getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }

    //method to share image
    public void share() {

        Uri imageUri = Uri.parse(getImageUri(shareQrActivity.this, bitmap));
        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Código Qr generado");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        //shareIntent.setType("image/*");

        startActivity(Intent.createChooser(shareIntent, "send"));
    }


    public void Delete_data(){
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("et_client_rut", "");
        obj_editor.putString("et_community_id", "");
        obj_editor.putString("mac_scanned1", "");
        obj_editor.putString("mac_scanned2", "");
        obj_editor.putString("et_number_one", "");
        obj_editor.putString("et_number_two", "");
        obj_editor.putString("et_number_three", "");
        obj_editor.putString("et_number_four", "");
        obj_editor.putString("et_number_five", "");
        obj_editor.putString("et_number_six", "");
        obj_editor.putString("et_number_seven", "");
        obj_editor.putString("et_number_eight", "");
        obj_editor.putString("et_number_nine", "");
        obj_editor.putString("et_number_ten", "");
        obj_editor.putString("et_number_eleven", "");
        obj_editor.putString("et_number_twelve", "");
        obj_editor.putString("et_number_thirteen", "");
        obj_editor.putString("et_number_fourteen", "");
        obj_editor.putString("et_number_fifteen", "");
        obj_editor.putString("et_number_sixteen", "");
        obj_editor.commit();
    }
}
