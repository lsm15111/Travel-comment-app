package com.example.hee.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hee.FirebaseID;
import com.example.hee.RecyclerViewItemClickListener;
import com.example.hee.activity.MyPost2Activity;
import com.example.hee.activity.MyPostActivity;
import com.example.hee.activity.Post2Activity;
import com.example.hee.databinding.FragmentMyListBinding;
import com.example.hee.models.MyList;
import com.example.hee.view.adapter.MyListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyListFragment extends Fragment implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private RecyclerView mListRecycleView;
    private MyListAdapter mAdapter;
    private List<MyList> mDatas;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String usernicname = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentMyListBinding binding = FragmentMyListBinding.inflate(inflater);
        binding.listPostEdit.setOnClickListener(this);
        mListRecycleView = binding.listRecyclerview;


        mListRecycleView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), mListRecycleView, this));


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        String nic = "";
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.user).document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult() != null) {
                            usernicname = (String) task.getResult().getData().get(FirebaseID.nicname);
                        }
                    }
                });
        mStore.collection(FirebaseID.mylist)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                mDatas.clear();
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String nicname = String.valueOf(shot.get(FirebaseID.nicname));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String date = String.valueOf(shot.get(FirebaseID.date));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String writedate = String.valueOf(shot.get(FirebaseID.writedate));
                                MyList data = new MyList(documentId, nicname, title, date, contents, writedate);
                                if (data.getNicname().contains(usernicname)) {
                                    mDatas.add(data);
                                }
                            }

                            mAdapter = new MyListAdapter(mDatas);
                            mListRecycleView.setAdapter(mAdapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), MyPostActivity.class));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), MyPost2Activity.class);
        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("삭제 하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.mylist).document(mDatas.get(position).getDocumentId()).delete();
                Toast.makeText(getActivity(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림");
        dialog.show();
    }

}