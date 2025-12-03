package com.example.codaiapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.dao.UserDAO;
import com.example.codaiapp.model.User;
import com.example.codaiapp.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etRole, etAbout;
    private SessionManager sessionManager;
    private UserDAO userDAO;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sessionManager = new SessionManager(this);
        userDAO = new UserDAO(this);

        MaterialToolbar toolbar = findViewById(R.id.editProfileToolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        etName = findViewById(R.id.etEditName);
        etEmail = findViewById(R.id.etEditEmail);
        etRole = findViewById(R.id.etEditRole);
        etAbout = findViewById(R.id.etEditAbout);

        MaterialButton btnSave = findViewById(R.id.btnSaveProfile);

        loadCurrentUserData();

        btnSave.setOnClickListener(v -> handleSaveProfile());
    }

    private void loadCurrentUserData() {
        String loggedInEmail = sessionManager.getUserEmail();

        if (loggedInEmail == null) {
            Toast.makeText(this, "Sessão inválida. Faça login novamente.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        currentUser = userDAO.getUserDetails(loggedInEmail);

        if (currentUser != null) {
            etName.setText(currentUser.getNome());
            etEmail.setText(currentUser.getEmail());

            // O campo Email não deve ser editável após ser carregado para evitar erros de UPDATE
            etEmail.setEnabled(false);

            // Como 'role' não faz parte do User Model, estamos usando um valor padrão no ProfileActivity.
            // Para manter o fluxo, você pode carregá-lo de SharedPreferences ou setar um valor default:
            etRole.setText("Estudante / Dev em formação");

            if (currentUser.getBio() != null) {
                etAbout.setText(currentUser.getBio());
            }
        } else {
            Toast.makeText(this, "Erro: Não foi possível carregar os dados do usuário.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void handleSaveProfile() {
        String newName = etName.getText() != null ? etName.getText().toString().trim() : "";
        String newAbout = etAbout.getText() != null ? etAbout.getText().toString().trim() : "";

        if (newName.isEmpty()) {
            Toast.makeText(this, "O campo Nome é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Atualiza o objeto User (apenas Nome e BIO/About)
        currentUser.setNome(newName);
        currentUser.setBio(newAbout);

        // 2. Chama o DAO para salvar no banco. A senha, email e contadores permanecem os mesmos.
        boolean success = userDAO.updateProfile(currentUser);

        if (success) {
            // Se o nome foi alterado, atualiza a sessão para que o nome no Menu/Home mude
            sessionManager.createLoginSession(currentUser.getId(), newName, currentUser.getEmail());

            Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Falha ao salvar o perfil. Tente novamente.", Toast.LENGTH_LONG).show();
        }
    }
}