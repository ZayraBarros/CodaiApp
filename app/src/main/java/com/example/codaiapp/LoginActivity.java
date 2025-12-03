package com.example.codaiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.codaiapp.dao.UserDAO; // Importar a classe DAO
import com.example.codaiapp.model.User; // Importar a classe Model


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etLoginEmail, etLoginPassword;
    private MaterialButton btnLogin;
    private TextView tvGoToRegister;
    private UserDAO userDAO;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 1. Inicializa o DAO (acesso ao banco)
        userDAO = new UserDAO(this);

        // 2. Referência aos elementos da UI (Layout: activity_login.xml)
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        // 3. Configura o Listener do Botão Entrar
        btnLogin.setOnClickListener(v -> handleLogin());

        // 4. Configura o Listener para ir ao Cadastro
        tvGoToRegister.setOnClickListener(v -> {
            // REVISÃO: Aponta para a nova e correta RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecoveryPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {

        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString(); // Senha em texto puro

        // 5. Validação de campos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Informe email e senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 6. Chamada ao DAO para verificar o login
        User loggedInUser = userDAO.login(email, password);

        if (loggedInUser != null) {
            SessionManager session = new SessionManager(this);
            session.createLoginSession(loggedInUser.getId(), loggedInUser.getNome(), loggedInUser.getEmail());
            Toast.makeText(this, "Bem-vindo(a), " + loggedInUser.getNome() + "!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // Falha: Credenciais inválidas (email não existe ou senha errada)
            Toast.makeText(this, "❌ Credenciais inválidas.", Toast.LENGTH_LONG).show();
        }
    }
}