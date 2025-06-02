package com.caueobm.casahub.network;

import androidx.annotation.NonNull; // Necessário para @NonNull nos Callbacks

import com.caueobm.casahub.model.Imovel;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call; // Importe Call
import retrofit2.Callback; // Importe Callback
import retrofit2.Response; // Importe Response

// 1. Faça FakeImovelService implementar ImovelService
public class FakeImovelService implements ImovelService { // Mude aqui

    private static List<Imovel> listaDeImoveisEstatica = new ArrayList<>();

    // Bloco estático para popular a lista uma vez
    static {
        listaDeImoveisEstatica.add(new Imovel(
                1, "Apartamento Fake", "Rua A, 123", "São Paulo", "SP", "01234-567",
                350000.00, 2000.00, 2, 1, 70, true,
                "Apartamento mobiliado com ótima localização (FAKE)", true, "2024-01-15"
        ));
        listaDeImoveisEstatica.add(new Imovel(
                2, "Casa Fake", "Rua B, 456", "Rio de Janeiro", "RJ", "12345-678",
                750000.00, 0.0, 3, 2, 120, false,
                "Casa ampla, perfeita para famílias (FAKE)", true, "2024-03-10"
        ));
        listaDeImoveisEstatica.add(new Imovel(
                3, "Studio Fake", "Rua C, 789", "Belo Horizonte", "MG", "23456-789",
                200000.00, 1500.00, 1, 1, 40, true,
                "Studio moderno, ideal para solteiros ou estudantes (FAKE)", false, "2024-05-01"
        ));
    }

    // 2. Implemente os métodos da interface ImovelService
    @Override
    public Call<List<Imovel>> listarImoveis() {
        // Crie um Call<List<Imovel>> "fake"
        return new Call<List<Imovel>>() {
            @Override
            public Response<List<Imovel>> execute() {
                // Para chamadas síncronas (geralmente não usado diretamente na UI)
                return Response.success(listaDeImoveisEstatica);
            }

            @Override
            public void enqueue(@NonNull Callback<List<Imovel>> callback) {
                // Simule uma resposta bem-sucedida
                // Para simular um atraso de rede, você poderia usar um Handler.postDelayed aqui
                callback.onResponse(this, Response.success(listaDeImoveisEstatica));
                // Para simular uma falha:
                // callback.onFailure(this, new IOException("Fake network error"));
            }

            @Override
            public boolean isExecuted() { return false; }
            @Override
            public void cancel() {}
            @Override
            public boolean isCanceled() { return false; }
            @Override
            public Call<List<Imovel>> clone() { return this; }
            @Override
            public Request request() { return null; } // Pode retornar um request dummy se necessário
            @Override
            public okio.Timeout timeout() {return okio.Timeout.NONE;} // Adicionado para compatibilidade com versões mais recentes
        };
    }

    @Override
    public Call<Imovel> getImovelPorId(long id) {
        return null;
    }

    @Override
    public Call<Imovel> buscarImovelPorId(long imovelId) {
        Imovel encontrado = null;
        for (Imovel imovel : listaDeImoveisEstatica) {
            if (imovel.getId() == imovelId) {
                encontrado = imovel;
                break;
            }
        }
        final Imovel resultadoFinal = encontrado; // Precisa ser final para usar no Callback

        return new Call<Imovel>() {
            @Override
            public Response<Imovel> execute() {
                return Response.success(resultadoFinal);
            }

            @Override
            public void enqueue(@NonNull Callback<Imovel> callback) {
                if (resultadoFinal != null) {
                    callback.onResponse(this, Response.success(resultadoFinal));
                } else {
                    // Simula um erro 404 Not Found
                    // Você pode criar uma Response de erro mais elaborada se precisar
                    // Response.error(404, ResponseBody.create(null, "Imóvel não encontrado"))
                    callback.onFailure(this, new Exception("Imóvel com id " + imovelId + " não encontrado (FAKE)"));
                }
            }
            @Override
            public boolean isExecuted() { return false; }
            @Override
            public void cancel() {}
            @Override
            public boolean isCanceled() { return false; }
            @Override
            public Call<Imovel> clone() { return this; }
            @Override
            public Request request() { return null; }
            @Override
            public okio.Timeout timeout() {return okio.Timeout.NONE;}
        };
    }
}