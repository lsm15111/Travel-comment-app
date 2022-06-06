package com.example.hee.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hee.R;
import com.example.hee.models.Post;

import java.util.List;

public class Post2Adapter extends RecyclerView.Adapter<Post2Adapter.Post2ViewHolder> {
    private List<Post.Comment> datas;

    public Post2Adapter(List<Post.Comment> datas) {
        this.datas = datas;
    }
    @NonNull
    @Override
    public Post2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Post2Adapter.Post2ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Post2ViewHolder holder, int position) {
        Post.Comment data = datas.get(position);
        holder.nicname.setText("작성자:" + data.getNicname());
        holder.comments.setText(data.getComments());
        holder.writedate.setText(data.getWritedate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Post2ViewHolder extends RecyclerView.ViewHolder {

        private TextView nicname, comments,writedate;

        public Post2ViewHolder(@NonNull View itemView) {
            super(itemView);

            nicname = itemView.findViewById(R.id.commentviewitem_userid);
            comments = itemView.findViewById(R.id.commentviewitem_comment);
            writedate = itemView.findViewById(R.id.commentviewitem_writedate);
        }
    }
}
