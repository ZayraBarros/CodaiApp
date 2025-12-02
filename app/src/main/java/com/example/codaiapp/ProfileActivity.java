package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.dao.UserDAO;
import com.example.codaiapp.model.User;
import com.example.codaiapp.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private UserDAO userDAO;

    private TextView tvUserName, tvUserEmail, tvUserRole, tvUserBio;
    private TextView tvArticlesCount, tvPostsCount, tvCommentsCount;
    private MaterialButton btnEditProfile, btnLogout;
    private MaterialButton btnChangePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        userDAO = new UserDAO(this);

        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.profileToolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // IDs corrigidos de acordo com activity_profile.xml
        tvUserName = findViewById(R.id.tvProfileName);
        tvUserEmail = findViewById(R.id.tvProfileEmail);
        tvUserRole = findViewById(R.id.tvProfileRole);
        tvUserBio = findViewById(R.id.tvProfileAbout);

        // IDs de contagem corrigidos
        tvArticlesCount = findViewById(R.id.tvProfileArticlesCount);
        tvPostsCount = findViewById(R.id.tvProfileForumPostsCount);
        tvCommentsCount = findViewById(R.id.tvProfileBadgesCount); // Mapeado para Comentários/Badges

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        loadProfileData();

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            String email = sessionManager.getUserEmail();

            Intent intent = new Intent(ProfileActivity.this, ResetPasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void loadProfileData() {
        String loggedInEmail = sessionManager.getUserEmail();

        if (loggedInEmail != null) {
            User user = userDAO.getUserDetails(loggedInEmail);

            if (user != null) {
                tvUserName.setText(user.getNome());
                tvUserEmail.setText(user.getEmail());

                if (user.getBio() != null && !user.getBio().isEmpty()) {
                    tvUserBio.setText(user.getBio());
                } else {
                    tvUserBio.setText("Adicione uma breve descrição sobre você, seus objetivos de aprendizado e áreas de interesse.");
                }

                tvArticlesCount.setText(String.valueOf(user.getArticlesReadCount()));
                tvPostsCount.setText(String.valueOf(user.getForumPostsCount()));
                tvCommentsCount.setText(String.valueOf(user.getCommentsCount()));

                tvUserRole.setText("Estudante / Dev em formação");
            }
        }
    }

    private void handleLogout() {
        sessionManager.logoutUser();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) {
            loadProfileData();
        }
    }
}