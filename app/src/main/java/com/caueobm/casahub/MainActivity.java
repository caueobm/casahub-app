package com.caueobm.casahub;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caueobm.casahub.adapter.ImovelAdapter;
import com.caueobm.casahub.model.Imovel;
import com.caueobm.casahub.network.ImovelService;
import com.caueobm.casahub.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImovelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Seu layout com RecyclerView

        recyclerView = findViewById(R.id.recyclerViewImoveis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buscarImoveis();
    }

    private void buscarImoveis() {
        Retrofit retrofit = RetrofitClient.getClient();
        ImovelService service = retrofit.create(ImovelService.class);

        Call<List<Imovel>> call = service.listarImoveis();

        call.enqueue(new Callback<List<Imovel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Imovel>> call, @NonNull Response<List<Imovel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Imovel> imoveis = response.body();
                    adapter = new ImovelAdapter(imoveis);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao carregar imóveis", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Imovel>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MainActivity", "Erro na API", t);
            }
        });
    }
}
