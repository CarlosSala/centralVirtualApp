package com.example.macscanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String AUTHOR_KEY = "author";
    public static final String QUOTE_KEY = "quote";
    public static final String TAG = "InspiringQuote";



    private DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("sampleData").document("inspiration");

    private Button button, button_firebase;
    private TextView textView;


    private TextView mQuoteTextView;
    private Button button_firebase2;


    @Override
    protected void onStart() {
        super.onStart();
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){

                    String quoteText =  documentSnapshot.getString(QUOTE_KEY);
                    String authorText =  documentSnapshot.getString(AUTHOR_KEY);
                    mQuoteTextView.setText("\"" + quoteText + "\" -- " + authorText);
                    //InspiringQuote myQuote = documentSnapshot.toObject(InspiringQuote);

            }else if (e != null){
                Log.w(TAG, "Got an exeception", e);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // FirebaseApp.initializeApp(this);
        button = findViewById(R.id.btn_scan);
        textView = findViewById(R.id.tv1);

        mQuoteTextView = (TextView)findViewById(R.id.tv2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customScanner(view);
            }
        });


        button_firebase = findViewById(R.id.btn_firebase);

        button_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuote(view);
            }
        });

        button_firebase2 = findViewById(R.id.btn_firebase2);

        button_firebase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchQuote(view);
            }
        });

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, QrActivity.class);
                intent.putExtra("codeScanned", result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void customScanner(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        integrator.setPrompt("");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setTimeout(20000);
        integrator.addExtra("title", "MACs SCANNER");
        integrator.initiateScan();
    }



    public void saveQuote(View view){

        EditText quoteView = (EditText)findViewById(R.id.editTextQuote);
        EditText authorView = (EditText)findViewById(R.id.editTextAuthor);

        String quoteText = quoteView.getText().toString();
        String authorText = authorView.getText().toString();


        if (quoteText.isEmpty() || authorText.isEmpty()){
            return;
        }

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(QUOTE_KEY, quoteText);
        dataToSave.put(AUTHOR_KEY, authorText);

        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not saved!", e);

            }
        });

    }

    public void fetchQuote(View view){

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){

                    String quoteText =  documentSnapshot.getString(QUOTE_KEY);
                    String authorText =  documentSnapshot.getString(AUTHOR_KEY);
                    mQuoteTextView.setText("\"" + quoteText + "\" -- " + authorText);
                    //InspiringQuote myQuote = documentSnapshot.toObject(InspiringQuote);
                }
            }
        });
    }




}
