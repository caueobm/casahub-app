package com.caueobm.casahub.network;

import com.caueobm.casahub.model.Cliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InteresseService {
        @GET("api/interesses/meus-clientes")
        Call<List<Cliente>> getMeusCliente();
    }
