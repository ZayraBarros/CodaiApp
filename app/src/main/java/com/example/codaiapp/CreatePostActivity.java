package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.dao.ForumDAO;
import com.example.codaiapp.model.Post;
import com.example.codaiapp.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    private EditText inputPostTitle, inputPostContent;
    private Spinner spinnerCategory;
    private Button btnSubmitPost, btnCancelPost;

    // Dependências
    private ForumDAO forumDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assumindo que o nome do layout é activity_create_forum_post
        setContentView(R.layout.activity_create_post);

        // Inicializa as dependências
        forumDAO = new ForumDAO(this);
        sessionManager = new SessionManager(this);

        initComponents();
        setupCategorySpinner();

        btnCancelPost.setOnClickListener(v -> finish());
        btnSubmitPost.setOnClickListener(v -> handleSubmit());
    }

    private void initComponents() {
        inputPostTitle = findViewById(R.id.inputPostTitle);
        inputPostContent = findViewById(R.id.inputPostContent);

        spinnerCategory = findViewById(R.id.spinnerPostCategory);

        btnSubmitPost = findViewById(R.id.btnSubmitPost);
        btnCancelPost = findViewById(R.id.btnCancelPost);
    }

    private void setupCategorySpinner() {
        String[] categories = {
                "Selecione a categoria",
                "Geral",
                "Dúvidas Técnicas",
                "Empregos",
                "Off Topic",
                "Feedback"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item, // Reutilizando layout de spinner existente
                categories
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item); // Reutilizando layout de dropdown existente
        spinnerCategory.setAdapter(adapter);
    }

    private void handleSubmit() {
        String title = inputPostTitle.getText().toString().trim();
        String content = inputPostContent.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty()) {
            inputPostTitle.setError("O título da postagem é obrigatório.");
            return;
        }

        if (content.isEmpty()) {
            inputPostContent.setError("O conteúdo da postagem é obrigatório.");
            return;
        }

        if (category.equals("Selecione a categoria")) {
            Toast.makeText(this, "Por favor, escolha uma categoria para o seu post.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 1. Preparar Dados Dinâmicos ---

        // Obter nome do autor logado
        String authorName = sessionManager.getUserName();
        // Nota: Idealmente, usaríamos o sessionManager.getUserId() aqui também.
        if (authorName.equals("Visitante") || authorName.isEmpty()) {
            Toast.makeText(this, "Erro: Faça login para postar no fórum.", Toast.LENGTH_LONG).show();
            return;
        }

        // Obter data formatada (ex: dd/MM/yyyy)
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // --- 2. Criar Objeto e Salvar no DB ---

        // Construtor: title, content, author, date, category, replyCount
        Post newPost = new Post(
                title,
                content,
                authorName,
                currentDate,
                category,
                0 // replyCount inicial
        );

        boolean success = forumDAO.insertPost(newPost);

        // --- 3. Finalizar e Retornar ---

        if (success) {
            Toast.makeText(this, "Postagem criada com sucesso!", Toast.LENGTH_SHORT).show();

            // Retorna RESULT_OK para que ForumActivity recarregue a lista
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Falha ao salvar a postagem no banco de dados.", Toast.LENGTH_LONG).show();
        }
    }
}