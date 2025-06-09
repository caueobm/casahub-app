package com.caueobm.casahub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.caueobm.casahub.R;
import com.caueobm.casahub.ui.CadastroUIState;
import com.caueobm.casahub.viewmodel.CadastroViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";

    private CadastroViewModel cadastroViewModel;

    private TextInputLayout tilNomeCadastro, tilEmailCadastro, tilSenhaCadastro, tilConfirmarSenhaCadastro;
    private TextInputEditText etNomeCadastro, etEmailCadastro, etSenhaCadastro, etConfirmarSenhaCadastro;
    private MaterialButton btnCadastrarSubmit;
    private ProgressBar progressBarCadastro;
    private TextView tvCadastroError, textTelaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cadastroViewModel = new ViewModelProvider(this).get(CadastroViewModel.class);

        tilNomeCadastro = findViewById(R.id.tilNomeCadastro);
        etNomeCadastro = findViewById(R.id.etNomeCadastro);
        tilEmailCadastro = findViewById(R.id.tilEmailCadastro);
        etEmailCadastro = findViewById(R.id.etEmailCadastro);
        tilSenhaCadastro = findViewById(R.id.tilSenhaCadastro);
        etSenhaCadastro = findViewById(R.id.etSenhaCadastro);
        tilConfirmarSenhaCadastro = findViewById(R.id.tilConfirmarSenhaCadastro);
        etConfirmarSenhaCadastro = findViewById(R.id.etConfirmarSenhaCadastro);
        btnCadastrarSubmit = findViewById(R.id.btnCadastrarSubmit);
        progressBarCadastro = findViewById(R.id.progressBarCadastro);
        tvCadastroError = findViewById(R.id.tvCadastroError);
        textTelaLogin = findViewById(R.id.text_tela_login);

        btnCadastrarSubmit.setOnClickListener(v -> {
            tilNomeCadastro.setError(null);
            tilEmailCadastro.setError(null);
            tilSenhaCadastro.setError(null);
            tilConfirmarSenhaCadastro.setError(null);
            tvCadastroError.setVisibility(View.GONE);

            String nome = etNomeCadastro.getText() != null ? etNomeCadastro.getText().toString().trim() : "";
            String email = etEmailCadastro.getText() != null ? etEmailCadastro.getText().toString().trim() : "";
            String senha = etSenhaCadastro.getText() != null ? etSenhaCadastro.getText().toString() : "";
            String confirmarSenha = etConfirmarSenhaCadastro.getText() != null ? etConfirmarSenhaCadastro.getText().toString() : "";

            boolean isValid = true;
            if (nome.isEmpty()) {
                tilNomeCadastro.setError("Nome não pode estar vazio");
                isValid = false;
            }
            if (email.isEmpty()) {
                tilEmailCadastro.setError("Email não pode estar vazio");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmailCadastro.setError("Formato de email inválido");
                isValid = false;
            }
            if (senha.isEmpty()) {
                tilSenhaCadastro.setError("Senha não pode estar vazia");
                isValid = false;
            } else if (senha.length() < 6) {
                tilSenhaCadastro.setError("Senha deve ter pelo menos 6 caracteres");
                isValid = false;
            }
            if (confirmarSenha.isEmpty()) {
                tilConfirmarSenhaCadastro.setError("Confirmação de senha não pode estar vazia");
                isValid = false;
            } else if (!senha.equals(confirmarSenha)) {
                tilConfirmarSenhaCadastro.setError("As senhas não coincidem");
                isValid = false;
            }

            if (!isValid) {
                return;
            }
            Log.d(TAG, "Botão Cadastrar clicado. Chamando ViewModel.");
            cadastroViewModel.cadastrar(nome, email, senha, confirmarSenha);
        });

        cadastroViewModel.cadastroState.observe(this, cadastroUIState -> {
            progressBarCadastro.setVisibility(View.GONE);
            btnCadastrarSubmit.setEnabled(true);

            if (cadastroUIState instanceof CadastroUIState.Loading) {
                progressBarCadastro.setVisibility(View.VISIBLE);
                btnCadastrarSubmit.setEnabled(false);
                tvCadastroError.setVisibility(View.GONE);
                Log.d(TAG, "Observando CadastroUIState: Loading...");
            } else if (cadastroUIState instanceof CadastroUIState.Success) {
                Log.d(TAG, "Observando CadastroUIState: Success!");
                Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso! Faça login para continuar.", Toast.LENGTH_LONG).show();
                navigateToLoginActivity();
            } else if (cadastroUIState instanceof CadastroUIState.Error) {
                CadastroUIState.Error errorState = (CadastroUIState.Error) cadastroUIState;
                String errorMessage = errorState.getMessage();
                Log.e(TAG, "Observando CadastroUIState: Error - " + errorMessage);

                Toast.makeText(CadastroActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                // Tenta direcionar o erro para o campo específico
                // Adapte essas strings para corresponderem às mensagens de erro exatas do seu ViewModel/Backend
                String lowerErrorMessage = errorMessage.toLowerCase();
                if (lowerErrorMessage.contains("nome")) {
                    tilNomeCadastro.setError(errorMessage);
                } else if (lowerErrorMessage.contains("email já cadastrado")) {
                    tilEmailCadastro.setError("Este email já está cadastrado.");
                } else if (lowerErrorMessage.contains("email")) { // erro genérico de email
                    tilEmailCadastro.setError(errorMessage);
                } else if (lowerErrorMessage.contains("senha deve ter pelo menos 6 caracteres")) {
                    tilSenhaCadastro.setError("Senha deve ter pelo menos 6 caracteres.");
                } else if (lowerErrorMessage.contains("senha") && !lowerErrorMessage.contains("confirmar")) {
                    tilSenhaCadastro.setError(errorMessage);
                } else if (lowerErrorMessage.contains("senhas não coincidem") || lowerErrorMessage.contains("confirmar")) {
                    tilConfirmarSenhaCadastro.setError(errorMessage);
                } else {
                    // Erro genérico
                    tvCadastroError.setText("Erro: " + errorMessage);
                    tvCadastroError.setVisibility(View.VISIBLE);
                }
            }
        });

        // Configurar OnClickListener para o TextView de "Já tem uma conta? Faça login"
        if (textTelaLogin != null) {
            textTelaLogin.setOnClickListener(v -> {
                Log.d(TAG, "Link 'Faça login' clicado. Navegando para LoginActivity.");
                navigateToLoginActivity();
                // finish(); // Opcional: finalizar CadastroActivity ao ir para Login
            });
        } else {
            Log.e(TAG, "TextView 'text_tela_login' (R.id.text_tela_login) não foi encontrado no layout.");
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        // Limpar a pilha de atividades para evitar voltar à tela de cadastro
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}