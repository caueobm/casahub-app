package com.caueobm.casahub;

import android.content.Intent; // Importe o Intent
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caueobm.casahub.activity.DetalheImovelActivity;
import com.caueobm.casahub.adapter.ImovelAdapter;
import com.caueobm.casahub.model.Imovel;
import com.caueobm.casahub.network.FakeImovelService;
import com.caueobm.casahub.network.ImovelService;
import com.caueobm.casahub.network.RetrofitClient;
// Importe sua DetalheImovelActivity se estiver em outro pacote
// import com.caueobm.casahub.activity.DetalheImovelActivity;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// 1. Implemente a interface
public class MainActivity extends AppCompatActivity implements ImovelAdapter.OnImovelAction {

    private RecyclerView recyclerView;
    private ImovelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewImoveis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buscarImoveis();
    }

    private void buscarImoveis() {
        // Defina uma flag para controlar se usa o serviço fake ou real
        // Isso pode vir de uma configuração de build, uma constante de debug, etc.
        boolean usarFakeService = true; // Mude para false para usar o Retrofit real

        ImovelService service;

        if (usarFakeService) {
            service = new FakeImovelService(); // Instancia diretamente o FakeImovelService
        } else {
            Retrofit retrofit = RetrofitClient.getClient();
            service = retrofit.create(ImovelService.class);
        }

        // O resto do código continua o mesmo, pois ambos os serviços retornam Call
        Call<List<Imovel>> call = service.listarImoveis();

        call.enqueue(new Callback<List<Imovel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Imovel>> call, @NonNull Response<List<Imovel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Imovel> imoveis = response.body();
                    adapter = new ImovelAdapter(imoveis);
                    // 3. Configure o listener no adapter
                    adapter.setOnImovelActionListener(MainActivity.this); // 'this' porque MainActivity implementa a interface
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

    // 2. Sobrescreva o método da interface
    @Override
    public void onVerDetalhesClick(long imovelId) {
        // Crie o Intent para a DetalheImovelActivity
        // Certifique-se de que DetalheImovelActivity.class está correto (importe se necessário)
        Intent intent = new Intent(MainActivity.this, DetalheImovelActivity.class);
        // Passe o ID do imóvel para a próxima Activity
        // Assumindo que o ID na sua classe Imovel é int e o método é getId()
        // Se o ID for long, está correto. Se for int no seu Imovel.java, o parâmetro da interface e o getExtra devem ser int.
        intent.putExtra("IMOVEL_ID", imovelId);
        startActivity(intent);
    }
}