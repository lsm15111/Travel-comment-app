package com.example.hee.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hee.R;

import java.util.List;

import com.example.hee.models.Post;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.SelectViewHolder> {

    private List<Post> datas;

    public SelectAdapter(List<Post> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {

        Post data = datas.get(position);
        holder.nicname.setText("작성자:" + data.getNicname());
        holder.title.setText(data.getTitle());
        holder.date.setText(data.getDate());
        holder.writedate.setText(data.getWritedate());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class SelectViewHolder extends RecyclerView.ViewHolder {

        private TextView nicname, title, date , writedate;

        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);

            nicname = itemView.findViewById(R.id.item_post_nicname);
            title = itemView.findViewById(R.id.item_post_title);
            date = itemView.findViewById(R.id.item_post_date);
            writedate = itemView.findViewById(R.id.item_post_writedate);
        }
    }

}
