package com.example.centralvirtual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.centralvirtual.databinding.ActivityAddNumbers1Binding;
import com.example.centralvirtual.databinding.ActivityCustomScannerBinding;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.Random;

public class CustomScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private ActivityCustomScannerBinding binding;
    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomScannerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        setSupportActionBar(binding.toolbar);

        String codeScanned = getIntent().getExtras().getString("title", "QR");

        //toolbar.setTitleTextColor(Color.rgb(200,200,200));
        setTitle(codeScanned);

        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.toolbar.setBackgroundColor(Color.rgb(45, 45, 45));


        binding.zxingBarcodeScanner.setTorchListener(this);


        // ViewfinderView viewfinderView = findViewById(R.id.zxing_viewfinder_view);

        Drawable img = this.getResources().getDrawable(R.drawable.ic_flash);
        img.setBounds(0, 0, 60, 60);
        binding.switchFlashlight.setCompoundDrawables(img, null, null, null);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            binding.switchFlashlight.setVisibility(View.GONE);
        }

        capture = new CaptureManager(this, binding.zxingBarcodeScanner);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        changeMaskColor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(binding.switchFlashlight.getText())) {
            binding.zxingBarcodeScanner.setTorchOn();
        } else {
            binding.zxingBarcodeScanner.setTorchOff();
        }
    }

    public void changeMaskColor(View view) {
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        // viewfinderView.setMaskColor(color);
    }

    @Override
    public void onTorchOn() {
        binding.switchFlashlight.setText(R.string.turn_off_flashlight);
        //  binding.switchFlashlight.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_flash_off));
        Drawable img = this.getResources().getDrawable(R.drawable.ic_flash_off);
        img.setBounds(0, 0, 60, 60);
        binding.switchFlashlight.setCompoundDrawables(img, null, null, null);

    }

    @Override
    public void onTorchOff() {
        binding.switchFlashlight.setText(R.string.turn_on_flashlight);

        // binding.switchFlashlight.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_flash));
        Drawable img = this.getResources().getDrawable(R.drawable.ic_flash);
        img.setBounds(0, 0, 60, 60);
        binding.switchFlashlight.setCompoundDrawables(img, null, null, null);
    }

}
