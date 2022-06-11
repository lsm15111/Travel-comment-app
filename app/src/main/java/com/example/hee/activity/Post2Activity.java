package com.example.hee.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hee.FirebaseID;
import com.example.hee.R;
import com.example.hee.RecyclerViewItemClickListener;
import com.example.hee.models.Post;
import com.example.hee.view.adapter.Post2Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post2Activity extends AppCompatActivity implements RecyclerViewItemClickListener.OnItemClickListener{

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RecyclerView mPost2RecyclerView;
    private Post2Adapter mAdapter;
    private List<Post.Comment> mDatas;

    private TextView mTitleText, mDateText,mContentsText, mNameText;
    private EditText mComment;
    private InputMethodManager imm;

    private TextView text ;

    private String id;
    private String nicname;


    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);



        mTitleText = findViewById(R.id.post2_title);
        mDateText = findViewById(R.id.post2_date);
        mContentsText = findViewById(R.id.post2_contents);
        mNameText = findViewById(R.id.post2_name);
        text = (TextView)findViewById(R.id.comment_edit_message);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mPost2RecyclerView = findViewById(R.id.comment_recyclerview);

        Intent getIntent = getIntent();
        id = getIntent.getStringExtra(FirebaseID.documentId);
        Log.e("ITEM DOCUMENT ID: ", id);
        mComment = findViewById(R.id.comment_edit_message);
        mPost2RecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(Post2Activity.this, mPost2RecyclerView, this));

        findViewById(R.id.comment_btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() !=null) {
                    String commentId = mStore.collection(FirebaseID.post).document(id).collection(FirebaseID.comment).document().getId();
                    Map<String, Object> data = new HashMap<>();
                    data.put(FirebaseID.documentId, commentId);
                    data.put(FirebaseID.nicname, nicname);
                    data.put(FirebaseID.comments, mComment.getText().toString());
                    data.put(FirebaseID.timestamp, FieldValue.serverTimestamp());
                    data.put(FirebaseID.writedate, getTime());
                    mStore.collection(FirebaseID.post).document(id).collection(FirebaseID.comment).document(commentId).set(data, SetOptions.merge());
                }
                text.setText(null);
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
            }
        });


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
        mStore.collection(FirebaseID.post).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
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
                                    mNameText.setText("작성자:"+name);
                                }
                            } else {
                                Toast.makeText(Post2Activity.this,"",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post).document(id).collection(FirebaseID.comment)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot2 = snap.getData();
                                String documentId = String.valueOf(shot2.get(FirebaseID.documentId));
                                String nicname =  String.valueOf(shot2.get(FirebaseID.nicname));
                                String comments = String.valueOf(shot2.get(FirebaseID.comments));
                                String writedate = String.valueOf(shot2.get(FirebaseID.writedate));
                                Post.Comment data2 = new Post.Comment(documentId,nicname, comments,writedate);
                                mDatas.add(data2);
                            }
                            mAdapter = new Post2Adapter(mDatas);
                            mPost2RecyclerView.setAdapter(mAdapter);
                        }
                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("삭제 하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.post).document(id).collection(FirebaseID.comment).document(mDatas.get(position).getDocumentId()).delete();
                Toast.makeText(Post2Activity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Post2Activity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림");
        dialog.show();
    }
}