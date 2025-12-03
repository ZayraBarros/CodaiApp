package com.example.codaiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import corrigido para o modelo ForumPost (é crucial que o modelo tenha este nome)
import com.example.codaiapp.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> posts; // Usando ForumPost para consistência de dados
    private final OnPostClickListener listener; // NOVO: Listener de clique

    // 1. Definição da Interface de Clique
    public interface OnPostClickListener {
        void onPostClick(long postId);
    }

    // 2. Construtor com Listener
    public PostAdapter(List<Post> posts, OnPostClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.tvPostTitle.setText(post.getTitle());
        holder.tvPostContent.setText(post.getContent());
        holder.tvCategoryTag.setText(post.getCategory());
        holder.tvAuthor.setText(post.getAuthor());
        holder.tvDate.setText(post.getDate());

        // Assumindo que você usará getReplyCount ou renomeou getCommentCount no modelo
        holder.tvComments.setText(String.format("%d resposta(s)", post.getReplyCount()));

        // Lógica de destaque
        if (post.isHighlighted()) {
            // TODO: Adicionar estilo de destaque
        }

        // 3. Implementação do Clique (usa o ID do post)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPostClick(post.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostTitle, tvPostContent, tvCategoryTag, tvAuthor, tvDate, tvComments;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            tvCategoryTag = itemView.findViewById(R.id.tvCategoryTag);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvComments = itemView.findViewById(R.id.tvComments);
        }
    }
}