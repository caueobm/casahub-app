package com.caueobm.casahub;

import android.content.Intent; // Importe o Intent
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caueobm.casahub.activity.DetalheImovelActivity;
import com.caueobm.casahub.activity.LoginActivity;
import com.caueobm.casahub.activity.MeusClientesActivity;
import com.caueobm.casahub.adapter.ImovelAdapter;
import com.caueobm.casahub.model.Imovel;
import com.caueobm.casahub.network.ImovelService;
import com.caueobm.casahub.network.RetrofitClient;
import com.caueobm.casahub.ui.BaseActivity;
import com.caueobm.casahub.util.TokenManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
// Importe sua DetalheImovelActivity se estiver em outro pacote
// import com.caueobm.casahub.activity.DetalheImovelActivity;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// 1. Implemente a interface
public class MainActivity extends BaseActivity implements ImovelAdapter.OnImovelAction {

    private RecyclerView recyclerView;
    private ImovelAdapter adapter;
    private MaterialToolbar topAppBar;
    private MaterialButton btnLogin, btnLogout, btnListarClientes;
    private TokenManager tokenManager; // Declarado aqui

    // Não se esqueça da TAG para os logs, se estiver usando Log.i(TAG, ...)
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurarToolbar();

        // INICIALIZE O TOKENMANAGER AQUI!
        tokenManager = new TokenManager(getApplicationContext());

        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        recyclerView = findViewById(R.id.recyclerViewImoveis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buscarImoveis();
    }

    private void buscarImoveis() {
        ImovelService service;
        Retrofit retrofit = RetrofitClient.getClient(getApplicationContext());
        service = retrofit.create(ImovelService.class);


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

    // 2. Sobrescreva o metodo da interface
    @Override
    public void onVerDetalhesClick(long imovelId) {
        Intent intent = new Intent(MainActivity.this, DetalheImovelActivity.class);
        intent.putExtra("IMOVEL_ID", imovelId);
        startActivity(intent);
    }

}
