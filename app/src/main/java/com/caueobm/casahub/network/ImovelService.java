package com.caueobm.casahub.network;

import com.caueobm.casahub.model.Imovel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImovelService {

    @GET("api/imoveis")
    Call<List<Imovel>> listarImoveis();

    @GET("api/imoveis/{id}")
    Call<Imovel> getImovelPorId(@Path("id") long id);

    @POST("api/imoveis/usuario/{usuarioId}")
    Call<Imovel> criarImovel(@Path("usuarioId") Long usuarioId, @Body Imovel imovel);

    @Multipart
    @POST("imoveis/{id}/fotos")
    Call<ResponseBody> uploadFotos(
            @Path("id") Long imovelId,
            @Part List<MultipartBody.Part> files
    );

    @POST("api/interesses/imovel/{imovelId}")
    Call<ResponseBody> registrarInteresse(@Path("imovelId") long imovelId);


}
