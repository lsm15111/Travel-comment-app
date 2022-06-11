package com.example.hee.fragment;

import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hee.FirebaseID;
import com.example.hee.R;
import com.example.hee.RecyclerViewItemClickListener;
import com.example.hee.activity.Post2Activity;
import com.example.hee.databinding.FragmentSelectBinding;
import com.example.hee.models.Post;
import com.example.hee.view.adapter.SelectAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectFragment extends Fragment implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mPostRecycleView;
    private SelectAdapter mAdapter;
    private List<Post> mDatas;

    private EditText mword;
    private Button input1 ;
    private InputMethodManager imm;

    int searchOption =0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSelectBinding binding = FragmentSelectBinding.inflate(inflater);
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);


        mPostRecycleView = binding.recyclerview;
        Spinner spinner = binding.spinner;
        binding.searchBtn.setOnClickListener(this);
        mword = binding.searchWord;
        input1 = binding.searchBtn;
        mPostRecycleView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), mPostRecycleView, this));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    searchOption = 0;
                else if (position == 1)
                    searchOption = 1;
                else if (position == 2)
                    searchOption = 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return binding.getRoot();
    }



    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), Post2Activity.class);
        intent.putExtra(FirebaseID.documentId, mDatas.get(position).getDocumentId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
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
                                if (searchOption == 0 && String.valueOf(shot.get(FirebaseID.title)).contains(mword.getText().toString())) {
                                    mDatas.add(data);
                                }else if(searchOption == 1 && String.valueOf(shot.get(FirebaseID.nicname)).contains(mword.getText().toString())){
                                    mDatas.add(data);
                                }else if(searchOption == 2 && String.valueOf(shot.get(FirebaseID.date)).contains(mword.getText().toString())){
                                    mDatas.add(data);
                                }
                            }
                            mAdapter = new SelectAdapter(mDatas);
                            mPostRecycleView.setAdapter(mAdapter);
                        }
                    }
                });
        imm.hideSoftInputFromWindow(input1.getWindowToken(), 0);


    }
}