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
import com.google.android.material.chip.Chip;

public class ArticlesActivity extends AppCompatActivity {

    private RecyclerView rvArticles;
    private List<Article> allArticles; // Lista completa
    private List<Article> filteredArticles; // Lista filtrada
    private LinearLayout emptyStateLayout;
    private ImageButton backButton;
    private ArticleAdapter articleAdapter;

    // Chips de filtro
    private Chip chipAll, chipPython, chipUIUX;
    private String selectedCategory = "Todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        // Inicializar views
        rvArticles = findViewById(R.id.rvArticles);
        emptyStateLayout = findViewById(R.id.emptyStateArticlesLayout);
        backButton = findViewById(R.id.backButton);
        Button btnWriteArticle = findViewById(R.id.btnWriteArticle);

        // Inicializar chips de filtro
        chipAll = findViewById(R.id.chipAllArticles);
        chipPython = findViewById(R.id.chipPythonArticles);
        chipUIUX = findViewById(R.id.chipUiUxArticles);

        // Configurar listeners
        btnWriteArticle.setOnClickListener(v -> {
            Intent intent = new Intent(ArticlesActivity.this, CreateArticleActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());

        setupRecyclerView();
        loadArticles();
        setupFilterChips();

        // Aplicar filtro inicial (Todos)
        filterByCategory("Todos");
    }

    private void setupRecyclerView() {
        rvArticles.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadArticles() {
        allArticles = new ArrayList<>();
        filteredArticles = new ArrayList<>();

        // Adicione artigos com as categorias corretas
        allArticles.add(new Article("Intro a Python", "Aprenda os conceitos básicos...",
                "Leo", "Ontem", "#Python", 5, 150, false));

        allArticles.add(new Article("Design Systems", "Como criar um design system do zero...",
                "Sofia", "2 dias atrás", "#UI/UX", 10, 450, true));

        allArticles.add(new Article("Flexbox vs Grid", "Quando usar Flexbox e quando usar Grid no CSS.",
                "Miguel", "3 dias atrás", "#CSS", 8, 320, false));

        allArticles.add(new Article("State Management", "Gerenciamento de estado em apps modernos.",
                "Julia", "4 dias atrás", "#Kotlin", 12, 600, false));

        allArticles.add(new Article("Python Avançado", "Decorators e generators em Python.",
                "Carlos", "5 dias atrás", "#Python", 15, 800, false));

        allArticles.add(new Article("UI Design Trends", "Tendências de design para 2024.",
                "Ana", "1 semana atrás", "#UI/UX", 20, 950, false));

        // Inicializar adapter
        articleAdapter = new ArticleAdapter(filteredArticles);
        rvArticles.setAdapter(articleAdapter);
    }

    private void setupFilterChips() {
        chipAll.setOnClickListener(v -> filterByCategory("Todos"));
        chipPython.setOnClickListener(v -> filterByCategory("Python"));
        chipUIUX.setOnClickListener(v -> filterByCategory("UI/UX"));
    }

    private void filterByCategory(String category) {
        selectedCategory = category;
        filteredArticles.clear();

        if (category.equals("Todos")) {
            // Mostrar todos os artigos
            filteredArticles.addAll(allArticles);
        } else {
            // Filtrar artigos pela categoria
            for (Article article : allArticles) {
                // Comparar com a categoria do artigo (remove # se necessário)
                String articleCategory = article.getCategory().replace("#", "");
                if (articleCategory.equals(category)) {
                    filteredArticles.add(article);
                }
            }
        }

        // Atualizar RecyclerView
        articleAdapter.notifyDataSetChanged();

        // Atualizar estado visual dos chips
        updateChipStates();

        // Verificar estado vazio
        checkEmptyState();
    }

    private void updateChipStates() {
        // Remover seleção de todos
        chipAll.setChecked(false);
        chipPython.setChecked(false);
        chipUIUX.setChecked(false);

        // Marcar o chip selecionado
        switch (selectedCategory) {
            case "Todos":
                chipAll.setChecked(true);
                break;
            case "Python":
                chipPython.setChecked(true);
                break;
            case "UI/UX":
                chipUIUX.setChecked(true);
                break;
        }
    }

    private void checkEmptyState() {
        if (filteredArticles.isEmpty()) {
            rvArticles.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            rvArticles.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar artigos quando voltar para a activity
        loadArticles();
        filterByCategory(selectedCategory);
    }
}