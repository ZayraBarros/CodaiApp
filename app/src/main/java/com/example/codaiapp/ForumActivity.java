package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codaiapp.dao.ForumDAO;
import com.example.codaiapp.model.Post; // <-- USANDO POST

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.chip.Chip;

public class ForumActivity extends AppCompatActivity
        implements PostAdapter.OnPostClickListener {

    private static final int REQUEST_CODE_CREATE_POST = 2001;
    public static final String EXTRA_POST_ID = "extra_post_id";

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private ForumDAO forumDAO;
    private Button btnCreatePost;

    private List<Post> allPosts; // <-- USANDO POST
    private List<Post> filteredPosts; // <-- USANDO POST
    private LinearLayout emptyStateLayout;
    private ImageButton backButton;

    // Chips de filtro
    private Chip chipAll, chipPython, chipCSS, chipKotlin;
    private String selectedCategory = "Todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        forumDAO = new ForumDAO(this);

        initComponents();
        setupRecyclerView();
        setupFilterChips();

        backButton.setOnClickListener(v -> finish());

        btnCreatePost.setOnClickListener(v -> {
            // Certifique-se que o nome da classe é CreateForumPostActivity (ou CreatePostActivity se você o renomeou)
            Intent intent = new Intent(ForumActivity.this, CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CREATE_POST);
        });

        loadPosts();
        filterByCategory(selectedCategory);
    }

    private void initComponents() {
        rvPosts = findViewById(R.id.rvPosts);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        backButton = findViewById(R.id.backButton);
        btnCreatePost = findViewById(R.id.btnCreatePost);

        chipAll = findViewById(R.id.chipAll);
        chipPython = findViewById(R.id.chipPython);
        chipCSS = findViewById(R.id.chipCss);
        chipKotlin = findViewById(R.id.chipKotlin);
    }

    private void setupRecyclerView() {
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        allPosts = new ArrayList<>();
        filteredPosts = new ArrayList<>();

        postAdapter = new PostAdapter(filteredPosts, this);
        rvPosts.setAdapter(postAdapter);
    }

    private void loadPosts() {
        allPosts.clear();
        allPosts.addAll(forumDAO.getAllPosts());

        if (allPosts.isEmpty()) {
            addMockData();
        }
    }

    private void addMockData() {
        // <-- USANDO POST
        allPosts.add(new Post("Dúvida Loop", "...", "Ana", "2 min atrás", "Python", 5));
        allPosts.add(new Post("Centralizar div", "...", "Carlos", "15 min atrás", "CSS", 12));
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
            filteredPosts.addAll(allPosts);
        } else {
            for (Post post : allPosts) { // <-- USANDO POST
                String postCategory = post.getCategory().replace("#", "");
                if (postCategory.equals(category)) {
                    filteredPosts.add(post);
                }
            }
        }

        postAdapter.notifyDataSetChanged();
        updateChipStates();
        checkEmptyState();
    }

    private void updateChipStates() {
        chipAll.setChecked(selectedCategory.equals("Todos"));
        chipPython.setChecked(selectedCategory.equals("Python"));
        chipCSS.setChecked(selectedCategory.equals("CSS"));
        chipKotlin.setChecked(selectedCategory.equals("Kotlin"));
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
    public void onPostClick(long postId) {
        Intent intent = new Intent(ForumActivity.this, PostDetailActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();
        filterByCategory(selectedCategory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CREATE_POST && resultCode == RESULT_OK) {
            loadPosts();
            filterByCategory(selectedCategory);
        }
    }
}