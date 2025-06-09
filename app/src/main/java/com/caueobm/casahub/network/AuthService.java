package com.caueobm.casahub.network;

import com.caueobm.casahub.model.CadastroDto;
import com.caueobm.casahub.model.JwtAuthResponse;
import com.caueobm.casahub.model.LoginDto;
// Importe SignupDto se você também for implementar o cadastro aqui
// import com.caueobm.casahub.model.SignupDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("api/auth/login") // Endpoint completo: BASE_URL + "api/auth/login"
    Call<JwtAuthResponse> login(@Body LoginDto loginDto);

    @POST("api/auth/signup")
    Call<JwtAuthResponse> signup(@Body CadastroDto cadastroDtoDto);
}