package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.dao.UserDAO;
import com.google.android.material.button.MaterialButton;

public class RecoveryPasswordActivity extends AppCompatActivity {

    private EditText etRecoveryEmail;
    private MaterialButton btnNext;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);

        userDAO = new UserDAO(this);

        etRecoveryEmail = findViewById(R.id.etRecoveryEmail);
        btnNext = findViewById(R.id.btnRecoveryNext);

        btnNext.setOnClickListener(v -> {
            String email = etRecoveryEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu email.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifica se existe usuário com o email
            if (!userDAO.emailExists(email)) {
                Toast.makeText(this, "Email não encontrado!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Passa o email para a próxima tela
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}