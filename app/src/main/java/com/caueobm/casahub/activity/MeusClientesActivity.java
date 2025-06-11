package com.caueobm.casahub.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caueobm.casahub.R;
import com.caueobm.casahub.adapter.ClientesAdapter;
import com.caueobm.casahub.model.Cliente;
import com.caueobm.casahub.network.RetrofitClient;
import com.caueobm.casahub.network.InteresseService;
import com.caueobm.casahub.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeusClientesActivity extends BaseActivity {

    private InteresseService interesseService;
    private RecyclerView recyclerView;
    private ClientesAdapter adapter;
    private List<Cliente> listaClientes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_clientes);
        configurarToolbar();

        recyclerView = findViewById(R.id.recyclerViewClientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClientesAdapter(listaClientes);
        recyclerView.setAdapter(adapter);

        interesseService = RetrofitClient.getClient(this).create(InteresseService.class);

        carregarClientes();
    }

    private void carregarClientes() {
        Call<List<Cliente>> call = interesseService.getMeusCliente();
        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaClientes.clear();
                    listaClientes.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MeusClientesActivity.this, "Falha ao carregar clientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(MeusClientesActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
