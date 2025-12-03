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

import com.example.codaiapp.dao.ArticleDAO;
import com.example.codaiapp.model.Article;
import com.google.android.material.chip.Chip;

// 1. Implementa a interface de clique do Adapter
public class ArticlesActivity extends AppCompatActivity
        implements ArticleAdapter.OnArticleClickListener {

    private RecyclerView rvArticles;
    private List<Article> allArticles;
    private List<Article> filteredArticles;
    private LinearLayout emptyStateLayout;
    private ImageButton backButton;
    private ArticleAdapter articleAdapter;

    private ArticleDAO articleDAO;

    private Chip chipAll, chipPython, chipUIUX;
    private String selectedCategory = "Todos";

    // Constante para a chave do Intent
    public static final String EXTRA_ARTICLE_ID = "extra_article_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        articleDAO = new ArticleDAO(this);

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
        setupFilterChips();

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

        // 2. Inicializa o Adapter, passando 'this' como o listener
        if (articleAdapter == null) {
            articleAdapter = new ArticleAdapter(filteredArticles, this);
            rvArticles.setAdapter(articleAdapter);
        }

        allArticles.clear();
        allArticles.addAll(loadArticlesFromDatabase());
    }

    private List<Article> loadArticlesFromDatabase() {
        return articleDAO.getAllArticles();
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
            filteredArticles.addAll(allArticles);
        } else {
            for (Article article : allArticles) {
                String articleCategory = article.getCategory().replace("#", "");
                if (articleCategory.equals(category)) {
                    filteredArticles.add(article);
                }
            }
        }

        articleAdapter.notifyDataSetChanged();
        updateChipStates();
        checkEmptyState();
    }

    private void updateChipStates() {
        chipAll.setChecked(false);
        chipPython.setChecked(false);
        chipUIUX.setChecked(false);

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
        loadArticles();
        filterByCategory(selectedCategory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadArticles();
            filterByCategory(selectedCategory);
        }
    }

    // 3. Implementação do método da interface OnArticleClickListener
    @Override
    public void onArticleClick(long articleId) {
        // Inicia a nova Activity de detalhes, passando o ID do artigo clicado
        Intent intent = new Intent(ArticlesActivity.this, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE_ID, articleId);
        startActivity(intent);
    }
}