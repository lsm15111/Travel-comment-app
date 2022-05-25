package com.example.hee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hee.FirebaseID;
import com.example.hee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MyPost2Activity extends AppCompatActivity {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private TextView mTitleText, mDateText, mContentsText, mNameText;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post2);

        mTitleText = findViewById(R.id.my_post2_title);
        mDateText = findViewById(R.id.my_post2_date);
        mContentsText = findViewById(R.id.my_post2_contents);
        mNameText = findViewById(R.id.my_post2_name);

        Intent getIntent = getIntent();
        id = getIntent.getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID: ", id);

        mStore.collection(FirebaseID.mylist).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                if (task.getResult() != null) {
                                    Map<String, Object> snap = task.getResult().getData();
                                    String title = String.valueOf(snap.get(FirebaseID.title));
                                    String date = String.valueOf(snap.get(FirebaseID.date));
                                    String contents = String.valueOf(snap.get(FirebaseID.contents));
                                    String name = String.valueOf(snap.get(FirebaseID.nicname));

                                    mTitleText.setText(title);
                                    mDateText.setText(date);
                                    mContentsText.setText(contents);
                                    mNameText.setText(name);
                                }
                            } else {
                                Toast.makeText(MyPost2Activity.this, "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }
}