package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.card.MaterialCardView;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        topAppBar      = findViewById(R.id.topAppBar);

        // Abre o menu hambúrguer quando clica no ícone da AppBar
        topAppBar.setNavigationOnClickListener(
                v -> drawerLayout.openDrawer(GravityCompat.START)
        );

        // CARD "Introdução a Data Science" → página de artigos
        MaterialCardView cardArticle = findViewById(R.id.cardArticle);
        cardArticle.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ArticlesActivity.class);
            startActivity(intent);
        });

        // BOTÃO "COMEÇAR" → LoginActivity
        MaterialButton btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //levar pro forum
        MaterialCardView cardLastPost = findViewById(R.id.cardLastPost);
        cardLastPost.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ForumActivity.class);
            startActivity(intent);
        });

        // CLIQUES DO MENU LATERAL
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // já está na home, só fecha o menu
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (id == R.id.nav_articles) {
                // Abre a tela de artigos
                Intent intent = new Intent(HomeActivity.this, ArticlesActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (id == R.id.nav_forum) {
                Intent intent = new Intent(HomeActivity.this, ForumActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            return true;
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}


