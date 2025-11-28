package com.example.codaiapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codaiapp.dao.UserDAO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText etNewPassword, etConfirmPassword;
    private MaterialButton btnSave;
    private UserDAO userDAO;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userDAO = new UserDAO(this);

        email = getIntent().getStringExtra("email");

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSavePassword);

        btnSave.setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();

            if (newPass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Preencha os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirm)) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = userDAO.updatePassword(email, newPass);

            if (success) {
                Toast.makeText(this, "Senha atualizada com sucesso!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao atualizar senha.", Toast.LENGTH_LONG).show();
            }
        });
    }
}