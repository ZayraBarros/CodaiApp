package com.example.codaiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codaiapp.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    // 1. Definição da Interface de Clique
    private OnArticleClickListener listener;

    // Interface para comunicação de clique
    public interface OnArticleClickListener {
        void onArticleClick(long articleId);
    }

    // 2. Construtor com Listener
    public ArticleAdapter(List<Article> articles, OnArticleClickListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvArticleTitle.setText(article.getTitle());
        holder.tvArticleExcerpt.setText(article.getExcerpt());
        holder.tvArticleCategoryTag.setText(article.getCategory());
        holder.tvArticleAuthor.setText(article.getAuthor());
        holder.tvArticleDate.setText(article.getDate());
        holder.tvArticleReadTime.setText(String.format("%d min", article.getReadTime()));
        holder.tvArticleViews.setText(String.format("%d visualizações", article.getViewCount()));

        if (article.isFeatured()) {
            // TODO: Adicionar estilo de destaque
        } else {
            // TODO: Remover estilo de destaque
        }

        // 3. Implementação do Clique no Item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Passamos o ID do artigo, que o ArticleDetailActivity usará para buscar o conteúdo
                listener.onArticleClick(article.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvArticleTitle, tvArticleExcerpt, tvArticleCategoryTag, tvArticleAuthor, tvArticleDate, tvArticleReadTime, tvArticleViews;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArticleTitle = itemView.findViewById(R.id.tvArticleTitle);
            tvArticleExcerpt = itemView.findViewById(R.id.tvArticleExcerpt);
            tvArticleCategoryTag = itemView.findViewById(R.id.tvArticleCategoryTag);
            tvArticleAuthor = itemView.findViewById(R.id.tvArticleAuthor);
            tvArticleDate = itemView.findViewById(R.id.tvArticleDate);
            tvArticleReadTime = itemView.findViewById(R.id.tvArticleReadTime);
            tvArticleViews = itemView.findViewById(R.id.tvArticleViews);
        }
    }
}