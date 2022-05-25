package com.example.hee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hee.FirebaseID;
import com.example.hee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private String nicname;
    private EditText mTitle,mDate, mContents;

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.post_title_edit);
        mDate = findViewById(R.id.post_date_edit);
        mContents = findViewById(R.id.post_contents_edit);

        findViewById(R.id.post_save_button).setOnClickListener(this);

        if (mAuth.getCurrentUser() != null) {
            mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult() != null) {
                        nicname = (String) task.getResult().getData().get(FirebaseID.nicname);
                    }
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (mAuth.getCurrentUser() !=null) {
            String postId = mStore.collection(FirebaseID.post).document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(FirebaseID.documentId, postId);
            data.put(FirebaseID.nicname, nicname);
            data.put(FirebaseID.title, mTitle.getText().toString());
            data.put(FirebaseID.date, mDate.getText().toString());
            data.put(FirebaseID.contents, mContents.getText().toString());
            data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
            data.put(FirebaseID.writedate, getTime());
            mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
            finish();
        }
    }


}