package com.example.hee.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.hee.activity.Post2Activity;
import com.example.hee.activity.PostActivity;
import com.example.hee.databinding.FragmentMyListBinding;
import com.example.hee.databinding.FragmentPostListBinding;
import com.example.hee.models.Post;
import com.example.hee.view.adapter.PostAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostListFragment extends Fragment implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener{

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private RecyclerView mPostRecycleView;

    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentMyListBinding binding = FragmentMyListBinding.inflate(inflater);
        binding.listPostEdit.setOnClickListener(this);
        mPostRecycleView = binding.listRecyclerview;

        mPostRecycleView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), mPostRecycleView, this));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String nicname = String.valueOf(shot.get(FirebaseID.nicname));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String date = String.valueOf(shot.get(FirebaseID.date));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                String writedate = String.valueOf(shot.get(FirebaseID.writedate));
                                Post data = new Post(documentId, nicname, title, date, contents, writedate);
                                mDatas.add(data);
                            }
                            mAdapter = new PostAdapter(mDatas);
                            mPostRecycleView.setAdapter(mAdapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) { startActivity(new Intent(getActivity(), PostActivity.class)); }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), Post2Activity.class);
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
                mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentId()).delete();
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