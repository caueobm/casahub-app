package com.caueobm.casahub.network;

import com.caueobm.casahub.model.Imovel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImovelService {

    @GET("/api/imoveis")
    Call<List<Imovel>> listarImoveis();

    // Novo método para buscar um imóvel por ID
    // Assumindo que o ID na URL é um número (long ou int)
    @GET("/api/imoveis/{id}")
    Call<Imovel> getImovelPorId(@Path("id") long id); // Use o mesmo tipo do seu ID (long, int, String)
    // O nome "id" em @Path("id") deve corresponder a {id} na URL
}
