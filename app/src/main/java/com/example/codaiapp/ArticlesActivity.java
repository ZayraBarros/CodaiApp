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
            startActivityForResult(intent, 1001);
        });

        backButton.setOnClickListener(v -> finish());

        setupRecyclerView();
        loadArticles();
        filterByCategory("Todos");
        setupFilterChips();

        // Aplicar filtro inicial (Todos)
        filterByCategory("Todos");
    }

    private void setupRecyclerView() {
        rvArticles.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadArticles() {
        if (allArticles == null) {
            allArticles = new ArrayList<>();
        }

        if (filteredArticles == null) {
            filteredArticles = new ArrayList<>();
        }

        if (articleAdapter == null) {
            articleAdapter = new ArticleAdapter(filteredArticles);
            rvArticles.setAdapter(articleAdapter);
        }

        // recarregar lista atual para o filtro funcionar
        filteredArticles.clear();
        filteredArticles.addAll(allArticles);
        articleAdapter.notifyDataSetChanged();
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
        loadArticles();            // Recarrega tudo do banco
        filterByCategory(selectedCategory);  // Depois aplica o filtro
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {

            String title = data.getStringExtra("title");
            String excerpt = data.getStringExtra("excerpt");
            int readTime = data.getIntExtra("readTime", 0);
            String category = data.getStringExtra("category");
            boolean featured = data.getBooleanExtra("featured", false);

            Article newArticle = new Article(
                    title,
                    excerpt,
                    "Você",
                    "Hoje",
                    category,
                    readTime,
                    0,
                    featured
            );

            allArticles.add(0, newArticle);   // adiciona no início da lista
            filterByCategory(selectedCategory); // atualiza filtro atual
        }
    }
}