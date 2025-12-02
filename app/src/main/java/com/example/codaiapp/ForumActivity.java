package com.example.codaiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.chip.Chip;

public class ForumActivity extends AppCompatActivity {

    private RecyclerView rvPosts;
    private List<Post> allPosts; // Lista completa
    private List<Post> filteredPosts; // Lista filtrada
    private LinearLayout emptyStateLayout;
    private ImageButton backButton;
    private PostAdapter postAdapter;

    // Chips de filtro
    private Chip chipAll, chipPython, chipCSS, chipKotlin;
    private String selectedCategory = "Todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        // Inicializar views
        rvPosts = findViewById(R.id.rvPosts);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        backButton = findViewById(R.id.backButton);

        // Inicializar chips de filtro
        chipAll = findViewById(R.id.chipAll);
        chipPython = findViewById(R.id.chipPython);
        chipCSS = findViewById(R.id.chipCss);
        chipKotlin = findViewById(R.id.chipKotlin);

        // Configurar listener do botão voltar
        backButton.setOnClickListener(v -> finish());

        setupRecyclerView();
        loadPosts();
        setupFilterChips();

        // Aplicar filtro inicial (Todos)
        filterByCategory("Todos");
    }

    private void setupRecyclerView() {
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadPosts() {
        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        // Adicionar posts de exemplo com categorias
        allPosts.add(new Post("Dúvida com Loop em Python",
                "Estou com dificuldades para entender como usar o loop 'for' com dicionários.",
                "Ana", "2 min atrás", "#Python", 5));

        allPosts.add(new Post("Como centralizar uma div?",
                "Qual é a forma mais moderna e eficiente de centralizar uma div com CSS?",
                "Carlos", "15 min atrás", "#CSS", 12));

        Post highlightedPost = new Post("Melhores práticas em Kotlin",
                "Discussão sobre as melhores práticas para desenvolvimento Android com Kotlin.",
                "Mariana", "1 hora atrás", "#Kotlin", 28);
        highlightedPost.setHighlighted(true);
        allPosts.add(highlightedPost);

        allPosts.add(new Post("Python para Data Science",
                "Quais bibliotecas Python são essenciais para começar em Data Science?",
                "Pedro", "2 horas atrás", "#Python", 18));

        allPosts.add(new Post("Flexbox vs Grid CSS",
                "Quando devo usar Flexbox e quando devo usar CSS Grid?",
                "Julia", "3 horas atrás", "#CSS", 22));

        allPosts.add(new Post("Coroutines no Kotlin",
                "Como funcionam as coroutines e quando usá-las?",
                "Lucas", "4 horas atrás", "#Kotlin", 15));

        // Inicializar adapter
        postAdapter = new PostAdapter(filteredPosts);
        rvPosts.setAdapter(postAdapter);
    }

    private void setupFilterChips() {
        chipAll.setOnClickListener(v -> filterByCategory("Todos"));
        chipPython.setOnClickListener(v -> filterByCategory("Python"));
        chipCSS.setOnClickListener(v -> filterByCategory("CSS"));
        chipKotlin.setOnClickListener(v -> filterByCategory("Kotlin"));
    }

    private void filterByCategory(String category) {
        selectedCategory = category;
        filteredPosts.clear();

        if (category.equals("Todos")) {
            // Mostrar todos os posts
            filteredPosts.addAll(allPosts);
        } else {
            // Filtrar posts pela categoria
            for (Post post : allPosts) {
                // Comparar com a categoria do post (remove # se necessário)
                String postCategory = post.getCategory().replace("#", "");
                if (postCategory.equals(category)) {
                    filteredPosts.add(post);
                }
            }
        }

        // Atualizar RecyclerView
        postAdapter.notifyDataSetChanged();

        // Atualizar estado visual dos chips
        updateChipStates();

        // Verificar estado vazio
        checkEmptyState();
    }

    private void updateChipStates() {
        // Remover seleção de todos
        chipAll.setChecked(false);
        chipPython.setChecked(false);
        chipCSS.setChecked(false);
        chipKotlin.setChecked(false);

        // Marcar o chip selecionado
        switch (selectedCategory) {
            case "Todos":
                chipAll.setChecked(true);
                break;
            case "Python":
                chipPython.setChecked(true);
                break;
            case "CSS":
                chipCSS.setChecked(true);
                break;
            case "Kotlin":
                chipKotlin.setChecked(true);
                break;
        }
    }

    private void checkEmptyState() {
        if (filteredPosts.isEmpty()) {
            rvPosts.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            rvPosts.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar posts quando voltar para a activity
        loadPosts();
        filterByCategory(selectedCategory);
    }
}