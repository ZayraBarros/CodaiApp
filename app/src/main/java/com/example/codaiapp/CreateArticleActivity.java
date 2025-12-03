
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

public class CreateArticleActivity extends AppCompatActivity {

    private EditText inputTitle, inputExcerpt, inputReadTime, inputContent;
    private Spinner spinnerCategory;
    private CheckBox checkFeatured;
    private Button btnSubmit, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

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

        // ❗ Valida a categoria
        if (category.equals("Selecione a categoria")) {
            Toast.makeText(this, "Por favor, escolha uma categoria.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 Adiciona o hashtag
        category = "#" + category;

        int readTime = Integer.parseInt(readTimeStr);

        // 🔥 Simulando criação de artigo
        Article newArticle = new Article(
                title,
                excerpt,
                "Você", // autor fixo
                "Hoje", // data temporária
                category,
                readTime,
                0,
                featured
        );

        // Resultado enviado de volta
        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("excerpt", excerpt);
        resultIntent.putExtra("readTime", readTime);
        resultIntent.putExtra("category", category);
        resultIntent.putExtra("featured", featured);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
