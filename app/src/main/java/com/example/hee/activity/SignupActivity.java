package com.example.hee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hee.FirebaseID;
import com.example.hee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNicnameText, mEmailText, mPasswordText;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNicnameText = findViewById(R.id.sign_nicname);
        mEmailText = findViewById(R.id.sign_email);
        mPasswordText = findViewById(R.id.sign_pass);

        findViewById(R.id.sign_success).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAuth.createUserWithEmailAndPassword(mEmailText.getText().toString(), mPasswordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           FirebaseUser user = mAuth.getCurrentUser();
                           if(user != null) {
                               Map<String, Object> userMap = new HashMap<>();
                               userMap.put(FirebaseID.documentId, user.getUid());
                               userMap.put(FirebaseID.nicname, mNicnameText.getText().toString());
                               userMap.put(FirebaseID.email, mEmailText.getText().toString());
                               userMap.put(FirebaseID.password, mPasswordText.getText().toString());
                               mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge());
                               Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                               finish();
                           }

                           } else {
                              Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}