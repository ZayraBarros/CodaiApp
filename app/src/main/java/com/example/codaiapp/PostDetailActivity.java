package com.example.codaiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog; // Importação necessária para o diálogo
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codaiapp.dao.CommentDAO;
import com.example.codaiapp.dao.ForumDAO;
import com.example.codaiapp.dao.UserDAO;
import com.example.codaiapp.model.Comment;
import com.example.codaiapp.model.Post;
import com.example.codaiapp.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    // Views de Post
    private TextView tvDetailTitle, tvDetailAuthor, tvDetailDate, tvDetailContent, tvDetailCategory;
    private ImageButton backButton;
    private ImageButton btnDeletePost; // NOVO: Botão de Exclusão

    // Views de Comentários
    private RecyclerView rvComments;
    private EditText etCommentInput;
    private ImageButton btnSendComment;
    private TextView tvCommentsCount;

    // DAOs, Adapters e Session Manager
    private ForumDAO forumDAO;
    private CommentDAO commentDAO;
    private UserDAO userDAO;
    private CommentAdapter commentAdapter;
    private SessionManager sessionManager;

    private long postId;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        forumDAO = new ForumDAO(this);
        commentDAO = new CommentDAO(this);
        userDAO = new UserDAO(this);
        sessionManager = new SessionManager(this);

        // Tenta obter o ID do usuário logado
        currentUserId = sessionManager.getUserId();

        // 1. Obter o Post ID do Intent
        postId = getIntent().getLongExtra(ForumActivity.EXTRA_POST_ID, -1);

        // Checagem de ID da Postagem e de Login
        if (postId == -1 || currentUserId == -1) {
            String errorMsg = (postId == -1) ? "ID da postagem não fornecido." : "Você precisa estar logado para comentar.";
            Toast.makeText(this, "Erro: " + errorMsg, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initComponents();
        setupCommentsRecyclerView();

        loadPostDetails(postId);

        backButton.setOnClickListener(v -> finish());
        btnSendComment.setOnClickListener(v -> sendComment());

        // Listener do botão de exclusão
        btnDeletePost.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void initComponents() {
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailAuthor = findViewById(R.id.tvDetailAuthor);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        backButton = findViewById(R.id.backButtonDetail);
        btnDeletePost = findViewById(R.id.btnDeletePost); // NOVO: Inicialização do botão de exclusão

        // Views de Comentários
        rvComments = findViewById(R.id.rvComments);
        etCommentInput = findViewById(R.id.etCommentInput);
        btnSendComment = findViewById(R.id.btnSendComment);
        tvCommentsCount = findViewById(R.id.tvCommentsCount);
    }

    private void setupCommentsRecyclerView() {
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        List<Comment> initialComments = new ArrayList<>();
        commentAdapter = new CommentAdapter(initialComments);
        rvComments.setAdapter(commentAdapter);
    }

    private void loadPostDetails(long id) {
        Post post = forumDAO.getPostById(id);

        if (post != null) {
            tvDetailTitle.setText(post.getTitle());
            tvDetailAuthor.setText("Postado por: " + post.getAuthor());
            tvDetailDate.setText(post.getDate());
            tvDetailContent.setText(post.getContent());
            tvDetailCategory.setText(post.getCategory());

            loadComments(id);
        } else {
            Toast.makeText(this, "Postagem não encontrada.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadComments(long id) {
        List<Comment> comments = commentDAO.getCommentsByPostId(id);

        commentAdapter.updateComments(comments);
        tvCommentsCount.setText("Respostas (" + comments.size() + ")");
    }

    // --- FUNÇÕES DE EXCLUSÃO ---

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Postagem")
                .setMessage("Tem certeza que deseja excluir esta postagem? Esta ação não pode ser desfeita.")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    performDeletePost();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void performDeletePost() {
        // Tenta excluir o post usando o ID do post carregado na Activity
        if (forumDAO.deletePost(postId)) {

            // Limpeza de comentários: Chamada explícita. O ON DELETE CASCADE deve funcionar,
            // mas esta linha garante a exclusão dos comentários.
            commentDAO.deleteCommentsByPostId(postId);

            Toast.makeText(this, "Postagem excluída com sucesso!", Toast.LENGTH_LONG).show();

            // Retorna para a tela do fórum
            finish();
        } else {
            Toast.makeText(this, "Falha ao excluir a postagem.", Toast.LENGTH_LONG).show();
        }
    }

    // --- FUNÇÕES DE COMENTÁRIO ---

    private void sendComment() {
        String content = etCommentInput.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "O comentário não pode estar vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserId == -1) {
            Toast.makeText(this, "Erro: Você não está logado.", Toast.LENGTH_LONG).show();
            return;
        }

        // 1. Obter o nome real usando o DAO, baseado no currentUserId da sessão
        String author = userDAO.getUserNameById(currentUserId);

        // Fallback caso o nome não seja encontrado
        if (author == null || author.isEmpty()) {
            author = "Usuário Desconhecido (ID: " + currentUserId + ")";
        }

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        Comment newComment = new Comment(postId, content, author, currentDate);

        // 2. Salvar no banco de dados
        if (commentDAO.insertComment(newComment)) {
            Toast.makeText(this, "Comentário enviado por " + author + "!", Toast.LENGTH_SHORT).show();

            // 3. Limpar o campo e recarregar a lista
            etCommentInput.setText("");
            loadComments(postId);

            // Rolar para o novo comentário
            rvComments.scrollToPosition(commentAdapter.getItemCount() - 1);
        } else {
            Toast.makeText(this, "Falha ao enviar o comentário.", Toast.LENGTH_SHORT).show();
        }
    }
}