package com.example.macscanner.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.macscanner.LoginActivity;
import com.example.macscanner.menu.addMac.addMacActivity;
import com.example.macscanner.R;
import com.google.firebase.auth.FirebaseAuth;

public class principalActivity extends AppCompatActivity {

    private Button btn_add_mac, btn_logout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_add_mac = findViewById(R.id.btn_add_mac);

        btn_add_mac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(principalActivity.this, addMacActivity.class);
                startActivity(intent);
            }
        });

        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
    }

    private void Logout() {

        firebaseAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
