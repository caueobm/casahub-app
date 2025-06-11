package com.caueobm.casahub.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.caueobm.casahub.model.CadastroDto;
import com.caueobm.casahub.model.JwtAuthResponse;
import com.caueobm.casahub.network.AuthService;
import com.caueobm.casahub.network.RetrofitClient;
import com.caueobm.casahub.ui.CadastroUIState;
import com.caueobm.casahub.ui.LoginUIState;
import com.caueobm.casahub.util.TokenManager; // Para salvar o token se o cadastro fizer login

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroViewModel extends AndroidViewModel {
    private static final String TAG = "CadastroViewModel";

    private final MutableLiveData<CadastroUIState> _cadastroState = new MutableLiveData<>();
    public LiveData<CadastroUIState> cadastroState = _cadastroState;

    private final AuthService authService;
    private final TokenManager tokenManager;

    public CadastroViewModel(@NonNull Application application) {
        super(application);
        authService = RetrofitClient.getClient(application.getApplicationContext()).create(AuthService.class);
        tokenManager = new TokenManager(application.getApplicationContext());
    }

    public void cadastrar(String nome, String email, String senha, String confirmarSenha) {
        // Validação básica
        if (nome.isEmpty()) {
            _cadastroState.setValue(new CadastroUIState.Error("Nome não pode estar vazio"));
            return;
        }
        if (email.isEmpty()) {
            _cadastroState.setValue(new CadastroUIState.Error("Email não pode estar vazio"));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _cadastroState.setValue(new CadastroUIState.Error("Formato de email inválido"));
            return;
        }
        if (senha.isEmpty()) {
            _cadastroState.setValue(new CadastroUIState.Error("Senha não pode estar vazia"));
            return;
        }
        if (senha.length() < 6) { // Exemplo de regra de senha
            _cadastroState.setValue(new CadastroUIState.Error("Senha deve ter pelo menos 6 caracteres"));
            return;
        }
        if (confirmarSenha.isEmpty()) {
            _cadastroState.setValue(new CadastroUIState.Error("Confirmação de senha não pode estar vazia"));
            return;
        }
        if (!senha.equals(confirmarSenha)) {
            _cadastroState.setValue(new CadastroUIState.Error("As senhas não coincidem"));
            return;
        }

        _cadastroState.setValue(new CadastroUIState.Loading());
        Log.d(TAG, "Tentando cadastrar usuário: " + email);

        CadastroDto cadastroDto = new CadastroDto(nome, email, senha);
        // Se seu backend espera a confirmação de senha, adicione-a ao CadastroRequest

        authService.signup(cadastroDto).enqueue(new Callback<JwtAuthResponse>() { // Assumindo que a resposta do cadastro é similar à do login (retorna um token)
            // Se for diferente, crie um modelo de resposta de Cadastro (ex: CadastroResponse)
            @Override
            public void onResponse(@NonNull Call<JwtAuthResponse> call, @NonNull Response<JwtAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtAuthResponse authResponse = response.body();
                    String token = authResponse.getAccessToken();

                    if (token != null && !token.isEmpty()) {
                        tokenManager.saveAuthToken(token);
                        _cadastroState.setValue(new CadastroUIState.Success(token));
                        Log.i(TAG, "Cadastro bem-sucedido e token salvo: " + token);
                    } else {
                        // Não tente usar Toast aqui!
                        _cadastroState.setValue(new CadastroUIState.Error("Erro: Token não recebido. Verifique a resposta do servidor."));
                    }
                } else {
                    String errorMessage = "Erro no cadastro";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                            Log.e(TAG, "Erro no cadastro (errorBody): " + errorMessage + " Código: " + response.code());
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao parsear errorBody: ", e);
                        }
                    } else {
                        errorMessage = "Erro desconhecido no cadastro (Código: " + response.code() + ")";
                        Log.e(TAG, errorMessage);
                    }
                    // Tenta extrair uma mensagem mais amigável se possível
                    if (response.code() == 409) { // HTTP 409 Conflict (ex: email já existe)
                        _cadastroState.setValue(new CadastroUIState.Error("Email já cadastrado."));
                    } else if (response.code() == 400) { // HTTP 400 Bad Request (ex: dados inválidos)
                        _cadastroState.setValue(new CadastroUIState.Error("Dados inválidos. Verifique os campos."));
                    }
                    else {
                        _cadastroState.setValue(new CadastroUIState.Error(errorMessage));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtAuthResponse> call, @NonNull Throwable t) {
                // Tratar falhas de rede ou outras exceções
                Log.e(TAG, "Falha na chamada de Cadastro: ", t);
                _cadastroState.setValue(new CadastroUIState.Error("Falha na conexão: " + t.getMessage()));
            }
        });
    }
}