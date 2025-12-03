package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.dao.ArticleDAO;
import com.example.codaiapp.model.Article;
import com.example.codaiapp.utils.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateArticleActivity extends AppCompatActivity {

    private EditText inputTitle, inputExcerpt, inputReadTime, inputContent;
    private Spinner spinnerCategory;
    private CheckBox checkFeatured;
    private Button btnSubmit, btnCancel;

    // Novas dependências
    private ArticleDAO articleDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        // Inicializa as dependências
        articleDAO = new ArticleDAO(this);
        sessionManager = new SessionManager(this);

        initComponents();
        setupCategorySpinner();

        btnCancel.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void initComponents() {
        inputTitle = findViewById(R.id.inputTitle);
        inputExcerpt = findViewById(R.id.inputExcerpt);
        inputReadTime = findViewById(R.id.inputReadTime);
        inputContent = findViewById(R.id.inputContent);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        checkFeatured = findViewById(R.id.checkFeatured);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupCategorySpinner() {
        String[] categories = {
                "Selecione a categoria",
                "JavaScript",
                "React",
                "Python",
                "CSS",
                "Git",
                "DataScience"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                categories
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }


    private void handleSubmit() {
        String title = inputTitle.getText().toString().trim();
        String excerpt = inputExcerpt.getText().toString().trim();
        String readTimeStr = inputReadTime.getText().toString().trim();
        String content = inputContent.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        boolean featured = checkFeatured.isChecked();

        if (title.isEmpty()) {
            inputTitle.setError("O título é obrigatório.");
            return;
        }

        if (readTimeStr.isEmpty()) {
            inputReadTime.setError("Informe o tempo de leitura");
            return;
        }

        if (content.isEmpty()) {
            inputContent.setError("Escreva o conteúdo do artigo");
            return;
        }

        if (category.equals("Selecione a categoria")) {
            Toast.makeText(this, "Por favor, escolha uma categoria.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 1. Preparar Dados Dinâmicos ---

        // Obter nome do autor logado
        String authorName = sessionManager.getUserName();
        if (authorName.equals("Visitante")) {
            Toast.makeText(this, "Erro: Faça login para postar.", Toast.LENGTH_LONG).show();
            return;
        }

        // Obter data formatada (ex: dd/MM/yyyy)
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // Adicionar o hashtag
        category = "#" + category;

        int readTime = Integer.parseInt(readTimeStr);

        // --- 2. Criar Objeto e Salvar no DB ---
        Article newArticle = new Article(
                title,
                excerpt,
                content, // <<< CONTEÚDO ADICIONADO AQUI
                authorName,
                currentDate,
                category,
                readTime,
                0, // viewCount inicial
                featured
        );

        boolean success = articleDAO.insertArticle(newArticle);

        // --- 3. Finalizar e Retornar ---

        if (success) {
            Toast.makeText(this, "Artigo criado com sucesso!", Toast.LENGTH_SHORT).show();

            // Retorna RESULT_OK para que ArticlesActivity recarregue a lista do banco
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Falha ao salvar o artigo no banco de dados.", Toast.LENGTH_LONG).show();
        }
    }
}