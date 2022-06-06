package com.example.hee.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hee.FirebaseID;
import com.example.hee.R;

import java.util.List;

import com.example.hee.models.MyList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListViewHolder> {

    private List<MyList> datas;


    public MyListAdapter(List<MyList> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {
        MyList data = datas.get(position);

        holder.nicname.setText("작성자:" + data.getNicname());
        holder.title.setText(data.getTitle());
        holder.date.setText(data.getDate());
        holder.writedate.setText(data.getWritedate());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyListViewHolder extends RecyclerView.ViewHolder {

        private TextView nicname, title, date, writedate;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);


            nicname = itemView.findViewById(R.id.item_my_list_nicname);
            title = itemView.findViewById(R.id.item_my_list_title);
            date = itemView.findViewById(R.id.item_my_list_date);
            writedate = itemView.findViewById(R.id.item_my_list_writedate);
        }
    }

}
