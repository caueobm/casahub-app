package com.caueobm.casahub.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.caueobm.casahub.model.JwtAuthResponse;
import com.caueobm.casahub.model.LoginDto;
import com.caueobm.casahub.network.AuthService;
import com.caueobm.casahub.network.RetrofitClient;
import com.caueobm.casahub.ui.LoginUIState;
import com.caueobm.casahub.util.TokenManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";
    private final AuthService authService;
    private final MutableLiveData<LoginUIState> _loginState = new MutableLiveData<>(new LoginUIState.Idle());
    public LiveData<LoginUIState> loginState = _loginState; // Exposto para a Activity observar
    private final TokenManager tokenManager;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        authService = RetrofitClient.getClient(application.getApplicationContext()).create(AuthService.class);
        tokenManager = new TokenManager(application.getApplicationContext());
    }

    public void login(String usernameOrEmail, String password) {
        // Validação básica de entrada
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            _loginState.setValue(new LoginUIState.Error("Usuário/Email não pode estar vazio."));
            return;
        }
        if (password == null || password.isEmpty()) {
            _loginState.setValue(new LoginUIState.Error("Senha não pode estar vazia."));
            return;
        }

        _loginState.setValue(new LoginUIState.Loading()); // Informa a UI que o login está em progresso

        LoginDto loginDto = new LoginDto(usernameOrEmail.trim(), password);

        authService.login(loginDto).enqueue(new Callback<JwtAuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtAuthResponse> call, @NonNull Response<JwtAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtAuthResponse authResponse = response.body();
                    String token = authResponse.getAccessToken();

                    if (token != null && !token.isEmpty()) {
                        tokenManager.saveAuthToken(token);
                        _loginState.setValue(new LoginUIState.Success(token));
                        Log.i(TAG, "Login bem-sucedido. Token: " + token);
                    } else {
                        _loginState.setValue(new LoginUIState.Error("Token não recebido do servidor."));
                        Log.e(TAG, "Token nulo ou vazio na resposta, mesmo com sucesso na requisição.");
                    }
                } else {
                    // Tratar erros de resposta (ex: 401 Unauthorized, 400 Bad Request)
                    String errorMessage = "Erro no login. Tente novamente.";
                    if (response.errorBody() != null) {
                        try {
                            // Tente parsear uma mensagem de erro do corpo da resposta, se seu backend enviar uma
                            // JSONObject errorObj = new JSONObject(response.errorBody().string());
                            // errorMessage = errorObj.getString("message");
                            Log.e(TAG, "Erro na resposta do login: " + response.code() + " - " + response.message());
                            // Por simplicidade, usamos uma mensagem genérica ou o código de erro
                            errorMessage = "Erro no login (" + response.code() + "). Verifique suas credenciais.";
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao parsear errorBody: ", e);
                        }
                    } else {
                        Log.e(TAG, "Erro na resposta do login (sem errorBody): " + response.code() + " - " + response.message());
                    }
                    _loginState.setValue(new LoginUIState.Error(errorMessage));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtAuthResponse> call, @NonNull Throwable t) {
                // Tratar falhas de rede ou outras exceções
                Log.e(TAG, "Falha na chamada de login: ", t);
                _loginState.setValue(new LoginUIState.Error("Falha na conexão: " + t.getMessage()));
            }
        });
    }

    // Método para verificar se o usuário já está logado ao iniciar a activity
    public boolean checkIfUserIsLoggedIn() {
        return tokenManager.isLoggedIn();
    }
}