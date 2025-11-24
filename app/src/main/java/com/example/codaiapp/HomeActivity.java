package com.example.codaiapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // Clique no hambúrguer (no futuro você pode abrir um Drawer aqui)
        toolbar.setNavigationOnClickListener(v ->
                Toast.makeText(this, "Menu clicado", Toast.LENGTH_SHORT).show()
        );
    }
}
