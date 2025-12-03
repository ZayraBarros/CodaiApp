package com.example.codaiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codaiapp.model.Comment; // Certifique-se de que o modelo Comment está correto

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * 1. Cria a View Holder, inflando o layout do item.
     */
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // O layout precisa ser criado como res/layout/item_comment.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    /**
     * 2. Liga os dados do objeto Comment aos elementos da View.
     */
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.tvAuthor.setText(comment.getAuthor());
        holder.tvContent.setText(comment.getContent());
        holder.tvDate.setText("• " + comment.getDate()); // Adiciona o ponto separador

        // Futuro: Se houver lógica para Best Answer, implemente aqui
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * Método para atualizar a lista de comentários (chamado na PostDetailActivity)
     */
    public void updateComments(List<Comment> newComments) {
        this.comments.clear();
        this.comments.addAll(newComments);
        notifyDataSetChanged();
    }

    // --- ViewHolder Interna ---
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        // Views do item_comment.xml
        TextView tvAuthor;
        TextView tvContent;
        TextView tvDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.tvCommentAuthor);
            tvContent = itemView.findViewById(R.id.tvCommentContent);
            tvDate = itemView.findViewById(R.id.tvCommentDate);
        }
    }
}