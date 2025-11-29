package com.example.codaiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.widget.Button;


public class ArticlesActivity extends AppCompatActivity {

    private RecyclerView rvArticles;
    private List<Article> articleList;
    private LinearLayout emptyStateLayout;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        rvArticles = findViewById(R.id.rvArticles);
        emptyStateLayout = findViewById(R.id.emptyStateArticlesLayout);
        backButton = findViewById(R.id.backButton);


        Button btnWriteArticle = findViewById(R.id.btnWriteArticle);

        btnWriteArticle.setOnClickListener(v -> {
            Intent intent = new Intent(ArticlesActivity.this, CreateArticleActivity.class);
            startActivity(intent);
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRecyclerView();
        loadArticles();
    }

    private void setupRecyclerView() {
        rvArticles.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadArticles() {
        articleList = new ArrayList<>();
        // Adicione artigos de exemplo
        articleList.add(new Article("Intro a Python", "Aprenda os conceitos básicos...", "Leo", "Ontem", "#Python", 5, 150, false));
        articleList.add(new Article("Design Systems", "Como criar um design system do zero...", "Sofia", "2 dias atrás", "#UI/UX", 10, 450, true)); // Destaque
        articleList.add(new Article("Flexbox vs Grid", "Quando usar Flexbox e quando usar Grid no CSS.", "Miguel", "3 dias atrás", "#CSS", 8, 320, false));
        articleList.add(new Article("State Management", "Gerenciamento de estado em apps modernos.", "Julia", "4 dias atrás", "#Kotlin", 12, 600, false));

        ArticleAdapter articleAdapter = new ArticleAdapter(articleList);
        rvArticles.setAdapter(articleAdapter);

        checkEmptyState();
    }

    private void checkEmptyState() {
        if (articleList.isEmpty()) {
            rvArticles.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            rvArticles.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}
