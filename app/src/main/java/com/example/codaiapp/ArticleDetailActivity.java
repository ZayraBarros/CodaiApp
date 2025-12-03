package com.example.codaiapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog; // Import necessário

import com.example.codaiapp.dao.ArticleDAO;
import com.example.codaiapp.model.Article;

public class ArticleDetailActivity extends AppCompatActivity {

    private TextView tvDetailTitle, tvDetailAuthorDate, tvDetailReadTime, tvDetailCategory, tvDetailContent;
    private ImageButton btnBack;
    // NOVO: Botão de deletar
    private ImageButton btnDeleteArticle;
    private ArticleDAO articleDAO;

    // Código de resultado para sinalizar exclusão bem-sucedida para ArticlesActivity
    public static final int RESULT_DELETED = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        articleDAO = new ArticleDAO(this);

        initComponents();
        btnBack.setOnClickListener(v -> finish());

        // CONECTAR LÓGICA DE DELEÇÃO
        btnDeleteArticle.setOnClickListener(v -> showDeleteConfirmationDialog());

        loadArticleDetails();
    }

    private void initComponents() {
        btnBack = findViewById(R.id.btnBackDetail);
        // NOVO: Inicializar o botão de deletar
        btnDeleteArticle = findViewById(R.id.btnDeleteArticle);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailAuthorDate = findViewById(R.id.tvDetailAuthorDate);
        tvDetailReadTime = findViewById(R.id.tvDetailReadTime);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailContent = findViewById(R.id.tvDetailContent);
    }

    private void loadArticleDetails() {
        long articleId = getIntent().getLongExtra(ArticlesActivity.EXTRA_ARTICLE_ID, -1);

        if (articleId != -1) {
            Article article = articleDAO.getArticleById(articleId);

            if (article != null) {
                tvDetailTitle.setText(article.getTitle());
                tvDetailContent.setText(article.getContent());

                String authorDateText = article.getAuthor() + " • " + article.getDate();
                tvDetailAuthorDate.setText(authorDateText);

                tvDetailReadTime.setText(String.format("%d min de leitura", article.getReadTime()));
                tvDetailCategory.setText(article.getCategory());

                // 🌟 CHAMADA DA CONTAGEM DE VISUALIZAÇÕES
                articleDAO.incrementViewCount(articleId);

            } else {
                Toast.makeText(this, "Artigo não encontrado.", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID do artigo inválido.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Exibe um diálogo de confirmação antes de deletar o artigo.
     */
    private void showDeleteConfirmationDialog() {
        long articleId = getIntent().getLongExtra(ArticlesActivity.EXTRA_ARTICLE_ID, -1);
        if (articleId == -1) return;

        new AlertDialog.Builder(this)
                .setTitle("Excluir Artigo")
                .setMessage("Tem certeza de que deseja excluir este artigo permanentemente? Esta ação é irreversível.")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    performDelete(articleId);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /**
     * Executa a operação de deleção e retorna à lista.
     */
    private void performDelete(long articleId) {
        boolean success = articleDAO.deleteArticle(articleId);

        if (success) {
            Toast.makeText(this, "Artigo excluído com sucesso!", Toast.LENGTH_SHORT).show();

            // Sinaliza para ArticlesActivity que uma deleção ocorreu
            setResult(RESULT_DELETED);

            finish();
        } else {
            Toast.makeText(this, "Falha ao excluir o artigo.", Toast.LENGTH_LONG).show();
        }
    }
}