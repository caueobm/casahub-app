
package com.caueobm.casahub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caueobm.casahub.R;
import com.caueobm.casahub.activity.LoginActivity;
import com.caueobm.casahub.activity.MeusClientesActivity;
import com.caueobm.casahub.util.TokenManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

// BaseActivity.java
public abstract class BaseActivity extends AppCompatActivity {

    protected MaterialToolbar topAppBar;
    protected MaterialButton btnLogin, btnLogout, btnListarClientes;
    protected TokenManager tokenManager;

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenManager = new TokenManager(getApplicationContext()); // Inicialize antes de configurarToolbar
    }

    protected void configurarToolbar() {
        topAppBar = findViewById(R.id.topAppBar);
        if (topAppBar == null) {
            Log.e(TAG, "MaterialToolbar (R.id.topAppBar) não encontrada no layout da Activity filha.");
            return; // Não continuar se a toolbar não existir
        }
        btnLogin = topAppBar.findViewById(R.id.btnLogin);
        btnLogout = topAppBar.findViewById(R.id.btnLogout);
        btnListarClientes = topAppBar.findViewById(R.id.btnListarClientes);

        // Verifica se os botões foram realmente encontrados antes de configurar listeners
        if (btnLogin == null || btnLogout == null) {
            Log.w(TAG, "Um ou ambos os botões btnLogin/btnLogout não foram encontrados na topAppBar.");
            // Não configurar listeners se os botões não existem para evitar NullPointerException
        } else {
            // A lógica de click listener será configurada em atualizarVisibilidadeBotoesLogin
        }


        if (btnListarClientes != null) {
            btnListarClientes.setOnClickListener(v -> {
                Intent intent = new Intent(this, MeusClientesActivity.class);
                startActivity(intent);
            });
        }

        atualizarVisibilidadeBotoesLogin();
    }

    protected void atualizarVisibilidadeBotoesLogin() {
        if (btnLogin == null || btnLogout == null) {

            return;
        }

        if (tokenManager.isLoggedIn()) {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogout.setOnClickListener(v -> {
                tokenManager.clearAuthToken();
                Toast.makeText(this, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show();
                atualizarVisibilidadeBotoesLogin(); // Re-chama para atualizar a UI da activity ATUAL
            });

            if (btnListarClientes != null) btnListarClientes.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
            btnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            });

            if (btnListarClientes != null) btnListarClientes.setVisibility(View.GONE);
        }
    }

    // Adicione onResume para que as classes filhas possam chamar a atualização
    @Override
    protected void onResume() {
        super.onResume();
        if (topAppBar != null && btnLogin != null && btnLogout != null) {
            atualizarVisibilidadeBotoesLogin();
        }
    }
}