package com.caueobm.casahub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView; // Importação já presente
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.caueobm.casahub.MainActivity;
import com.caueobm.casahub.R;
import com.caueobm.casahub.ui.LoginUIState;
import com.caueobm.casahub.viewmodel.LoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private LoginViewModel loginViewModel;

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLoginSubmit;
    private ProgressBar progressBarLogin;
    private TextView tvLoginError, textTelaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar o ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Referenciar as Views do layout
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etPassword = findViewById(R.id.etPassword);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        tvLoginError = findViewById(R.id.tvLoginError);
        textTelaCadastro = findViewById(R.id.text_tela_cadastro);

        // Verificar se o usuário já está logado
        if (loginViewModel.checkIfUserIsLoggedIn()) {
            navigateToMainApp();
            return; // Importante para não continuar com o resto do onCreate
        }

        // Configurar o botão de login
        btnLoginSubmit.setOnClickListener(v -> {
            String emailOrUsername = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

            boolean isValid = true;
            if (emailOrUsername.isEmpty()) {
                tilEmail.setError("Email/Usuário não pode estar vazio");
                isValid = false;
            } else {
                tilEmail.setError(null); // Limpa o erro se o campo for preenchido
            }

            if (password.isEmpty()) {
                tilPassword.setError("Senha não pode estar vazia");
                isValid = false;
            } else {
                tilPassword.setError(null); // Limpa o erro se o campo for preenchido
            }

            if (!isValid) {
                return;
            }
            tvLoginError.setVisibility(View.GONE); // Limpar erro geral antes de tentar logar
            loginViewModel.login(emailOrUsername, password);
        });

        // Observar mudanças de estado no ViewModel
        loginViewModel.loginState.observe(this, loginUIState -> {
            // Resetar estado da UI para cada nova emissão
            progressBarLogin.setVisibility(View.GONE);
            btnLoginSubmit.setEnabled(true);
            tilEmail.setError(null); // Limpa erros de validação anteriores ao observar novo estado
            tilPassword.setError(null);
            // tvLoginError.setVisibility(View.GONE); // Descomente se quiser limpar o erro geral aqui também

            if (loginUIState instanceof LoginUIState.Loading) {
                progressBarLogin.setVisibility(View.VISIBLE);
                btnLoginSubmit.setEnabled(false);
                tvLoginError.setVisibility(View.GONE); // Esconde erro durante o carregamento
                Log.d(TAG, "Login em progresso...");
            } else if (loginUIState instanceof LoginUIState.Success) {
                LoginUIState.Success successState = (LoginUIState.Success) loginUIState;
                Log.d(TAG, "Login bem-sucedido, token: " + successState.getToken());
                Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                navigateToMainApp();
            } else if (loginUIState instanceof LoginUIState.Error) {
                LoginUIState.Error errorState = (LoginUIState.Error) loginUIState;
                Log.e(TAG, "Erro no login: " + errorState.getMessage());

                // Verifica se o erro é específico de campo ou geral
                // Esta é uma lógica de exemplo, ajuste conforme as mensagens de erro do seu backend
                String errorMessage = errorState.getMessage().toLowerCase();
                if (errorMessage.contains("email") || errorMessage.contains("usuário não encontrado")) {
                    tilEmail.setError(errorState.getMessage());
                } else if (errorMessage.contains("senha") || errorMessage.contains("credenciais inválidas")) {
                    tilPassword.setError(errorState.getMessage());
                } else {
                    // Erro mais genérico
                    tvLoginError.setText("Erro: " + errorState.getMessage());
                    tvLoginError.setVisibility(View.VISIBLE);
                }
            }
        });

        if (textTelaCadastro != null) {
            textTelaCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Link 'Cadastre-se' clicado. Navegando para CadastroActivity.");
                    Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e(TAG, "TextView 'text_tela_cadastro' (R.id.text_tela_cadastro) não foi encontrado no layout activity_login.xml");
            // Se este log aparecer, verifique o ID no seu arquivo XML.
        }
    }

    private void navigateToMainApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}