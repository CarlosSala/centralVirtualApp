package com.example.centralvirtual.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.centralvirtual.LoginActivity;
import com.example.centralvirtual.R;
import com.example.centralvirtual.menu.addMac.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "PrincipalActivity";

    private FirebaseAuth firebaseAuth;

    private DrawerLayout drawerLayout;

    private TextView tv_user_name, tv_user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // drawerLayout is the principal layout that contains everything
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // sidebar that appears and hides
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get access to elements of sidebar
        View header = navigationView.getHeaderView(0);
        tv_user_name = header.findViewById(R.id.tv_user_name);
        tv_user_email = header.findViewById(R.id.tv_user_email);

        GetUserData();

        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        Log.i("punto", "onCreate ocurrido");
    }


    private void GetUserData() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        if (user != null) {

            if (user.getEmail() != null) {

                String email = user.getEmail();

                DocumentReference docRef = firebaseFirestore.collection("users").document(email)
                        .collection("info").document("user_info");

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {

                                String name = document.getString("name");
                                String email = document.getString("email");

                                tv_user_name.setText(name);
                                tv_user_email.setText(email);

                                //Toast.makeText(PrincipalActivity.this, "Bienvenido " + name, Toast.LENGTH_LONG).show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        }
    }


    private void Logout() {

        firebaseAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Allow select an element of activity_menu_drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_about) {

        } else if (id == R.id.nav_close_session) {
            Logout();
        }

        return false;
    }

    // Lifecycle activity
    @Override
    protected void onStart() {
        super.onStart();

        Log.i("punto", "onStart ocurrido");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("punto", "onResume ocurrido");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("punto", "onStop ocurrido");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("punto", "onDestroy ocurrido");
    }

}

