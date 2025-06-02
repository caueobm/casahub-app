package com.caueobm.casahub.network;

import com.caueobm.casahub.model.Imovel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImovelService {

    @GET("/api/imoveis")
    Call<List<Imovel>> listarImoveis();

    @GET("imoveis/{id}")
    Call<Imovel> getImovelPorId(@Path("id") long id);

}
